package com.github.melin.jobserver.extensions.execution

import com.github.melin.jobserver.extensions.util.zip.{ProcessFile, ZipFileInputFormat}
import io.github.melin.spark.jobserver.api.JobServerException
import io.github.melin.superior.parser.spark.relational.CreateFileView
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{BytesWritable, Text}
import org.apache.spark.sql.execution.command.LeafRunnableCommand
import org.apache.spark.sql.{Row, SparkSession}

import java.util.UUID

case class CreateFileViewCommand(createFileView: CreateFileView) extends LeafRunnableCommand {
  private val options = createFileView.getProperties
  private val filePath = createFileView.getPath
  private val tableName = createFileView.getTableId.getTableName

  override def run(sparkSession: SparkSession): Seq[Row] = {
    val hadoopConf = sparkSession.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(hadoopConf)
    if (!fs.exists(new Path(filePath))) {
      throw new JobServerException()("文件不存在: " + filePath + ", 可以是HDFS完整路径，或者我的资源下路径")
    }


    if (filePath.endsWith(".zip")) {
      val zipFileRDD = sparkSession.sparkContext.newAPIHadoopFile(
        filePath,
        classOf[ZipFileInputFormat],
        classOf[Text],
        classOf[BytesWritable],
        hadoopConf)

      val location = "/temp/" + UUID.randomUUID().toString
      try {
        val fileType = options.get("fileType")

        zipFileRDD.foreach { zip =>
          var fileName = zip._1.toString
          fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length)
          if (!fileName.startsWith(".")) {
            if (fileName.endsWith(".csv")) {
              val content = zip._2
              ProcessFile.write(hadoopConf, fileName, content, location)
            } else if (fileName.endsWith(".json")) {
              val content = zip._2
              ProcessFile.write(hadoopConf, fileName, content, location)
            }
          }
        }

        if ("csv".equals(fileType)) {
          val csvRDD = sparkSession.read.options(options).csv(location)
          csvRDD.createOrReplaceTempView(tableName)
          sparkSession.conf.set("spark.load.zip.temp.path", location)
        } else if ("json".equals(fileType)) {
          val jsonRDD = sparkSession.read.options(options).json(location)
          jsonRDD.createOrReplaceTempView(tableName)
          sparkSession.conf.set("spark.load.zip.temp.path", location)
        } else {
          throw new JobServerException("文件格式不支持：" + fileType + ", zip文件请添加参数 fileType, 指定 csv 或 json")
        }
      } catch {
        case e: Exception =>
          fs.delete(new Path(location), true)
          throw new RuntimeException(e.getMessage, e)
      }

    } else if (filePath.endsWith(".csv")) {
      sparkSession.read.options(options).csv(filePath).createOrReplaceTempView(tableName)
    } else if (filePath.endsWith(".json")) {
      sparkSession.read.options(options).json(filePath).createOrReplaceTempView(tableName)
    } else if (filePath.endsWith(".xlsx")) {
      sparkSession.read
        .options(options)
        .format("com.crealytics.spark.excel")
        .load(filePath).createOrReplaceTempView(tableName)
    } else {
      throw new Exception("当前只支持导入json、csv和xlsx文件")
    }
    Seq.empty[Row]
  }
}
