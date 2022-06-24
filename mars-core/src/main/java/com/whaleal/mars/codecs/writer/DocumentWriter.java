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
package com.whaleal.mars.codecs.writer;

import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;



@SuppressWarnings("unchecked")
public class DocumentWriter implements BsonWriter {
    private final RootState root;
    private WriteState state;

    private MongoMappingContext mapper;

    /**
     * 生成新的DocumentWrite对象
     */
    public DocumentWriter() {
        root = new RootState(this);
        state = root;
    }

    /**
     * 生成包含seeded Document的DocumentWrite对象
     *
     * @param seed   the seed Document
     */
    public DocumentWriter(Document seed) {
        root = new RootState(this, seed);
        state = root;
    }


    public DocumentWriter(MongoMappingContext mapper) {
        this.mapper = mapper;
        root = new RootState(this);
        state = root;
    }

    public DocumentWriter(MongoMappingContext mapper,Document seed) {
        this.mapper = mapper;
        root = new RootState(this,seed);
        state = root;
    }

    /**
     * Encodes a value in to this Writer
     *
     * @param codecRegistry  the registry to use
     * @param value          the value to encode
     * @param encoderContext the context
     * @return this
     */
    public DocumentWriter encode(CodecRegistry codecRegistry, Object value, EncoderContext encoderContext) {
        ((Codec) codecRegistry.get(value.getClass()))
                .encode(this, value, encoderContext);

        return this;
    }

    @Override
    public void flush() {
    }

    /**
     * @return the root, or output, of this writer.  usually a Document.
     */
    public Document getDocument() {
        return root.getDocument();
    }

    public WriteState state() {
        return state;
    }

    @Override
    public void writeBinaryData(BsonBinary binary) {
        state.value(binary);
    }

    @Override
    public void writeBinaryData(String name, BsonBinary binary) {
        state.name(name).value(binary);
    }

    @Override
    public void writeBoolean(boolean value) {
        state.value(value);
    }

    @Override
    public void writeBoolean(String name, boolean value) {
        state.name(name).value(value);
    }


    @Override
    public void writeDateTime(long value) {
        state.value(LocalDateTime.ofInstant(Instant.ofEpochMilli(value), mapper.getDateStorage().getZone()));
    }

    @Override
    public void writeDateTime(String name, long value) {
        state.name(name);
        writeDateTime(value);
    }

    @Override
    public void writeDBPointer(BsonDbPointer value) {
        state.value(value);
    }

    @Override
    public void writeDBPointer(String name, BsonDbPointer value) {
        state.name(name).value(value);
    }

    @Override
    public void writeDouble(double value) {
        state.value(value);
    }

    @Override
    public void writeDouble(String name, double value) {
        state.name(name).value(value);
    }

    @Override
    public void writeEndArray() {
        state.end();
    }

    @Override
    public void writeEndDocument() {
        state.end();
    }

    @Override
    public void writeInt32(int value) {
        state.value(value);
    }

    @Override
    public void writeInt32(String name, int value) {
        state.name(name).value(value);
    }

    @Override
    public void writeInt64(long value) {
        state.value(value);
    }

    @Override
    public void writeInt64(String name, long value) {
        state.name(name).value(value);
    }

    @Override
    public void writeDecimal128(Decimal128 value) {
        state.value(value);
    }

    @Override
    public void writeDecimal128(String name, Decimal128 value) {
        state.name(name).value(value);
    }

    @Override
    public void writeJavaScript(String code) {
        state.value(code);
    }

    @Override
    public void writeMaxKey(String name) {
        writeName(name);
        writeMaxKey();
    }

    @Override
    public void writeJavaScript(String name, String code) {
        state.name(name).value(code);
    }

    @Override
    public void writeMinKey(String name) {
        writeName(name);
        writeMinKey();
    }

    @Override
    public void writeJavaScriptWithScope(String code) {
        state.value(code);
    }

    @Override
    public void writeJavaScriptWithScope(String name, String code) {
        state.name(name).value(code);
    }

    @Override
    public void writeMaxKey() {
        state.value(new BsonMaxKey());
    }

    @Override
    public void writeMinKey() {
        state.value(new BsonMinKey());
    }

    @Override
    public void writeName(String name) {
        state.name(name);
    }

    @Override
    public void writeNull() {
        state.value(null);
    }

    @Override
    public void writeNull(String name) {
        writeName(name);
        state.value(null);
    }

    @Override
    public void writeObjectId(ObjectId objectId) {
        state.value(objectId);
    }

    @Override
    public void writeStartArray(String name) {
        writeName(name);
        writeStartArray();
    }

    @Override
    public void writeObjectId(String name, ObjectId objectId) {
        state.name(name).value(objectId);
    }

    @Override
    public void writeRegularExpression(BsonRegularExpression regularExpression) {
        state.value(regularExpression);
    }

    @Override
    public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
        state.name(name).value(regularExpression);
    }

    @Override
    public void writeStartArray() {
        state.array();
    }

    @Override
    public void writeStartDocument() {
        state.document();
    }

    @Override
    public void writeSymbol(String name, String value) {
        writeName(name);
        writeSymbol(value);
    }

    @Override
    public void writeStartDocument(String name) {
        state.name(name).document();
    }

    @Override
    public void writeString(String value) {
        state.value(value);
    }

    @Override
    public void writeString(String name, String value) {
        state.name(name).value(value);
    }

    @Override
    public void writeUndefined(String name) {
        writeName(name);
        writeUndefined();
    }

    @Override
    public void pipe(BsonReader reader) {
        throw new UnsupportedOperationException("org.bson.io.TestingDocumentWriter.pipe has not yet been implemented.");
    }

    @Override
    public void writeSymbol(String value) {
        state.value(new BsonSymbol(value));
    }

    @Override
    public void writeTimestamp(BsonTimestamp value) {
        state.value(value);
    }

    @Override
    public void writeTimestamp(String name, BsonTimestamp value) {
        writeName(name);
        state.value(value);
    }

    @Override
    public void writeUndefined() {
        state.value(new BsonUndefined());
    }

    @Override
    public String toString() {
        return root.toString();
    }

    void state(WriteState state) {
        this.state = state;
    }

}
