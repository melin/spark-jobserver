package com.github.melin.jobserver.extensions.execution

import java.util
import java.util.concurrent.{ForkJoinPool, TimeUnit}
import java.util.concurrent.atomic.AtomicLong
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.mutable.ParArray
import com.github.melin.jobserver.extensions.util.ByteUnit._
import com.github.melin.jobserver.extensions.util.{CommonUtils, FileType}
import com.typesafe.scalalogging.Logger
import io.github.melin.spark.jobserver.api.JobServerException
import io.github.melin.superior.parser.spark.relational.MergeFileData
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileStatus, FileSystem, Path}
import org.apache.orc.OrcFile
import org.apache.parquet.hadoop.ParquetFileReader
import org.apache.parquet.hadoop.util.{HadoopInputFile, HiddenFileFilter}
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.catalog.{CatalogTable, CatalogTablePartition}
import org.apache.spark.sql.execution.command.LeafRunnableCommand
import org.apache.spark.sql.{DataFrameWriter, Row, SparkSession}

import scala.collection.JavaConverters._

case class MergeSmallFileCommand(mergeFileData: MergeFileData) extends LeafRunnableCommand  {

  private val logger = Logger(classOf[MergeSmallFileCommand])

  private val options = mergeFileData.getProperties
  private val tableId = mergeFileData.getTableId

  override def run(sparkSession: SparkSession): Seq[Row] = {
    val catalog = sparkSession.sessionState.catalog
    val tableName = tableId.getTableName
    var schemaName = catalog.getCurrentDatabase
    if (tableId.getSchemaName != null) {
      schemaName = tableId.getSchemaName
    }

    val currTableIdentifier = TableIdentifier(tableName, Option(schemaName))
    val catalogTable = catalog.getTableRawMetadata(currTableIdentifier)
    val fileType = CommonUtils.getTableFileType(catalogTable)
    if (FileType.HUDI == fileType) {
      throw new JobServerException(s"${currTableIdentifier.unquotedString} 是hudi 类型表，不只支持compression")
    }

    val partions = getMergePartitions(sparkSession, catalogTable)
    if (partions == null) {
      merge(sparkSession, catalogTable, fileType, null)
    } else {
      logInfo(s"查询合并分区数量: ${partions.size}")
      partions.foreach(partition => {
        val msg = partition.spec.map { case (k, v) => s"$k=$v" }.mkString("/")
        logInfo(s"合并分区: $msg")
        merge(sparkSession, catalogTable, fileType, partition)
      })
    }
    Seq.empty[Row]
  }

  def merge(sparkSession: SparkSession, catalogTable: CatalogTable,
            fileType: String, partition: CatalogTablePartition): Seq[Row] = {
    var mergeTempDir = ""
    val conf = sparkSession.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(conf)
    try {
      val tablePath = catalogTable.location.getPath
      logger.info("table {} type {}, location: {}", catalogTable.identifier.table, catalogTable.location, catalogTable.tableType)

      if (partition == null) {
        mergeTempDir = tablePath + "/.mergeTemp"
        mergePath(sparkSession, fs, catalogTable, tablePath, mergeTempDir, fileType)
      } else {
        val location = partition.location.getPath
        mergeTempDir = location + "/.mergeTemp"
        mergePath(sparkSession, fs, catalogTable, location, mergeTempDir, fileType)
      }
    } catch {
      case e: Throwable =>
        logger.error("merge table error:" + ExceptionUtils.getStackTrace(e))
        throw e
    } finally {
      if (StringUtils.isNotBlank(mergeTempDir)) {
        val path = new Path(mergeTempDir)
        if (fs.exists(path)) {
          fs.delete(path, true)
        }
      }
    }

    Seq.empty[Row]
  }

  private def mergePath(sparkSession: SparkSession,
                        fs: FileSystem,
                        catalogTable: CatalogTable,
                        location: String,
                        mergeTempDir: String,
                        fileType: String): Unit = {

    if (!fs.exists(new Path(location))) {
      throw new JobServerException(location + " does not exist!")
    }

    val fileStatusList = fs.listStatus(new Path(location), HiddenFileFilter.INSTANCE)
    if (fileStatusList.isEmpty) {
      logWarning("表或分区为空")
      return
    }

    // 获取访问时间
    var accessTime = -28800000L // 文件最后访问时间,默认"1970-01-01 00:00:00"
    var modificationTime = -28800000L

    val inputFileList = new util.LinkedList[FileStatus]()
    val mergePaths = new util.LinkedList[String]()
    var fragmentFileCount = 0
    var totalSize = 0L
    val compression = getTableCompression(catalogTable)
    logger.info("table properties: " + catalogTable.properties.mkString(","))

    for (fileStatus <- fileStatusList) {
      val path = fileStatus.getPath

      if (fileStatus.isFile) {
        fragmentFileCount = fragmentFileCount + 1
        inputFileList.add(fileStatus)
        mergePaths.add(path.toString)
        totalSize = totalSize + fileStatus.getLen
      }

      val fileAccessTime = fileStatus.getAccessTime
      val fileModificationTime = fileStatus.getModificationTime
      if (fileAccessTime > accessTime) {
        accessTime = fileAccessTime
      }
      if (fileModificationTime > modificationTime) {
        modificationTime = fileModificationTime
      }
    }

    // 如果碎片文件少于两个则不合并
    if (fragmentFileCount <= 2 && !options.containsKey("compression")) {
      logger.info("fragment files <= 2, merge stopped")
      return
    }

    val beforeRowCount = fileType match {
      case FileType.ORC => statOrcRowCount(fs.getConf, inputFileList)
      case FileType.PARQUET => statParquetRowCount(fs.getConf, inputFileList)
    }

    val mergeNum = genOutputFileCount(beforeRowCount, totalSize)
    logger.info("merge temp path: " + location)

    val startTime = System.nanoTime()
    logger.info("prepare to merge data under path:{} total {} files", location, inputFileList.size())

    val tempPath = new Path(mergeTempDir)
    if (fs.exists(tempPath)) {
      logger.info(mergeTempDir + " 已经存在")
      fs.delete(tempPath, true)
    }

    fileType match {
      case FileType.ORC =>
        val df = sparkSession.read.orc(mergePaths.asScala: _*)
        // 小于 20G 使用 repartition，大于 20G 使用 coalesce
        if (totalSize < 20 * GiB) {
          val dfw = df.repartition(mergeNum).write.option("compression", compression)
          addTableOption(catalogTable, dfw)
          dfw.orc(mergeTempDir)
        } else {
          val dfw = df.coalesce(mergeNum).write.option("compression", compression)
          addTableOption(catalogTable, dfw)
          dfw.orc(mergeTempDir)
        }
      case FileType.PARQUET =>
        val df = sparkSession.read.parquet(mergePaths.asScala: _*)
        // 小于 20G 使用 repartition，大于 20G 使用 coalesce
        if (totalSize < 20 * GiB) {
          val dfw = df.repartition(mergeNum).write.option("compression", compression)
          addTableOption(catalogTable, dfw)
          dfw.parquet(mergeTempDir)
        } else {
          val dfw = df.coalesce(mergeNum).write.option("compression", compression)
          addTableOption(catalogTable, dfw)
          dfw.parquet(mergeTempDir)
        }
    }

    logger.info("merge table 结束")

    logger.info("统计压缩后文件记录数量")
    val afterRowCount = fileType match {
        case FileType.ORC => statOrcRowCount(fs, mergeTempDir)
        case FileType.PARQUET => statParquetRowCount(fs, mergeTempDir)
      }

    if (beforeRowCount != afterRowCount) {
      fs.delete(tempPath, true)
      logger.info("合并前后文件记录数不一致，退出合并，合并前：{}, 合并后：{}", beforeRowCount, afterRowCount)
      return
    }
    mergeFinish(sparkSession, mergeTempDir, location, fs, modificationTime, accessTime, inputFileList, tempPath)

    val execTimes = TimeUnit.MICROSECONDS.toSeconds(System.nanoTime() - startTime)
    val tableName = tableId.getTableName
    if (catalogTable.partitionColumnNames.nonEmpty) {
      val partSpec = StringUtils.substringAfter(location, tableName + "/")
      if (StringUtils.isNotBlank(partSpec)) {
        val msg = s"merge partition finished, $partSpec, compression: compression, fileCount: ${inputFileList.size()}, mergeNum: $mergeNum" +
          s", recordCount: $afterRowCount, execTime(ms): $execTimes"

        logger.info(msg)
        logInfo(msg)
      }
    } else {
      val msg = s"merge table finished, compression: $compression, fileCount: ${inputFileList.size()}, mergeNum: $mergeNum" +
        s", recordCount: $afterRowCount, execTime(ms): $execTimes"

      logger.info(msg)
      logInfo(msg)
    }
  }

  private def getTableCompression(catalogTable: CatalogTable) = {
    if (options.containsKey("compression")) {
      val value = options.get("compression")
      if (!"snappy".equals(value) && !"zstd".equals(value) && !"lz4".equals(value)) {
        throw new JobServerException("compress type " + value + " is not support")
      }
      value
    } else if (catalogTable.properties.contains("parquet.compression")) {
      catalogTable.properties("parquet.compression")
    } else if (catalogTable.properties.contains("orc.compress")) {
      catalogTable.properties("orc.compress")
    } else {
      "zstd"
    }
  }

  private def mergeFinish(sparkSession: SparkSession,
                          tempDir: String,
                          location: String,
                          fs: FileSystem,
                          modificationTime: Long,
                          accessTime: Long,
                          inputFileList: util.LinkedList[FileStatus],
                          tempPath: Path): Unit = {
    val destList = getPathFromDirectory(sparkSession.sparkContext.hadoopConfiguration, tempDir)
    for (path <- destList.asScala) {
      val newLocation = location + "/" + path.getName
      fs.rename(path, new Path(newLocation))
      fs.setTimes(new Path(newLocation), modificationTime, accessTime) // 设置访问时间
    }

    logger.info("开始删除源文件: " + inputFileList.size())
    for (path <- inputFileList.asScala) {
      fs.delete(path.getPath, true)
    }
    logger.info("开始删除源文件end")
    fs.delete(tempPath, true)
  }

  // 从目录中获取 hdfs path
  private def getPathFromDirectory(configuration: Configuration
                                   , dir: String): util.List[Path] = {
    val dirPath = new Path(dir)
    val fs = dirPath.getFileSystem(configuration)
    val status = fs.getFileStatus(dirPath)
    val inputFiles = fs.listStatus(status.getPath, HiddenFileFilter.INSTANCE)
    val list = new util.LinkedList[Path]()
    for (file <- inputFiles) {
      list.add(file.getPath)
    }
    list
  }

  // 计算输出文件(分区)数量
  private def genOutputFileCount(beforeRowCount: Long, totalSize: Long): Int = {

    if (options.containsKey("fileCount")) {
      return options.get("fileCount").toInt
    } else if (options.containsKey("maxRecordsPerFile")) {
      return (beforeRowCount / options.get("maxRecordsPerFile").toLong + 1).toInt
    }

    val count = totalSize / (256 * MiB)
    count.toInt + 1
  }

  private def statParquetRowCount(fileSystem: FileSystem, dir: String): Long = {
    logger.info("dir = {}", dir)
    if (StringUtils.isBlank(dir)) return 0L

    val fileRowCount = new AtomicLong(0)
    try {
      val dirPath: Path = new Path(dir)
      val inputFiles = fileSystem.listStatus(dirPath, HiddenFileFilter.INSTANCE)

      val parFiles = ParArray(inputFiles: _*)
      parFiles.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(3))

      parFiles.foreach(fileStatus => {
        val inputFile = HadoopInputFile.fromStatus(fileStatus, fileSystem.getConf)
        val blockMetaDataList = ParquetFileReader.open(inputFile).getFooter.getBlocks
        for (b <- blockMetaDataList.asScala) {
          fileRowCount.addAndGet(b.getRowCount)
        }
      });
    } catch {
      case e: Exception => logger.error(e.getMessage, e)
    }
    fileRowCount.get()
  }

  private def statParquetRowCount(conf: Configuration, inputFiles: util.LinkedList[FileStatus]): Long = {
    val fileRowCount = new AtomicLong(0)
    try {
      val parFiles = ParArray(inputFiles.asScala: _*)
      parFiles.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(3))

      parFiles.foreach(fileStatus => {
        val inputFile = HadoopInputFile.fromStatus(fileStatus, conf)
        val blockMetaDataList = ParquetFileReader.open(inputFile).getFooter.getBlocks
        for (b <- blockMetaDataList.asScala) {
          fileRowCount.addAndGet(b.getRowCount)
        }
      });
    } catch {
      case e: Exception => logger.error(e.getMessage, e)
    }
    fileRowCount.get()
  }

  private def statOrcRowCount(fileSystem: FileSystem, dir: String): Long = {
    logger.info("dir = {}", dir)
    if (StringUtils.isBlank(dir)) return 0L

    val fileRowCount = new AtomicLong(0)
    try {
      val dirPath: Path = new Path(dir)
      val inputFiles = fileSystem.listStatus(dirPath, HiddenFileFilter.INSTANCE)

      val parFiles = ParArray(inputFiles: _*)
      parFiles.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(3))

      parFiles.foreach(fileStatus => {
        val reader = OrcFile.createReader(fileStatus.getPath, OrcFile.readerOptions(fileSystem.getConf))
        fileRowCount.addAndGet(reader.getNumberOfRows)
      })
    } catch {
      case e: Exception => logger.error(e.getMessage, e)
    }
    fileRowCount.get()
  }

  private def statOrcRowCount(conf: Configuration, inputFiles: util.LinkedList[FileStatus]): Long = {
    val fileRowCount = new AtomicLong(0)
    try {
      val parFiles = ParArray(inputFiles.asScala: _*)
      parFiles.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(3))

      parFiles.foreach(fileStatus => {
        val reader = OrcFile.createReader(fileStatus.getPath, OrcFile.readerOptions(conf))
        fileRowCount.addAndGet(reader.getNumberOfRows)
      })
    } catch {
      case e: Exception => logger.error(e.getMessage, e)
    }
    fileRowCount.get()
  }

  // 校验分区信息，返回所有分区表，如果是非分区表则返回空
  private def getMergePartitions(sparkSession: SparkSession, catalogTable: CatalogTable): Seq[CatalogTablePartition] = {
    if (catalogTable.partitionColumnNames.isEmpty && mergeFileData.getPartitionVals.size() > 0) {
      throw new JobServerException("非分区表，不用指定分区")
    }

    if (catalogTable.partitionColumnNames.nonEmpty && mergeFileData.getPartitionVals.size() == 0) {
      throw new JobServerException("分区表请指定具体分区, 如果多个分区字段，可以设置其中一个或多个, 分区字段为: "
        + catalogTable.partitionColumnNames.mkString(","))
    }

    if (catalogTable.partitionColumnNames.nonEmpty) {
      var parts = Map[String, String]()
      for (partition <- mergeFileData.getPartitionVals.entrySet().asScala) {
        val key = CommonUtils.cleanQuote(partition.getKey)
        val value = CommonUtils.cleanQuote(partition.getValue)
        if (catalogTable.partitionColumnNames.contains(key)) {
          parts = parts + (key -> value)
        } else {
          throw new JobServerException(s"当前表没有分区字段: $key, 分区字段为: " + catalogTable.partitionColumnNames.mkString(","))
        }
      }

      sparkSession.sessionState.catalog.listPartitions(catalogTable.identifier, Some(parts))
    } else {
      null
    }
  }

  private def addTableOption(tableMeta: CatalogTable, dfw: DataFrameWriter[Row]): Unit = {
    tableMeta.properties.foreach { case (key, value) =>
      if (StringUtils.startsWith(key, "orc.") || StringUtils.startsWith(key, "parquet.")) {
        dfw.option(key, value)
      }
    }
  }
}
