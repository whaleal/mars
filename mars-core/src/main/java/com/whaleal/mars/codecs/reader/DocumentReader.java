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
package com.whaleal.mars.codecs.reader;


import com.whaleal.mars.codecs.BsonTypeMap;
import com.whaleal.mars.codecs.Conversions;
import org.bson.*;
import org.bson.types.Binary;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

import static java.lang.String.format;


public class DocumentReader implements BsonReader {
    private static final BsonTypeMap TYPE_MAP = new BsonTypeMap();
    private final ReaderState start;
    private ReaderState current;


    public DocumentReader(Document document) {
        current = new DocumentState(this, document);
        start = current;
    }


    public ReaderState currentState() {
        return current;
    }

    @Override
    public BsonType getCurrentBsonType() {
        return stage().getCurrentBsonType();
    }

    @Override
    public String getCurrentName() {
        return stage().name();
    }

    @Override
    public BsonBinary readBinaryData() {
        Object value = stage().value();
        if (value instanceof UUID) {
            return new BsonBinary((UUID) value);
        } else if (value instanceof Binary) {
            return new BsonBinary(((Binary) value).getType(), ((Binary) value).getData());
        } else {
            return (BsonBinary) value;
        }
    }

    @Override
    public byte peekBinarySubType() {
        BsonReaderMark mark = getMark();
        try {
            Object binary = stage().value();
            if (binary instanceof UUID) {
                return (byte) ((UUID) binary).version();
            } else {
                return ((BsonBinary) binary).getType();
            }
        } finally {
            mark.reset();
        }

    }

    @Override
    public int peekBinarySize() {
        return stage().<BsonBinary>value().getData().length;
    }

    @Override
    public BsonBinary readBinaryData(String name) {
        verifyName(name);
        return readBinaryData();
    }

    @Override
    public boolean readBoolean() {
        return stage().value();
    }

    @Override
    public boolean readBoolean(String name) {
        verifyName(name);
        return readBoolean();
    }

    @Override
    public BsonType readBsonType() {
        return stage().getCurrentBsonType();
    }

    @Override
    public long readDateTime() {
        Long value = Conversions.convert(stage().value(), long.class);
        if (value != null) {
            return value;
        }
        throw new IllegalStateException("valueCannotBeNull()");
    }

    @Override
    public long readDateTime(String name) {
        verifyName(name);
        return readDateTime();
    }

    @Override
    public double readDouble() {
        return stage().value();
    }

    @Override
    public double readDouble(String name) {
        verifyName(name);
        return readDouble();
    }

    @Override
    public void readEndArray() {
        stage().endArray();
    }

    @Override
    public void readEndDocument() {
        stage().endDocument();
    }

    @Override
    public int readInt32() {
        return stage().value();
    }

    @Override
    public int readInt32(String name) {
        verifyName(name);
        return readInt32();
    }

    @Override
    public long readInt64() {
        return stage().value();
    }

    @Override
    public long readInt64(String name) {
        verifyName(name);
        return readInt64();
    }

    @Override
    public Decimal128 readDecimal128() {
        return stage().value();
    }

    @Override
    public Decimal128 readDecimal128(String name) {
        verifyName(name);
        return readDecimal128();
    }

    @Override
    public String readJavaScript() {
        return stage().<BsonJavaScript>value().getCode();
    }

    @Override
    public String readJavaScript(String name) {
        verifyName(name);
        return readJavaScript();
    }

    @Override
    public String readJavaScriptWithScope() {
        return stage().<BsonJavaScriptWithScope>value().getCode();
    }

    @Override
    public String readJavaScriptWithScope(String name) {
        verifyName(name);
        return readJavaScriptWithScope();
    }

    @Override
    public void readMaxKey() {
    }

    @Override
    public void readMaxKey(String name) {
        verifyName(name);
        readMaxKey();
    }

    @Override
    public void readMinKey() {
    }

    @Override
    public void readMinKey(String name) {
        verifyName(name);
        readMinKey();
    }

    @Override
    public String readName() {
        return stage().name();
    }

    @Override
    public void readName(String name) {
        verifyName(name);
    }

    @Override
    public void readNull() {
        stage().advance();
    }

    @Override
    public void readNull(String name) {
        verifyName(name);
        readNull();
    }

    @Override
    public ObjectId readObjectId() {
        return stage().value();
    }

    @Override
    public ObjectId readObjectId(String name) {
        verifyName(name);
        return readObjectId();
    }

    @Override
    public BsonRegularExpression readRegularExpression() {
        return stage().value();
    }

    @Override
    public BsonRegularExpression readRegularExpression(String name) {
        verifyName(name);
        return readRegularExpression();
    }

    @Override
    public BsonDbPointer readDBPointer() {
        return stage().value();
    }

    @Override
    public BsonDbPointer readDBPointer(String name) {
        verifyName(name);
        return readDBPointer();
    }

    @Override
    public void readStartArray() {
        stage().startArray();
    }

    @Override
    public void readStartDocument() {
        stage().startDocument();
    }

    @Override
    public String readString() {
        return stage().value();
    }

    @Override
    public String readString(String name) {
        verifyName(name);
        return readString();
    }

    @Override
    public String readSymbol() {
        return stage().value();
    }

    @Override
    public String readSymbol(String name) {
        verifyName(name);
        return readSymbol();
    }

    @Override
    public BsonTimestamp readTimestamp() {
        return stage().value();
    }

    @Override
    public BsonTimestamp readTimestamp(String name) {
        verifyName(name);
        return readTimestamp();
    }

    @Override
    public void readUndefined() {
    }

    @Override
    public void readUndefined(String name) {
        verifyName(name);
        readUndefined();
    }

    @Override
    public void skipName() {
        stage().skipName();
    }

    @Override
    public void skipValue() {
        stage().skipValue();
    }

    @Override
    public BsonReaderMark getMark() {
        return new Mark(this, stage());
    }

    @Override
    public void close() {
    }

    protected void verifyName(String expectedName) {
        String actualName = readName();
        if (!actualName.equals(expectedName)) {
            throw new BsonSerializationException(format("Expected element name to be '%s', not '%s'.",
                    expectedName, actualName));
        }
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", DocumentReader.class.getSimpleName() + "[", "]");
        ReaderState location = start;
        while (location != null) {
            if (location == current) {
                joiner.add("<<" + location + ">>");
            } else {
                joiner.add(location.toString());
            }
            location = location.nextState();
        }
        return joiner
                .toString();
    }

    BsonType getBsonType( Object o ) {
        BsonType bsonType = o == null ? BsonType.NULL : TYPE_MAP.get(o.getClass());
        if (bsonType == null) {
            if (o instanceof List) {
                bsonType = BsonType.ARRAY;
            } else {
                bsonType = BsonType.UNDEFINED;
            }
        }
        return bsonType;
    }

    void reset(ReaderState bookmark) {
        current = bookmark;
    }

    ReaderState stage() {
        return this.current;
    }

    void state( ReaderState next ) {
        current = next;
    }
}
