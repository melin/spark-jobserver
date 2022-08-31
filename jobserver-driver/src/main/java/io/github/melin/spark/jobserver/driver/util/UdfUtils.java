package io.github.melin.spark.jobserver.driver.util;

public class UdfUtils {
    private static final String JAVA_REGEX_SPECIALS = "[]()|^-+*?{}$\\.";

    public static String sqlToRegexLike(String sqlPattern, CharSequence escapeStr) {
        final char escapeChar;
        if (escapeStr != null) {
            if (escapeStr.length() != 1) {
                throw invalidEscapeCharacter(escapeStr.toString());
            }
            escapeChar = escapeStr.charAt(0);
        } else {
            escapeChar = 0;
        }
        return sqlToRegexLike(sqlPattern, escapeChar);
    }

    private static RuntimeException invalidEscapeCharacter(String s) {
        return new RuntimeException("Invalid escape character: " + s);
    }

    private static RuntimeException invalidEscapeSequence(String s, int i) {
        return new RuntimeException("Invalid escape sequence" + s);
    }

    /**
     * Translates a SQL LIKE pattern to Java regex pattern.
     */
    public static String sqlToRegexLike(String sqlPattern, char escapeChar) {
        int i;
        final int len = sqlPattern.length();
        final StringBuilder javaPattern = new StringBuilder(len + len);
        for (i = 0; i < len; i++) {
            char c = sqlPattern.charAt(i);
            if (JAVA_REGEX_SPECIALS.indexOf(c) >= 0) {
                javaPattern.append('\\');
            }
            if (c == escapeChar) {
                if (i == (sqlPattern.length() - 1)) {
                    throw invalidEscapeSequence(sqlPattern, i);
                }
                char nextChar = sqlPattern.charAt(i + 1);
                if ((nextChar == '_')
                        || (nextChar == '%')
                        || (nextChar == escapeChar)) {
                    javaPattern.append(nextChar);
                    i++;
                } else {
                    throw invalidEscapeSequence(sqlPattern, i);
                }
            } else if (c == '_') {
                javaPattern.append('.');
            } else if (c == '%') {
                javaPattern.append(".");
                javaPattern.append('*');
            } else {
                javaPattern.append(c);
            }
        }
        return javaPattern.toString();
    }
}
