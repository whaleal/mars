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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public final class DiscriminatorLookup {
    private final Map<String, Class<?>> discriminatorClassMap = new ConcurrentHashMap<String, Class<?>>();
    private final Set<String> packages;

    public DiscriminatorLookup(final Map<Class<?>, EntityModel<?>> classModels, final Set<String> packages) {
        for (EntityModel<?> entityModel : classModels.values()) {
            if (entityModel.getDiscriminator() != null) {
                discriminatorClassMap.put(entityModel.getDiscriminator(), entityModel.getType());
            }
        }
        this.packages = packages;
    }

    public Class<?> lookup(final String discriminator) {
        if (discriminatorClassMap.containsKey(discriminator)) {
            return discriminatorClassMap.get(discriminator);
        }

        Class<?> clazz = getClassForName(discriminator);
        if (clazz == null) {
            clazz = searchPackages(discriminator);
        }

        if (clazz == null) {
            throw new CodecConfigurationException(format("A class could not be found for the discriminator: '%s'.", discriminator));
        } else {
            discriminatorClassMap.put(discriminator, clazz);
        }
        return clazz;
    }

    void addClassModel(final EntityModel<?> entityModel) {
        if (entityModel.getDiscriminator() != null) {
            discriminatorClassMap.put(entityModel.getDiscriminator(), entityModel.getType());
        }
    }


    public void addModel(EntityModel entityModel) {
        if (entityModel.getDiscriminator() != null) {
            discriminatorClassMap.put(entityModel.getDiscriminator(), entityModel.getType());
        }
    }

    private Class<?> getClassForName(final String discriminator) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(discriminator);
        } catch (final ClassNotFoundException e) {
            // Ignore
        }
        return clazz;
    }

    private Class<?> searchPackages(final String discriminator) {
        Class<?> clazz = null;
        for (String packageName : packages) {
            clazz = getClassForName(packageName + "." + discriminator);
            if (clazz != null) {
                return clazz;
            }
        }
        return clazz;
    }
}
