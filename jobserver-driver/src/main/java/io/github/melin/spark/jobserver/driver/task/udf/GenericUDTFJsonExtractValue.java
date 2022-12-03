package io.github.melin.spark.jobserver.driver.task.udf;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author melin 2021/9/13 10:08 上午
 */
public class GenericUDTFJsonExtractValue extends GenericUDTF {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        JSON_MAPPER.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        JSON_MAPPER.enable(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature());
        JSON_MAPPER.enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature());
        JSON_MAPPER.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @Override
    public StructObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        if (arguments.length < 2) {
            throw new UDFArgumentLengthException("JsonExtractValueUDTF 至少两个参数");
        }
        if (arguments[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("JsonExtractValueUDTF 第一个参数为String类型");
        }

        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs   = new ArrayList<ObjectInspector>();
        for (int i = 1; i < arguments.length; i++) {
            fieldNames.add("col" + i);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        }

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    @Override
    public void process(Object[] arguments) throws HiveException {
        String inputJson = "";
        String[] result = new String[arguments.length - 1];
        try {
            if (arguments[0] instanceof Text) {
                inputJson = arguments[0].toString();
            } else {
                inputJson = (String) arguments[0];
            }

            if (StringUtils.isNotBlank(inputJson)) {
                JsonNode node = JSON_MAPPER.readTree(inputJson);
                for (int i = 1; i < arguments.length; i++) {
                    String key = arguments[i].toString();

                    if (!StringUtils.startsWith(key, "/")) {
                        key = "/" + key;
                    }
                    JsonNode jsonNode = node.at(key);
                    String value;

                    if (jsonNode instanceof MissingNode) {
                        value = null;
                    } else {
                        if (jsonNode.isValueNode()) {
                            if (jsonNode.isBigDecimal()) {
                                BigDecimal decimalValue = jsonNode.decimalValue();
                                value = decimalValue.toPlainString();
                            } else {
                                value = jsonNode.asText();
                            }
                        } else {
                            value = JSON_MAPPER.writeValueAsString(jsonNode);
                        }
                    }

                    result[i - 1] = value;
                }
            }
        } catch (Exception e) {
            result[0] = "error: " + e.getMessage();
        }
        forward(result);
    }

    @Override
    public void close() throws HiveException {
    }
}
