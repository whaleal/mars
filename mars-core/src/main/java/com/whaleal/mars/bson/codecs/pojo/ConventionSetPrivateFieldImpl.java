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

import com.whaleal.mars.bson.codecs.Convention;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.pojo.PropertyAccessor;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPrivate;

final class ConventionSetPrivateFieldImpl implements Convention {

    @Override
    public void apply(final EntityModelBuilder<?> entityModelBuilder) {
        for (PropertyModelBuilder<?> propertyModelBuilder : entityModelBuilder.getPropertyModelBuilders()) {
            if (!(propertyModelBuilder.getPropertyAccessor() instanceof PropertyAccessorImpl)) {
                throw new CodecConfigurationException(format("The SET_PRIVATE_FIELDS_CONVENTION is not compatible with "
                                + "propertyModelBuilder instance that have custom implementations of PropertyAccessor: %s",
                        propertyModelBuilder.getPropertyAccessor().getClass().getName()));
            }
            PropertyAccessorImpl<?> defaultAccessor = (PropertyAccessorImpl<?>) propertyModelBuilder.getPropertyAccessor();
            PropertyMetadata<?> propertyMetaData = defaultAccessor.getPropertyMetadata();
            if (!propertyMetaData.isDeserializable() && propertyMetaData.getField() != null
                    && isPrivate(propertyMetaData.getField().getModifiers())) {
                setPropertyAccessor(propertyModelBuilder);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void setPropertyAccessor(final PropertyModelBuilder<T> propertyModelBuilder) {

        propertyModelBuilder.propertyAccessor(new PrivatePropertyAccessor<T>(
                (PropertyAccessorImpl<T>) propertyModelBuilder.getPropertyAccessor()));
    }

    private static final class PrivatePropertyAccessor<T> implements PropertyAccessor<T> {
        private final PropertyAccessorImpl<T> wrapped;

        private PrivatePropertyAccessor(final PropertyAccessorImpl<T> wrapped) {
            this.wrapped = wrapped;
            try {
                wrapped.getPropertyMetadata().getField().setAccessible(true);
            } catch (Exception e) {
                throw new CodecConfigurationException(format("Unable to make private field accessible '%s' in %s",
                        wrapped.getPropertyMetadata().getName(), wrapped.getPropertyMetadata().getDeclaringClassName()), e);
            }
        }

        @Override
        public <S> T get(final S instance) {
            return wrapped.get(instance);
        }

        @Override
        public <S> void set(final S instance, final T value) {
            try {
                wrapped.getPropertyMetadata().getField().set(instance, value);
            } catch (Exception e) {
                throw new CodecConfigurationException(format("Unable to set value for property '%s' in %s",
                        wrapped.getPropertyMetadata().getName(), wrapped.getPropertyMetadata().getDeclaringClassName()), e);
            }
        }
    }
}
