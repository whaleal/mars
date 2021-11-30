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
import com.whaleal.mars.bson.codecs.pojo.annotations.*;
import com.whaleal.mars.codecs.pojo.annotations.*;
import org.bson.BsonType;
import org.bson.codecs.configuration.CodecConfigurationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

final class ConventionDefaultsImpl implements Convention {
    @Override
    public void apply(final EntityModelBuilder<?> entityModelBuilder) {
        for (final Annotation annotation : entityModelBuilder.getAnnotations()) {
            processClassAnnotation(entityModelBuilder, annotation);
        }

        for (PropertyModelBuilder<?> propertyModelBuilder : entityModelBuilder.getPropertyModelBuilders()) {
            processPropertyAnnotations(entityModelBuilder, propertyModelBuilder);
        }

        processCreatorAnnotation(entityModelBuilder);

        cleanPropertyBuilders(entityModelBuilder);


    }


    @SuppressWarnings("unchecked")
    private <T> void processCreatorAnnotation(final EntityModelBuilder<T> entityModelBuilder) {
        Class<T> clazz = entityModelBuilder.getType();
        CreatorExecutable<T> creatorExecutable = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (isPublic(constructor.getModifiers()) && !constructor.isSynthetic()) {
                for (Annotation annotation : constructor.getDeclaredAnnotations()) {
                    if (annotation.annotationType().equals(MongoCreator.class)) {
                        if (creatorExecutable != null) {
                            throw new CodecConfigurationException("Found multiple constructors annotated with @MongoCreator");
                        }
                        creatorExecutable = new CreatorExecutable<T>(clazz, (Constructor<T>) constructor);
                    }
                }
            }
        }

        Class<?> bsonCreatorClass = clazz;
        boolean foundStaticBsonCreatorMethod = false;
        while (bsonCreatorClass != null && !foundStaticBsonCreatorMethod) {
            for (Method method : bsonCreatorClass.getDeclaredMethods()) {
                if (isStatic(method.getModifiers()) && !method.isSynthetic() && !method.isBridge()) {
                    for (Annotation annotation : method.getDeclaredAnnotations()) {
                        if (annotation.annotationType().equals(MongoCreator.class)) {
                            if (creatorExecutable != null) {
                                throw new CodecConfigurationException("Found multiple constructors / methods annotated with @MongoCreator");
                            } else if (!bsonCreatorClass.isAssignableFrom(method.getReturnType())) {
                                throw new CodecConfigurationException(
                                        format("Invalid method annotated with @MongoCreator. Returns '%s', expected %s",
                                                method.getReturnType(), bsonCreatorClass));
                            }
                            creatorExecutable = new CreatorExecutable<T>(clazz, method);
                            foundStaticBsonCreatorMethod = true;
                        }
                    }
                }
            }

            bsonCreatorClass = bsonCreatorClass.getSuperclass();
        }

