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
package com.whaleal.mars.core.query;


import com.whaleal.mars.codecs.MongoMappingContext;

import com.whaleal.mars.codecs.pojo.EntityModel;
import com.whaleal.mars.core.aggregation.stages.filters.OperationTarget;
import com.whaleal.mars.core.internal.PathTarget;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;


class FieldCriteria extends Criteria {
    private final String field;
    private final FilterOperator operator;
    private final Object value;
    private final boolean not;
    private final MongoMappingContext mapper;

    FieldCriteria( MongoMappingContext mapper, String field, FilterOperator op, Object value, EntityModel model,
                   boolean validating) {
        this(mapper, field, op, value, false, model, validating);
    }

    FieldCriteria( MongoMappingContext mapper, String fieldName, FilterOperator op, Object value, boolean not, EntityModel model, boolean validating) {
        this.mapper = mapper;
        final PathTarget pathTarget = new PathTarget(mapper, model, fieldName, validating);

        this.field = pathTarget.translatedPath();

        this.operator = op;
        this.value = ((Document) new OperationTarget(pathTarget, value).encode(mapper)).get(this.field);
        this.not = not;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @return the operator used against this field
     * @see FilterOperator
     */
    public FilterOperator getOperator() {
        return operator;
    }

    /**
     * @return the value used in the Criteria
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return true if 'not' has been applied against this Criteria
     */
    public boolean isNot() {
        return not;
    }


    public Document toDocument() {
        final Document obj = new Document();
        if (FilterOperator.EQUAL.equals(operator)) {
            // no operator, prop equals (or NOT equals) value
            if (not) {
                obj.put(field, new Document("$not", value));
            } else {
                obj.put(field, value);
            }

        } else {
            final Object object = obj.get(field); // operator within inner object
            Map<String, Object> inner;
            if (!(object instanceof Map)) {
                inner = new HashMap<>();
                obj.put(field, inner);
            } else {
                inner = (Map<String, Object>) object;
            }

            if (not) {
                inner.put("$not", new Document(operator.val(), value));
            } else {
                inner.put(operator.val(), value);
            }
        }
        return obj;
    }


    public String getFieldName() {
        return field;
    }

    @Override
    public String toString() {
        return field + " " + operator.val() + " " + value;
    }

    protected MongoMappingContext getMapper() {
        return mapper;
    }
}
