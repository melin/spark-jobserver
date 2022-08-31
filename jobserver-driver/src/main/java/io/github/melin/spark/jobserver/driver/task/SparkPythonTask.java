package io.github.melin.spark.jobserver.driver.task;

import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.core.exception.SparkJobException;
import io.github.melin.spark.jobserver.driver.SparkEnv;
import io.github.melin.spark.jobserver.driver.support.Py4jEndPoint;
import io.github.melin.spark.jobserver.driver.util.DriverUtils;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import py4j.GatewayServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

@Service
public class SparkPythonTask extends AbstractSparkTask {

    private static final Logger LOG = LoggerFactory.getLogger(SparkPythonTask.class);

    private static final String LINE_SPE = "\r?\n";

    @Autowired
    private Py4jEndPoint py4jEndPoint;

    private volatile int pythonPid = -1;

    @Override
    protected void executeJobText(InstanceDto instanceDto) throws Exception {
        GatewayServer gatewayServer = null;
        String pythonFile = null;
        LinkedList<Pair<String, String>> fileEntries = null;
        try {
            String instanceCode = instanceDto.getInstanceCode();

            pythonFile = createTempPythonFile(instanceDto.getJobName(), instanceDto.getJobText());
            LOG.info("Create Temp Python File: " + pythonFile);

            // 开启GatewayServer服务
            String secret = DriverUtils.createSecret();
            gatewayServer = createPy4jServer(secret);

            String errMsg = execCommand(instanceCode, pythonFile, secret, gatewayServer.getListeningPort());

            if (StringUtils.isNotBlank(errMsg)) {
                LOG.info("(" + instanceCode + ")原始msg: \n" + errMsg);

                boolean printLog = SparkEnv.sparkConf().getBoolean("spark.python.print.full.logs", false);
                if (printLog) {
                    LogUtils.error(errMsg);
                }

                errMsg = LogUtils.resolvePythonErrMsg(errMsg);
                throw new SparkJobException(errMsg);
            }
        } finally {
            if (gatewayServer != null) {
                gatewayServer.shutdown();
            }
            pythonPid = -1;
        }
    }

    private String execCommand(String instanceCode, String pythonFile, String secret, int port) throws Exception {
        String pythonPath = SparkEnv.sparkConf().get("spark.pyspark.driver.python");
        String pysparkPath = SparkEnv.sparkConf().get("spark.executorEnv.PYTHONPATH");

        String cmd = pythonPath + " " + pythonFile + " " + port + " " + instanceCode + " " + secret;
        String user = System.getProperty("user.name");
        LogUtils.info(user + " execute python: " + cmd);
        Process process = Runtime.getRuntime().exec(cmd, new String[]{"PYSPARK_PYTHON=" + pythonPath,
                "PYTHONPATH=" + pysparkPath, "LANG=zh_CN.UTF-8"});

        pythonPid = getPid(process);
        LogUtils.info("start python process: " + pythonPid);

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        LinkedList<String> list = new LinkedList<>();
        while (process.isAlive()) {
            String line = reader.readLine();
            if (StringUtils.isNotBlank(line)) {
                LOG.info("python command log: {}", line);

                if (StringUtils.isNotBlank(line.replaceAll(LINE_SPE, "").trim())) {
                    list.add(line);
                }
            }
        }

        if (process.exitValue() != 0) {
            if (list.size() == 0) {
                return "作业运行失败";
            } else {
                return StringUtils.join(list, "\n");
            }
        } else {
            return null;
        }
    }

    private String createTempPythonFile(String jobName, String script) throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("pythonJobTemplate.py");
        InputStreamReader inputStream = new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        byte[] bytes = IOUtils.toByteArray(inputStream, StandardCharsets.UTF_8);

        String tempScript = new String(bytes);
        String pythonTaskText = StringUtils.replace(tempScript, "##PYTHON_TEMPLE##", script);

        File file = new File(DriverUtils.CURRENT_PATH + "/" + jobName + ".py");
        FileUtils.writeStringToFile(file, pythonTaskText, "UTF-8");
        return file.getPath();
    }

    private GatewayServer createPy4jServer(String secret) {
        InetAddress localhost = InetAddress.getLoopbackAddress();
        GatewayServer gatewayServer = new GatewayServer.GatewayServerBuilder()
                .authToken(secret)
                .entryPoint(py4jEndPoint)
                .javaPort(0)
                .javaAddress(localhost)
                .callbackClient(GatewayServer.DEFAULT_PYTHON_PORT, localhost, secret)
                .build();

        gatewayServer.start();

        int port = gatewayServer.getListeningPort();
        LOG.info("GatewayServer Started on Port: " + port);
        LogUtils.info("GatewayServer Started on Port: " + port);

        return gatewayServer;
    }

    private static int getPid(Process process) {
        try {
            Class<?> cProcessImpl = process.getClass();
            Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (Exception e) {
            return -1;
        }
    }

    public int getPythonPid() {
        return pythonPid;
    }
}
