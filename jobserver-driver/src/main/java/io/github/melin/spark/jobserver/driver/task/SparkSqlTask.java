package io.github.melin.spark.jobserver.driver.task;

import io.github.melin.spark.jobserver.core.util.CommonUtils;
import io.github.melin.spark.jobserver.driver.SparkEnv;
import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import com.github.melin.superior.sql.parser.StatementType;
import com.github.melin.superior.sql.parser.model.StatementData;
import com.github.melin.superior.sql.parser.spark.SparkSQLHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.github.melin.superior.sql.parser.StatementType.*;

/**
 * huaixin 2022/4/7 6:03 PM
 */
@Service
public class SparkSqlTask extends AbstractSparkTask {

    private static final Logger LOG = LoggerFactory.getLogger(SparkSqlTask.class);

    @Override
    protected void executeJobText(InstanceDto instanceDto) throws Exception {
        String noCommentJobText = CommonUtils.cleanSqlComment(instanceDto.getJobText());
        List<String> sqls = CommonUtils.splitMultiSql(noCommentJobText);

        for (String row : sqls) {
            String sql = StringUtils.trim(row);
            if (StringUtils.isNotBlank(sql)) {
                StatementData statementData = SparkSQLHelper.getStatementData(sql);
                StatementType type = statementData.getType();

                if (INSERT_SELECT == type || INSERT_VALUES == type
                        || CREATE_TABLE_AS_SELECT == type || REPLACE_TABLE_AS_SELECT == type) {
                    SparkEnv.sql(sql);
                } else if (SELECT == type || CALL == type) {
                    Dataset<Row> dataSet = SparkEnv.sql(sql);
                    if (dataSet.schema().fields().length > 0) {
                        String result = dataSet.showString(20, 20, false);
                        LogUtils.stdout("query result:\n" + result);
                    }
                } else if (StatementType.EXPLAIN == type) {
                    Dataset<Row> dataSet = SparkEnv.sql(sql);
                    List<Row> rows = dataSet.collectAsList();
                    if (rows.size() > 0) {
                        String plan = rows.get(0).getString(0);
                        LogUtils.info("------sql plan------\n" + plan);
                    }
                } else {
                    LogUtils.error("not support sql: " + sql);
                }
            }
        }
    }
}
