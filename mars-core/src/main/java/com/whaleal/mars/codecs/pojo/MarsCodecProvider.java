
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

import com.whaleal.mars.codecs.Convention;
import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class MarsCodecProvider implements CodecProvider {
    private final Map<Class<?>, Codec<?>> codecs = new HashMap<>();
    private final MongoMappingContext mapper;
    private final List<PropertyCodecProvider> propertyCodecProviders;
    private final List<Convention> conventions;


    public MarsCodecProvider(MongoMappingContext mapper) {
        this.mapper = mapper;

        this.conventions = Conventions.DEFAULT_CONVENTIONS;
        propertyCodecProviders = Arrays.asList(new MarsMapPropertyCodecProvider(),
                new MarsCollectionPropertyCodecProvider());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {


        MarsCodec<T> codec = (MarsCodec<T>) codecs.get(clazz);


        if (codec != null) {
            return codec;
        } else {

            EntityModel<T> classModel = (EntityModel<T>) this.mapper.getEntityModel(clazz);

            if (classModel != null) {
                codec = new MarsCodec<T>(mapper, classModel, propertyCodecProviders, mapper.getDiscriminatorLookup(), registry);
                codecs.put(clazz, codec);
                return codec;
            } else {
                //  todo  查看 model   生成问题 需要在筛选一下
                try {
                    classModel = createClassModel(clazz, conventions);
                    if (clazz.isInterface() || !classModel.getPropertyModels().isEmpty()) {

                        codec = new MarsCodec<T>(mapper, classModel, propertyCodecProviders, mapper.getDiscriminatorLookup(), registry);

                        if (codec != null) {
                            codecs.put(clazz, codec);
                            return codec;
                        }

                    }
                } catch (Exception e) {

                }
            }
        }


        //if (codec == null && (mapper.isMapped(type) || mapper.isMappable(type))) {
        /*{
            EntityModel model = mapper.getEntityModel(type);
            codec = new MarsCodec<>(mapper, model, propertyCodecProviders, mapper.getDiscriminatorLookup(), registry);
            if (false) {
                codec.setEncoder(new LifecycleEncoder(codec));
            }
            if (false  ) {
                codec.setDecoder(new LifecycleDecoder(codec));
            }
            codecs.put(type, codec);
        }*/

        return codec;
    }


    private static <T> EntityModel<T> createClassModel(final Class<T> clazz, final List<Convention> conventions) {
        EntityModelBuilder<T> builder = new EntityModelBuilder<>(clazz);
        if (conventions != null) {
            builder.conventions(conventions);
        }
        return builder.build();
    }


    public <T> Codec<T> getRefreshCodec(T entity, CodecRegistry registry) {
        EntityModel model = mapper.getEntityModel(entity.getClass());
        return new MarsCodec<T>(mapper, model, propertyCodecProviders, mapper.getDiscriminatorLookup(), registry) {
            @Override
            protected EntityDecoder getDecoder() {
                return new EntityDecoder(this) {
                    @Override
                    protected MarsInstanceCreator getInstanceCreator() {
                        return new MarsInstanceCreator() {
                            @Override
                            public T getInstance() {
                                return entity;
                            }

                            @Override
                            public void set(Object value, PropertyModel model) {
                                model.getPropertyAccessor().set(entity, value);
                            }
                        };
                    }
                };
            }
        };
    }

}
