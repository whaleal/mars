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
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.mars.core.query.Converter;
import org.bson.Document;

import java.util.*;

/**
 * Utility methods for JSON serialization.
 */
public abstract class SerializationUtil {

    private SerializationUtil() {

    }

    /**
     * Flattens out a given {@link Document}.
     *
     * <pre>
     * <code>
     * {
     *   _id : 1
     *   nested : { value : "conflux"}
     * }
     * </code>
     * will result in
     * <code>
     * {
     *   _id : 1
     *   nested.value : "conflux"
     * }
     * </code>
     * </pre>
     *
     * @param source can be {@literal null}.
     * @return {@link Collections#emptyMap()} when source is {@literal null}
     */
    public static Map<String, Object> flattenMap(@Nullable Document source) {

        if (source == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        toFlatMap("", source, result);
        return result;
    }

    private static void toFlatMap(String currentPath, Object source, Map<String, Object> map) {

        if (source instanceof Document) {

            Document document = (Document) source;
            Iterator<Map.Entry<String, Object>> it = document.entrySet().iterator();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + '.';

            while (it.hasNext()) {

                Map.Entry<String, Object> entry = it.next();

                if (entry.getKey().startsWith("$")) {
                    if (map.containsKey(currentPath)) {
                        ((Document) map.get(currentPath)).put(entry.getKey(), entry.getValue());
                    } else {
                        map.put(currentPath, new Document(entry.getKey(), entry.getValue()));
                    }
                } else {

                    toFlatMap(pathPrefix + entry.getKey(), entry.getValue(), map);
                }
            }
        } else {
            map.put(currentPath, source);
        }
    }

    /**
     * Serializes the given object into pseudo-JSON meaning it's trying to create a JSON representation as far as possible
     * but falling back to the given object's {@link Object#toString()} method if it's not serializable. Useful for
     * printing raw {@link Document}s containing complex values before actually converting them into Mongo native types.
     *
     * @param value
     * @return the serialized value or {@literal null}.
     */
    @Nullable
    public static String serializeToJsonSafely(@Nullable Object value) {

        if (value == null) {
            return null;
        }

        try {
            String json = value instanceof Document ? ((Document) value).toJson() : serializeValue(value);
            return json.replaceAll("\":", "\" :").replaceAll("\\{\"", "{ \"");
        } catch (Exception e) {

            if (value instanceof Collection) {
                return toString((Collection<?>) value);
            } else if (value instanceof Map) {
                return toString((Map<?, ?>) value);
            } else if (ObjectUtil.isArray(value)) {
                return toString(Arrays.asList(ObjectUtil.toArray(value)));
            } else {
                return String.format("{ \"$java\" : %s }", value.toString());
            }
        }
    }

    public static String serializeValue(@Nullable Object value) {

        if (value == null) {
            return "null";
        }

        String documentJson = new Document("toBeEncoded", value).toJson();
        return documentJson.substring(documentJson.indexOf(':') + 1, documentJson.length() - 1).trim();
    }

    private static String toString(Map<?, ?> source) {
        return iterableToDelimitedString(source.entrySet(), "{ ", " }",
                entry -> String.format("\"%s\" : %s", entry.getKey(), serializeToJsonSafely(entry.getValue())));
    }

    private static String toString(Collection<?> source) {
        return iterableToDelimitedString(source, "[ ", " ]", SerializationUtil::serializeToJsonSafely);
    }

    /**
     * Creates a string representation from the given {@link Iterable} prepending the postfix, applying the given
     * {@link Converter} to each element before adding it to the result {@link String}, concatenating each element with
     * {@literal ,} and applying the postfix.
     *
     * @param source
     * @param prefix
     * @param postfix
     * @param transformer
     * @return
     */
    private static <T> String iterableToDelimitedString(Iterable<T> source, String prefix, String postfix,
                                                        Converter<? super T, Object> transformer) {

        StringBuilder builder = new StringBuilder(prefix);
        Iterator<T> iterator = source.iterator();

        while (iterator.hasNext()) {

            builder.append(transformer.convert(iterator.next()));
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        return builder.append(postfix).toString();
    }
}