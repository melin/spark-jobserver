package com.github.melin.jobserver.extensions.execution

import com.github.melin.jobserver.extensions.util.{CommonUtils, DataConvertionUtil}
import io.github.melin.spark.jobserver.api.JobServerException

import java.io.{ByteArrayInputStream, InputStream}
import java.util
import java.util.UUID
import scala.collection.JavaConverters._
import io.github.melin.superior.parser.spark.relational.ExportData
import net.lingala.zip4j.io.outputstream.ZipOutputStream
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs._
import org.apache.hadoop.io.IOUtils
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.execution.QueryExecution
import org.apache.spark.sql.execution.command.LeafRunnableCommand
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.slf4j.LoggerFactory
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.EncryptionMethod
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.catalyst.catalog.CatalogTable

case class ExportTableCommand(
    exportData: ExportData,
    nameAndQuery: Seq[(String, String)]
) extends LeafRunnableCommand {

  private final val logger = LoggerFactory.getLogger(classOf[ExportTableCommand])

  private val exportFileName = exportData.getPath
  private val options = exportData.getProperties

  override def run(sparkSession: SparkSession): Seq[Row] = {
    if (StringUtils.contains(exportFileName, "/")) {
      throw new JobServerException(exportFileName + "导出文件名不能包含: /")
    }

    val catalog = sparkSession.sessionState.catalog

    val tableName = exportData.getTableId.getTableName
    val db = CommonUtils.getCurrentDatabase(exportData.getTableId.getSchemaName)
    var dataFrame: DataFrame = null
    val dbTable = TableIdentifier(tableName, Option(db))

    // 如果有 cte 语法，先注册临时表
    if (nameAndQuery.nonEmpty) {
      nameAndQuery.foreach {
        case (name, query) =>
          sparkSession.sql(query).createOrReplaceTempView(name)
      }
      dataFrame = sparkSession.table(tableName)
    } else if (catalog.tableExists(dbTable)) {
      val catalogTable = catalog.getTableMetadata(dbTable)
      logger.info("table {} type {}, location: {}", tableName, catalogTable.tableType, catalogTable.location.getPath)

      val condition = getPartitionCondition(catalogTable)
      val sql = if (StringUtils.isBlank(condition)) {
        s"select * from ${db}.${tableName}"
      } else {
        s"select * from ${db}.${tableName} where $condition"
      }

      logger.info("export sql: " + sql)
      dataFrame = sparkSession.sql(sql)
    } else if (catalog.getTempView(tableName).isDefined) {
      val logicPlan = catalog.getTempView(tableName).get
      val execution = sparkSession.sessionState.executePlan(logicPlan)
      execution.assertAnalyzed()

      val clazz = classOf[Dataset[Row]]
      val constructor = clazz.getDeclaredConstructor(classOf[SparkSession], classOf[QueryExecution], classOf[Encoder[_]])
      dataFrame = constructor.newInstance(sparkSession, execution, RowEncoder.apply(execution.analyzed.schema))
    } else {
      throw new JobServerException(String.format("table %s is neither exists in database %s nor in the temp view!", tableName, db))
    }

    if (StringUtils.endsWithIgnoreCase(exportFileName, ".json") ||
      StringUtils.endsWithIgnoreCase(exportFileName, ".json.zip")) {
      exportJson(sparkSession, dataFrame)
    } else if (StringUtils.endsWithIgnoreCase(exportFileName, ".dc.xlsx")) {
      exportExcel(sparkSession, dataFrame)
    } else if (StringUtils.endsWithIgnoreCase(exportFileName, ".xlsx")) {
      exportCsv(sparkSession, dataFrame, true)
    } else {
      exportCsv(sparkSession, dataFrame, false)
    }

    Seq.empty[Row]
  }

  private def exportExcel(sparkSession: SparkSession, df: DataFrame): Unit = {
    val dataFrame: DataFrame = df

    val hadoopConf = sparkSession.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(hadoopConf)

    val overwrite = if (options.containsKey("overwrite")) {
      options.get("overwrite").toBoolean
    } else false

    val dstPath = "/temp/export/" + exportFileName

    if (overwrite) {
      fs.delete(new Path(dstPath), true)
    } else {
      if (fs.exists(new Path(dstPath))) {
        throw new JobServerException("file " + exportFileName + " already exists in " + dstPath)
      }
    }

    dataFrame.write
      .format("com.crealytics.spark.excel")
      .option("dataAddress", "MyTable[#All]")
      .option("useHeader", "true")
      .save(dstPath)
  }

  private def exportJson(sparkSession: SparkSession, df: DataFrame): Unit = {
    var dataFrame: DataFrame = df

    val uuid = UUID.randomUUID().toString
    val savePath = "/user/superior/temp/" + UUID.randomUUID().toString

    val hadoopConf = sparkSession.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(hadoopConf)

    try {
      if (fs.exists(new Path(savePath))) fs.delete(new Path(savePath), true)

      val fileCount = if (options.containsKey("fileCount")) {
        options.get("fileCount").toInt
      } else 0

      if (fileCount > 0) {
        dataFrame = dataFrame.coalesce(fileCount)
      }

      var writer = dataFrame.write.format("json")
      val overwrite = if (options.containsKey("overwrite")) {
        options.get("overwrite").toBoolean
      } else false

      for (entry <- options.entrySet().asScala) {
        if (!StringUtils.equalsIgnoreCase(entry.getKey, "fileCount")
          && !StringUtils.equalsIgnoreCase(entry.getKey, "compression")
          && !StringUtils.equalsIgnoreCase(entry.getKey, "password")) {
          writer = writer.option(entry.getKey, entry.getValue)
        }
      }

      val dstPath = "/temp/export/" + exportFileName

      if (overwrite) {
        fs.delete(new Path(dstPath), true)
      } else {
        if (fs.exists(new Path(dstPath))) {
          throw new JobServerException("file " + exportFileName + " already exists in " + dstPath)
        }
      }

      writer.save(savePath)

      if (StringUtils.endsWithIgnoreCase(exportFileName, ".zip")) {
        zipDir(fs, new Path(savePath), new Path(dstPath), null, hadoopConf)
      } else {
        CommonUtils.copyMerge(fs, new Path(savePath), fs, new Path(dstPath), true, hadoopConf)
      }

      logger.info("saved json file in " + dstPath)
    } finally {
      if (fs.exists(new Path(savePath))) {
        fs.delete(new Path(savePath), true)
      }
    }
  }

  private def exportCsv(sparkSession: SparkSession, df: DataFrame, excel: Boolean): Unit = {
    var dataFrame: DataFrame = df

    val uuid = UUID.randomUUID().toString
    val jobCode = sparkSession.sparkContext.getConf.get("spark.dataworker.job.code", uuid)
    val savePath = "/user/superior/temp/export_" + jobCode

    val hadoopConf = sparkSession.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(hadoopConf)

    try {
      if (fs.exists(new Path(savePath))) {
        fs.delete(new Path(savePath), true)
      }

      val fileCount = if (options.containsKey("fileCount")) {
        options.get("fileCount").toInt
      } else 0
      val complexTypeToJson = if (options.containsKey("complexTypeToJson")) {
        options.get("complexTypeToJson").toBoolean
      } else false

      if (fileCount > 0) {
        dataFrame = dataFrame.coalesce(fileCount)
      }

      if (complexTypeToJson) {
        val complexTypeNames = new util.ArrayList[String]()
        dataFrame.schema.fields.foreach(field => {
          field.dataType match {
            case _: ArrayType | _: MapType | _: StructType => complexTypeNames.add(field.name)
            case _ =>
          }
        })
        logger.info("complex type :" + complexTypeNames.asScala.mkString(","))
        complexTypeNames.asScala.foreach(name => {
          val col = dataFrame.col(name)
          dataFrame = dataFrame.withColumn(name + "_json", to_json(col))
          dataFrame = dataFrame.drop(name)
        })
      }

      var writer = dataFrame.write.format("csv")
      val delimiter = if (options.containsKey("delimiter")) {
        options.get("delimiter")
      } else ","
      val overwrite = if (options.containsKey("overwrite")) {
        options.get("overwrite").toBoolean
      } else false
      val header = if (options.containsKey("header")) {
        options.get("header").toBoolean
      } else true

      for (entry <- options.entrySet().asScala) {
        if (!StringUtils.equalsIgnoreCase(entry.getKey, "header")
          && !StringUtils.equalsIgnoreCase(entry.getKey, "inferSchema")
          && !StringUtils.equalsIgnoreCase(entry.getKey, "fileCount")
          && !StringUtils.equalsIgnoreCase(entry.getKey, "compression")
          && !StringUtils.equalsIgnoreCase(entry.getKey, "password")) {
          writer = writer.option(entry.getKey, entry.getValue)
        }
      }

      val dstPath = "/temp/export/" + exportFileName

      if (overwrite) {
        fs.delete(new Path(dstPath), true)
      } else {
        if (fs.exists(new Path(dstPath))) {
          throw new JobServerException("file " + exportFileName + " already exists in " + dstPath)
        }
      }

      writer.option("header", "false").option("inferSchema", "true").save(savePath)

      val list = new util.LinkedList[String]()
      for (field <- dataFrame.schema.fields) list.add(field.name)
      val head = StringUtils.join(list, delimiter) + "\n"
      if (StringUtils.endsWithIgnoreCase(exportFileName, ".zip")) {
        if (header) {
          zipDir(fs, new Path(savePath), new Path(dstPath), head, hadoopConf)
        } else {
          zipDir(fs, new Path(savePath), new Path(dstPath), null, hadoopConf)
        }
      } else if (StringUtils.endsWithIgnoreCase(exportFileName, ".csv") ||
        StringUtils.endsWithIgnoreCase(exportFileName, ".xlsx")) {

        if (header) {
          val out = fs.create(new Path(savePath + "/000000-head.csv"))
          out.write(head.getBytes("UTF-8"))
          out.close()
        }

        if (excel) {
          val tempDstPath = new Path("/temp/export/_temp_" + exportFileName)
          try {
            CommonUtils.copyMerge(fs, new Path(savePath), fs, tempDstPath, true, hadoopConf)
            DataConvertionUtil.csvToEXCEL(fs, tempDstPath, new Path(dstPath))
          } finally {
            fs.delete(tempDstPath, false)
          }
        } else {
          CommonUtils.copyMerge(fs, new Path(savePath), fs, new Path(dstPath), true, hadoopConf)
        }
      }
      logger.info("saved csv file in " + dstPath)
    } finally {
      if (fs.exists(new Path(savePath))) {
        fs.delete(new Path(savePath), true)
      }
    }
  }

  def zipDir(fs: FileSystem, srcDir: Path, dstFile: Path, head: String, conf: Configuration): Boolean = {
    val password = if (options.containsKey("password")) {
      options.get("password")
    } else ""

    if (password.nonEmpty && password.length < 6) {
      throw new JobServerException("密码长度不能小于6")
    }

    if (!fs.getFileStatus(srcDir).isDirectory) {
      return false
    } else {
      val encrypt = if (password.nonEmpty) true else false
      val zipParameters = buildZipParameters(encrypt);

      val out: FSDataOutputStream = fs.create(dstFile)
      val zipOut = if (encrypt) new ZipOutputStream(out, password.toCharArray)
        else new ZipOutputStream(out)

      try {
        val contents: Array[FileStatus] = fs.listStatus(srcDir)
        contents.foreach(content => {
          if (content.isFile) {
            zipParameters.setFileNameInZip(content.getPath.getName)
            zipOut.putNextEntry(zipParameters)

            val in: FSDataInputStream = fs.open(content.getPath)
            try {
              if (StringUtils.isNotBlank(head)) {
                val inputStream: InputStream = new ByteArrayInputStream(head.getBytes("UTF-8"))
                IOUtils.copyBytes(inputStream, zipOut, conf, false)
              }
              IOUtils.copyBytes(in, zipOut, conf, false)
            } finally {
              in.close()
            }

            zipOut.closeEntry();
          }
        })
      } finally {
        zipOut.close()
        out.close()
      }
    }

    fs.delete(srcDir, true)

    true
  }

  private def getPartitionCondition(catalogTable: CatalogTable): String = {
    if (catalogTable.partitionColumnNames.isEmpty && exportData.getPartitionVals.size() > 0) {
      throw new JobServerException("非分区表，不用指定分区")
    }

    if (catalogTable.partitionColumnNames.nonEmpty && exportData.getPartitionVals.size() == 0) {
      throw new JobServerException("分区表导出请指定具体分区")
    }

    if (catalogTable.partitionColumnNames.nonEmpty) {
      val map = new util.HashMap[String, String]()
      for (partition <- exportData.getPartitionVals.entrySet().asScala) {
        val key = partition.getKey
        val value = partition.getValue
        if (catalogTable.partitionColumnNames.contains(key)) {
          map.put(key, value)
        } else {
          throw new JobServerException(s"当前表没有分区字段: $key, 分区字段为: " + catalogTable.partitionColumnNames.mkString(","))
        }
      }

      if (map.keySet().size() != catalogTable.partitionColumnNames.size) {
        throw new JobServerException("当前表分区字段为: " + catalogTable.partitionColumnNames.mkString(","))
      }

      exportData.getPartitionVals.asScala.map { case (key, value) => key + " = " + value }.mkString(" and ")
    } else {
      null
    }
  }

  private def buildZipParameters(encrypt: Boolean) = {
    val zipParameters = new ZipParameters
    zipParameters.setEncryptionMethod(EncryptionMethod.AES)
    zipParameters.setEncryptFiles(encrypt)
    zipParameters
  }
}
