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


import com.whaleal.mars.codecs.Conversions;

import java.lang.reflect.Array;
import java.util.List;

import static java.lang.String.format;

@SuppressWarnings("rawtypes")
public class ArrayFieldAccessor extends PropertyAccessorImpl {

    private final TypeData<?> typeData;
    private final Class<?> componentType;


    public ArrayFieldAccessor(PropertyMetadata propertyMetadata) {
        super(propertyMetadata);
        this.typeData = propertyMetadata.getTypeData();
        componentType = propertyMetadata.getField().getType().getComponentType();
    }

    @Override
    public void set(Object instance, Object value) {
        Object newValue = value;
        if (value.getClass().getComponentType() != componentType) {
            newValue = value instanceof List ? convert((List) value) : convert((Object[]) value);
        }
        super.set(instance, newValue);
    }

    private Object convert(Object[] value) {
        final Object newArray = Array.newInstance(componentType, value.length);
        for (int i = 0; i < value.length; i++) {
            Object convert = convert(value[i], componentType);
            Array.set(newArray, i, convert);
        }
        return newArray;
    }

    private Object convert(List value) {
        final Object newArray = Array.newInstance(componentType, value.size());
        for (int i = 0; i < value.size(); i++) {
            Object converted = convert(value.get(i), componentType);
            if (converted != null) {
                try {
                    Array.set(newArray, i, converted);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(format("Can't set %s with a value type of %s", getPropertyMetadata().getField(), converted.getClass()));
                }
            } else {
                throw new IllegalArgumentException(format("Can not convert '%s' to type '%s' ", value.get(i), componentType.getName()));
            }
        }
        return newArray;
    }


    private Object convert(Object o, Class<?> type) {
        if (o instanceof List) {
            List list = (List) o;
            final Object newArray = Array.newInstance(type.getComponentType(), list.size());
            for (int i = 0; i < list.size(); i++) {
                Object converted = convert(list.get(i), type.getComponentType());
                try {
                    Array.set(newArray, i, converted);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(format("Can't set %s with a value type of %s", "getPropertyMetadata().getField()", converted.getClass()));
                }
            }

            return newArray;
        }
        return Conversions.convert(o, type);
    }
}
