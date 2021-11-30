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
package com.whaleal.mars.codecs.pojo;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class PropertyCodecRegistryImpl implements PropertyCodecRegistry {
    private final List<PropertyCodecProvider> propertyCodecProviders;
    private final ConcurrentHashMap<TypeWithTypeParameters<?>, Codec<?>> propertyCodecCache;

    //  这个用于提供 property  codec 的 注册
    PropertyCodecRegistryImpl(Codec<?> marsCodec, final CodecRegistry codecRegistry,
                              final List<PropertyCodecProvider> propertyCodecProviders) {
        List<PropertyCodecProvider> augmentedProviders = new ArrayList<PropertyCodecProvider>();
        if (propertyCodecProviders != null) {
            augmentedProviders.addAll(propertyCodecProviders);
        }
        augmentedProviders.add(new CollectionPropertyCodecProvider());
        augmentedProviders.add(new MarsMapPropertyCodecProvider());
        augmentedProviders.add(new MarsCollectionPropertyCodecProvider());
        augmentedProviders.add(new EnumPropertyCodecProvider(codecRegistry));
        augmentedProviders.add(new FallbackPropertyCodecProvider(marsCodec, codecRegistry));
        augmentedProviders.addAll(propertyCodecProviders);
        this.propertyCodecProviders = augmentedProviders;
        this.propertyCodecCache = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S> Codec<S> get(final TypeWithTypeParameters<S> typeWithTypeParameters) {
        if (propertyCodecCache.containsKey(typeWithTypeParameters)) {
            return (Codec<S>) propertyCodecCache.get(typeWithTypeParameters);
        }

        for (PropertyCodecProvider propertyCodecProvider : propertyCodecProviders) {
            Codec<S> codec = propertyCodecProvider.get(typeWithTypeParameters, this);
            if (codec != null) {
                propertyCodecCache.put(typeWithTypeParameters, codec);
                return codec;
            }
        }
        return null;
    }


}
