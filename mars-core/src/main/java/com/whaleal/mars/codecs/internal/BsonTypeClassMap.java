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
package com.whaleal.mars.codecs.internal;

import org.bson.*;
import org.bson.types.*;

import java.util.*;

public class BsonTypeClassMap {
    static final BsonTypeClassMap DEFAULT_BSON_TYPE_CLASS_MAP = new BsonTypeClassMap();
    private final Map<BsonType, Class<?>> map;

    public BsonTypeClassMap(Map<BsonType, Class<?>> replacementsForDefaults) {
        this.map = new HashMap();
        this.addDefaults();
        this.map.putAll(replacementsForDefaults);
    }

    public BsonTypeClassMap() {
        this(Collections.emptyMap());
    }

    Set<BsonType> keys() {
        return this.map.keySet();
    }

    public Class<?> get(BsonType bsonType) {
        return (Class) this.map.get(bsonType);
    }

    private void addDefaults() {
        this.map.put(BsonType.ARRAY, List.class);
        this.map.put(BsonType.BINARY, Binary.class);
        this.map.put(BsonType.BOOLEAN, Boolean.class);
        this.map.put(BsonType.DATE_TIME, Date.class);
        this.map.put(BsonType.DB_POINTER, BsonDbPointer.class);
        this.map.put(BsonType.DOCUMENT, Document.class);
        this.map.put(BsonType.DOUBLE, Double.class);
        this.map.put(BsonType.INT32, Integer.class);
        this.map.put(BsonType.INT64, Long.class);
        this.map.put(BsonType.DECIMAL128, Decimal128.class);
        this.map.put(BsonType.MAX_KEY, MaxKey.class);
        this.map.put(BsonType.MIN_KEY, MinKey.class);
        this.map.put(BsonType.JAVASCRIPT, Code.class);
        this.map.put(BsonType.JAVASCRIPT_WITH_SCOPE, CodeWithScope.class);
        this.map.put(BsonType.OBJECT_ID, ObjectId.class);
        this.map.put(BsonType.REGULAR_EXPRESSION, BsonRegularExpression.class);
        this.map.put(BsonType.STRING, String.class);
        this.map.put(BsonType.SYMBOL, Symbol.class);
        this.map.put(BsonType.TIMESTAMP, BsonTimestamp.class);
        this.map.put(BsonType.UNDEFINED, BsonUndefined.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            BsonTypeClassMap that = (BsonTypeClassMap) o;
            return this.map.equals(that.map);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}
