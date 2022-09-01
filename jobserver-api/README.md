### 依赖
```xml
<dependency>
    <groupId>io.github.melin.spark.jobserver</groupId>
    <artifactId>spark-jobserver-api</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Spark Jar 任务

```java
import io.github.melin.spark.jobserver.api.LogUtils;
import io.github.melin.spark.jobserver.api.SparkJob;
import org.apache.spark.sql.SparkSession;

public class SparkDemoTest implements SparkJob {

    @Override
    public void runJob(SparkSession sparkSession, String[] args) throws Exception {

        // LogUtils 方法打印的日志，收集到作业实例日志文件中，而不是输出到yarn 日志中
        LogUtils.info("execute spark examples");

        LogUtils.info("hello {}", args);

        sparkSession.sql("select * from bigdata.test_demo_dt").show();
    }
}
```

### Build
将自定义实现的类打成jar包，上传到HDFS中，新建Spark Jar作业:
jar包名 入口类名 [自定义参数, 多个参数空格分隔]

可以输入多行命令，各行之间分号分割, 例如：

job-demo-1.0.0.jar com.aloudata.spark.job.JavaJobDemo huaixin;

推荐使用maven-shade-plugin 打包，排除SPAKE_HOME/jars目录中不存在的jar，合并成一个jar。