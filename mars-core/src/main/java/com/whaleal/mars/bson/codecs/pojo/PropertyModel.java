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
package com.whaleal.mars.bson.codecs.pojo;

import org.bson.BsonType;
import org.bson.codecs.Codec;
import org.bson.codecs.pojo.PropertyAccessor;

import java.util.List;


public final class PropertyModel<T> {
    private final String name;
    private final String readName;
    private final String writeName;
    private final TypeData<T> typeData;
    private final Codec<T> codec;
    private final PropertySerialization<T> propertySerialization;
    private final Boolean useDiscriminator;
    private final PropertyAccessor<T> propertyAccessor;
    private final String error;
    private volatile Codec<T> cachedCodec;
    private final BsonType bsonRepresentation;

    PropertyModel(final String name, final String readName, final String writeName, final TypeData<T> typeData,
                  final Codec<T> codec, final PropertySerialization<T> propertySerialization, final Boolean useDiscriminator,
                  final PropertyAccessor<T> propertyAccessor, final String error, final BsonType bsonRepresentation) {
        this.name = name;
        this.readName = readName;
        this.writeName = writeName;
        this.typeData = typeData;
        this.codec = codec;
        this.cachedCodec = codec;
        this.propertySerialization = propertySerialization;
        this.useDiscriminator = useDiscriminator;
        this.propertyAccessor = propertyAccessor;
        this.error = error;
        this.bsonRepresentation = bsonRepresentation;
    }


    public static <T> PropertyModelBuilder<T> builder() {
        return new PropertyModelBuilder<T>();
    }


    public String getName() {
        return name;
    }


    public String getWriteName() {
        return writeName;
    }

    public String getReadName() {
        return readName;
    }


    public Class<?> getType() {
        return getTypeData().getType();
    }


    public boolean isWritable() {
        return writeName != null;
    }


    public boolean isReadable() {
        return readName != null;
    }


    public TypeData<T> getTypeData() {
        return typeData;
    }

    private Class<?> normalizedType;


    public Class<?> getNormalizedType() {
        if (normalizedType == null) {
            normalizedType = normalize(getTypeData());
        }

        return normalizedType;
    }


    public static Class<?> normalize(TypeData<?> toNormalize) {
        Class<?> type;
        TypeData<?> typeData = toNormalize;
        while (!typeData.getTypeParameters().isEmpty()) {
            List<TypeData<?>> typeParameters = typeData.getTypeParameters();
            typeData = typeParameters.get(typeParameters.size() - 1);
        }
        type = typeData.getType();
        while (type.isArray()) {
            type = type.getComponentType();
        }
        return type;
    }


    public Codec<T> getCodec() {
        return codec;
    }


    public BsonType getBsonRepresentation() {
        return bsonRepresentation;
    }


    public boolean shouldSerialize(final T value) {
        return propertySerialization.shouldSerialize(value);
    }


    public PropertyAccessor<T> getPropertyAccessor() {
        return propertyAccessor;
    }


    public Boolean useDiscriminator() {
        return useDiscriminator;
    }

    @Override
    public String toString() {
        return "PropertyModel{"
                + "propertyName='" + name + "'"
                + ", readName='" + readName + "'"
                + ", writeName='" + writeName + "'"
                + ", typeData=" + typeData
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PropertyModel<?> that = (PropertyModel<?>) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getReadName() != null ? !getReadName().equals(that.getReadName()) : that.getReadName() != null) {
            return false;
        }
        if (getWriteName() != null ? !getWriteName().equals(that.getWriteName()) : that.getWriteName() != null) {
            return false;
        }
        if (getTypeData() != null ? !getTypeData().equals(that.getTypeData()) : that.getTypeData() != null) {
            return false;
        }
        if (getCodec() != null ? !getCodec().equals(that.getCodec()) : that.getCodec() != null) {
            return false;
        }
        if (getPropertySerialization() != null ? !getPropertySerialization().equals(that.getPropertySerialization()) : that
                .getPropertySerialization() != null) {
            return false;
        }
        if (useDiscriminator != null ? !useDiscriminator.equals(that.useDiscriminator) : that.useDiscriminator != null) {
            return false;
        }
        if (getPropertyAccessor() != null ? !getPropertyAccessor().equals(that.getPropertyAccessor())
                : that.getPropertyAccessor() != null) {
            return false;
        }

        if (getError() != null ? !getError().equals(that.getError()) : that.getError() != null) {
            return false;
        }

        if (getCachedCodec() != null ? !getCachedCodec().equals(that.getCachedCodec()) : that.getCachedCodec() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getReadName() != null ? getReadName().hashCode() : 0);
        result = 31 * result + (getWriteName() != null ? getWriteName().hashCode() : 0);
        result = 31 * result + (getTypeData() != null ? getTypeData().hashCode() : 0);
        result = 31 * result + (getCodec() != null ? getCodec().hashCode() : 0);
        result = 31 * result + (getPropertySerialization() != null ? getPropertySerialization().hashCode() : 0);
        result = 31 * result + (useDiscriminator != null ? useDiscriminator.hashCode() : 0);
        result = 31 * result + (getPropertyAccessor() != null ? getPropertyAccessor().hashCode() : 0);
        result = 31 * result + (getError() != null ? getError().hashCode() : 0);
        result = 31 * result + (getCachedCodec() != null ? getCachedCodec().hashCode() : 0);
        return result;
    }

    boolean hasError() {
        return error != null;
    }

    String getError() {
        return error;
    }

    PropertySerialization<T> getPropertySerialization() {
        return propertySerialization;
    }

    /**
     * 在  MarsCodec  中已经将 此 codec  及 bsonRepresentation 绑定
     *
     * @param codec
     */
    void cachedCodec(final Codec<T> codec) {

        this.cachedCodec = codec;

    }

    Codec<T> getCachedCodec() {
        return cachedCodec;
    }


}