        if (creatorExecutable != null) {
            List<MongoProperty> properties = creatorExecutable.getProperties();
            List<Class<?>> parameterTypes = creatorExecutable.getParameterTypes();
            List<Type> parameterGenericTypes = creatorExecutable.getParameterGenericTypes();

            if (properties.size() != parameterTypes.size()) {
                throw creatorExecutable.getError(clazz, "All parameters in the @MongoCreator method / constructor must be annotated "
                        + "with a @MongoProperty.");
            }
            for (int i = 0; i < properties.size(); i++) {
                boolean isIdProperty = creatorExecutable.getIdPropertyIndex() != null && creatorExecutable.getIdPropertyIndex().equals(i);
                Class<?> parameterType = parameterTypes.get(i);
                Type genericType = parameterGenericTypes.get(i);
                PropertyModelBuilder<?> propertyModelBuilder = null;

                if (isIdProperty) {
                    propertyModelBuilder = entityModelBuilder.getProperty(entityModelBuilder.getIdPropertyName());
                } else {
                    MongoProperty mongoProperty = properties.get(i);

                    // Find the property using write name and falls back to read name
                    for (PropertyModelBuilder<?> builder : entityModelBuilder.getPropertyModelBuilders()) {
                        if (mongoProperty.value().equals(builder.getWriteName())) {
                            propertyModelBuilder = builder;
                            break;
                        } else if (mongoProperty.value().equals(builder.getReadName())) {
                            // When there is a property that matches the read name of the parameter, save it but continue to look
                            // This is just in case there is another property that matches the write name.
                            propertyModelBuilder = builder;
                        }
                    }

                    // Support legacy options, when MongoProperty matches the actual POJO property name (e.g. method name or field name).
                    if (propertyModelBuilder == null) {
                        propertyModelBuilder = entityModelBuilder.getProperty(mongoProperty.value());
                    }

                    if (propertyModelBuilder == null) {
                        propertyModelBuilder = addCreatorPropertyToClassModelBuilder(entityModelBuilder, mongoProperty.value(),
                                parameterType);
                    } else {
                        // If not using a legacy MongoProperty reference to the property set the write name to be the annotated name.
                        if (!mongoProperty.value().equals(propertyModelBuilder.getName())) {
                            propertyModelBuilder.writeName(mongoProperty.value());
                        }
                        tryToExpandToGenericType(parameterType, propertyModelBuilder, genericType);
                    }
                }

                if (!propertyModelBuilder.getTypeData().isAssignableFrom(parameterType)) {
                    throw creatorExecutable.getError(clazz, format("Invalid Property type for '%s'. Expected %s, found %s.",
                            propertyModelBuilder.getWriteName(), propertyModelBuilder.getTypeData().getType(), parameterType));
                }
            }
            entityModelBuilder.instanceCreatorFactory(new InstanceCreatorFactoryImpl<T>(creatorExecutable));
        }
    }


    /**
     * 设置 类相关的 注解
     * 此处值解析了 discriminator  相关的数据
     * 后期的先关 属性都应该在这里添加
     */
    private void processClassAnnotation(final EntityModelBuilder<?> entityModelBuilder, final Annotation annotation) {


        if (annotation instanceof Entity) {
            Entity document = (Entity) annotation;

            if (document.useDiscriminator()) {
                String key = document.discriminatorKey();
                if (!key.equals("")) {
                    entityModelBuilder.discriminatorKey(key);
                }

                String name = document.discriminator();
                if (!name.equals("")) {
                    entityModelBuilder.discriminator(name);
                }
                entityModelBuilder.enableDiscriminator(true);
            } else {
                entityModelBuilder.enableDiscriminator(false);
            }
        }
    }

    /**
     * 设置 字段级别的 读写参数
     */
    private void processPropertyAnnotations(final EntityModelBuilder<?> entityModelBuilder,
                                            final PropertyModelBuilder<?> propertyModelBuilder) {
        for (Annotation annotation : propertyModelBuilder.getReadAnnotations()) {

            if (annotation instanceof MongoProperty) {
                MongoProperty mongoProperty = (MongoProperty) annotation;
                if (!"".equals(mongoProperty.value())) {
                    propertyModelBuilder.readName(mongoProperty.value());
                }
                propertyModelBuilder.discriminatorEnabled(mongoProperty.useDiscriminator());
                if (propertyModelBuilder.getName().equals(entityModelBuilder.getIdPropertyName())) {
                    entityModelBuilder.idPropertyName(null);
                }


                BsonType bsonRep = mongoProperty.storageType().getJavaClass();
                propertyModelBuilder.bsonRepresentation(bsonRep);
            } else if (annotation instanceof MongoId) {
                MongoId mongoId = (MongoId) annotation;
                entityModelBuilder.idPropertyName(propertyModelBuilder.getName());
                BsonType bsonRep = mongoId.value().getJavaClass();
                propertyModelBuilder.bsonRepresentation(bsonRep);
            } else if (annotation instanceof MongoId) {
                propertyModelBuilder.readName(null);
            } else {
                String propertyName = propertyModelBuilder.getName();
                if (propertyName.equals("_id")) {
                    entityModelBuilder.idPropertyName(propertyName);
                }
            }
        }

        for (Annotation annotation : propertyModelBuilder.getWriteAnnotations()) {
            if (annotation instanceof MongoProperty) {
                MongoProperty mongoProperty = (MongoProperty) annotation;
                if (!"".equals(mongoProperty.value())) {
                    propertyModelBuilder.writeName(mongoProperty.value());
                }

            } else if (annotation instanceof MongoId) {
                MongoId mongoProperty = (MongoId) annotation;

                propertyModelBuilder.writeName("_id");


            } else if (annotation instanceof MongoIgnore) {
                propertyModelBuilder.writeName(null);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void tryToExpandToGenericType(final Class<?> parameterType, final PropertyModelBuilder<T> propertyModelBuilder,
                                                     final Type genericType) {
        if (parameterType.isAssignableFrom(propertyModelBuilder.getTypeData().getType())) {
            // The existing getter for this field returns a more specific type than what the constructor accepts
            // This is typical when the getter returns a specific subtype, but the constructor accepts a more
            // general one (e.g.: getter returns ImmutableList<T>, while constructor just accepts List<T>)
            propertyModelBuilder.typeData(TypeData.newInstance(genericType, (Class<T>) parameterType));
        }
    }

    private <T, S> PropertyModelBuilder<S> addCreatorPropertyToClassModelBuilder(final EntityModelBuilder<T> entityModelBuilder,
                                                                                 final String name,
                                                                                 final Class<S> clazz) {
        PropertyModelBuilder<S> propertyModelBuilder = MarsBuilderHelper.createPropertyModelBuilder(new PropertyMetadata<S>(name,
                entityModelBuilder.getType().getSimpleName(), TypeData.builder(clazz).build())).readName(null).writeName(name);
        entityModelBuilder.addProperty(propertyModelBuilder);
        return propertyModelBuilder;
    }

    private void cleanPropertyBuilders(final EntityModelBuilder<?> entityModelBuilder) {
        List<String> propertiesToRemove = new ArrayList<String>();
        for (PropertyModelBuilder<?> propertyModelBuilder : entityModelBuilder.getPropertyModelBuilders()) {
            if (!propertyModelBuilder.isReadable() && !propertyModelBuilder.isWritable()) {
                propertiesToRemove.add(propertyModelBuilder.getName());
            }
        }
        for (String propertyName : propertiesToRemove) {
            entityModelBuilder.removeProperty(propertyName);
        }
    }
}
