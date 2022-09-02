## REST job server for Apache Spark 

主要特点

1. 通过Rest API 提交spark 作业运行，支持sql，java/scala，python类型作业，解耦业务系统与spark 集群。
2. Spark Job 运行资源相互隔离以及高可用性，每一个job 独立运行在一个Spark driver中。
3. 预启动 Spark Driver，提高Job 启动速度，Driver 共享运行多个Job(同时只有一个job运行)
4. 支持多Yarn集群部署，Client 提交Job 到指定集群运行。
5. Driver 定制化，可以实现比较多能力，例如：表权限，碎片文件压缩，DQC等功能。
6. 支持作业实例任务依赖(DAG),
7. 支持kerberos 认证

已经有kyuubi，livy，为什么开发jobserver？几年前就开始中台产品工作, DataStudio需要有一个getway 提交作业，spark 往yarn 提交driver会比较慢，为了更好的交互体验，需要预想启动spark driver，类型jdbc connection 管理spark driver，同时需要解决资源隔离问题。那个时候还没有kyuubi。kyuubi目前支持sql，java/scala 任务开发中，缺少python任务能力。工作中遇到大部分任务是python 作业，spark 最开始定位面向AI人员工具，pyspark是最大优势。livy主要为了交互式场景，需要客户端管理sesssionId。spark thrift 没有资源离，一个作业可能就会把所有资源用户，导致其它用户无法使用。

@TODO
1. 集成k8s
2. 支持作业定时调度, 可以减少其它调度系统依赖。
3. 集成数据权限、数据血缘(借鉴 kyuubi，但不依赖ranger)

## 一、Build

```
mvn clean package -Prelease,hadoop-2 -DlibScope=provided
mvn clean package -Prelease,hadoop-3 -DlibScope=provided
mvn clean package -Prelease,cdh6 -DlibScope=provided
```

## 二、作业实例接口
包含作业实例提交、作业实例状态查询、作业实例运行日志查询、终止作业实例接口，具体接口：[Rest API](https://github.com/melin/spark-jobserver/blob/master/jobserver-admin/src/main/java/io/github/melin/spark/jobserver/web/rest/JobServerRestApi.java)

## 三、作业开发
spark jar和python 作业类型，作业代码中不需要初始化sparkSession，SparkJobserver 初始化sparkSession，传递给作业，目的是为了统一规范作业配置，以及共享sparkSession。

### 1、Spark jar 作业
请参考: [Spark jar](https://github.com/melin/spark-jobserver/tree/master/jobserver-api)

### 2、Spark pyhton 作业
python 作业入口方法为：main，
```python
def main(sparkSession):
  print("hello world")
```

## 四、Yarn Cluster 模式部署
### 1、准备环境
Spark 任务运行环境：Hadoop 2.7.7，Spark 3.3.0。为避免每次任务运行上传jar，提前把相关jar 上传到 hdfs 路径。根路径：/user/superior/spark-jobserver (可以在集群管理中通过，修改jobserver.driver.home 参数设置)，根路径下有不同jar 和 目录
1. spark-jobserver-driver-0.1.0.jar  -- spark jobserver driver jar，jar 参考编译spark jobserver 部分。
2. aspectjweaver-1.9.9.1.jar  -- aspectj 拦截spark 相关代码，实现功能增强，直接maven 仓库下载
3. spark-3.3.0  --Spark 依赖所有jar，从spark 官网下载: spark-3.3.0-bin-hadoop2.tgz, 解压后把jars 目录下所有jar 上传到 spark-3.3.0 目录。

### 2、Yarn 集群配置要求
1. yarn 开启日志聚合，方便出现问题查看日志
2. yarn 配置spark_shuffle，spark 需要开启动态资源分配
3. yarn 开启cgroup cpu 资源隔离，避免资源争抢
4. 开启 history server
5. hdfs block 设置为256M

### 3、部署 jobserver admin 服务

上传 spark-jobserver-0.1.0.tar.gz 文件到服务器，直接解压生成目录：spark-jobserver-0.1.0
> tar -zxf spark-jobserver-0.1.0.tar.gz

创建数据jobserver，执行 script/jobserver.sql 脚本，创建表。

### 4、集群相关参数

[参考代码](https://github.com/melin/spark-jobserver/blob/master/jobserver-admin/src/main/java/io/github/melin/spark/jobserver/SparkJobServerConf.java)

### 5、启动服务

```
./bin/server.sh start dev
-- 启动脚本有两个参数
第一个参数可选值：start、stop、restart、status、log，启动status 和 log 不需要指定第二参数。
第二个参数可选值：dev、test、production。对应spring boot profile，对应应用启动加载conf目录下application-[profile].properties 文件
```

Web Console: http://ip:9001, 默认账号/密码: jobserver/jobserver2022

## 五、相关项目
1. https://gitee.com/melin/bee
2. https://github.com/melin/superior-sql-parser
3. https://github.com/melin/superior-sql-formatter
4. https://github.com/melin/superior-sql-autocomplete
5. https://github.com/melin/datatunnel
6. https://github.com/melin/flink-jobserver

