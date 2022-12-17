package io.github.melin.spark.jobserver.web.controller;

import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.SparkJobServerConf;
import io.github.melin.spark.jobserver.logs.SparkLogService;
import io.github.melin.spark.jobserver.support.ClusterConfig;
import io.github.melin.spark.jobserver.support.YarnClientService;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.enums.DriverStatus;
import io.github.melin.spark.jobserver.core.service.ClusterService;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import com.gitee.melin.bee.core.support.Pagination;
import com.gitee.melin.bee.core.support.Result;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

@Controller
public class DriverController implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DriverController.class);

    private static final String TMP_DIR = FileUtils.getUserDirectory() + "/tmp/";

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    protected JobInstanceService jobInstanceService;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private YarnClientService yarnClientService;

    @Autowired
    private SparkLogService sparkLogService;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ClusterConfig clusterConfig;

    @Value("${spring.profiles.active}")
    protected String profiles;

    public static String sparkLauncherFailedMsg = "";

    @Override
    public void afterPropertiesSet() throws Exception {
        FileUtils.forceMkdir(new File(TMP_DIR));
    }

    @RequestMapping("/driver")
    public String home(ModelMap model) {
        List<Cluster> clusters = clusterService.queryValidClusters();

        model.addAttribute("sparkLauncherFailedMsg", sparkLauncherFailedMsg);
        model.addAttribute("clusters", clusters);
        return "driver";
    }

    @RequestMapping("/driver/queryDrivers")
    @ResponseBody
    public Pagination<SparkDriver> queryDrivers(String applicationId, int page, int limit, HttpServletRequest request) {
        String sort = request.getParameter("sort");
        String orderStr = request.getParameter("order");

        Order order = Order.desc("gmtModified");
        if (StringUtils.isNotEmpty(sort)) {
            if ("asc".equals(orderStr)) {
                order = Order.asc(sort);
            } else {
                order = Order.desc(sort);
            }
        }

        List<String> params = Lists.newArrayList();
        List<Object> values = Lists.newArrayList();
        if (StringUtils.isNotBlank(applicationId)) {
            params.add("applicationId");
            values.add(applicationId);
        }
        Pagination<SparkDriver> pagination = driverService.findPageByNamedParamAndOrder(params, values,
                Lists.newArrayList(order), page, limit);

        pagination.getResult().forEach(driver -> {
            String url = clusterConfig.getValue(driver.getClusterCode(), SparkJobServerConf.JOBSERVER_YARN_PROXY_URI);
            driver.setSparkYarnProxyUri(url);
        });

        return pagination;
    }

    /**
     * driver 启动以后，driver 通知启动日志线程,
     */
    @RequestMapping("/driver/startJobLogThread")
    @ResponseBody
    public Result<String> startJobLogThread(String instanceCode) {
        LOG.info("startJobLogThread: {}", instanceCode);
        boolean result = sparkLogService.startJobLogThread(instanceCode);
        if (result) {
            return Result.successResult();
        } else {
            return Result.failureResult("Job log thread not exist");
        }
    }

    @PostMapping("/driver/killDriver")
    @ResponseBody
    public Result<String> killDriver(Long driverId) {
        SparkDriver driver = driverService.getEntity(driverId);
        if (driver == null) {
            return Result.failureResult("停止失败, Drover 不存在" + driverId);
        }

        String applicationId = driver.getApplicationId();
        String clusterCode = driver.getClusterCode();

        if (StringUtils.startsWith(applicationId, "application_")) {
            LOG.info("kill application {}", applicationId);
            if (DriverStatus.INIT == driver.getStatus()) {
                yarnClientService.killApplicationOnYarn(clusterCode, applicationId);
            } else {
                String result = yarnClientService.killApplication(clusterCode, applicationId);
                if (!"success".equals(result)) {
                    return Result.failureResult("停止失败: " + result);
                }
            }
        }

        driverService.deleteEntity(driverId);
        return Result.successResult();
    }

    @RequestMapping("/driver/downloadYarnLog")
    public void downloadYarnLog(String applicationId, HttpServletResponse response) throws Exception {
        SparkDriver driver = driverService.queryDriverByAppId(applicationId);
        if (driver != null) {
            String urlStr = driver.getSparkDriverUrl() + "/sparkDriver/downloadYarnLog";
            LOG.info("下载yarn log: {}", urlStr);
            String downFilename = applicationId + ".log"; //要下载的文件名称
            response.setContentType("application/x-download");
            response.setHeader("Location", downFilename);
            response.setHeader("Content-Disposition", "attachment; filename=" + downFilename);
            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                outputStream = new BufferedOutputStream(response.getOutputStream());

                URL url = new URL(urlStr);
                URLConnection uc = url.openConnection();
                inputStream = uc.getInputStream();

                IOUtils.copyLarge(inputStream, outputStream);
            } catch (IOException e) {
                LOG.error("下载日志异常: " + applicationId, e);
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(inputStream);
            }
        } else {
            FileInputStream fileInputStream = null;
            OutputStream outputStream = null;
            File logFile = null;
            try {
                String fileName = applicationId + ".log";
                String filePath = TMP_DIR + fileName;
                logFile = new File(filePath);

                String[] cmds = buildDownloadYarnCmd(applicationId, filePath);
                String commandLine = StringUtils.join(cmds, " && ");
                LOG.info("导出yarn 日志执行命令: " + commandLine);

                execYarnLogsCommand(logFile, commandLine);

                response.reset();
                response.setContentType("application/x-download");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                response.addHeader("Content-Length", "" + FileUtils.sizeOf(logFile));

                outputStream = new BufferedOutputStream(response.getOutputStream());
                fileInputStream = new FileInputStream(logFile);
                IOUtils.copyLarge(fileInputStream, outputStream);
            } catch (Throwable e) {
                LOG.error("下载 " + applicationId + " yarn 日志失败: " + e.getMessage(), e);

                String error = "";
                if (e.getMessage() != null) {
                    error = e.getMessage();
                    if (StringUtils.containsIgnoreCase(error, "Invalid AppAttemptId")) {
                        error = "该applicationId：" + applicationId + "，格式不正确";
                    }
                } else {
                    if (e.getCause() == null) {
                        error = e.getMessage();
                    } else {
                        error = e.getCause().getMessage();
                    }
                    if (StringUtils.containsIgnoreCase(e.getCause().getCause().toString(), "ApplicationNotFoundException")) {
                        error = "applicationId：" + applicationId + " 不存在!";
                    }
                }
                response.reset();
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().append(error);
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(fileInputStream);
                FileUtils.deleteQuietly(logFile);
            }
        }
    }

    private void execYarnLogsCommand(File logFile, String commandLine) throws IOException, InterruptedException {
        String[] cmd = {"sh", "-c", commandLine};
        LOG.info(StringUtils.join(cmd, ";"));
        Process process = Runtime.getRuntime().exec(cmd);
        String errLog = processErrLog(process);
        if (errLog != null) {
            FileUtils.write(logFile, errLog);
        }

        LOG.info("process exit value: " + process.exitValue() + ", filePath: " + logFile.getPath());
    }

    private String[] buildDownloadYarnCmd(String applicationId, String filePath) {
        String yarnLocation = "/usr/bin/yarn";
        String baseConf = configProperties.getJobserverConfDir();
        String hadoopConf = "export HADOOP_CONF_DIR=" + baseConf;
        String[] cmds = {hadoopConf};

        String cmd = yarnLocation + " logs -applicationId " + applicationId + " > " + filePath;
        cmds = ArrayUtils.add(cmds, cmd);

        return cmds;
    }

    private String processErrLog(Process process) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            LinkedList<String> list = new LinkedList<>();
            while (process.isAlive()) {
                String line = reader.readLine();
                if (StringUtils.isNotBlank(line)) {
                    list.add(line);
                }
            }

            if (process.exitValue() != 0) {
                return StringUtils.join(list, "\n");
            } else {
                return null;
            }
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
