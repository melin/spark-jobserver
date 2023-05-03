package io.github.melin.spark.jobserver.driver.task;

import com.gitee.melin.bee.util.JsonUtils;
import io.github.melin.spark.jobserver.core.util.CommonUtils;
import io.github.melin.spark.jobserver.core.util.HttpClientUtils;
import io.github.melin.spark.jobserver.driver.SparkDriverEnv;
import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.driver.support.ConfigClient;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import io.github.melin.superior.common.SQLParserException;
import io.github.melin.superior.common.StatementType;
import io.github.melin.superior.common.relational.StatementData;
import io.github.melin.superior.common.relational.dml.QueryStmt;
import io.github.melin.superior.parser.spark.SparkSqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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

        final String instanceCode = instanceDto.getInstanceCode();
        final String resultCallbackUri = instanceDto.getResultCallbackUri();

        for (String row : sqls) {
            String sql = StringUtils.trim(row);
            if (StringUtils.isNotBlank(sql)) {
                StatementData statementData = SparkSqlHelper.getStatementData(sql);
                StatementType type = statementData.getType();

                if (StatementType.SELECT == type) {
                    QueryStmt tableData = (QueryStmt) statementData.getStatement();
                    int maxRecords = ConfigClient.getInstance()
                            .getInt("jobserver.driver.sql.query.max.records", 1000);

                    if (tableData.getInputTables().size() > 0) {
                        if (tableData.getLimit() == null) {
                            sql = sql + " limit " + maxRecords;
                        } else if (tableData.getLimit() > maxRecords) {
                            throw new SQLParserException("sql limit 不能超过 " + maxRecords);
                        }
                    }

                    Dataset<Row> dataSet = SparkDriverEnv.sql(sql);
                    if (dataSet.schema().fields().length > 0) {
                        if (StringUtils.isBlank(resultCallbackUri)) {
                            String result = dataSet.showString(20, 20, false);
                            LogUtils.stdout("query result:\n" + result);
                        } else {
                            callbackResultData(dataSet, instanceCode, resultCallbackUri, sql);
                        }
                    }
                } else if (StatementType.EXPLAIN == type) {
                    Dataset<Row> dataSet = SparkDriverEnv.sql(sql);
                    if (StringUtils.isBlank(resultCallbackUri)) {
                        List<Row> rows = dataSet.collectAsList();
                        if (rows.size() > 0) {
                            String plan = rows.get(0).getString(0);
                            LogUtils.info("------sql plan------\n" + plan);
                        }
                    } else {
                        callbackResultData(dataSet, instanceCode, resultCallbackUri, sql);
                    }
                } else if (StatementType.CALL == type || StatementType.SHOW == type || StatementType.DESC == type) {
                    Dataset<Row> dataSet = SparkDriverEnv.sql(sql);
                    if (StringUtils.isBlank(resultCallbackUri)) {
                        String result = dataSet.showString(20, 20, false);
                        LogUtils.stdout("query result:\n" + result);
                    } else {
                        callbackResultData(dataSet, instanceCode, resultCallbackUri, sql);
                    }
                } else {
                    SparkDriverEnv.sql(sql);
                }
            }
        }
    }

    public void callbackResultData(Dataset<Row> dataSet, String instanceCode, String resultCallbackUri, String sql) {
        if (StringUtils.isBlank(resultCallbackUri)) {
            return;
        }

        try {
            LinkedList<String> schemaList = new LinkedList<>();
            LinkedList<String> columnNames = new LinkedList<>();
            LinkedList<String> typeNames = new LinkedList<>();
            LinkedList<StructField> fieldList = new LinkedList<>();
            StructType schema = dataSet.schema();

            //防止重列名
            for (int index = 0, len = schema.fields().length; index < len; index++) {
                StructField field = schema.fields()[index];
                fieldList.add(field);
                columnNames.add(field.name());
                String dataType = field.dataType().catalogString();
                typeNames.add(dataType);
                schemaList.add(field.name() + "_" + index);
            }

            List<Row> rows = dataSet.collectAsList();
            LinkedList<Map<String, String>> data = new LinkedList<>();
            for (Row row : rows) {
                HashMap<String, String> map = new HashMap<>();
                for (int index = 0, len = row.length(); index < len; index++) {
                    String value = "#NULL";
                    Object obj = row.get(index);
                    StructField field = fieldList.get(index);
                    String fieldType = field.dataType().typeName();
                    if (obj != null) {
                        if (fieldType.contains("map") || fieldType.contains("array")
                                || fieldType.contains("struct")) {

                            value = JsonUtils.toJSONString(obj);
                        } else {
                            value = obj.toString();
                        }
                    }
                    map.put(schemaList.get(index), value);
                }
                data.add(map);
            }

            HashMap<String, Object> result = new HashMap<>();
            result.put("schemas", schemaList);
            result.put("columnNames", columnNames);
            result.put("data", data);
            result.put("typeNames", typeNames);
            result.put("command", sql);
            String content = JsonUtils.toJSONString(result);

            HttpClientUtils.postRequet(resultCallbackUri + "/innerApi/v1/receiveJobResult",
                    "instanceCode", instanceCode, "result", content);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            LogUtils.error("调用接口失败: " + e.getMessage());
        }
    }
}
