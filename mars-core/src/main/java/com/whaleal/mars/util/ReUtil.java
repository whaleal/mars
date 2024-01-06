package com.whaleal.mars.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 正则相关工具类<br>
 *
 *
 *
 * @author wh
 */
public class ReUtil {


    public static final Pattern WILDCARD_PATTERN = Pattern.compile("\\?|\\*\\*|\\*");

    /**
     *
     * 返回与此 path 等效的正则表达式。
     * @param path  传入的字符串
     * @return
     */
    public static String toRegex(String path) {

        StringBuilder patternBuilder = new StringBuilder();
        Matcher m = WILDCARD_PATTERN.matcher(path);
        int end = 0;

        while (m.find()) {

            patternBuilder.append(quote(path, end, m.start()));
            String match = m.group();

            if ("?".equals(match)) {
                patternBuilder.append('.');
            } else if ("**".equals(match)) {
                patternBuilder.append(".*");
            } else if ("*".equals(match)) {
                patternBuilder.append("[^/]*");
            }

            end = m.end();
        }

        patternBuilder.append(quote(path, end, path.length()));
        return patternBuilder.toString();
    }

    private static String quote(String s, int start, int end) {
        if (start == end) {
            return "";
        }
        return Pattern.quote(s.substring(start, end));
    }


    /**
     *
     * 返回是否为一个 正则式
     *
     * @param path  字符串
     * @return
     */
    public static boolean isPattern(String path) {
        String path0 = stripPrefix(path);
        return (path0.indexOf('*') != -1 || path0.indexOf('?') != -1);
    }

    private static String stripPrefix(String path) {
        int index = path.indexOf( ":");
        return (index > -1 ? path.substring(index + 1) : path);
    }





}
