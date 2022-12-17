package io.github.melin.spark.jobserver.driver.util;

import io.github.melin.spark.jobserver.api.LogLevel;
import io.github.melin.spark.jobserver.core.util.LogRecord;
import io.github.melin.spark.jobserver.core.util.TaskStatusFlag;
import io.github.melin.spark.jobserver.driver.InstanceContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;

public class LogUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtils.class);

    public static ConcurrentSkipListSet<String> executorLogSet = new ConcurrentSkipListSet<>();

    private static final ConcurrentHashMap<String, LinkedBlockingQueue<LogRecord>> LOG_QUEUE_MAP = new ConcurrentHashMap<>();

    private static final List<LogRecord> LOG_CACHE = new LinkedList<>();

    // pythonJobTemplate.py 文件中 ##PYTHON_TEMPLE## 所在行号
    public static final int USE_CODE_START_LINENO = 116 - 1;

    public static List<LogRecord> getMessage(String instanceCode){
        LOG_CACHE.clear();
        while (LOG_CACHE.size() < 10) {
            LogRecord log = getLogQueue(instanceCode).poll();
            if (log != null) {
                LOG_CACHE.add(log);
            } else {
                break;
            }
        }
        return LOG_CACHE;
    }

    private static LinkedBlockingQueue<LogRecord> getLogQueue(String instanceCode) {
        if (!LOG_QUEUE_MAP.containsKey(instanceCode)) {
            LOG_QUEUE_MAP.computeIfAbsent(instanceCode, (key) -> new LinkedBlockingQueue<>());
        }
        return LOG_QUEUE_MAP.get(instanceCode);
    }

    private static void putMsg(String instanceCode, String message, LogLevel level){
        try {
            LinkedBlockingQueue<LogRecord> logger = getLogQueue(instanceCode);
            if (logger != null && StringUtils.isNotBlank(message)) {
                if (LogLevel.STDOUT == level) {
                    level = LogLevel.INFO;
                }

                logger.put(LogRecord.of(level, message));
            }
        } catch (Exception e){
            LOGGER.error("发送消息 " + message + ", 失败：" + e.getMessage());
        }
    }

    public static void info(String format, Object... params) {
        String message = format;
        if (params.length > 0) {
            message = MessageFormatter.arrayFormat(format, params).getMessage();
        }

        putMsg(InstanceContext.getInstanceCode(), message, LogLevel.INFO);
    }

    public static void warn(String format, Object... params) {
        String message = format;
        if (params.length > 0) {
            message = MessageFormatter.arrayFormat(format, params).getMessage();
        }

        putMsg(InstanceContext.getInstanceCode(), message, LogLevel.WARN);
    }

    public static void error(String format, Object... params) {
        String message = format;
        if (params.length > 0) {
            message = MessageFormatter.arrayFormat(format, params).getMessage();
        }

        putMsg(InstanceContext.getInstanceCode(), message, LogLevel.ERROR);
    }

    public static void stdout(String format, Object... params) {
        String message = format;
        if (params.length > 0) {
            message = MessageFormatter.arrayFormat(format, params).getMessage();
        }

        putMsg(InstanceContext.getInstanceCode(), message, LogLevel.STDOUT);
    }

    public static void clearLog(String instanceCode){
        LOG_QUEUE_MAP.remove(instanceCode);
    }

    public static void sendTaskStatusFlag(TaskStatusFlag flag) {
        sendTaskStatusFlag(flag, flag.name());
    }

    public static void sendTaskStatusFlag(TaskStatusFlag flag, String message){
        try {
            String instanceCode = InstanceContext.getInstanceCode();
            LinkedBlockingQueue<LogRecord> logger = getLogQueue(instanceCode);
            if (logger.size() > 100) {
                logger.clear();
            }
            if (StringUtils.isNotBlank(message)) {
                logger.put(LogRecord.of(LogLevel.ERROR, flag, message));
            }
        } catch (Exception ignored){ }
    }

    public static String resolvePythonErrMsg(String msg) {
        return resolvePythonErrMsg(null, msg, false);
    }

    /**
     * 处理python异常信息
     */
    public static String resolvePythonErrMsg(String executorHost, String msg, boolean executor) {
        String instanceCode = InstanceContext.getInstanceCode();
        String[] lines = msg.split("\n");
        int i = 0;
        int lineNo = 0;
        String codeLine = null;
        String causeMessage = null;

        while (i < lines.length) {
            String line = StringUtils.trim(lines[i]);
            i = i + 1;

            if (StringUtils.startsWith(line, "File ") && StringUtils.contains(line, instanceCode)) {
                String lineNoStr = StringUtils.substringBetween(line, "line ", ",");
                if (StringUtils.isBlank(lineNoStr)) {
                    lineNoStr = StringUtils.substringAfter(line, "line");
                }
                int tmpLineNo = Integer.parseInt(lineNoStr.trim());
                // 定位用户代码所在行号
                if (USE_CODE_START_LINENO < tmpLineNo) {
                    lineNo = tmpLineNo;
                    codeLine = StringUtils.trim(line);
                }

                i = i + 1;
            } else if (StringUtils.startsWith(line, "Caused by:")) {
                causeMessage = StringUtils.substringAfter(line, "Caused by:");

                String cause = "empty fields are illegal, the field should be ommited completely instead";
                if (StringUtils.contains(line, cause)) {
                    causeMessage = cause + "\n" + "当map或array类型的列插入空集合或者map中存在key为null时，触发此错误，空集合置为null可以解决此错误";
                }
            } else if (StringUtils.contains(line, "Exception:") || StringUtils.contains(line, "Error:")) {
                causeMessage = line;
            }
        }

        if (StringUtils.startsWith(codeLine, "TypeError:")) {
            causeMessage = codeLine;
        }

        causeMessage = StringUtils.replaceAll(causeMessage, "\\\\n", "\n");

        if (StringUtils.contains(causeMessage,
                "org.apache.arrow.vector.util.OversizedAllocationException: Unable to expand the buffer")) {
            causeMessage = "Arrow OversizedAllocationException: Unable to expand the buffer，尝试适当调整大 spark.sql.shuffle.partitions 值，默认值：400";
        } else if (StringUtils.contains(causeMessage,
                "Futures timed out after")) {
            causeMessage = causeMessage + ", 广播数据量太大超时，设置 spark.sql.autoBroadcastJoinThreshold = -1 关闭广播";
        }

        // 去掉异常名称前缀
        String cm = StringUtils.substringAfter(causeMessage, "Exception: ");
        if (StringUtils.isNotBlank(cm)) {
            causeMessage = cm;
        }
        cm = StringUtils.substringAfter(causeMessage, "Error: ");
        if (StringUtils.isNotBlank(cm)) {
            causeMessage = cm;
        }

        if (StringUtils.isBlank(causeMessage)) {
            return msg;
        } else {
            if (executor) {
                if (lineNo > 0) {
                    return "Executor(" + executorHost + ")执行失败, 失败所在代码行号：" + (lineNo - USE_CODE_START_LINENO) + ", 失败原因：" + causeMessage;
                } else {
                    return "Executor(" + executorHost + ")执行失败， 失败原因：" + causeMessage;
                }
            } else {
                return "\n\t失败所在代码行号：" + (lineNo - USE_CODE_START_LINENO) + "\n\t失败原因：" + causeMessage;
            }
        }
    }
}
