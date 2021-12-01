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
import org.bson.codecs.pojo.PropertyAccessor;

import static java.lang.String.format;

public class PropertyAccessorImpl<T> implements PropertyAccessor<T> {

    private final PropertyMetadata<T> propertyMetadata;

    PropertyAccessorImpl(final PropertyMetadata<T> propertyMetadata) {
        this.propertyMetadata = propertyMetadata;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <S> T get(final S instance) {

        try {
            if (propertyMetadata.isSerializable()) {
                if (propertyMetadata.getGetter() != null) {
                    return (T) propertyMetadata.getGetter().invoke(instance);
                } else {
                    return (T) propertyMetadata.getField().get(instance);
                }
            } else {
                throw getError(null);
            }
        } catch (final Exception e) {
            throw getError(e);
        }
    }

    @Override
    public <S> void set(final S instance, final T value) {
        try {
            if (propertyMetadata.isDeserializable()) {
                if (propertyMetadata.getSetter() != null) {
                    propertyMetadata.getSetter().invoke(instance, value);
                } else {
                    propertyMetadata.getField().set(instance, value);
                }
            }
        } catch (final Exception e) {
            throw setError(e);
        }
    }

    protected PropertyMetadata<T> getPropertyMetadata() {
        return propertyMetadata;
    }

    private CodecConfigurationException getError(final Exception cause) {
        return new CodecConfigurationException(format("Unable to get value for property '%s' in %s", propertyMetadata.getName(),
                propertyMetadata.getDeclaringClassName()), cause);
    }

    private CodecConfigurationException setError(final Exception cause) {
        return new CodecConfigurationException(format("Unable to set value for property '%s' in %s", propertyMetadata.getName(),
                propertyMetadata.getDeclaringClassName()), cause);
    }
}
