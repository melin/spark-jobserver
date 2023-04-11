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
import io.github.melin.superior.parser.spark.SparkStreamSQLHelper;
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

        List<StatementData> statementDatas = SparkStreamSQLHelper.getStatementData(noCommentJobText);
        statementDatas.forEach(statementData -> {
            if (!checkValidSql(statementData.getType())) {
                LogUtils.info("不支持sql 类型: " + statementData.getType());
            } else {
                Statement statement = statementData.getStatement();
                if (statement instanceof StreamTable) {
                    StreamTable streamTable = (StreamTable) statement;
                    buildSourceTable(streamTable);
                } else if (statement instanceof StreamInsertStatement) {
                    HudiUtils.deltaInsertStreamSelectAdapter(SparkDriverEnv.getSparkSession(),
                            (StreamInsertStatement) statement);
                } else if (statement instanceof SetData) {
                    SetData setData = (SetData) statement;
                    String sql = "set " + setData.getKey() + " = " + setData.getValue();
                    SparkDriverEnv.sql(sql);
                }
            }
        });
    }

    private void buildSourceTable(StreamTable streamTable) {
        try {
            String sourceType = streamTable.getProperties().get("type");

            if (!ArrayUtils.contains(SOURCE_TYPES, sourceType)) {
                throw new IllegalArgumentException("not support source type: " + sourceType);
            }

            if ("kafka".equals(sourceType)) {
                KafkaSouce kafkaSouce = new KafkaSouce();
                kafkaSouce.createStreamTempTable(SparkDriverEnv.getSparkSession(), streamTable);
            } else if ("hudi".equals(sourceType)) {
                SparkSession spark = SparkDriverEnv.getSparkSession();
                String databaseName = streamTable.getProperties().get("databaseName");
                String tableName = streamTable.getProperties().get("tableName");
                if (StringUtils.isBlank(databaseName) || StringUtils.isBlank(tableName)) {
                    throw new IllegalArgumentException("databaseName and tableName cannot be empty");
                }

                TableIdentifier identifier = new TableIdentifier(tableName, new Some<String>(databaseName));
                CatalogTable table = spark.sessionState().catalog().getTableMetadata(identifier);
                String location = table.location().getPath();
                LOG.info("hudi table: {}.{}, location: {}", databaseName, tableName, location);

                SparkDriverEnv.getSparkSession().readStream().format("hudi")
                        .load(location).createGlobalTempView(streamTable.getTableName());
            }
        } catch (Exception e) {
            throw new SparkJobException("create source table failed", e);
        }
    }

    private boolean checkValidSql(StatementType sqlType) {
        if (StatementType.INSERT_SELECT == sqlType ||
                StatementType.CREATE_TABLE == sqlType ||
                StatementType.SET == sqlType) {
            return true;
        } else {
            return false;
        }
    }
}
