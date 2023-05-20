package io.github.melin.spark.jobserver.driver.util

import com.typesafe.scalalogging.Logger
import io.github.melin.spark.jobserver.core.exception.SparkJobException
import io.github.melin.superior.common.relational.dml.InsertTable
import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hudi.DataSourceWriteOptions
import org.apache.hudi.common.model.{HoodieTableType, WriteOperationType}
import org.apache.hudi.common.table.HoodieTableMetaClient
import org.apache.hudi.config.{HoodieCompactionConfig, HoodieWriteConfig}
import org.apache.hudi.hive.{HiveStylePartitionValueExtractor, HiveSyncConfigHolder}
import org.apache.hudi.sync.common.HoodieSyncConfig
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.catalog.CatalogTable
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 多数据源简单适配
 */
object HudiUtils {
  private val LOGGER = Logger(HudiUtils.getClass)

  private val PARTITION_COL_NAME = "ds";

  def isHoodieTable(table: CatalogTable): Boolean = {
    table.provider.map(_.toLowerCase(Locale.ROOT)).orNull == "hudi"
  }

  def isHoodieTable(spark: SparkSession,
                    tableName: String,
                    database: String): Boolean = {
    val tableId = TableIdentifier(tableName, Some(database))
    val table = spark.sessionState.catalog.getTableMetadata(tableId)
    isHoodieTable(table)
  }

  private def getHudiTablePrimaryKey(spark: SparkSession,
                                     catalogTable: CatalogTable,
                                     properties: Map[String, String]): String = {

    val qualifiedName = catalogTable.qualifiedName
    if (isHoodieTable(catalogTable)) {
      // scalastyle:off hadoopconfiguration
      val hadoopConf = spark.sparkContext.hadoopConfiguration
      // scalastyle:on hadoopconfiguration
      val tablPath = catalogTable.location.toString
      val metaClient = HoodieTableMetaClient.builder().setConf(hadoopConf).setBasePath(tablPath).build()
      val tableType = metaClient.getTableType
      if (tableType == null || HoodieTableType.COPY_ON_WRITE == tableType) {
        LOGGER.error(s"${qualifiedName} ${tableType} location: $tablPath")
        throw new SparkJobException(s"${qualifiedName} 是hudi COW类型表，不支持流数据写入，请使用MOR类型表")
      } else {
        if (catalogTable.partitionColumnNames.isEmpty
          || catalogTable.partitionColumnNames.size != 1
          || !catalogTable.partitionColumnNames.head.equals(PARTITION_COL_NAME)) {
          throw new SparkJobException(s"${qualifiedName} 必须是分区表，写分区字段名必须为: $PARTITION_COL_NAME")
        }

        LOGGER.info("{} table properties: {}", qualifiedName, properties.mkString(","))
        val primaryKey = properties("primaryKey")
        if (StringUtils.isBlank(primaryKey)) {
          throw new SparkJobException(s"$catalogTable 主键不能为空")
        }
        LogUtils.info(qualifiedName + " 主键为 " + primaryKey)

        primaryKey
      }
    } else {
      throw new SparkJobException(s"${catalogTable.qualifiedName} 不是hudi 类型表，不支持流数据写入")
    }
  }

  /**
   * delta insert select 操作
   */
  def deltaInsertStreamSelectAdapter(spark: SparkSession,
                                     insertTable: InsertTable): Unit = {
    val querySql = insertTable.getQuerySql
    val schemaName = getDbName(spark, insertTable.getTableId.getSchemaName)
    val tableName = insertTable.getTableId.getTableName
    val catalogTable = spark.sessionState.catalog.getTableMetadata(TableIdentifier(tableName, Some(schemaName)))
    val properties = spark.sessionState.catalog.externalCatalog.getTable(schemaName, tableName).properties
    val primaryKey = getHudiTablePrimaryKey(spark, catalogTable, properties)

    val driverHdfsHome = System.getProperties.getProperty("driver.hdfs.home")
    val checkpointLocation = s"$driverHdfsHome/spark-checkpoints/$schemaName.db/$tableName"
    mkCheckpointDir(spark, checkpointLocation)

    val streamingInput = spark.sql(querySql)
    val writer = streamingInput.writeStream.format("org.apache.hudi")
      .option(DataSourceWriteOptions.OPERATION.key, WriteOperationType.INSERT.value)
      .option(DataSourceWriteOptions.TABLE_TYPE.key, HoodieTableType.MERGE_ON_READ.name)
      .option(DataSourceWriteOptions.RECORDKEY_FIELD.key, primaryKey)
      .option(DataSourceWriteOptions.PARTITIONPATH_FIELD.key, PARTITION_COL_NAME)
      .option(DataSourceWriteOptions.HIVE_STYLE_PARTITIONING.key, "true")
      .option(DataSourceWriteOptions.PRECOMBINE_FIELD.key, PARTITION_COL_NAME)
      .option(HoodieCompactionConfig.INLINE_COMPACT_NUM_DELTA_COMMITS.key, "5")
      .option(DataSourceWriteOptions.ASYNC_COMPACT_ENABLE.key, "true")
      .option(DataSourceWriteOptions.ASYNC_CLUSTERING_ENABLE.key, "true")
      .option(DataSourceWriteOptions.STREAMING_RETRY_CNT.key, 3)
      .option(HoodieWriteConfig.TBL_NAME.key, tableName)
      .option("checkpointLocation", checkpointLocation)
      .option("path", catalogTable.location.toString)
      .outputMode(OutputMode.Append)

    writer.option(HoodieSyncConfig.META_SYNC_TABLE_NAME.key, tableName)
      .option(HoodieSyncConfig.META_SYNC_DATABASE_NAME.key, schemaName)
      .option(HiveSyncConfigHolder.HIVE_SYNC_MODE.key, "HMS")
      .option(HiveSyncConfigHolder.HIVE_SYNC_ENABLED.key, "true")
      .option(HoodieSyncConfig.META_SYNC_ENABLED.key, "false")

      .option(HoodieSyncConfig.META_SYNC_PARTITION_EXTRACTOR_CLASS.key,
        classOf[HiveStylePartitionValueExtractor].getName)
      .option(HoodieSyncConfig.META_SYNC_PARTITION_FIELDS.key, PARTITION_COL_NAME)

    writer.trigger(Trigger.ProcessingTime(1, TimeUnit.SECONDS)).start().awaitTermination()
  }

  private def mkCheckpointDir(sparkSession: SparkSession, path: String): Unit = {
    val configuration = sparkSession.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(configuration)
    if (!fs.exists(new Path(path))) fs.mkdirs(new Path(path))
  }

  /**
   * 如果用户没有填写数据库名，则以当前的项目名作为数据库名
   */
  private def getDbName(spark: SparkSession,
                        name: String): String = {
    if (StringUtils.isBlank(name)) {
      spark.catalog.currentDatabase
    } else {
      name
    }
  }
}
