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
package com.whaleal.mars.util;

import com.mongodb.lang.Nullable;

/**
 * Helper class for resolving placeholders in texts. Usually applied to file paths.
 *
 * <p>A text may contain {@code ${...}} placeholders, to be resolved as system properties:
 * e.g. {@code ${user.dir}}. Default values can be supplied using the ":" separator
 * between key and value.
 */
public abstract class SystemPropertyUtils {

    /**
     * Prefix for system property placeholders: "${".
     */
    public static final String PLACEHOLDER_PREFIX = "${";

    /**
     * Suffix for system property placeholders: "}".
     */
    public static final String PLACEHOLDER_SUFFIX = "}";

    /**
     * Value separator for system property placeholders: ":".
     */
    public static final String VALUE_SEPARATOR = ":";


    private static final PropertyPlaceholderHelper strictHelper =
            new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, false);

    private static final PropertyPlaceholderHelper nonStrictHelper =
            new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, true);


    /**
     * Resolve {@code ${...}} placeholders in the given text, replacing them with
     * corresponding system property values.
     *
     * @param text the String to resolve
     * @return the resolved String
     * @throws IllegalArgumentException if there is an unresolvable placeholder
     * @see #PLACEHOLDER_PREFIX
     * @see #PLACEHOLDER_SUFFIX
     */
    public static String resolvePlaceholders(String text) {
        return resolvePlaceholders(text, false);
    }

    /**
     * Resolve {@code ${...}} placeholders in the given text, replacing them with
     * corresponding system property values. Unresolvable placeholders with no default
     * value are ignored and passed through unchanged if the flag is set to {@code true}.
     *
     * @param text                           the String to resolve
     * @param ignoreUnresolvablePlaceholders whether unresolved placeholders are to be ignored
     * @return the resolved String
     * @throws IllegalArgumentException if there is an unresolvable placeholder
     * @see #PLACEHOLDER_PREFIX
     * @see #PLACEHOLDER_SUFFIX
     * and the "ignoreUnresolvablePlaceholders" flag is {@code false}
     */
    public static String resolvePlaceholders(String text, boolean ignoreUnresolvablePlaceholders) {
        if (text.isEmpty()) {
            return text;
        }
        PropertyPlaceholderHelper helper = (ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper);
        return helper.replacePlaceholders(text, new SystemPropertyPlaceholderResolver(text));
    }


    /**
     * PlaceholderResolver implementation that resolves against system properties
     * and system environment variables.
     */
    private static class SystemPropertyPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final String text;

        public SystemPropertyPlaceholderResolver(String text) {
            this.text = text;
        }

        @Override
        @Nullable
        public String resolvePlaceholder(String placeholderName) {
            try {
                String propVal = System.getProperty(placeholderName);
                if (propVal == null) {
                    // Fall back to searching the system environment.
                    propVal = System.getenv(placeholderName);
                }
                return propVal;
            } catch (Throwable ex) {
                System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" +
                        this.text + "] as system property: " + ex);
                return null;
            }
        }
    }

}
