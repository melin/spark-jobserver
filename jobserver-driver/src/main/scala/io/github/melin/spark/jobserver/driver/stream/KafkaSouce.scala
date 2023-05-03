package io.github.melin.spark.jobserver.driver.stream

import com.google.common.collect.Lists
import io.github.melin.spark.jobserver.core.exception.SparkJobException
import io.github.melin.spark.jobserver.driver.util.LogUtils
import io.github.melin.superior.common.relational.create.CreateTable
import org.apache.commons.lang3.StringUtils
import org.apache.kafka.clients.admin.AdminClient
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import java.util
import java.util.Properties
import scala.collection.JavaConverters._

/**
 * Created by libinsong on 2020/7/29 12:06 下午
 */
class KafkaSouce extends Logging {

  def createStreamTempTable(spark: SparkSession, createTable: CreateTable) {
    val tableName = createTable.getTableId.getTableName
    val properties = createTable.getProperties
    checkConfig(properties);
    checkKafkaStatus(properties)

    val format = properties.get("format")
    val lineRow = createDataSet(spark, properties)
    if ("json".equalsIgnoreCase(format)) {
      val ds = convertJsonDataSet(spark, lineRow, createTable)
      ds.createOrReplaceTempView(tableName);
    } else {
      val ds = convertTextDataSet(spark, lineRow, createTable)
      ds.createOrReplaceTempView(tableName);
    }
  }

  private def checkConfig(properties: util.Map[String, String]): Unit = {
    val format = properties.get("format");
    if (StringUtils.isBlank(format)) {
      throw new SparkJobException("format 不能为空")
    } else if (!"text".equalsIgnoreCase(format) && !"json".equalsIgnoreCase(format)) {
      throw new SparkJobException("format 支持: text, json格式，当前格式为: " + format)
    }

    var isValid = properties.containsKey("kafka.bootstrap.servers") &&
      !properties.get("kafka.bootstrap.servers").trim.isEmpty
    if (!isValid) {
      throw new SparkJobException("kafka.bootstrap.servers 不能为空")
    }

    isValid = properties.containsKey("subscribe") &&
      !properties.get("subscribe").trim.isEmpty
    if (!isValid) {
      throw new SparkJobException("subscribe 不能为空")
    }
  }

  private def checkKafkaStatus(options: util.Map[String, String]): Unit = {
    val servers = options.get("kafka.bootstrap.servers")
    val subscribe = options.get("subscribe")

    val props = new Properties()
    props.put("bootstrap.servers", servers)
    props.put("connections.max.idle.ms", "10000")
    props.put("request.timeout.ms", "5000")

    var adminClient: AdminClient = null
    try {
      adminClient = AdminClient.create(props)
      val topics = adminClient.listTopics().namesToListings().get()
      if (!topics.containsKey(subscribe)) {
        val value = adminClient.listTopics().names().get().asScala.mkString(",")
        throw new SparkJobException("topic 不存在: " + subscribe + ", 可用topic: " + value)
      }

      LogUtils.info("检测kafka 集群可用")
    } catch {
      case e: Exception => throw new SparkJobException("kafka broker " + servers + " 不可用: " + e.getMessage)
    } finally if (adminClient != null) adminClient.close()
  }

  private def createDataSet(spark: SparkSession, options: util.Map[String, String]): Dataset[Row] = {
    options.remove("format")
    options.remove("type")
    val lines = spark.readStream.format("kafka").options(options)
      .option("enable.auto.commit", "false")
      .load

    if (options.containsKey("includeHeaders") && "true".equals(options.get("includeHeaders"))) {
      lines.selectExpr("CAST(value AS STRING) as content", "CAST(key AS STRING) as kafka_key",
        "topic as kafka_topic", "partition as kafka_partition", "offset as kafka_offset",
        "timestamp as kafka_timestamp", "timestampType as kafka_timestampType")
    } else {
      lines.selectExpr("CAST(value AS STRING) as content", "CAST(key AS STRING) as kafka_key",
        "topic as kafka_topic", "partition as kafka_partition", "offset as kafka_offset",
        "timestamp as kafka_timestamp", "timestampType as kafka_timestampType", "headers as kafka_headers")
    }
  }

  private def convertJsonDataSet(spark: SparkSession, lineRow: Dataset[Row], createTable: CreateTable): Dataset[Row] = {
    val tableName = "tdl_stream_" + createTable.getTableId.getTableName
    lineRow.createOrReplaceTempView(tableName);

    val jsonPaths = Lists.newArrayList[String]()
    val fieldNames = Lists.newArrayList[String]()
    val kafkaFields = Lists.newArrayList[String]()
    for (column <- createTable.getColumnRels.asScala) {
      var jsonPath = column.getJsonPath
      val fieldName = column.getName
      if (!StringUtils.startsWith(column.getName, "kafka_")) {
        if (StringUtils.isBlank(jsonPath)) {
          jsonPath = "'/" + fieldName + "'"
        } else {
          jsonPath = "'" + jsonPath + "'"
        }

        jsonPaths.add(jsonPath)
        fieldNames.add(fieldName)
      } else {
        kafkaFields.add(column.getName)
      }
    }

    var streamSql = if (kafkaFields.size() > 0) {
      s"select ${StringUtils.join(kafkaFields, ',')}, stream_json_extract_value(content, ${StringUtils.join(jsonPaths, ',')}) " +
        s"as (${StringUtils.join(fieldNames, ',')}) from $tableName"
    } else {
      s"select stream_json_extract_value(content, ${StringUtils.join(jsonPaths, ',')}) " +
        s"as (${StringUtils.join(fieldNames, ',')}) from $tableName"
    }

    log.info("stream sql: {}", streamSql)
    streamSql = convertDataType(streamSql, createTable)
    spark.sql(streamSql)
  }

  private def convertTextDataSet(spark: SparkSession, lineRow: Dataset[Row], createTable: CreateTable): Dataset[Row] = {
    val tableName = "tdl_stream_" + createTable.getTableId.getTableName
    lineRow.createOrReplaceTempView(tableName)

    val kafkaFields = Lists.newArrayList[String]()
    val colNames = Lists.newArrayList[String]()
    for (column <- createTable.getColumnRels.asScala) {
      if (StringUtils.startsWith(column.getName, "kafka_")) {
        kafkaFields.add(column.getName)
      } else {
        colNames.add(column.getName)
      }
    }

    if (colNames.size() != 1) {
      throw new SparkJobException("format 为text 时，有且仅有一个前缀非kafka_字段, 当前字段: " + String.join(",", colNames))
    }

    val streamSql = if (kafkaFields.size() > 0) {
      s"select ${StringUtils.join(kafkaFields, ',')}, content as ${colNames.get(0)} from $tableName"
    } else {
      s"select content as ${colNames.get(0)} from $tableName"
    }

    log.info("stream sql: {}", streamSql)
    spark.sql(streamSql)
  }

  // 支持数据类型: STRING | BOOLEAN | INT | BIGINT | FLOAT | DOUBLE | DATE  | TIMESTAMP
  private def convertDataType(streamSql: String, createTable: CreateTable): String = {
    val fieldNames = Lists.newArrayList[String]()
    for (column <- createTable.getColumnRels.asScala) {
      if ("int".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else if ("bigint".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else if ("boolean".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else if ("float".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else if ("double".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else if ("date".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else if ("timestamp".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else if ("string".equalsIgnoreCase(column.getType)) {
        fieldNames.add(column.getName)
      } else {
        throw new SparkJobException("不支持的数据类型: " + column.getType)
      }
    }

    s"select ${StringUtils.join(fieldNames, ',')} from ($streamSql) a"
  }
}
