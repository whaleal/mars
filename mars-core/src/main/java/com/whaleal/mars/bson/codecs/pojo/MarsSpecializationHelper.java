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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class MarsSpecializationHelper {

    @SuppressWarnings("unchecked")
    static <V> TypeData<V> specializeTypeData(final TypeData<V> typeData, final List<TypeData<?>> typeParameters,
                                              final TypeParameterMap typeParameterMap) {
        if (!typeParameterMap.hasTypeParameters() || typeParameters.isEmpty()) {
            return typeData;
        }

        Map<Integer, Either<Integer, TypeParameterMap>> propertyToClassParamIndexMap = typeParameterMap.getPropertyToClassParamIndexMap();
        Either<Integer, TypeParameterMap> classTypeParamRepresentsWholeField = propertyToClassParamIndexMap.get(-1);
        if (classTypeParamRepresentsWholeField != null) {
            Integer index = classTypeParamRepresentsWholeField.map(i -> i, e -> {
                throw new IllegalStateException("Invalid state, the whole class cannot be represented by a subtype.");
            });
            return (TypeData<V>) typeParameters.get(index);
        } else {
            return getTypeData(typeData, typeParameters, propertyToClassParamIndexMap);
        }
    }

    private static <V> TypeData<V> getTypeData(final TypeData<V> typeData, final List<TypeData<?>> specializedTypeParameters,
                                               final Map<Integer, Either<Integer, TypeParameterMap>> propertyToClassParamIndexMap) {
        List<TypeData<?>> subTypeParameters = new ArrayList<>(typeData.getTypeParameters());
        for (int i = 0; i < typeData.getTypeParameters().size(); i++) {
            subTypeParameters.set(i, getTypeData(subTypeParameters.get(i), specializedTypeParameters, propertyToClassParamIndexMap, i));
        }
        return TypeData.builder(typeData.getType()).addTypeParameters(subTypeParameters).build();
    }

    private static TypeData<?> getTypeData(final TypeData<?> typeData, final List<TypeData<?>> specializedTypeParameters,
                                           final Map<Integer, Either<Integer, TypeParameterMap>> propertyToClassParamIndexMap,
                                           final int index) {
        if (!propertyToClassParamIndexMap.containsKey(index)) {
            return typeData;
        }
        return propertyToClassParamIndexMap.get(index).map(l -> {
                    if (typeData.getTypeParameters().isEmpty()) {
                        // Represents the whole typeData
                        return specializedTypeParameters.get(l);
                    } else {
                        // Represents a single nested type parameter within this typeData
                        TypeData.Builder<?> builder = TypeData.builder(typeData.getType());
                        List<TypeData<?>> typeParameters = new ArrayList<>(typeData.getTypeParameters());
                        typeParameters.set(index, specializedTypeParameters.get(l));
                        builder.addTypeParameters(typeParameters);
                        return builder.build();
                    }
                },
                r -> {
                    // Represents a child type parameter of this typeData
                    return getTypeData(typeData, specializedTypeParameters, r.getPropertyToClassParamIndexMap());
                });
    }

    private MarsSpecializationHelper() {
    }
}
