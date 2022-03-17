/**
 * Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 * <p>
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.whaleal.com/licensing/server-side-public-license>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the Server Side Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.whaleal.mars.core.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoClientSettings;
import com.whaleal.icefrog.core.util.ArrayUtil;
import com.whaleal.icefrog.core.util.ObjectUtil;
import org.bson.*;
import org.bson.conversions.Bson;
import org.bson.json.JsonParseException;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;


public class BsonUtil {

    @SuppressWarnings("unchecked")
    public static < T > T get( Bson bson, String key ) {
        return (T) asMap(bson).get(key);
    }

    public static Map< String, Object > asMap( Bson bson ) {

        if (bson instanceof Document) {
            return (Document) bson;
        }
        if (bson instanceof BasicDBObject) {
            return ((BasicDBObject) bson);
        }

        return (Map) bson.toBsonDocument(Document.class, MongoClientSettings.getDefaultCodecRegistry());
    }

    public static void addToMap( Bson bson, String key, Object value ) {

        if (bson instanceof Document) {
            ((Document) bson).put(key, value);
            return;
        }
        if (bson instanceof DBObject) {
            ((DBObject) bson).put(key, value);
            return;
        }
        throw new IllegalArgumentException("o_O what's that? Cannot add value to " + bson.getClass());
    }

    /**
     * 从BsonValue中提取响应的普通值，例如从BsonString中抽取普通的String
     *
     * @param value 不能为空.
     * @return
     */
    public static Object toJavaType( BsonValue value ) {

        switch (value.getBsonType()) {
            case INT32:
                return value.asInt32().getValue();
            case INT64:
                return value.asInt64().getValue();
            case STRING:
                return value.asString().getValue();
            case DECIMAL128:
                return value.asDecimal128().doubleValue();
            case DOUBLE:
                return value.asDouble().getValue();
            case BOOLEAN:
                return value.asBoolean().getValue();
            case OBJECT_ID:
                return value.asObjectId().getValue();
            case DB_POINTER:
                return new DBRef(value.asDBPointer().getNamespace(), value.asDBPointer().getId());
            case BINARY:
                return value.asBinary().getData();
            case DATE_TIME:
                return new Date(value.asDateTime().getValue());
            case SYMBOL:
                return value.asSymbol().getSymbol();
            case ARRAY:
                return value.asArray().toArray();
            case DOCUMENT:
                return Document.parse(value.asDocument().toJson());
            default:
                return value;
        }
    }

    /**
     * 把给定的简单值（如String，Long）转换为其对应的BsonValue
     *
     * @param source 不能为空.
     * @return 对应的BsonValue.
     * @throws IllegalArgumentException 如果source类型不支持转换为BsonValue.
     */
    public static BsonValue simpleToBsonValue( Object source ) {

        if (source instanceof BsonValue) {
            return (BsonValue) source;
        }

        if (source instanceof ObjectId) {
            return new BsonObjectId((ObjectId) source);
        }

        if (source instanceof String) {
            return new BsonString((String) source);
        }

        if (source instanceof Double) {
            return new BsonDouble((Double) source);
        }

        if (source instanceof Integer) {
            return new BsonInt32((Integer) source);
        }

        if (source instanceof Long) {
            return new BsonInt64((Long) source);
        }

        if (source instanceof byte[]) {
            return new BsonBinary((byte[]) source);
        }

        if (source instanceof Boolean) {
            return new BsonBoolean((Boolean) source);
        }

        if (source instanceof Float) {
            return new BsonDouble((Float) source);
        }

        throw new IllegalArgumentException(String.format("Unable to convert %s (%s) to BsonValue.", source,
                source != null ? source.getClass().getName() : "null"));
    }

    /**
     * 按入参的顺序把多个document合并到一个document中，同一key值的多个document只会保留最后一个document
     *
     * @param documents 必须传参，但参数可以为"empty".
     * @return 如果document为空，则返回一个空的document，不为空，返回合并后的结果.
     */
    public static Document merge( Document... documents ) {

        if (ObjectUtil.isEmpty(documents)) {
            return new Document();
        }

        if (documents.length == 1) {
            return documents[0];
        }

        Document target = new Document();
        Arrays.asList(documents).forEach(target::putAll);
        return target;
    }


    private static String toJson( Object value ) {

        if (value == null) {
            return null;
        }

        try {
            return value instanceof Document
                    ? ((Document) value).toJson(MongoClientSettings.getDefaultCodecRegistry().get(Document.class))
                    : serializeValue(value);

        } catch (Exception e) {

            if (value instanceof Collection) {
                return toString((Collection< ? >) value);
            } else if (value instanceof Map) {
                return toString((Map< ?, ? >) value);
            } else if (ObjectUtil.isArray(value)) {
                return toString(Arrays.asList(ArrayUtil.toArray(value)));
            }

            throw e instanceof JsonParseException ? (JsonParseException) e : new JsonParseException(e);
        }
    }

    private static String serializeValue( Object value ) {

        if (value == null) {
            return "null";
        }

        String documentJson = new Document("toBeEncoded", value).toJson();
        return documentJson.substring(documentJson.indexOf(':') + 1, documentJson.length() - 1).trim();
    }

    private static String toString( Map< ?, ? > source ) {

        return iterableToDelimitedString(source.entrySet(), "{ ", " }",
                entry -> String.format("\"%s\" : %s", entry.getKey(), toJson(entry.getValue())));
    }

    private static String toString( Collection< ? > source ) {
        return iterableToDelimitedString(source, "[ ", " ]", BsonUtil::toJson);
    }

    private static < T > String iterableToDelimitedString( Iterable< T > source, String prefix, String suffix,
                                                           Function< T, String > transformer ) {

        StringJoiner joiner = new StringJoiner(", ", prefix, suffix);

        StreamSupport.stream(source.spliterator(), false).map(transformer).forEach(joiner::add);

        return joiner.toString();
    }
}
