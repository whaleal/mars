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
import org.bson.codecs.pojo.IdGenerator;

import static java.lang.String.format;

final class IdPropertyModelHolder<I> {
    private final PropertyModel<I> propertyModel;
    private final IdGenerator<I> idGenerator;

    static <T, I> IdPropertyModelHolder<I> create(final EntityModel<T> entityModel, final PropertyModel<I> idPropertyModel) {
        return create(entityModel.getType(), idPropertyModel, entityModel.getIdPropertyModelHolder().getIdGenerator());
    }

    @SuppressWarnings("unchecked")
    static <T, I, V> IdPropertyModelHolder<I> create(final Class<T> type, final PropertyModel<I> idProperty,
                                                     final IdGenerator<V> idGenerator) {
        if (idProperty == null && idGenerator != null) {
            throw new CodecConfigurationException(format("Invalid IdGenerator. There is no IdProperty set for: %s", type));
        } else if (idGenerator != null && !idProperty.getTypeData().getType().isAssignableFrom(idGenerator.getType())) {
            throw new CodecConfigurationException(format("Invalid IdGenerator. Mismatching types, the IdProperty type is: %s but"
                    + " the IdGenerator type is: %s", idProperty.getTypeData().getType(), idGenerator.getType()));
        }
        return new IdPropertyModelHolder<I>(idProperty, (IdGenerator<I>) idGenerator);
    }

    private IdPropertyModelHolder(final PropertyModel<I> propertyModel, final IdGenerator<I> idGenerator) {
        this.propertyModel = propertyModel;
        this.idGenerator = idGenerator;
    }

    PropertyModel<I> getPropertyModel() {
        return propertyModel;
    }

    IdGenerator<I> getIdGenerator() {
        return idGenerator;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IdPropertyModelHolder<?> that = (IdPropertyModelHolder<?>) o;

        if (propertyModel != null ? !propertyModel.equals(that.propertyModel) : that.propertyModel != null) {
            return false;
        }
        return idGenerator != null ? idGenerator.equals(that.idGenerator) : that.idGenerator == null;
    }

    @Override
    public int hashCode() {
        int result = propertyModel != null ? propertyModel.hashCode() : 0;
        result = 31 * result + (idGenerator != null ? idGenerator.hashCode() : 0);
        return result;
    }
}
