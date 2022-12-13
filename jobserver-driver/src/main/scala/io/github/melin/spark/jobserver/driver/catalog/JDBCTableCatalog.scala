package io.github.melin.spark.jobserver.driver.catalog

import io.github.melin.spark.jobserver.core.exception.SparkJobException
import io.github.melin.spark.jobserver.driver.util.LogUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.StopWatch
import org.apache.spark.internal.Logging
import org.apache.spark.sql.catalyst.analysis.{NoSuchNamespaceException, NoSuchTableException}
import org.apache.spark.sql.connector.catalog._
import org.apache.spark.sql.connector.expressions.Transform
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCOptions, JDBCRDD}
import org.apache.spark.sql.execution.datasources.v2.jdbc.JDBCTable
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcDialects}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.util.CaseInsensitiveStringMap
import org.springframework.jdbc.support.JdbcUtils

import java.sql.{Connection, PreparedStatement, SQLException}
import java.util
import scala.collection.JavaConverters._

/**
 * huaixin 2022/3/17 6:53 PM
 */
class JDBCTableCatalog extends TableCatalog with SupportsNamespaces with Logging {
  private var catalogName: String = null
  private var parameters: Map[String, String] = _
  private var dialect: JdbcDialect = _
  private val partitionMaxCount = 1000000

  override def initialize(name: String, caseMap: CaseInsensitiveStringMap): Unit = {
    assert(catalogName == null, "The JDBC table catalog is already initialed")
    this.catalogName = name

    parameters = caseMap.asCaseSensitiveMap().asScala.toMap
    // The `JDBCOptions` checks the existence of the table option. This is required by JDBC v1, but
    // JDBC V2 only knows the table option when loading a table. Here we put a table option with a
    // fake value, so that it can pass the check of `JDBCOptions`.
    val options = new JDBCOptions(parameters + (JDBCOptions.JDBC_TABLE_NAME -> "__invalid_dbtable"))
    dialect = JdbcDialects.get(options.url)
  }

  override def name: String = {
    if (catalogName == null) {
      throw new IllegalArgumentException("requirement failed: The JDBC table catalog is not initialed")
    }
    catalogName
  }

  override def listTables(namespace: Array[String]): Array[Identifier] = {
    throw new UnsupportedOperationException("not support")
  }

  override def loadTable(ident: Identifier): Table = {
    val databaseName = ident.namespace()(0)
    val tableName = ident.name()
    LogUtils.info("query table schema: {}.{}.{}", catalogName, databaseName, tableName)

    var params = this.parameters + (JDBCOptions.JDBC_TABLE_NAME -> (databaseName + "." + tableName))
    val partParams = buildPartitionInfo(params, databaseName, tableName)
    params ++= partParams
    val option = new JDBCOptions(params)

    try {
      val watch = StopWatch.createStarted()
      val schema = JDBCRDD.resolveTable(option)
      watch.stop()
      logInfo(s"get jdbc schema times: ${watch.getTime}")
      JDBCTable(ident, schema, option)
    } catch {
      case e: Exception =>
        logError(e.getMessage)
        throw new NoSuchTableException(ident)
    }
  }

  override def createTable(
      ident: Identifier,
      schema: StructType,
      partitions: Array[Transform],
      properties: java.util.Map[String, String]): Table = {
    throw new UnsupportedOperationException("not support")
  }

  override def alterTable(ident: Identifier, changes: TableChange*): Table = {
    throw new UnsupportedOperationException("not support")
  }

  override def dropTable(ident: Identifier): Boolean = {
    throw new UnsupportedOperationException("not support")
  }

  override def renameTable(oldIdent: Identifier, newIdent: Identifier): Unit = {
    throw new UnsupportedOperationException("not support")
  }

  override def listNamespaces: Array[Array[String]] = {
    new Array[Array[String]](0)
  }

  override def listNamespaces(namespace: Array[String]): Array[Array[String]] = {
    throw new NoSuchNamespaceException(namespace)
  }

  override def loadNamespaceMetadata(namespace: Array[String]): util.Map[String, String] = {
    throw new NoSuchNamespaceException(namespace)
  }

  override def createNamespace(namespace: Array[String], metadata: util.Map[String, String]): Unit = {
    throw new UnsupportedOperationException("not support")
  }

  override def alterNamespace(namespace: Array[String], changes: NamespaceChange*): Unit = {
    throw new UnsupportedOperationException("not support")
  }

  override def dropNamespace(namespace: Array[String], cascade: Boolean): Boolean = {
    throw new UnsupportedOperationException("not support")
  }

  private def buildPartitionInfo(params: Map[String, String], databaseName: String, tableName: String): Map[String, String] = {
    var stmt: PreparedStatement = null
    try {
      val dialect = JdbcDialects.get(JDBCOptions.JDBC_URL)
      val options = new JDBCOptions(params)
      val connection = dialect.createConnectionFactory(options)(-1)
      // get table primarykey
      val partitionColumn: String = getPrimaryKey(connection, databaseName, tableName)

      var sql = "select count(1) as num "
      if (StringUtils.isNotBlank(partitionColumn)) {
        sql += ", max(" + partitionColumn + ") max_value, min(" + partitionColumn + ") min_value from " + databaseName + "." + tableName
      } else {
        sql += " from " + databaseName + "." + tableName
      }
      stmt = connection.prepareStatement(sql)
      val resultSet = stmt.executeQuery
      resultSet.next
      val count = resultSet.getObject("num").asInstanceOf[Long]
      LogUtils.info("table {}.{} record count: {}", databaseName, tableName, count.toString)

      var partParams: Map[String, String] = Map()
      if (StringUtils.isNotBlank(partitionColumn) && count > partitionMaxCount) {
        val maxValue = String.valueOf(resultSet.getObject("max_value"))
        val minValue = String.valueOf(resultSet.getObject("min_value"))
        val numPartitions = count / partitionMaxCount + (if (count % partitionMaxCount > 0) 1 else 0)

        partParams = partParams + ("partitionColumn" -> partitionColumn)
        partParams = partParams + ("numPartitions" -> numPartitions.toInt.toString)
        partParams = partParams + ("lowerBound" -> minValue)
        partParams = partParams + ("upperBound" -> maxValue)
        LogUtils.info("table {}.{} max value: {}, min value: {}, partitionColumn: {}, numPartitions: {}",
          databaseName, tableName, maxValue, minValue, partitionColumn, numPartitions.toString)
      }

      partParams
    } catch {
      case e: SQLException => throw new SparkJobException(e.getMessage, e)
    } finally JdbcUtils.closeStatement(stmt)
  }

  protected def getPrimaryKey(connection: Connection, databaseName: String, tableName: String): String = {
    val resultSet = connection.getMetaData.getPrimaryKeys(null, databaseName, tableName)
    while (resultSet.next) {
      return resultSet.getString("COLUMN_NAME")
    }

    null
  }
}
