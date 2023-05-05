package com.github.melin.jobserver.extensions.util

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.IOUtils
import org.apache.spark.sql.catalyst.catalog.CatalogTable
import org.apache.spark.sql.DataFrameWriter

import java.io.IOException
import scala.util.Try

object CommonUtils {

  def getCurrentDatabase(schemaName: String): String = {
    schemaName
  }

  /**
    * @param partitionStr year=2017/month=12/day=2/eventtype=login
    * @param joinString   ", "
    * @return year=2017,month=12,day=2,eventtype='login'
    */
  def formatPartitionExpr(partitionStr: String,
                          joinString: String): String = {
    StringUtils.split(partitionStr, "/")
      .map(p => {
        val items = StringUtils.split(p, "=")
        if (items.length == 2 && !NumberUtils.isCreatable(items(1))) {
          s"${items(0)}='${items(1)}'"
        } else {
          s"${items(0)}=${items(1)}"
        }
      }).mkString(joinString)
  }

  def cleanQuote(value: String): String = {
    val result = if (StringUtils.startsWith(value, "'") && StringUtils.endsWith(value, "'")) {
      StringUtils.substring(value, 1, -1);
    } else if (StringUtils.startsWith(value, "\"") && StringUtils.endsWith(value, "\"")) {
      StringUtils.substring(value, 1, -1);
    } else {
      value
    }

    result.trim
  }

  def getTableFileType(catalogTable: CatalogTable): String = {
    if (catalogTable.storage.inputFormat.get == "org.apache.hadoop.hive.ql.io.orc.OrcInputFormat") {
      FileType.ORC
    } else {
      FileType.PARQUET
    }
  }

  def copyMerge(srcFS: FileSystem, srcDir: Path,
                dstFS: FileSystem, dstFile: Path,
                deleteSource: Boolean, conf: Configuration): Boolean = {
    if (dstFS.exists(dstFile)) {
      throw new IOException(s"Target $dstFile already exists")
    }

    // Source path is expected to be a directory:
    if (srcFS.getFileStatus(srcDir).isDirectory()) {

      val outputFile = dstFS.create(dstFile)
      Try {
        srcFS
          .listStatus(srcDir)
          .sortBy(_.getPath.getName)
          .collect {
            case status if status.isFile() =>
              val inputFile = srcFS.open(status.getPath())
              Try(IOUtils.copyBytes(inputFile, outputFile, conf, false))
              inputFile.close()
          }
      }
      outputFile.close()

      if (deleteSource) srcFS.delete(srcDir, true) else true
    }
    else false
  }

  /**
    * [[DataFrameWriter]] implicits
    */
  implicit class ImplicitDataFrameWriter[T](dataFrameWriter: DataFrameWriter[T]) {

    /**
      * Adds an output option for the underlying data source if the option has a value.
      */
    def optionNoNull(key: String, optionValue: Option[String]): DataFrameWriter[T] = {
      optionValue match {
        case Some(_) => dataFrameWriter.option(key, optionValue.get)
        case None => dataFrameWriter
      }
    }
  }
}
