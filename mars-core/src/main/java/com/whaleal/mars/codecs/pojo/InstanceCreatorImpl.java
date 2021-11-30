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

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

final class InstanceCreatorImpl<T> implements MarsInstanceCreator<T> {
    private final CreatorExecutable<T> creatorExecutable;
    private final Map<PropertyModel<?>, Object> cachedValues;
    private final Map<String, Integer> properties;
    private final Object[] params;

    private T newInstance;

    InstanceCreatorImpl(final CreatorExecutable<T> creatorExecutable) {
        this.creatorExecutable = creatorExecutable;
        if (creatorExecutable.getProperties().isEmpty()) {
            this.cachedValues = null;
            this.properties = null;
            this.params = null;
            this.newInstance = creatorExecutable.getInstance();
        } else {
            this.cachedValues = new HashMap<PropertyModel<?>, Object>();
            this.properties = new HashMap<String, Integer>();

            for (int i = 0; i < creatorExecutable.getProperties().size(); i++) {
                if (creatorExecutable.getIdPropertyIndex() != null && creatorExecutable.getIdPropertyIndex() == i) {
                    this.properties.put(EntityModelBuilder.ID_PROPERTY_NAME, creatorExecutable.getIdPropertyIndex());
                } else {
                    this.properties.put(creatorExecutable.getProperties().get(i).value(), i);
                }
            }

            this.params = new Object[properties.size()];
        }
    }

    @Override
    public <S> void set(final S value, final PropertyModel<S> propertyModel) {
        if (newInstance != null) {
            propertyModel.getPropertyAccessor().set(newInstance, value);
        } else {
            if (!properties.isEmpty()) {
                String propertyName = propertyModel.getWriteName();

                if (!properties.containsKey(propertyName)) {
                    // Support legacy MongoProperty settings where the property name was used instead of the write name.
                    propertyName = propertyModel.getName();
                }

                Integer index = properties.get(propertyName);
                if (index != null) {
                    params[index] = value;
                }
                properties.remove(propertyName);
            }

            if (properties.isEmpty()) {
                constructInstanceAndProcessCachedValues();
            } else {
                cachedValues.put(propertyModel, value);
            }
        }
    }

    @Override
    public T getInstance() {
        if (newInstance == null) {
            try {
                for (Map.Entry<String, Integer> entry : properties.entrySet()) {
                    params[entry.getValue()] = null;
                }
                constructInstanceAndProcessCachedValues();
            } catch (CodecConfigurationException e) {
                throw new CodecConfigurationException(format("Could not construct new instance of: %s. "
                                + "Missing the following properties: %s",
                        creatorExecutable.getType().getSimpleName(), properties.keySet()), e);
            }
        }
        return newInstance;
    }

    private void constructInstanceAndProcessCachedValues() {
        try {
            newInstance = creatorExecutable.getInstance(params);
        } catch (Exception e) {
            throw new CodecConfigurationException(e.getMessage(), e);
        }

        for (Map.Entry<PropertyModel<?>, Object> entry : cachedValues.entrySet()) {
            setPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private <S> void setPropertyValue(final PropertyModel<S> propertyModel, final Object value) {
        set((S) value, propertyModel);
    }
}
