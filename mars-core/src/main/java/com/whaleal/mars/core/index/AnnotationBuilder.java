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
package com.whaleal.mars.core.index;


import com.whaleal.mars.codecs.MarsOrmException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;


public abstract class AnnotationBuilder<T extends Annotation> implements Annotation {
    /**使用一个 Map  来进行 基础信息的存储 这里主要用来存储  注解里的值;
     * 默认情况下 当构造该对象时会自动的加载所有的默认值
     */
    private final Map<String, Object> values = new HashMap<String, Object>();

    /**
     * 加载默认设置
     */
    protected AnnotationBuilder() {
        for (Method method : annotationType().getDeclaredMethods()) {
            values.put(method.getName(), method.getDefaultValue());
        }
    }

    /**
     * 加载对象的值
     * @param original
     */
    protected AnnotationBuilder(T original) {
        try {
            for (Method method : annotationType().getDeclaredMethods()) {
                values.put(method.getName(), method.invoke(original));
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    static <A extends Annotation> Map<String, Object> toMap(A annotation) {
        final Map<String, Object> map = new HashMap<String, Object>();
        try {
            Class<A> annotationType = (Class<A>) annotation.annotationType();
            for (Method method : annotationType.getDeclaredMethods()) {
                Object value = unwrapAnnotation(method.invoke(annotation));
                final Object defaultValue = unwrapAnnotation(method.getDefaultValue());
                if (value != null && !value.equals(defaultValue)) {
                    map.put(method.getName(), value);
                }
            }
        } catch (Exception e) {
            throw new MarsOrmException(e.getMessage(), e);
        }
        return map;
    }

    private static Object unwrapAnnotation(Object o) {
        if (o instanceof Annotation) {
            return toMap((Annotation) o);
        } else {
            return o;
        }
    }

    @SuppressWarnings("unchecked")
    protected <V> V get(String key) {
        return (V) values.get(key);
    }

    protected void put(String key, Object value) {
        if (value != null) {
            values.put(key, value);
        }
    }

    void putAll(Map<String, Object> map) {
        values.putAll(map);
    }

    @Override
    public String toString() {
        return format("@%s %s", annotationType().getName(), values.toString());
    }

    /**
     * 由子类去完成 该泛型的具体类型
     *
     * @return
     */
    @Override
    public abstract Class<T> annotationType();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnotationBuilder)) {
            return false;
        }

        return values.equals(((AnnotationBuilder<?>) o).values);

    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
