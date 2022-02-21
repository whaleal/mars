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
import com.whaleal.icefrog.json.JSONUtil;
import org.bson.Document;

import java.util.*;

/**
 * Utility methods for JSON serialization.
 */
public abstract class DocumentUtil {

    private DocumentUtil() {
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
    public static String serializeToJsonSafely(@Nullable Document value) {

        if (value == null) {
            return null;
        }

        try {
            String json = value instanceof Document ? (value).toJson() : serializeValue(value);
            return json.replaceAll("\":", "\" :").replaceAll("\\{\"", "{ \"");
        } catch (Exception e) {

            if (value instanceof Collection) {
                return JSONUtil.toJsonStr((Collection<?>) value);
            } else if (value instanceof Map) {
                return JSONUtil.toJsonStr((Map<?, ?>) value);
            } else if (ObjectUtil.isArray(value)) {
                return JSONUtil.toJsonStr(Arrays.asList(ObjectUtil.toArray(value)));
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

}