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