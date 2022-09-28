/**
 * Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 * <p>
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.whaleal.com/licensing/server-side-public-license>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the Server Side Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.whaleal.mars.codecs.pojo;


import com.whaleal.icefrog.core.util.ClassUtil;
import com.whaleal.icefrog.core.util.ReflectUtil;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.*;
import java.util.*;

import static java.lang.String.format;
import static com.whaleal.icefrog.core.lang.Precondition.notNull;


public final class TypeData< T > implements TypeWithTypeParameters< T > {
    private final Class< T > type;
    private final List< TypeData< ? > > typeParameters;


    public static < T > Builder< T > builder( final Class< T > type ) {
        return new Builder< T >(notNull("type", type));
    }

    public static TypeData< ? > newInstance( final Method method ) {

        if (PropertyReflectionUtil.isGetter(method)) {
            return newInstance(method.getGenericReturnType(), method.getReturnType());
        } else {
            return newInstance(method.getGenericParameterTypes()[0], method.getParameterTypes()[0]);
        }
    }

    public static TypeData< ? > newInstance( final Field field ) {
        return newInstance(field.getGenericType(), field.getType());
    }

    public static < T > TypeData< T > newInstance( final Type genericType, final Class< T > clazz ) {

        Builder< T > builder = TypeData.builder(clazz);
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            for (Type argType : pType.getActualTypeArguments()) {
                getNestedTypeData(builder, argType);
            }
        }
        return builder.build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static < T > void getNestedTypeData( final Builder< T > builder, final Type type ) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Builder paramBuilder = TypeData.builder((Class) pType.getRawType());
            for (Type argType : pType.getActualTypeArguments()) {
                getNestedTypeData(paramBuilder, argType);
            }
            builder.addTypeParameter(paramBuilder.build());
        } else if (type instanceof WildcardType) {
            builder.addTypeParameter(TypeData.builder((Class) ((WildcardType) type).getUpperBounds()[0]).build());
        } else if (type instanceof TypeVariable) {
            builder.addTypeParameter(TypeData.builder(Object.class).build());
        } else if (type instanceof Class) {
            builder.addTypeParameter(TypeData.builder((Class) type).build());
        }
    }


    @Override
    public Class< T > getType() {
        return type;
    }


    @Override
    public List< TypeData< ? > > getTypeParameters() {
        return typeParameters;
    }


    public static final class Builder< T > {
        private final Class< T > type;
        private final List< TypeData< ? > > typeParameters = new ArrayList< TypeData< ? > >();

        private Builder( final Class< T > type ) {
            this.type = type;
        }


        public < S > Builder< T > addTypeParameter( final TypeData< S > typeParameter ) {
            typeParameters.add(notNull("typeParameter", typeParameter));
            return this;
        }


        public Builder< T > addTypeParameters( final List< TypeData< ? > > typeParameters ) {
            notNull("typeParameters", typeParameters);
            for (TypeData< ? > typeParameter : typeParameters) {
                addTypeParameter(typeParameter);
            }
            return this;
        }


        public TypeData< T > build() {
            return new TypeData< T >(type, Collections.unmodifiableList(typeParameters));
        }
    }

    @Override
    public String toString() {
        String typeParams = typeParameters.isEmpty() ? ""
                : ", typeParameters=[" + nestedTypeParameters(typeParameters) + "]";
        return "TypeData{"
                + "type=" + type.getSimpleName()
                + typeParams
                + "}";
    }

    private static String nestedTypeParameters( final List< TypeData< ? > > typeParameters ) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        int last = typeParameters.size();
        for (TypeData< ? > typeParameter : typeParameters) {
            count++;
            builder.append(typeParameter.getType().getSimpleName());
            if (!typeParameter.getTypeParameters().isEmpty()) {
                builder.append(format("<%s>", nestedTypeParameters(typeParameter.getTypeParameters())));
            }
            if (count < last) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals( final Object o ) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeData)) {
            return false;
        }

        TypeData< ? > that = (TypeData< ? >) o;

        if (!getType().equals(that.getType())) {
            return false;
        }
        if (!getTypeParameters().equals(that.getTypeParameters())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getTypeParameters().hashCode();
        return result;
    }

    private TypeData( final Class< T > type, final List< TypeData< ? > > typeParameters ) {
        this.type = boxType(type);
        this.typeParameters = typeParameters;
    }

    boolean isAssignableFrom( final Class< ? > cls ) {
        return type.isAssignableFrom(boxType(cls));
    }

    @SuppressWarnings("unchecked")
    private < S > Class< S > boxType( final Class< S > clazz ) {
        if (clazz.isPrimitive()) {
            return (Class< S >) PRIMITIVE_CLASS_MAP.get(clazz);
        } else {
            return clazz;
        }
    }

    private static final Map< Class< ? >, Class< ? > > PRIMITIVE_CLASS_MAP;

    static {
        Map< Class< ? >, Class< ? > > map = new HashMap< Class< ? >, Class< ? > >();
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(short.class, Short.class);
        PRIMITIVE_CLASS_MAP = map;
    }
}
