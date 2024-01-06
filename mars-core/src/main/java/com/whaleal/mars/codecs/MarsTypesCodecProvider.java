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

import com.whaleal.mars.codecs.internal.MapCodec;
import com.whaleal.mars.codecs.internal.URICodec;
import com.whaleal.mars.codecs.time.MarsLocalTimeCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MarsTypesCodecProvider implements CodecProvider {

    private final MongoMappingContext mapper;
    private final Map<Class<?>, Codec<?>> codecs = new HashMap<>();


    public MarsTypesCodecProvider(MongoMappingContext mapper) {
        this.mapper = mapper;

        //addCodec(new MarsDateCodec(mapper));
        //addCodec(new MarsLocalDateTimeCodec(mapper));
        addCodec(new MarsLocalTimeCodec());
        addCodec(new ClassCodec());
        //addCodec(new CenterCodec());
        addCodec(new HashMapCodec());
        //addCodec(new KeyCodec(mapper));
        addCodec(new LocaleCodec());
        addCodec(new ObjectCodec(mapper));
        //addCodec(new ShapeCodec());
        //addCodec(new LegacyQueryCodec(mapper));
        //addCodec(new MarsQueryCodec(mapper));
        addCodec(new URICodec());

        Arrays.asList(boolean.class, Boolean.class,
                byte.class, Byte.class,
                char.class, Character.class,
                double.class, Double.class,
                float.class, Float.class,
                int.class, Integer.class,
                long.class, Long.class,
                short.class, Short.class).forEach(c -> addCodec(new TypedArrayCodec(c, mapper)));
    }

    protected <T> void addCodec(Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        final Codec<T> codec = (Codec<T>) codecs.get(clazz);
        if (codec != null) {
            return codec;
        } else if (clazz.isArray() && !clazz.getComponentType().equals(byte.class)) {
            return (Codec<T>) new ArrayCodec(mapper, clazz);
        } else {
            return null;
        }
    }

    private static class HashMapCodec extends MapCodec {
        @Override
        public Class<Map<String, Object>> getEncoderClass() {
            return (Class<Map<String, Object>>) ((Class<?>) HashMap.class);
        }
    }
}
