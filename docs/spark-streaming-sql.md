### Spark Streaming SQL
使用 SQL 简化 Spark Streaming 应用，对接数据数据源: debezium json、kafka、canal、hudi cdc

### 1、Example: kafka 数据写入hudi

```sql
CREATE Stream TABLE tdl_kafka_message (
    id string,
    userid string,
    city '/ext/city' string, 
    kafka_topic string
)
WITH (
    type = 'kafka',
    subscribe = 'hudi_topic', 
    format = 'json',
    includeHeaders = 'true',
    failOnDataLoss = 'false',
    kafka.group.id = 'demo',
    failOnDataLoss = 'false',
    kafka.bootstrap.servers = '52.130.252.109:9092'
);

insert into bigdata.test_huid_stream_json_dt
SELECT id, userid, city, kafka_topic, date_format(current_timestamp, "yyyyMMddHH") ds 
FROM tdl_kafka_message;
```

### 2、Example: Hudi 增量查询写入 hudi

kafka 数据写入hudi，Hudi 增量查询写入 hudi，实现实时数仓

```sql
CREATE Stream TABLE tdl_hudi_stream_json_dt
WITH (
    type = 'hudi',
    databaseName = "bigdata",
    tableName = "hudi_stream_json_dt",
);

insert into bigdata.dws_orders
SELECT id, userid, city
FROM tdl_hudi_stream_json_dt where city is not null
```

### 资料

1. https://blog.51cto.com/u_14693305/4765083
2. [Apache Hudi 建表需要考虑哪些参数？（Spark）-- 上篇](https://mp.weixin.qq.com/s/oSkthOKqs9DxfBA7EoeaJw)
