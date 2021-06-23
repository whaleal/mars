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


import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;


final class TypeParameterMap {
    private final Map<Integer, Either<Integer, TypeParameterMap>> propertyToClassParamIndexMap;


    static Builder builder() {
        return new Builder();
    }


    Map<Integer, Either<Integer, TypeParameterMap>> getPropertyToClassParamIndexMap() {
        return propertyToClassParamIndexMap;
    }

    boolean hasTypeParameters() {
        return !propertyToClassParamIndexMap.isEmpty();
    }


    static final class Builder {
        private final Map<Integer, Either<Integer, TypeParameterMap>> propertyToClassParamIndexMap = new HashMap<>();

        private Builder() {
        }


        Builder addIndex(final int classTypeParameterIndex) {
            propertyToClassParamIndexMap.put(-1, Either.left(classTypeParameterIndex));
            return this;
        }


        Builder addIndex(final int propertyTypeParameterIndex, final int classTypeParameterIndex) {
            propertyToClassParamIndexMap.put(propertyTypeParameterIndex, Either.left(classTypeParameterIndex));
            return this;
        }


        Builder addIndex(final int propertyTypeParameterIndex, final TypeParameterMap typeParameterMap) {
            propertyToClassParamIndexMap.put(propertyTypeParameterIndex, Either.right(typeParameterMap));
            return this;
        }


        TypeParameterMap build() {
            if (propertyToClassParamIndexMap.size() > 1 && propertyToClassParamIndexMap.containsKey(-1)) {
                throw new IllegalStateException("You cannot have a generic field that also has type parameters.");
            }
            return new TypeParameterMap(propertyToClassParamIndexMap);
        }
    }

    @Override
    public String toString() {
        return "TypeParameterMap{"
                + "fieldToClassParamIndexMap=" + propertyToClassParamIndexMap
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

        TypeParameterMap that = (TypeParameterMap) o;

        if (!getPropertyToClassParamIndexMap().equals(that.getPropertyToClassParamIndexMap())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getPropertyToClassParamIndexMap().hashCode();
    }

    private TypeParameterMap(final Map<Integer, Either<Integer, TypeParameterMap>> propertyToClassParamIndexMap) {
        this.propertyToClassParamIndexMap = unmodifiableMap(propertyToClassParamIndexMap);
    }
}
