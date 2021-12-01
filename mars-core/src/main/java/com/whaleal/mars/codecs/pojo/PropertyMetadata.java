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

import org.bson.codecs.configuration.CodecConfigurationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.*;

final class PropertyMetadata<T> {
    private final String name;
    private final String declaringClassName;
    private final TypeData<T> typeData;
    private final Map<Class<? extends Annotation>, Annotation> readAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
    private final Map<Class<? extends Annotation>, Annotation> writeAnnotations = new HashMap<Class<? extends Annotation>, Annotation>();
    private TypeParameterMap typeParameterMap;
    private List<TypeData<?>> typeParameters;

    private String error;
    private Field field;
    private Method getter;
    private Method setter;

    PropertyMetadata(final String name, final String declaringClassName, final TypeData<T> typeData) {
        this.name = name;
        this.declaringClassName = declaringClassName;
        this.typeData = typeData;
    }

    public String getName() {
        return name;
    }

    public List<Annotation> getReadAnnotations() {
        return new ArrayList<Annotation>(readAnnotations.values());
    }

    public PropertyMetadata<T> addReadAnnotation(final Annotation annotation) {
        if (readAnnotations.containsKey(annotation.annotationType())) {
            if (annotation.equals(readAnnotations.get(annotation.annotationType()))) {
                return this;
            }
            throw new CodecConfigurationException(format("Read annotation %s for '%s' already exists in %s", annotation.annotationType(),
                    name, declaringClassName));
        }
        readAnnotations.put(annotation.annotationType(), annotation);
        return this;
    }

    public List<Annotation> getWriteAnnotations() {
        return new ArrayList<Annotation>(writeAnnotations.values());
    }

    public PropertyMetadata<T> addWriteAnnotation(final Annotation annotation) {
        if (writeAnnotations.containsKey(annotation.annotationType())) {
            if (annotation.equals(writeAnnotations.get(annotation.annotationType()))) {
                return this;
            }
            throw new CodecConfigurationException(format("Write annotation %s for '%s' already exists in %s", annotation.annotationType(),
                    name, declaringClassName));
        }
        writeAnnotations.put(annotation.annotationType(), annotation);
        return this;
    }

    public Field getField() {
        return field;
    }

    public PropertyMetadata<T> field(final Field field) {
        this.field = field;
        return this;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(final Method getter) {
        this.getter = getter;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(final Method setter) {
        this.setter = setter;
    }

    public String getDeclaringClassName() {
        return declaringClassName;
    }

    public TypeData<T> getTypeData() {
        return typeData;
    }

    public TypeParameterMap getTypeParameterMap() {
        return typeParameterMap;
    }

    public List<TypeData<?>> getTypeParameters() {
        return typeParameters;
    }

    public <S> PropertyMetadata<T> typeParameterInfo(final TypeParameterMap typeParameterMap, final TypeData<S> parentTypeData) {
        if (typeParameterMap != null && parentTypeData != null) {
            this.typeParameterMap = typeParameterMap;
            this.typeParameters = parentTypeData.getTypeParameters();
        }
        return this;
    }

    String getError() {
        return error;
    }

    void setError(final String error) {
        this.error = error;
    }

    public boolean isSerializable() {
        if (getter != null) {
            return field == null || notStaticOrTransient(field.getModifiers());
        } else {
            return field != null && isPublicAndNotStaticOrTransient(field.getModifiers());
        }
    }

    public boolean isDeserializable() {
        if (setter != null) {
            return field == null || !isFinal(field.getModifiers()) && notStaticOrTransient(field.getModifiers());
        } else {
            return field != null && !isFinal(field.getModifiers()) && isPublicAndNotStaticOrTransient(field.getModifiers());
        }
    }

    private boolean notStaticOrTransient(final int modifiers) {
        return !(isTransient(modifiers) || isStatic(modifiers));
    }

    private boolean isPublicAndNotStaticOrTransient(final int modifiers) {
        return isPublic(modifiers) && notStaticOrTransient(modifiers);
    }

    @Override
    public String toString() {
        return "PropertyMetadata{"
                + "name='" + name + '\''
                + ", declaringClassName='" + declaringClassName + '\''
                + ", typeData=" + typeData
                + ", readAnnotations=" + readAnnotations
                + ", writeAnnotations=" + writeAnnotations
                + ", typeParameterMap=" + typeParameterMap
                + ", typeParameters=" + typeParameters
                + ", error='" + error + '\''
                + ", field=" + field
                + ", getter=" + getter
                + ", setter=" + setter
                + '}';
    }
}
