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
package com.whaleal.mars.bson.codecs.pojo;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static java.lang.String.format;


public class CollectionCodec<T> implements Codec<Collection<T>> {
    private final Class<Collection<T>> encoderClass;
    private final Codec<T> codec;

    protected CollectionCodec(Class<Collection<T>> encoderClass, Codec<T> codec) {
        this.encoderClass = encoderClass;
        this.codec = codec;
    }

    protected Codec<T> getCodec() {
        return codec;
    }

    @Override
    public void encode(BsonWriter writer, Collection<T> collection, EncoderContext encoderContext) {
        writer.writeStartArray();
        for (T value : collection) {
            if (value == null) {
                writer.writeNull();
            } else {
                codec.encode(writer, value, encoderContext);
            }
        }
        writer.writeEndArray();
    }

    @Override
    public Collection<T> decode(BsonReader reader, DecoderContext context) {
        Collection<T> collection = getInstance();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (reader.getCurrentBsonType() == BsonType.NULL) {
                collection.add(null);
                reader.readNull();
            } else {
                collection.add(codec.decode(reader, context));
            }
        }
        reader.readEndArray();
        return collection;
    }

    @Override
    public Class<Collection<T>> getEncoderClass() {
        return encoderClass;
    }

    protected Collection<T> getInstance() {
        if (encoderClass.isInterface()) {
            if (encoderClass.isAssignableFrom(ArrayList.class)) {
                return new ArrayList<T>();
            } else if (encoderClass.isAssignableFrom(HashSet.class)) {
                return new HashSet<T>();
            } else {
                throw new CodecConfigurationException(format("Unsupported Collection interface of %s!", encoderClass.getName()));
            }
        }

        try {
            return encoderClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new CodecConfigurationException(e.getMessage(), e);
        }
    }
}
