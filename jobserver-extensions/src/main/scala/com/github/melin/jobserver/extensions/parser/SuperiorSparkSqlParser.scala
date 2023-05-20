package com.github.melin.jobserver.extensions.parser

import com.github.melin.jobserver.extensions.execution.{CreateFileViewCommand, ExportTableCommand, MergeSmallFileCommand}
import com.github.melin.jobserver.extensions.util.CommonUtils
import io.github.melin.superior.common.SQLParserException
import io.github.melin.superior.common.antlr4.UpperCaseCharStream
import io.github.melin.superior.common.relational.TableId
import io.github.melin.superior.parser.spark.SparkSqlPostProcessor
import io.github.melin.superior.parser.spark.antlr4.SparkSqlParser._
import io.github.melin.superior.parser.spark.antlr4.{SparkSqlLexer, SparkSqlParser, SparkSqlParserBaseVisitor}
import io.github.melin.superior.parser.spark.relational.{CreateFileView, ExportData, MergeFileData}
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.misc.ParseCancellationException
import org.antlr.v4.runtime.tree._
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.parser.{ParseErrorListener, ParseException, ParserInterface, ParserUtils}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.trees.Origin
import org.apache.spark.sql.catalyst.{FunctionIdentifier, TableIdentifier}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{AnalysisException, SparkSession}

import java.util
import java.util.Locale
import scala.collection.JavaConverters._

/**
  * A SQL parser that tries to parse Delta commands. If failng to parse the SQL text, it will
  * forward the call to `delegate`.
  */
class SuperiorSparkSqlParser(spark: SparkSession,
                             val delegate: ParserInterface) extends ParserInterface {

  private val builder = new SuperiorSqlAstBuilder

  override def parsePlan(sqlText: String): LogicalPlan = parse(sqlText) { parser =>
    builder.visit(parser.singleStatement()) match {
      case plan: LogicalPlan => plan
      case _ => delegate.parsePlan(sqlText)
    }
  }

  override def parseQuery(sqlText: String): LogicalPlan = parse(sqlText) { parser =>
    builder.visit(parser.singleStatement()) match {
      case plan: LogicalPlan => plan
      case _ => delegate.parseQuery(sqlText)
    }
  }

  // scalastyle:off line.size.limit
  /**
    * Fork from `org.apache.spark.sql.catalyst.parser.AbstractSqlParser#parse(java.lang.String, scala.Function1)`.
    *
    * @see https://github.com/apache/spark/blob/v2.4.4/sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/parser/ParseDriver.scala#L81
    */
  // scalastyle:on
  protected def parse[T](command: String)(toResult: SparkSqlParser => T): T = {
    val lexer = new SparkSqlLexer(
      new UpperCaseCharStream(CharStreams.fromString(command)))
    lexer.removeErrorListeners()
    lexer.addErrorListener(ParseErrorListener)

    val tokenStream = new CommonTokenStream(lexer)
    val parser = new SparkSqlParser(tokenStream)
    parser.addParseListener(new SparkSqlPostProcessor())
    parser.removeErrorListeners()
    parser.addErrorListener(ParseErrorListener)

    try {
      try {
        // first, try parsing with potentially faster SLL mode
        parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        toResult(parser)
      } catch {
        case e: ParseCancellationException =>
          // if we fail, parse with LL mode
          tokenStream.seek(0) // rewind input stream
          parser.reset()

          // Try Again.
          parser.getInterpreter.setPredictionMode(PredictionMode.LL)
          toResult(parser)
      }
    } catch {
      case e: ParseException if e.command.isDefined =>
        throw e
      case e: ParseException =>
        throw e.withCommand(command)
      case e: AnalysisException =>
        val position = Origin(e.line, e.startPosition)
        throw new ParseException(Option(command), e.message, position, position)
    }
  }

  override def parseExpression(sqlText: String): Expression = delegate.parseExpression(sqlText)

  override def parseTableIdentifier(sqlText: String): TableIdentifier =
    delegate.parseTableIdentifier(sqlText)

  override def parseFunctionIdentifier(sqlText: String): FunctionIdentifier =
    delegate.parseFunctionIdentifier(sqlText)

  override def parseTableSchema(sqlText: String): StructType = delegate.parseTableSchema(sqlText)

  override def parseDataType(sqlText: String): DataType = delegate.parseDataType(sqlText)

  override def parseMultipartIdentifier(sqlText: String): Seq[String] = delegate.parseMultipartIdentifier(sqlText)
}

