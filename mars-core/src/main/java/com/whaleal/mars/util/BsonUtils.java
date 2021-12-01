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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoClientSettings;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ArrayUtil;
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.mars.core.mapping.CodecRegistryProvider;
import com.whaleal.mars.core.query.Converter;
import org.bson.*;
import org.bson.codecs.DocumentCodec;
import org.bson.conversions.Bson;
import org.bson.json.JsonParseException;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;


public class BsonUtils {

    @SuppressWarnings("unchecked")

    public static <T> T get(Bson bson, String key) {
        return (T) asMap(bson).get(key);
    }

    public static Map<String, Object> asMap(Bson bson) {

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
     * Extract the corresponding plain value from {@link BsonValue}. Eg. plain {@link String} from
     * {@link BsonString}.
     *
     * @param value must not be {@literal null}.
     * @return
     */
    public static Object toJavaType(BsonValue value) {

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
     * Convert a given simple value (eg. {@link String}, {@link Long}) to its corresponding {@link BsonValue}.
     *
     * @param source must not be {@literal null}.
     * @return the corresponding {@link BsonValue} representation.
     * @throws IllegalArgumentException if {@literal source} does not correspond to a {@link BsonValue} type.
     */
    public static BsonValue simpleToBsonValue(Object source) {

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
     * Merge the given {@link Document documents} into on in the given order. Keys contained within multiple documents are
     * overwritten by their follow ups.
     *
     * @param documents must not be {@literal null}. Can be empty.
     * @return the document containing all key value pairs.
     */
    public static Document merge(Document... documents) {

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

    /**
     * @param source
     * @param orElse
     * @return
     */
    public static Document toDocumentOrElse(String source, Function<String, Document> orElse) {

        if (StrUtil.trimLeadingWhitespace(source).startsWith("{")) {
            return Document.parse(source);
        }

        return orElse.apply(source);
    }

    /**
     * Serialize the given {@link Document} as Json applying default codecs if necessary.
     *
     * @param source
     * @return
     */

    public static String toJson( Document source ) {

        if (source == null) {
            return null;
        }

        try {
            return source.toJson();
        } catch (Exception e) {
            return toJson((Object) source);
        }
    }

    /**
     * Check if a given String looks like {@link Document#parse(String) parsable} json.
     *
     * @param value can be {@literal null}.
     * @return {@literal true} if the given value looks like a json document.
     */
    public static boolean isJsonDocument( String value ) {
        return StrUtil.hasText(value) && (value.startsWith("{") && value.endsWith("}"));
    }

    /**
     * Check if a given String looks like {@link BsonArray#parse(String) parsable} json array.
     *
     * @param value can be {@literal null}.
     * @return {@literal true} if the given value looks like a json array.
     */
    public static boolean isJsonArray( String value ) {
        return StrUtil.hasText(value) && (value.startsWith("[") && value.endsWith("]"));
    }

    /**
     * Parse the given {@literal json} to {@link Document} applying transformations as specified by a potentially given
     * {@link org.bson.codecs.Codec}.
     *
     * @param json                  must not be {@literal null}.
     * @param codecRegistryProvider can be {@literal null}. In that case the default {@link DocumentCodec} is used.
     * @return never {@literal null}.
     * @throws IllegalArgumentException if the required argument is {@literal null}.
     */
    public static Document parse( String json, CodecRegistryProvider codecRegistryProvider ) {

        Precondition.notNull(json, "Json must not be null!");

        if (codecRegistryProvider == null) {
            return Document.parse(json);
        }

        return Document.parse(json, codecRegistryProvider.getCodecFor(Document.class)
                .orElseGet(() -> new DocumentCodec(codecRegistryProvider.getCodecRegistry())));
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
                return toString((Collection<?>) value);
            } else if (value instanceof Map) {
                return toString((Map<?, ?>) value);
            } else if (ObjectUtil.isArray(value)) {
                return toString(Arrays.asList(ObjectUtil.toArray(value)));
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

    private static String toString(Map<?, ?> source) {

        return iterableToDelimitedString(source.entrySet(), "{ ", " }",
                entry -> String.format("\"%s\" : %s", entry.getKey(), toJson(entry.getValue())));
    }

    private static String toString(Collection<?> source) {
        return iterableToDelimitedString(source, "[ ", " ]", BsonUtils::toJson);
    }

    private static <T> String iterableToDelimitedString(Iterable<T> source, String prefix, String suffix,
                                                        Converter<? super T, String> transformer) {

        StringJoiner joiner = new StringJoiner(", ", prefix, suffix);

        StreamSupport.stream(source.spliterator(), false).map(transformer::convert).forEach(joiner::add);

        return joiner.toString();
    }
}
