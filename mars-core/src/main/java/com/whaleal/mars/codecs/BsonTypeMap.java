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
package com.whaleal.mars.codecs;


import org.bson.*;
import org.bson.types.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BsonTypeMap {
    private final Map<Class<?>, BsonType> map = new HashMap<>();


    public BsonTypeMap() {
        map.put(List.class, BsonType.ARRAY);
        map.put(Binary.class, BsonType.BINARY);
        map.put(Boolean.class, BsonType.BOOLEAN);
        map.put(Date.class, BsonType.DATE_TIME);
        map.put(BsonDbPointer.class, BsonType.DB_POINTER);
        map.put(Document.class, BsonType.DOCUMENT);
        map.put(Double.class, BsonType.DOUBLE);
        map.put(Integer.class, BsonType.INT32);
        map.put(Long.class, BsonType.INT64);
        map.put(Decimal128.class, BsonType.DECIMAL128);
        map.put(MaxKey.class, BsonType.MAX_KEY);
        map.put(MinKey.class, BsonType.MIN_KEY);
        map.put(Code.class, BsonType.JAVASCRIPT);
        map.put(CodeWithScope.class, BsonType.JAVASCRIPT_WITH_SCOPE);
        map.put(ObjectId.class, BsonType.OBJECT_ID);
        map.put(BsonRegularExpression.class, BsonType.REGULAR_EXPRESSION);
        map.put(String.class, BsonType.STRING);
        map.put(Symbol.class, BsonType.SYMBOL);
        map.put(BsonTimestamp.class, BsonType.TIMESTAMP);
        map.put(BsonUndefined.class, BsonType.UNDEFINED);
    }


    public BsonType get(Class<?> type) {
        return map.get(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BsonTypeMap that = (BsonTypeMap) o;

        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
