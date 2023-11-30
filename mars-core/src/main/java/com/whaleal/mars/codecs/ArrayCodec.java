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


import org.bson.BsonBinarySubType;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class ArrayCodec implements Codec<Object> {

    private final Class type;
    private final MongoMappingContext mapper;
    private BsonTypeCodecMap bsonTypeCodecMap;

    <T> ArrayCodec(MongoMappingContext mapper, Class type) {
        this.mapper = mapper;
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void encode(BsonWriter writer, Object value, EncoderContext encoderContext) {
        writer.writeStartArray();
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(value, i);
            // 空值单独处理，按顺序写入。
            if(null == element){
               writer.writeNull();
            }else {
                Codec codec = mapper.getCodecRegistry().get(element.getClass());
                codec.encode(writer, element, encoderContext);
            }

        }
        writer.writeEndArray();
    }

    @Override
    public Class<Object> getEncoderClass() {
        return null;
    }

    @Override
    public Object[] decode(BsonReader reader, DecoderContext decoderContext) {
        List<Object> list = new ArrayList<>();
        if (reader.getCurrentBsonType() == BsonType.ARRAY) {
            reader.readStartArray();

            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                list.add(readValue(reader, decoderContext));
            }

            reader.readEndArray();
        } else {
            list.add(readValue(reader, decoderContext));
        }

        return list.toArray();
    }


    private Object readValue(BsonReader reader, DecoderContext decoderContext) {
        BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType == BsonType.NULL) {
            reader.readNull();
            return null;
        } else if (bsonType == BsonType.BINARY && BsonBinarySubType.isUuid(reader.peekBinarySubType()) && reader.peekBinarySize() == 16) {
            return mapper.getCodecRegistry().get(UUID.class).decode(reader, decoderContext);
        }
        return mapper.getCodecRegistry().get(type.getComponentType()).decode(reader, decoderContext);
    }

    private BsonTypeCodecMap getBsonTypeCodecMap() {
        if (bsonTypeCodecMap == null) {
            this.bsonTypeCodecMap = new BsonTypeCodecMap(new BsonTypeClassMap(), mapper.getCodecRegistry());
        }
        return bsonTypeCodecMap;
    }

}