/**
  * Define how to convert an AST generated from `DeltaSqlBase.g4` to a `LogicalPlan`. The parent
  * class `DeltaSqlBaseBaseVisitor` defines all visitXXX methods generated from `#` instructions in
  * `DeltaSqlBase.g4` (such as `#vacuumTable`).
  */
class SuperiorSqlAstBuilder extends SparkSqlParserBaseVisitor[AnyRef] {

  import ParserUtils._


  override def visitColTypeList(ctx: ColTypeListContext): Seq[StructField] = withOrigin(ctx) {
    ctx.colType().asScala.map(visitColType)
  }

  override def visitColType(ctx: ColTypeContext): StructField = withOrigin(ctx) {
    val builder = new MetadataBuilder

    // Add Hive type string to metadata.
    val rawDataType = typedVisit[DataType](ctx.dataType)

    StructField(
      ctx.colName.getText,
      rawDataType,
      nullable = false,
      builder.build())
  }

  protected def typedVisit[T](ctx: ParseTree): T = {
    ctx.accept(this).asInstanceOf[T]
  }

  override def visitPrimitiveDataType(ctx: PrimitiveDataTypeContext): DataType = withOrigin(ctx) {
    val dataType = ctx.identifier.getText.toLowerCase(Locale.ROOT)
    (dataType, ctx.INTEGER_VALUE().asScala.toList) match {
      case ("boolean", Nil) => BooleanType
      case ("tinyint" | "byte", Nil) => ByteType
      case ("smallint" | "short", Nil) => ShortType
      case ("int" | "integer", Nil) => IntegerType
      case ("bigint" | "long", Nil) => LongType
      case ("float", Nil) => FloatType
      case ("double", Nil) => DoubleType
      case ("date", Nil) => DateType
      case ("timestamp", Nil) => TimestampType
      case ("string", Nil) => StringType
      case ("char", length :: Nil) => CharType(length.getText.toInt)
      case ("varchar", length :: Nil) => VarcharType(length.getText.toInt)
      case ("binary", Nil) => BinaryType
      case ("decimal", Nil) => DecimalType.USER_DEFAULT
      case ("decimal", precision :: Nil) => DecimalType(precision.getText.toInt, 0)
      case ("decimal", precision :: scale :: Nil) =>
        DecimalType(precision.getText.toInt, scale.getText.toInt)
      case ("interval", Nil) => CalendarIntervalType
      case (dt, params) =>
        val dtStr = if (params.nonEmpty) s"$dt(${params.mkString(",")})" else dt
        throw new ParseException(s"DataType $dtStr is not supported.", ctx)
    }
  }

  override def visitNamedQuery(ctx: NamedQueryContext): (String, String) = withOrigin(ctx) {
    val subQuery: String = {
      val queryContext = ctx.query()
      val s = queryContext.start.getStartIndex
      val e = queryContext.stop.getStopIndex
      val interval = new misc.Interval(s, e)
      val viewSql = ctx.start.getInputStream.getText(interval)
      viewSql
    }
    (ctx.name.getText, subQuery)
  }

  private def withCTE(ctx: CtesContext): Seq[(String, String)] = {
    val ctes = ctx.namedQuery.asScala.map { nCtx =>
      visitNamedQuery(nCtx)
    }
    // Check for duplicate names.
    val duplicates = ctes.groupBy(_._1).filter(_._2.size > 1).keys
    if (duplicates.nonEmpty) {
      throw new ParseException(
        s"CTE definition can't have duplicate names: ${duplicates.mkString("'", "', '", "'")}.",
        ctx)
    }
    ctes
  }

