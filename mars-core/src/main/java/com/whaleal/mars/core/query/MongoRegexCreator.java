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
package com.whaleal.mars.core.query;

import com.mongodb.lang.Nullable;

import java.util.regex.Pattern;


public enum MongoRegexCreator {

    INSTANCE;

    private static final Pattern PUNCTATION_PATTERN = Pattern.compile("\\p{Punct}");

    /**
     * Creates a regular expression String to be used with {@code $regex}.
     *
     * @param source      the plain String
     * @param matcherType the type of matching to perform
     * @return {@literal source} when {@literal source} or {@literal matcherType} is {@literal null}.
     */
    @Nullable
    public String toRegularExpression(@Nullable String source, @Nullable MatchMode matcherType) {

        if (matcherType == null || source == null) {
            return source;
        }

        String regex = prepareAndEscapeStringBeforeApplyingLikeRegex(source, matcherType);

        switch (matcherType) {
            case STARTING_WITH:
                return String.format("^%s", regex);
            case ENDING_WITH:
                return String.format("%s$", regex);
            case CONTAINING:
                return String.format(".*%s.*", regex);
            case EXACT:
                return String.format("^%s$", regex);
            default:
                return regex;
        }
    }

    private String prepareAndEscapeStringBeforeApplyingLikeRegex(String source, MatchMode matcherType) {

        if (MatchMode.REGEX == matcherType) {
            return source;
        }

        if (MatchMode.LIKE != matcherType) {
            return PUNCTATION_PATTERN.matcher(source).find() ? Pattern.quote(source) : source;
        }

        if (source.equals("*")) {
            return ".*";
        }

        StringBuilder sb = new StringBuilder();

        boolean leadingWildcard = source.startsWith("*");
        boolean trailingWildcard = source.endsWith("*");

        String valueToUse = source.substring(leadingWildcard ? 1 : 0,
                trailingWildcard ? source.length() - 1 : source.length());

        if (PUNCTATION_PATTERN.matcher(valueToUse).find()) {
            valueToUse = Pattern.quote(valueToUse);
        }

        if (leadingWildcard) {
            sb.append(".*");
        }

        sb.append(valueToUse);

        if (trailingWildcard) {
            sb.append(".*");
        }

        return sb.toString();
    }

    /**
     * Match modes for treatment of {@link String} values.
     */
    public enum MatchMode {

        /**
         * Store specific default.
         */
        DEFAULT,

        /**
         * Matches the exact string
         */
        EXACT,

        /**
         * Matches string starting with pattern
         */
        STARTING_WITH,

        /**
         * Matches string ending with pattern
         */
        ENDING_WITH,

        /**
         * Matches string containing pattern
         */
        CONTAINING,

        /**
         * Treats strings as regular expression patterns
         */
        REGEX,

        LIKE;
    }
}
