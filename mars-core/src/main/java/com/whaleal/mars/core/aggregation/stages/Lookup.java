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
package com.whaleal.mars.core.aggregation.stages;

import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.expressions.impls.DocumentExpression;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;


public class Lookup extends Stage {
    private String from;
    private Class<?> fromType;
    private String localField;
    private String foreignField;
    private String as;
    private DocumentExpression variables;

    protected Lookup(Class<?> fromType) {
        super("$lookup");
        this.fromType = fromType;
    }

    protected Lookup(String from) {
        super("$lookup");
        this.from = from;
    }


    public static Lookup from(Class<?> from) {
        return new Lookup(from);
    }


    public static Lookup from(String from) {
        return new Lookup(from);
    }


    public Lookup as(String as) {
        this.as = as;
        return this;
    }


    public Lookup foreignField(String foreignField) {
        this.foreignField = foreignField;
        return this;
    }


    public String getAs() {
        return as;
    }

    public String getForeignField() {
        return foreignField;
    }


    public String getFrom() {
        return from;
    }


    public Class<?> getFromType() {
        return fromType;
    }


    public String getLocalField() {
        return localField;
    }


    public DocumentExpression getVariables() {
        return variables;
    }


    public Lookup let(String name, Expression expression) {
        if (variables == null) {
            variables = Expressions.of();
        }
        variables.field(name, expression);
        return this;
    }


    public Lookup localField(String localField) {
        this.localField = localField;
        return this;
    }
}
