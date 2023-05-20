package io.github.melin.spark.jobserver.driver.task;

import io.github.melin.spark.jobserver.api.SparkJob;
import io.github.melin.spark.jobserver.api.JobServerException;
import io.github.melin.spark.jobserver.core.util.CommonUtils;
import io.github.melin.spark.jobserver.driver.InstanceContext;
import io.github.melin.spark.jobserver.driver.SparkDriverEnv;
import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.driver.support.SparkClassLoader;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import io.github.melin.superior.common.relational.Statement;
import io.github.melin.superior.parser.appjar.AppJarHelper;
import io.github.melin.superior.parser.appjar.AppJarInfo;
import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * huaixin 2022/4/8 3:00 PM
 */
@Service
public class SparkAppTask extends AbstractSparkTask {

    private static final Logger LOG = LoggerFactory.getLogger(SparkAppTask.class);

    @Value("${driver.hdfs.home}/tempJars")
    protected String sparkTempJars;

    @Override
    protected void executeJobText(InstanceDto instanceDto) throws Exception {
        String jarHdfsPath = null;
        try {
            Configuration hadoopConf = SparkDriverEnv.hadoopConfiguration();

            String noCommentJobText = CommonUtils.cleanSqlComment(instanceDto.getJobText());
            List<Statement> statements = AppJarHelper.getStatementData(noCommentJobText);
            boolean executeJar = false;
            for (Statement statement : statements) {
                if (statement instanceof AppJarInfo) {
                    AppJarInfo data = (AppJarInfo) statement;
                    String filePath = data.getResourceName();
                    String className = data.getClassName();
                    List<String> params = data.getParams();
                    InstanceContext.setJobClassName(className);

                    SparkClassLoader loader = new SparkClassLoader(new URL[]{}, this.getClass().getClassLoader());
                    Thread.currentThread().setContextClassLoader(loader);

                    LogUtils.info("load jar: " + filePath);

                    //String destPath = createTempHdfsFile(hadoopConf, instanceDto, filePath);
                    String defaultFs = hadoopConf.get("fs.defaultFS");
                    jarHdfsPath = defaultFs + filePath;
                    loader.addJar(jarHdfsPath);
                    Class<?> clazz = loader.loadClass(className);
                    String groupId = "sg-" + UUID.randomUUID();

                    Object job = clazz.newInstance();
                    LOG.info("exec job classname: {}", className);
                    if (job instanceof SparkJob) {
                        SparkJob sparkJob = (SparkJob) job;
                        SparkDriverEnv.getSparkContext().setJobGroup(groupId, "", true);
                        SparkDriverEnv.getSparkContext().addJar(jarHdfsPath);

                        sparkJob.runJob(SparkDriverEnv.getSparkSession(), params.toArray(new String[0]));

                        Thread.currentThread().setContextClassLoader(SparkAppTask.class.getClassLoader());
                    } else {
                        throw new JobServerException("{} 不是SparkJob的实例", className);
                    }

                    executeJar = true;
                }
            }

            if (!executeJar) {
                LogUtils.warn("no job has executed!");
            }
        } finally {
            InstanceContext.setJobClassName("");
            //deleteJarHdfsPath(jarHdfsPath);
        }
    }
}
