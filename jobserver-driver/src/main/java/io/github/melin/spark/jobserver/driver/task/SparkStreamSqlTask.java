package io.github.melin.spark.jobserver.driver.task;

import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.core.exception.SparkJobException;
import io.github.melin.spark.jobserver.core.util.CommonUtils;
import io.github.melin.spark.jobserver.driver.SparkDriverEnv;
import io.github.melin.spark.jobserver.driver.stream.KafkaSouce;
import io.github.melin.spark.jobserver.driver.task.udf.GenericUDTFJsonExtractValue;
import io.github.melin.spark.jobserver.driver.util.HudiUtils;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import io.github.melin.superior.common.StatementType;
import io.github.melin.superior.common.relational.*;
import io.github.melin.superior.common.relational.common.SetStatement;
import io.github.melin.superior.common.relational.create.CreateTable;
import io.github.melin.superior.common.relational.dml.InsertTable;
import io.github.melin.superior.parser.spark.SparkStreamSqlHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.TableIdentifier;
import org.apache.spark.sql.catalyst.catalog.CatalogTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import scala.Some;

import java.util.List;

/**
 * huaixin 2022/4/12 16:13
 */
@Service
public class SparkStreamSqlTask extends AbstractSparkTask {

    private static final Logger LOG = LoggerFactory.getLogger(SparkStreamSqlTask.class);

    private static final String[] SOURCE_TYPES = new String[] {"kafka", "hudi"};

    @Override
    protected void executeJobText(InstanceDto instanceDto) throws Exception {
        LOG.info("create parser json function");
        SparkDriverEnv.sql("CREATE TEMPORARY FUNCTION stream_json_extract_value AS '"
                + GenericUDTFJsonExtractValue.class.getName() + "'");

        String noCommentJobText = CommonUtils.cleanSqlComment(instanceDto.getJobText());

        List<Statement> statements = SparkStreamSqlHelper.getStatementData(noCommentJobText);
        statements.forEach(statement -> {
            if (!checkValidSql(statement.getStatementType())) {
                LogUtils.info("不支持sql 类型: " + statement.getStatementType());
            } else {
                if (statement instanceof CreateTable) {
                    CreateTable createTable = (CreateTable) statement;
                    buildSourceTable(createTable);
                } else if (statement instanceof InsertTable) {
                    HudiUtils.deltaInsertStreamSelectAdapter(SparkDriverEnv.getSparkSession(),
                            (InsertTable) statement);
                } else if (statement instanceof SetStatement) {
                    SetStatement setData = (SetStatement) statement;
                    String sql = "set " + setData.getKey() + " = " + setData.getValue();
                    SparkDriverEnv.sql(sql);
                }
            }
        });
    }

    private void buildSourceTable(CreateTable createTable) {
        try {
            String sourceType = createTable.getProperties().get("type");

            if (!ArrayUtils.contains(SOURCE_TYPES, sourceType)) {
                throw new IllegalArgumentException("not support source type: " + sourceType);
            }

            if ("kafka".equals(sourceType)) {
                KafkaSouce kafkaSouce = new KafkaSouce();
                kafkaSouce.createStreamTempTable(SparkDriverEnv.getSparkSession(), createTable);
            } else if ("hudi".equals(sourceType)) {
                SparkSession spark = SparkDriverEnv.getSparkSession();
                String databaseName = createTable.getProperties().get("databaseName");
                String tableName = createTable.getProperties().get("tableName");
                if (StringUtils.isBlank(databaseName) || StringUtils.isBlank(tableName)) {
                    throw new IllegalArgumentException("databaseName and tableName cannot be empty");
                }

                TableIdentifier identifier = new TableIdentifier(tableName, new Some<String>(databaseName));
                CatalogTable table = spark.sessionState().catalog().getTableMetadata(identifier);
                String location = table.location().getPath();
                LOG.info("hudi table: {}.{}, location: {}", databaseName, tableName, location);

                SparkDriverEnv.getSparkSession().readStream().format("hudi")
                        .load(location).createGlobalTempView(createTable.getTableId().getTableName());
            }
        } catch (Exception e) {
            throw new SparkJobException("create source table failed", e);
        }
    }

    private boolean checkValidSql(StatementType sqlType) {
        if (StatementType.INSERT == sqlType ||
                StatementType.CREATE_TABLE == sqlType ||
                StatementType.SET == sqlType) {
            return true;
        } else {
            return false;
        }
    }
}
