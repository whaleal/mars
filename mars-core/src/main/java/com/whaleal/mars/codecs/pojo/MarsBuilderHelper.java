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



import com.whaleal.icefrog.core.util.ClassUtil;
import static com.whaleal.icefrog.core.lang.Precondition.notNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.asList;
import static java.util.Collections.reverse;


/**
 * 通过 classModelBuilder   及 clazz
 * 拿到class  相关的数据
 * 注解
 * 字段
 * 方法等
 * 封装到Builder  中去
 *
 * 基本上与实体相关的信息 都在这里封装
 *
 * 字段  1 字段名称 当前类型 注解
 * 方法  1 获取所有方法 ，分析 构造  getter  Setter 等 。
 *
 *
 *
 * @author wh
 * @since 0.1
 */
final class MarsBuilderHelper {

    @SuppressWarnings("unchecked")
    static <T> void configureClassModelBuilder(final EntityModelBuilder<T> entityModelBuilder, final Class<T> clazz) {
        // 断言 非空 并封装 type
        entityModelBuilder.type(notNull("clazz", clazz));

        //  类对象上的 注解保存
        List<Annotation> annotations = new ArrayList<Annotation>();
        Set<String> propertyNames = new TreeSet<String>();
        //Set<String> fieldNames = new TreeSet<String>();
        Map<String, TypeParameterMap> propertyTypeParameterMap = new HashMap<String, TypeParameterMap>();
        // 针对当前类赋值
        Class<? super T> currentClass = clazz;
        String declaringClassName = ClassUtil.getSimpleClassName(clazz);
        TypeData<?> parentClassTypeData = null;

        Map<String, PropertyMetadata<?>> propertyNameMap = new HashMap<String, PropertyMetadata<?>>();


        //  不停地 获取 该类的父类 currentClass = currentClass.getSuperclass();
        //  并迭代  获取getter  setter  field
        while (!currentClass.isEnum() && currentClass.getSuperclass() != null) {

            //  将该类的/父类 注解放到 annotations 这个List  中去
            annotations.addAll(asList(currentClass.getDeclaredAnnotations()));
            //  这个是当前类的泛型参数  getTypeParameters
            List<String> genericTypeNames = new ArrayList<String>();
            for (TypeVariable<? extends Class<? super T>> classTypeVariable : currentClass.getTypeParameters()) {
                genericTypeNames.add(classTypeVariable.getName());
            }

            //  获取声明的 开放的 getter  && setter  当前类及父类的
            PropertyReflectionUtil.PropertyMethods propertyMethods = PropertyReflectionUtil.getPropertyMethods(currentClass);


            // Note that we're processing setters before getters. It's typical for setters to have more general types
            // than getters (e.g.: getter returning ImmutableList, but setter accepting Collection), so by evaluating
            // setters first, we'll initialize the PropertyMetadata with the more general type
            // 原始顺序  setter  --> getter  --> field  且权重相同 其关系为求并集
            // 计划调整顺序 field  --- setter  ---getter  仍在考虑中
            // 调整顺序的同时 调整各个来源之间权重  主 Field 轻 Method
            for (Method method : propertyMethods.getSetterMethods()) {
                // 获取 相关的 method 名称 并同时 截取 ，解析到 field Name
                String propertyName = PropertyReflectionUtil.toPropertyName(method);
                propertyNames.add(propertyName);
                PropertyMetadata<?> propertyMetadata = getOrCreateMethodPropertyMetadata(propertyName, declaringClassName, propertyNameMap,
                        TypeData.newInstance(method), propertyTypeParameterMap, parentClassTypeData, genericTypeNames,
                        getGenericType(method));

                if (propertyMetadata.getSetter() == null) {
                    propertyMetadata.setSetter(method);
                    for (Annotation annotation : method.getDeclaredAnnotations()) {
                        propertyMetadata.addWriteAnnotation(annotation);
                    }
                }
            }

            for (Method method : propertyMethods.getGetterMethods()) {

                String propertyName = PropertyReflectionUtil.toPropertyName(method);
                propertyNames.add(propertyName);
                // If the getter is overridden in a subclass, we only want to process that property, and ignore
                // potentially less specific methods from super classes
                PropertyMetadata<?> propertyMetadata = propertyNameMap.get(propertyName);
                if (propertyMetadata != null && propertyMetadata.getGetter() != null) {
                    continue;
                }
                propertyMetadata = getOrCreateMethodPropertyMetadata(propertyName, declaringClassName, propertyNameMap,
                        TypeData.newInstance(method), propertyTypeParameterMap, parentClassTypeData, genericTypeNames,
                        getGenericType(method));
                if (propertyMetadata.getGetter() == null) {
                    propertyMetadata.setGetter(method);
                    for (Annotation annotation : method.getDeclaredAnnotations()) {
                        propertyMetadata.addReadAnnotation(annotation);
                    }
                }
            }


            //  处理相关 field
            for (Field field : currentClass.getDeclaredFields()) {
                propertyNames.add(field.getName());
                // Note if properties are present and types don't match, the underlying field is treated as an implementation detail.
                PropertyMetadata<?> propertyMetadata = getOrCreateFieldPropertyMetadata(field.getName(), declaringClassName,
                        propertyNameMap, TypeData.newInstance(field), propertyTypeParameterMap, parentClassTypeData, genericTypeNames,
                        field.getGenericType());
                if (propertyMetadata != null && propertyMetadata.getField() == null) {
                    propertyMetadata.field(field);
                    for (Annotation annotation : field.getDeclaredAnnotations()) {
                        propertyMetadata.addReadAnnotation(annotation);
                        propertyMetadata.addWriteAnnotation(annotation);
                    }
                }
            }

            parentClassTypeData = TypeData.newInstance(currentClass.getGenericSuperclass(), currentClass);
            currentClass = currentClass.getSuperclass();
        }

        if (currentClass.isInterface()) {
            annotations.addAll(asList(currentClass.getDeclaredAnnotations()));
        }

        for (String propertyName : propertyNames) {
            PropertyMetadata<?> propertyMetadata = propertyNameMap.get(propertyName);
            if (propertyMetadata.isSerializable() || propertyMetadata.isDeserializable()) {
                entityModelBuilder.addProperty(createPropertyModelBuilder(propertyMetadata));
            }
        }

        reverse(annotations);
        entityModelBuilder.annotations(annotations);
        entityModelBuilder.propertyNameToTypeParameterMap(propertyTypeParameterMap);

        Constructor<T> noArgsConstructor = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == 0
                    && (isPublic(constructor.getModifiers()) || isProtected(constructor.getModifiers()))) {
                noArgsConstructor = (Constructor<T>) constructor;
                noArgsConstructor.setAccessible(true);
            }
        }

        entityModelBuilder.instanceCreatorFactory(new InstanceCreatorFactoryImpl<T>(new CreatorExecutable<T>(clazz, noArgsConstructor)));

    }

    private static <T, S> PropertyMetadata<T> getOrCreateMethodPropertyMetadata(final String propertyName,
                                                                                final String declaringClassName,
                                                                                final Map<String, PropertyMetadata<?>> propertyNameMap,
                                                                                final TypeData<T> typeData,
                                                                                final Map<String, TypeParameterMap> propertyTypeParameterMap,
                                                                                final TypeData<S> parentClassTypeData,
                                                                                final List<String> genericTypeNames,
                                                                                final Type genericType) {
        PropertyMetadata<T> propertyMetadata = getOrCreatePropertyMetadata(propertyName, declaringClassName, propertyNameMap, typeData);
        if (!isAssignableClass(propertyMetadata.getTypeData().getType(), typeData.getType())) {
            propertyMetadata.setError(format("Property '%s' in %s, has differing data types: %s and %s.", propertyName,
                    declaringClassName, propertyMetadata.getTypeData(), typeData));
        }
        cachePropertyTypeData(propertyMetadata, propertyTypeParameterMap, parentClassTypeData, genericTypeNames, genericType);
        return propertyMetadata;
    }

    private static boolean isAssignableClass(final Class<?> propertyTypeClass, final Class<?> typeDataClass) {
        notNull("propertyTypeClass", propertyTypeClass);
        notNull("typeDataClass", typeDataClass);
        return propertyTypeClass.isAssignableFrom(typeDataClass) || typeDataClass.isAssignableFrom(propertyTypeClass);
    }

    private static <T, S> PropertyMetadata<T> getOrCreateFieldPropertyMetadata(final String propertyName,
                                                                               final String declaringClassName,
                                                                               final Map<String, PropertyMetadata<?>> propertyNameMap,
                                                                               final TypeData<T> typeData,
                                                                               final Map<String, TypeParameterMap> propertyTypeParameterMap,
                                                                               final TypeData<S> parentClassTypeData,
                                                                               final List<String> genericTypeNames,
                                                                               final Type genericType) {
        PropertyMetadata<T> propertyMetadata = getOrCreatePropertyMetadata(propertyName, declaringClassName, propertyNameMap, typeData);
        if (!propertyMetadata.getTypeData().getType().isAssignableFrom(typeData.getType())) {
            return null;
        }
        cachePropertyTypeData(propertyMetadata, propertyTypeParameterMap, parentClassTypeData, genericTypeNames, genericType);
        return propertyMetadata;
    }

    @SuppressWarnings("unchecked")
    private static <T> PropertyMetadata<T> getOrCreatePropertyMetadata(final String propertyName,
                                                                       final String declaringClassName,
                                                                       final Map<String, PropertyMetadata<?>> propertyNameMap,
                                                                       final TypeData<T> typeData) {
        PropertyMetadata<T> propertyMetadata = (PropertyMetadata<T>) propertyNameMap.get(propertyName);
        if (propertyMetadata == null) {
            propertyMetadata = new PropertyMetadata<T>(propertyName, declaringClassName, typeData);
            propertyNameMap.put(propertyName, propertyMetadata);
        }
        return propertyMetadata;
    }

    private static <T, S> void cachePropertyTypeData(final PropertyMetadata<T> propertyMetadata,
                                                     final Map<String, TypeParameterMap> propertyTypeParameterMap,
                                                     final TypeData<S> parentClassTypeData,
                                                     final List<String> genericTypeNames,
                                                     final Type genericType) {
        TypeParameterMap typeParameterMap = getTypeParameterMap(genericTypeNames, genericType);
        propertyTypeParameterMap.put(propertyMetadata.getName(), typeParameterMap);
        propertyMetadata.typeParameterInfo(typeParameterMap, parentClassTypeData);
    }

    private static Type getGenericType(final Method method) {
        return PropertyReflectionUtil.isGetter(method) ? method.getGenericReturnType() : method.getGenericParameterTypes()[0];
    }

    @SuppressWarnings("unchecked")
    static <T> PropertyModelBuilder<T> createPropertyModelBuilder(final PropertyMetadata<T> propertyMetadata) {
        PropertyModelBuilder<T> propertyModelBuilder = PropertyModel.<T>builder()
                .propertyName(propertyMetadata.getName())
                .readName(propertyMetadata.getName())
                .writeName(propertyMetadata.getName())
                .typeData(propertyMetadata.getTypeData())
                .readAnnotations(propertyMetadata.getReadAnnotations())
                .writeAnnotations(propertyMetadata.getWriteAnnotations())
                .propertySerialization(new PropertyModelSerializationImpl<T>())
                //.propertyAccessor(new PropertyAccessorImpl<T>(propertyMetadata))
                .propertyAccessor(getAccessor(propertyMetadata))
                .setError(propertyMetadata.getError());

        if (propertyMetadata.getTypeParameters() != null) {
            propertyModelBuilder.typeData(MarsSpecializationHelper.specializeTypeData(propertyModelBuilder.getTypeData(), propertyMetadata.getTypeParameters(),
                    propertyMetadata.getTypeParameterMap()));
        }

        return propertyModelBuilder;
    }


    private static <T> PropertyAccessorImpl<T> getAccessor(PropertyMetadata<T> propertyMetadata) {

        Field field = propertyMetadata.getField();
        if (field != null) {
            return field.getType().isArray() && !field.getType().getComponentType().equals(byte.class) ? new ArrayFieldAccessor(propertyMetadata)
                    : new PropertyAccessorImpl(propertyMetadata);
        } else {
            return new PropertyAccessorImpl(propertyMetadata);
        }

    }


    private static TypeParameterMap getTypeParameterMap(final List<String> genericTypeNames, final Type propertyType) {
        int classParamIndex = genericTypeNames.indexOf(propertyType.toString());
        TypeParameterMap.Builder builder = TypeParameterMap.builder();
        if (classParamIndex != -1) {
            builder.addIndex(classParamIndex);
        } else {
            if (propertyType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) propertyType;
                for (int i = 0; i < pt.getActualTypeArguments().length; i++) {
                    classParamIndex = genericTypeNames.indexOf(pt.getActualTypeArguments()[i].toString());
                    if (classParamIndex != -1) {
                        builder.addIndex(i, classParamIndex);
                    } else {
                        builder.addIndex(i, getTypeParameterMap(genericTypeNames, pt.getActualTypeArguments()[i]));
                    }
                }
            }
        }
        return builder.build();
    }

    static <V> V stateNotNull(final String property, final V value) {
        if (value == null) {
            throw new IllegalStateException(format("%s cannot be null", property));
        }
        return value;
    }

    private MarsBuilderHelper() {
    }
}
