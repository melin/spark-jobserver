package io.github.melin.spark.jobserver.driver.task;

import com.github.melin.superior.sql.parser.StatementType;
import com.github.melin.superior.sql.parser.model.*;
import com.github.melin.superior.sql.parser.spark.SparkStreamSQLHelper;
import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.core.util.CommonUtils;
import io.github.melin.spark.jobserver.driver.SparkDriverEnv;
import io.github.melin.spark.jobserver.driver.stream.KafkaSouce;
import io.github.melin.spark.jobserver.driver.task.udf.GenericUDTFJsonExtractValue;
import io.github.melin.spark.jobserver.driver.util.HudiUtils;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * huaixin 2022/4/12 16:13
 */
@Service
public class SparkStreamSqlTask extends AbstractSparkTask {

    private static final Logger LOG = LoggerFactory.getLogger(SparkStreamSqlTask.class);

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
                    KafkaSouce kafkaSouce = new KafkaSouce();
                    kafkaSouce.createStreamTempTable(SparkDriverEnv.getSparkSession(), (StreamTable) statement);
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
