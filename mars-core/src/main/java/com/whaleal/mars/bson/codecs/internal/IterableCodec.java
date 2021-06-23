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
package com.whaleal.mars.bson.codecs.internal;

import org.bson.*;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.BsonTypeCodecMap;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bson.assertions.Assertions.notNull;


@SuppressWarnings("rawtypes")
public class IterableCodec implements Codec<Iterable>, OverridableUuidRepresentationCodec<Iterable> {

    private final CodecRegistry registry;
    private final BsonTypeCodecMap bsonTypeCodecMap;
    private final Transformer valueTransformer;
    private final UuidRepresentation uuidRepresentation;

    public IterableCodec(final CodecRegistry registry, final BsonTypeClassMap bsonTypeClassMap) {
        this(registry, bsonTypeClassMap, null);
    }

    public IterableCodec(final CodecRegistry registry, final BsonTypeClassMap bsonTypeClassMap, final Transformer valueTransformer) {
        this(registry, new BsonTypeCodecMap(notNull("bsonTypeClassMap", bsonTypeClassMap), registry), valueTransformer,
                UuidRepresentation.UNSPECIFIED);
    }

    private IterableCodec(final CodecRegistry registry, final BsonTypeCodecMap bsonTypeCodecMap, final Transformer valueTransformer,
                          final UuidRepresentation uuidRepresentation) {
        this.registry = notNull("registry", registry);
        this.bsonTypeCodecMap = bsonTypeCodecMap;
        this.valueTransformer = valueTransformer != null ? valueTransformer : new Transformer() {
            @Override
            public Object transform(final Object objectToTransform) {
                return objectToTransform;
            }
        };
        this.uuidRepresentation = uuidRepresentation;
    }


    @Override
    public Codec<Iterable> withUuidRepresentation(final UuidRepresentation uuidRepresentation) {
        return new IterableCodec(registry, bsonTypeCodecMap, valueTransformer, uuidRepresentation);
    }

    @Override
    public Iterable decode(final BsonReader reader, final DecoderContext decoderContext) {
        reader.readStartArray();

        List<Object> list = new ArrayList<Object>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            list.add(readValue(reader, decoderContext));
        }

        reader.readEndArray();

        return list;
    }

    @Override
    public void encode(final BsonWriter writer, final Iterable value, final EncoderContext encoderContext) {
        writer.writeStartArray();
        for (final Object cur : value) {
            writeValue(writer, encoderContext, cur);
        }
        writer.writeEndArray();
    }

    @Override
    public Class<Iterable> getEncoderClass() {
        return Iterable.class;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void writeValue(final BsonWriter writer, final EncoderContext encoderContext, final Object value) {
        if (value == null) {
            writer.writeNull();
        } else {
            Codec codec = registry.get(value.getClass());
            encoderContext.encodeWithChildContext(codec, writer, value);
        }
    }

    private Object readValue(final BsonReader reader, final DecoderContext decoderContext) {
        BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType == BsonType.NULL) {
            reader.readNull();
            return null;
        } else {
            Codec<?> codec = bsonTypeCodecMap.get(bsonType);
            if (bsonType == BsonType.BINARY && reader.peekBinarySize() == 16) {
                switch (reader.peekBinarySubType()) {
                    case 3:
                        if (uuidRepresentation == UuidRepresentation.JAVA_LEGACY
                                || uuidRepresentation == UuidRepresentation.C_SHARP_LEGACY
                                || uuidRepresentation == UuidRepresentation.PYTHON_LEGACY) {
                            codec = registry.get(UUID.class);
                        }
                        break;
                    case 4:
                        if (uuidRepresentation == UuidRepresentation.STANDARD) {
                            codec = registry.get(UUID.class);
                        }
                        break;
                    default:
                        break;
                }
            }
            return valueTransformer.transform(codec.decode(reader, decoderContext));
        }
    }
}
