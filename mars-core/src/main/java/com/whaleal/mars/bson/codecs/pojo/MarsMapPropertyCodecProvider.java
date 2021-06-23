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

import com.whaleal.mars.bson.codecs.Conversions;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
class MarsMapPropertyCodecProvider extends MarsPropertyCodecProvider {
    @Override
    public <T> Codec<T> get(TypeWithTypeParameters<T> type, PropertyCodecRegistry registry) {
        if (Map.class.isAssignableFrom(type.getType())) {
            final List<? extends TypeWithTypeParameters<?>> typeParameters = type.getTypeParameters();
            TypeWithTypeParameters<?> keyType = getType(typeParameters, 0);
            final TypeWithTypeParameters<?> valueType = getType(typeParameters, 1);

            try {
                return new MapCodec(type.getType(), keyType.getType(), registry.get(valueType));
            } catch (CodecConfigurationException e) {
                if (valueType.getType().equals(Object.class)) {
                    try {
                        return (Codec<T>) registry.get(TypeData.builder(Map.class).build());
                    } catch (CodecConfigurationException e1) {
                        // Ignore and return original exception
                    }
                }
                throw e;
            }
        } else if (Enum.class.isAssignableFrom(type.getType())) {
            return new EnumCodec(type.getType());
        }
        return null;
    }

    private static class MapCodec<K, V> implements Codec<Map<K, V>> {
        private final Class<Map<K, V>> encoderClass;
        private final Class<K> keyType;
        private final Codec<V> codec;

        MapCodec(Class<Map<K, V>> encoderClass, Class<K> keyType, Codec<V> codec) {
            this.encoderClass = encoderClass;
            this.keyType = keyType;
            this.codec = codec;
        }

        @Override
        public void encode(BsonWriter writer, Map<K, V> map, EncoderContext encoderContext) {
            ExpressionHelper.document(writer, () -> {
                for (Entry<K, V> entry : map.entrySet()) {
                    final K key = entry.getKey();
                    writer.writeName(Conversions.convert(key, String.class));
                    if (entry.getValue() == null) {
                        writer.writeNull();
                    } else {
                        codec.encode(writer, entry.getValue(), encoderContext);
                    }
                }
            });
        }

        @Override
        public Map<K, V> decode(BsonReader reader, DecoderContext context) {
            reader.readStartDocument();
            Map<K, V> map = getInstance();
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                final K key = Conversions.convert(reader.readName(), keyType);
                if (reader.getCurrentBsonType() == BsonType.NULL) {
                    map.put(key, null);
                    reader.readNull();
                } else {
                    map.put(key, codec.decode(reader, context));
                }
            }
            reader.readEndDocument();
            return map;
        }

        @Override
        public Class<Map<K, V>> getEncoderClass() {
            return encoderClass;
        }

        private Map<K, V> getInstance() {
            if (encoderClass.isInterface()) {
                return new HashMap<>();
            }
            try {
                final Constructor<Map<K, V>> constructor = encoderClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new CodecConfigurationException(e.getMessage(), e);
            }
        }
    }

}
