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

import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Property;
import org.bson.codecs.configuration.CodecConfigurationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

final class CreatorExecutable<T> {
    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final Method method;
    private final List< Property > properties = new ArrayList< Property >();
    private final Integer idPropertyIndex;
    private final List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
    private final List<Type> parameterGenericTypes = new ArrayList<Type>();

    CreatorExecutable(final Class<T> clazz, final Constructor<T> constructor) {
        this(clazz, constructor, null);
    }

    CreatorExecutable(final Class<T> clazz, final Method method) {
        this(clazz, null, method);
    }

    private CreatorExecutable(final Class<T> clazz, final Constructor<T> constructor, final Method method) {
        this.clazz = clazz;
        this.constructor = constructor;
        this.method = method;
        Integer idPropertyIndex = null;

        if (constructor != null || method != null) {
            Class<?>[] paramTypes = constructor != null ? constructor.getParameterTypes() : method.getParameterTypes();
            Type[] genericParamTypes = constructor != null ? constructor.getGenericParameterTypes() : method.getGenericParameterTypes();
            parameterTypes.addAll(asList(paramTypes));
            parameterGenericTypes.addAll(asList(genericParamTypes));
            Annotation[][] parameterAnnotations = constructor != null ? constructor.getParameterAnnotations()
                    : method.getParameterAnnotations();

            for (int i = 0; i < parameterAnnotations.length; ++i) {
                Annotation[] parameterAnnotation = parameterAnnotations[i];

                for (Annotation annotation : parameterAnnotation) {
                    if (annotation.annotationType().equals(Property.class)) {
                        properties.add((Property) annotation);
                        break;
                    }

                    if (annotation.annotationType().equals(Id.class)) {
                        properties.add(null);
                        idPropertyIndex = i;
                        break;
                    }
                }
            }
        }

        this.idPropertyIndex = idPropertyIndex;
    }

    Class<T> getType() {
        return clazz;
    }

    List< Property > getProperties() {
        return properties;
    }

    Integer getIdPropertyIndex() {
        return idPropertyIndex;
    }

    List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    List<Type> getParameterGenericTypes() {
        return parameterGenericTypes;
    }

    @SuppressWarnings("unchecked")
    T getInstance() {
        checkHasAnExecutable();
        try {
            if (constructor != null) {
                return constructor.newInstance();
            } else {
                return (T) method.invoke(clazz);
            }
        } catch (Exception e) {
            throw new CodecConfigurationException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    T getInstance(final Object[] params) {
        checkHasAnExecutable();
        try {
            if (constructor != null) {
                return constructor.newInstance(params);
            } else {
                return (T) method.invoke(clazz, params);
            }
        } catch (Exception e) {
            throw new CodecConfigurationException(e.getMessage(), e);
        }
    }


    CodecConfigurationException getError(final Class<?> clazz, final String msg) {
        return getError(clazz, constructor != null, msg);
    }

    private void checkHasAnExecutable() {
        if (constructor == null && method == null) {
            throw new CodecConfigurationException(format("Cannot find a public constructor for '%s'.", clazz.getSimpleName()));
        }
    }

    private static CodecConfigurationException getError(final Class<?> clazz, final boolean isConstructor, final String msg) {
        return new CodecConfigurationException(format("Invalid @Constructor %s in %s. %s", isConstructor ? "constructor" : "method",
                clazz.getSimpleName(), msg));
    }

}