  override def visitExportTable(ctx: ExportTableContext): AnyRef = withOrigin(ctx) {
    val subqueryAlias =
      if (ctx.ctes() != null) {
        withCTE(ctx.ctes())
      } else {
        Seq[(String, String)]()
      }

    val tableId = parseTableName(ctx.multipartIdentifier())
    val path = CommonUtils.cleanQuote(ctx.filePath.getText)
    val properties = parseOptions(ctx.propertyList())
    val partitionVals = parsePartitionSpec(ctx.partitionSpec())
    var fileFormat: String = null
    var compression: String = null
    var maxFileSize: String = null
    var overwrite: Boolean = false
    var single: Boolean = false

    val causes = ctx.exportTableClauses()
    if (causes != null) {
      if (causes.fileformatName != null) fileFormat = causes.fileformatName.getText
      if (causes.compressionName != null) compression = causes.compressionName.getText
      if (causes.maxfilesize != null) maxFileSize = causes.maxfilesize.getText
      if (causes.overwrite != null) overwrite = causes.overwrite.TRUE() != null
      if (causes.single != null) single = causes.single.TRUE() != null
    }
    val exportData = new ExportData(tableId, path, properties, partitionVals,
      fileFormat, compression, maxFileSize, overwrite, single)
    ExportTableCommand(exportData, subqueryAlias)
  }

  override def visitMergeFile(ctx: MergeFileContext): LogicalPlan = withOrigin(ctx) {
    val tableId = parseTableName(ctx.multipartIdentifier())
    val properties = parseOptions(ctx.propertyList())
    val partitionVals = parsePartitionSpec(ctx.partitionSpec())

    val mergeFileData = new MergeFileData(tableId, properties, partitionVals)
    MergeSmallFileCommand(mergeFileData)
  }

  override def visitCreateFileView(ctx: CreateFileViewContext): LogicalPlan = withOrigin(ctx) {
    val tableId = parseTableName(ctx.multipartIdentifier())
    val path = CommonUtils.cleanQuote(ctx.path.getText)
    val properties = parseOptions(ctx.propertyList())

    var fileFormat: String = CommonUtils.cleanQuote(ctx.tableProvider().multipartIdentifier().getText)
    var compression: String = null
    var sizeLimit: String = null

    val causes = ctx.createFileViewClauses()
    if (causes != null) {
      if (causes.compressionName != null) compression = causes.compressionName.getText
      if (causes.sizelimit != null) sizeLimit = causes.sizelimit.getText
    }

    val createFileView = new CreateFileView(tableId, path, properties, fileFormat, compression, sizeLimit)
    CreateFileViewCommand(createFileView)
  }

  override def visitTableIdentifier(ctx: TableIdentifierContext): TableIdentifier = withOrigin(ctx) {
    TableIdentifier(ctx.table.getText, Option(ctx.db).map(_.getText))
  }

  override def visitSingleStatement(ctx: SingleStatementContext): LogicalPlan = withOrigin(ctx) {
    visit(ctx.statement).asInstanceOf[LogicalPlan]
  }

  private def parseTableName(ctx: SparkSqlParser.MultipartIdentifierContext): TableId = {
    if (ctx.parts.size == 4) {
      new TableId (ctx.parts.get(0).getText, ctx.parts.get(1).getText, ctx.parts.get(2).getText, ctx.parts.get(3).getText)
    } else if (ctx.parts.size == 3) {
      new TableId (ctx.parts.get(0).getText, ctx.parts.get(1).getText, ctx.parts.get(2).getText)
    } else if (ctx.parts.size == 2) {
      new TableId (null, ctx.parts.get(0).getText, ctx.parts.get(1).getText)
    } else if (ctx.parts.size == 1) {
      new TableId (null, null, ctx.parts.get(0).getText)
    } else {
      throw new SQLParserException("parse multipart error: " + ctx.parts.size)
    }
  }

  private def parseOptions(ctx: PropertyListContext): util.HashMap[String, String] = {
    val properties = new util.HashMap[String, String] ()
    if (ctx != null) {
      ctx.property().forEach { property =>
        val key = CommonUtils.cleanQuote(property.key.getText)
        val value = CommonUtils.cleanQuote(property.value.getText)
        properties.put(key, value)
      }
    }

    properties
  }

  private def parsePartitionSpec(ctx: PartitionSpecContext): util.LinkedHashMap[String, String] = {
    val partitions = new util.LinkedHashMap[String, String]()
    if (ctx != null) {
      val count = ctx.partitionVal().size
      ctx.partitionVal().forEach { partition =>
        if (count == 1) {
          partitions.put(partition.identifier().getText, "__dynamic__")
        } else {
          var value = partition.getChild(2).getText
          value = CommonUtils.cleanQuote(value)
          partitions.put(partition.identifier().getText, value)
        }
      }
    }
    partitions
  }
}
