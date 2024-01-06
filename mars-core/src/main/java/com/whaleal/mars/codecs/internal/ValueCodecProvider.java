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


import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wh
 * @see org.bson.codecs.ValueCodecProvider ;
 * 在原生驱动之前生效即可 覆盖原生驱动的相关编码逻辑
 * <ul>
 *     <li>{@link org.bson.codecs.BinaryCodec}</li>
 *     <li>{@link org.bson.codecs.BooleanCodec}</li>
 *     <li>{@link org.bson.codecs.DateCodec}</li>
 *     <li>{@link org.bson.codecs.DoubleCodec}</li>
 *     <li>{@link org.bson.codecs.IntegerCodec}</li>
 *     <li>{@link org.bson.codecs.LongCodec}</li>
 *     <li>{@link org.bson.codecs.Decimal128Codec}</li>
 *     <li>{@link org.bson.codecs.MinKeyCodec}</li>
 *     <li>{@link org.bson.codecs.MaxKeyCodec}</li>
 *     <li>{@link org.bson.codecs.CodeCodec}</li>
 *     <li>{@link org.bson.codecs.ObjectIdCodec}</li>
 *     <li>{@link org.bson.codecs.CharacterCodec}</li>
 *     <li>{@link org.bson.codecs.StringCodec}</li>
 *     <li>{@link org.bson.codecs.SymbolCodec}</li>
 *     <li>{@link org.bson.codecs.UuidCodec}</li>
 *     <li>{@link org.bson.codecs.ByteCodec}</li>
 *     <li>{@link org.bson.codecs.ShortCodec}</li>
 *     <li>{@link org.bson.codecs.ByteArrayCodec}</li>
 *     <li>{@link org.bson.codecs.FloatCodec}</li>
 *     <li>{@link org.bson.codecs.AtomicBooleanCodec}</li>
 *     <li>{@link org.bson.codecs.AtomicIntegerCodec}</li>
 *     <li>{@link org.bson.codecs.AtomicLongCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.AtomicIntegerCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.AtomicLongCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.FloatCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.DoubleCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.ShortCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.IntegerCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.LongCodec}</li>
 *     <li>{@link com.whaleal.mars.codecs.internal.StringCodec}</li>
 * </ul>
 *
 *
 *
 */
public class ValueCodecProvider implements CodecProvider {
    private final Map<Class<?>, Codec<?>> codecs = new HashMap<Class<?>, Codec<?>>();

    public ValueCodecProvider() {
        addCodecs();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        return (Codec<T>) codecs.get(clazz);
    }

    private void addCodecs() {
        addCodec(new BinaryCodec());
        addCodec(new BooleanCodec());
        addCodec(new DateCodec());
        addCodec(new DoubleCodec());
        addCodec(new IntegerCodec());
        addCodec(new LongCodec());
        addCodec(new MinKeyCodec());
        addCodec(new MaxKeyCodec());
        addCodec(new CodeCodec());
        addCodec(new Decimal128Codec());
        addCodec(new BigDecimalCodec());
        addCodec(new ObjectIdCodec());
        addCodec(new CharacterCodec());
        addCodec(new StringCodec());
        addCodec(new SymbolCodec());
        addCodec(new OverridableUuidRepresentationUuidCodec());
        addCodec(new ByteCodec());
        addCodec(new PatternCodec());
        addCodec(new ShortCodec());
        addCodec(new ByteArrayCodec());
        addCodec(new FloatCodec());
        addCodec(new AtomicBooleanCodec());
        addCodec(new AtomicIntegerCodec());
        addCodec(new AtomicLongCodec());
    }

    private <T> void addCodec(final Codec<T> codec) {
        codecs.put(codec.getEncoderClass(), codec);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
