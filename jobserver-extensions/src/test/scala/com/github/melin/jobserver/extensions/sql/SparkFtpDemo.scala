package com.github.melin.jobserver.extensions.sql

import com.github.melin.jobserver.extensions.SparkJobserverExtensions
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object SparkFtpDemo {
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


    val data = Seq(
      Row("James", 12),
      Row("Michael", 23),
      Row("Robert", 37),
      Row("Washington", null)
    )

    val arrayStructSchema = new StructType()
      .add("name", StringType)
      .add("age", IntegerType)

    val df = spark.createDataFrame(spark.sparkContext
      .parallelize(data), arrayStructSchema)

    // df.write.json("vfs://ftp://fcftp:fcftp@172.18.1.52/users.json")
  }
}
