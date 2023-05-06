package com.github.melin.jobserver.extensions.sql

import com.github.melin.jobserver.extensions.SparkJobserverExtensions
import org.apache.spark.sql.SparkSession

object SparkMaskDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .enableHiveSupport
      .master("local")
      .appName("spark example")
      .config("spark.sql.sources.partitionOverwriteMode", "dynamic")
      .config("spark.sql.extensions", classOf[SparkJobserverExtensions].getName)
      .getOrCreate


    val sql =
      """
        |CREATE VIEW tdl_spark_test
        |FILES '/user/dataworks/users/qianxiao/demo.csv'
        |OPTIONS(
        |  delimiter = ',',
        |  header = 'true'
        |)
        |FORMAT csv
        |COMPRESSION gz
              """.stripMargin

    spark.read.option("header", "true")
      .csv("vfs://tgz:ftp://fcftp:fcftp@172.18.1.52/csv.tar.gz!/csv").show()
  }
}
