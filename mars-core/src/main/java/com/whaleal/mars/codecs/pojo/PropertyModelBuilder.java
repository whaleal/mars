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

import org.bson.BsonType;
import org.bson.codecs.Codec;
import org.bson.codecs.pojo.PropertyAccessor;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.whaleal.icefrog.core.lang.Precondition.notNull;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public final class PropertyModelBuilder<T> {
    private String name;
    private String readName;
    private String writeName;
    private TypeData<T> typeData;
    private PropertySerialization<T> propertySerialization;
    private Codec<T> codec;
    private PropertyAccessor<T> propertyAccessor;
    private List<Annotation> readAnnotations = emptyList();
    private List<Annotation> writeAnnotations = emptyList();
    private Boolean discriminatorEnabled;
    private String error;
    private BsonType bsonRepresentation;

    PropertyModelBuilder() {
    }


    public String getName() {
        return name;
    }


    public String getReadName() {
        return readName;
    }

    /**
     * Sets the readName, the key for this property when deserializing the data from BSON.
     *
     * <p>Note: A null means this property will not used when deserializing.</p>
     *
     * @param readName the name of the property to use as the key when deserializing the data from BSON.
     * @param readName 从 BSON 反序列化数据时用作键的属性名称。
     * @return this
     * <p>
     * 设置 readName，这是从 BSON 反序列化数据时此属性的键。
     * <p>注意：null 表示反序列化时不会使用该属性。</p>
     * @return 这个
     */
    public PropertyModelBuilder<T> readName(final String readName) {
        this.readName = readName;
        return this;
    }


    public String getWriteName() {
        return writeName;
    }


    public PropertyModelBuilder<T> writeName(final String writeName) {
        this.writeName = writeName;
        return this;
    }


    public PropertyModelBuilder<T> codec(final Codec<T> codec) {
        this.codec = codec;
        return this;
    }


    Codec<T> getCodec() {
        return codec;
    }


    public PropertyModelBuilder<T> propertySerialization(final PropertySerialization<T> propertySerialization) {
        this.propertySerialization = notNull("propertySerialization", propertySerialization);
        return this;
    }


    public PropertySerialization<T> getPropertySerialization() {
        return propertySerialization;
    }


    public List<Annotation> getReadAnnotations() {
        return readAnnotations;
    }


    public PropertyModelBuilder<T> readAnnotations(final List<Annotation> annotations) {
        this.readAnnotations = unmodifiableList(notNull("annotations", annotations));
        return this;
    }


    public List<Annotation> getWriteAnnotations() {
        return writeAnnotations;
    }


    public PropertyModelBuilder<T> writeAnnotations(final List<Annotation> writeAnnotations) {
        this.writeAnnotations = writeAnnotations;
        return this;
    }


    public boolean isWritable() {
        return writeName != null;
    }


    public boolean isReadable() {
        return readName != null;
    }


    public Boolean isDiscriminatorEnabled() {
        return discriminatorEnabled;
    }


    public PropertyModelBuilder<T> discriminatorEnabled(final boolean discriminatorEnabled) {
        this.discriminatorEnabled = discriminatorEnabled;
        return this;
    }


    public PropertyAccessor<T> getPropertyAccessor() {
        return propertyAccessor;
    }


    public PropertyModelBuilder<T> propertyAccessor(final PropertyAccessor<T> propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
        return this;
    }


    public BsonType getBsonRepresentation() {
        return bsonRepresentation;
    }


    public PropertyModelBuilder<T> bsonRepresentation(final BsonType bsonRepresentation) {
        this.bsonRepresentation = bsonRepresentation;
        return this;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public PropertyModel<T> build() {
        if (!isReadable() && !isWritable()) {
            throw new IllegalStateException(format("Invalid PropertyModel '%s', neither readable or writable,", name));
        }
        return new PropertyModel(
                MarsBuilderHelper.stateNotNull("propertyName", name),
                readName,
                writeName,
                MarsBuilderHelper.stateNotNull("typeData", typeData),
                codec,
                MarsBuilderHelper.stateNotNull("propertySerialization", propertySerialization),
                discriminatorEnabled,
                MarsBuilderHelper.stateNotNull("propertyAccessor", propertyAccessor),
                error,
                bsonRepresentation);
    }

    @Override
    public String toString() {
        return format("PropertyModelBuilder{propertyName=%s, typeData=%s}", name, typeData);
    }

    PropertyModelBuilder<T> propertyName(final String propertyName) {
        this.name = notNull("propertyName", propertyName);
        return this;
    }

    TypeData<T> getTypeData() {
        return typeData;
    }

    PropertyModelBuilder<T> typeData(final TypeData<T> typeData) {
        this.typeData = notNull("typeData", typeData);
        return this;
    }

    PropertyModelBuilder<T> setError(final String error) {
        this.error = error;
        return this;
    }
}
