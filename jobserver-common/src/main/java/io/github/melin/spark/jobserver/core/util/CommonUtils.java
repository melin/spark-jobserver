package io.github.melin.spark.jobserver.core.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * huaixin 2022/4/7 12:32 PM
 */
public class CommonUtils {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private static final ImmutableMap<String, ByteUnit> byteSuffixes =
            ImmutableMap.<String, ByteUnit>builder()
                    .put("b", ByteUnit.BYTE)
                    .put("k", ByteUnit.KiB)
                    .put("kb", ByteUnit.KiB)
                    .put("m", ByteUnit.MiB)
                    .put("mb", ByteUnit.MiB)
                    .put("g", ByteUnit.GiB)
                    .put("gb", ByteUnit.GiB)
                    .put("t", ByteUnit.TiB)
                    .put("tb", ByteUnit.TiB)
                    .put("p", ByteUnit.PiB)
                    .put("pb", ByteUnit.PiB)
                    .build();

    /**
     * Convert a passed byte string (e.g. 50b, 100kb, or 250mb) to the given. If no suffix is
     * provided, a direct conversion to the provided unit is attempted.
     */
    public static long byteStringAs(String str, ByteUnit unit) {
        String lower = str.toLowerCase(Locale.ROOT).trim();

        try {
            Matcher m = Pattern.compile("([0-9]+)([a-z]+)?").matcher(lower);
            Matcher fractionMatcher = Pattern.compile("([0-9]+\\.[0-9]+)([a-z]+)?").matcher(lower);

            if (m.matches()) {
                long val = Long.parseLong(m.group(1));
                String suffix = m.group(2);

                // Check for invalid suffixes
                if (suffix != null && !byteSuffixes.containsKey(suffix)) {
                    throw new NumberFormatException("Invalid suffix: \"" + suffix + "\"");
                }

                // If suffix is valid use that, otherwise none was provided and use the default passed
                return unit.convertFrom(val, suffix != null ? byteSuffixes.get(suffix) : unit);
            } else if (fractionMatcher.matches()) {
                throw new NumberFormatException("Fractional values are not supported. Input was: "
                        + fractionMatcher.group(1));
            } else {
                throw new NumberFormatException("Failed to parse byte string: " + str);
            }

        } catch (NumberFormatException e) {
            String byteError = "Size must be specified as bytes (b), " +
                    "kibibytes (k), mebibytes (m), gibibytes (g), tebibytes (t), or pebibytes(p). " +
                    "E.g. 50b, 100k, or 250m.";

            throw new NumberFormatException(byteError + "\n" + e.getMessage());
        }
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to bytes for
     * internal use.
     * If no suffix is provided, the passed number is assumed to be in bytes.
     */
    public static long byteStringAsBytes(String str) {
        return byteStringAs(str, ByteUnit.BYTE);
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to kibibytes for
     * internal use.
     * If no suffix is provided, the passed number is assumed to be in kibibytes.
     */
    public static long byteStringAsKb(String str) {
        return byteStringAs(str, ByteUnit.KiB);
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to mebibytes for
     * internal use.
     * If no suffix is provided, the passed number is assumed to be in mebibytes.
     */
    public static long byteStringAsMb(String str) {
        return byteStringAs(str, ByteUnit.MiB);
    }

    /**
     * Convert a passed byte string (e.g. 50b, 100k, or 250m) to gibibytes for
     * internal use.
     * If no suffix is provided, the passed number is assumed to be in gibibytes.
     */
    public static long byteStringAsGb(String str) {
        return byteStringAs(str, ByteUnit.GiB);
    }

    public static String convertUnit(long memory) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if (memory < 1024) {
            return memory + "kB";
        } else if (memory < (1024 * 1024)) {
            return decimalFormat.format(memory / 1024) + "MB";
        } else if (memory < (1024 * 1024 * 1024)) {
            return decimalFormat.format(memory / (1024.0 * 1024)) + "GB";
        } else {
            return decimalFormat.format(memory / (1024.0 * 1024 * 1024)) + "TB";
        }
    }

    public static String convertTime(Long millSeconds) {
        long hours = millSeconds / (1000 * 3600);
        long minutes = (millSeconds - hours * 3600 * 1000) / (1000 * 60);
        long seconds = (millSeconds - hours * 3600 * 1000 - minutes * 60 * 1000) / 1000;

        StringBuilder time = new StringBuilder();
        if (hours > 0) {
            time.append(hours).append("时");
        }
        if (minutes > 0) {
            time.append(minutes).append("分");
        }
        if (seconds > 0) {
            time.append(seconds).append("秒");
        }

        if (StringUtils.isBlank(time.toString())) {
            return "0.1秒";
        } else {
            return time.toString();
        }
    }

    /**
     * 清除sql中多行和单行注释
     * http://daimojingdeyu.iteye.com/blog/382720
     */
    public static String cleanSqlComment(String sql) {
        boolean singleLineComment = false;
        List<Character> chars = Lists.newArrayList();
        List<Character> delChars = Lists.newArrayList();

        for (int i = 0, len = sql.length(); i < len; i++) {
            char ch = sql.charAt(i);

            if ((i + 1) < len) {
                char nextCh = sql.charAt(i + 1);
                if (ch == '-' && nextCh == '-' && !singleLineComment) {
                    singleLineComment = true;
                }
            }

            if (!singleLineComment) {
                chars.add(ch);
            }

            if (singleLineComment && ch == '\n') {
                singleLineComment = false;
                chars.add(ch);
            }
        }

        sql = StringUtils.join(chars, "");

        chars = Lists.newArrayList();
        boolean mutilLineComment = false;
        for (int i = 0, len = sql.length(); i < len; i++) {
            char ch = sql.charAt(i);

            if ((i + 2) < len) {
                char nextCh1 = sql.charAt(i + 1);
                char nextCh2 = sql.charAt(i + 2);
                if (ch == '/' && nextCh1 == '*' && nextCh2 != '+' && !mutilLineComment) {
                    mutilLineComment = true;
                }
            }

            if (!mutilLineComment) {
                chars.add(ch);

                if (delChars.size() > 0) {
                    delChars.clear();
                }
            } else {
                delChars.add(ch);
            }

            if ((i + 1) < len) {
                char nextCh1 = sql.charAt(i + 1);
                if (mutilLineComment && ch == '*' && nextCh1 == '/') {
                    mutilLineComment = false;
                    i++;
                }
            }
        }

        if (mutilLineComment) {
            chars.addAll(delChars);
            delChars.clear();
        }

        return StringUtils.join(chars, "");
    }

    /**
     * 多个sql语句用分号分割
     */
    public static List<String> splitMultiSql(String sql) {
        List<String> sqls = Lists.newArrayList();

        Character quote = null;
        Character lastChar = null;
        int lastIndex = 0;
        for (int i = 0, len = sql.length(); i < len; i++) {
            char ch = sql.charAt(i);
            if (i != 0) {
                lastChar = sql.charAt(i - 1);
            }

            if (ch == '\'') {
                if (quote == null) {
                    quote = ch;
                } else if (quote == '\'' && lastChar != '\\') {
                    quote = null;
                }
            } else if (ch == '"') {
                if (quote == null) {
                    quote = ch;
                } else if (quote == '"' && lastChar != '\\') {
                    quote = null;
                }
            } else if (ch == ';' && quote == null) {
                String content = StringUtils.substring(sql, lastIndex, i).trim();
                if (StringUtils.isNotBlank(content)) {
                    sqls.add(content);
                }
                lastIndex = i + 1;
            }
        }

        String content = StringUtils.substring(sql, lastIndex).trim();
        if (StringUtils.isNotBlank(content)) {
            sqls.add(content);
        }

        return sqls;
    }

    public static void checkDirExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalStateException("config dir/file not exists: " + path);
        }
    }
}
