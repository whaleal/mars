/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package com.whaleal.mars.gridfs;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路径校验规则
 *
 * @author cs
 * @date 2021/04/21
 *
 *
 */

@Deprecated
public class ReUtil {

    private static final String PREFIX_DELIMITER = ":";
    private static final Pattern WILDCARD_PATTERN = Pattern.compile("\\?|\\*\\*|\\*");



    /**
     * Returns whether the path is a pattern.
     *
     * @return
     */
    public static boolean isPattern(String path1) {
        String path = stripPrefix(path1);
        return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
    }

    private static String stripPrefix(String path) {
        int index = path.indexOf(PREFIX_DELIMITER);
        return (index > -1 ? path.substring(index + 1) : path);
    }

    /**
     * Returns the regular expression equivalent of this Ant path.
     *
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

}
