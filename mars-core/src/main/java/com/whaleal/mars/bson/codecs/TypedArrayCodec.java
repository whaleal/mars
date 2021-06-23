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
package com.whaleal.mars.bson.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

class TypedArrayCodec implements Codec {
    private final Class type;
    private final MongoMappingContext mapper;
    private Codec codec;

    TypedArrayCodec(Class type, MongoMappingContext mapper) {
        this.type = type;
        this.mapper = mapper;
    }

    @Override
    public void encode(BsonWriter writer, Object value, EncoderContext encoderContext) {
        writer.writeStartArray();
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            getCodec().encode(writer, Array.get(value, i), encoderContext);
        }
        writer.writeEndArray();
    }

    @Override
    public Class getEncoderClass() {
        return Array.newInstance(type, 0).getClass();
    }

    private Codec getCodec() {
        if (codec == null) {
            codec = mapper.getCodecRegistry().get(type);
        }
        return codec;
    }

    @Override
    public Object decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartArray();

        List list = new ArrayList<>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            list.add(getCodec().decode(reader, decoderContext));
        }

        reader.readEndArray();

        Object array = Array.newInstance(type, list.size());
        for (int i = 0; i < list.size(); i++) {
            Array.set(array, i, list.get(i));
        }

        return array;
    }

    @Override
    public String toString() {
        return format("%s<%s>", getClass().getName(), type.getSimpleName());
    }
}
