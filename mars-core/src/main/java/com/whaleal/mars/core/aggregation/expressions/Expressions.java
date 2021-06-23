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
package com.whaleal.mars.core.aggregation.expressions;

import com.whaleal.mars.core.aggregation.expressions.impls.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Defines helper methods for various expressions.
 *
 * @mongodb.driver.manual reference/operator/aggregation/ Aggregation pipeline operators
 */
public final class Expressions {
    private Expressions() {
    }

    /**
     * Creates a field expression for the given value.  If the value does not already start with '$', it will be prepended automatically.
     *
     * @param name the field name
     * @return the new expression
     */
    public static Expression field(String name) {
        return new ValueExpression(name.startsWith("$") ? name : "$" + name);
    }

    /**
     * Returns a value without parsing. Use for values that the aggregation pipeline may interpret as an expression.
     *
     * @param value the value
     * @return the new expression
     * @aggregation.expression $literal
     */
    public static Expression literal(Object value) {
        return new LiteralExpression(value);
    }

    /**
     * Returns the metadata associated with a document in a pipeline operations, e.g. "textScore" when performing text search.
     *
     * @return the new expression
     * @aggregation.expression $meta
     */
    public static Expression meta() {
        return new MetaExpression();
    }

    /**
     * Creates a new DocumentExpression.
     *
     * @return the new expression
     */
    public static DocumentExpression of() {
        return new DocumentExpression();
    }

    /**
     * @param first      the first item
     * @param additional additional items
     * @param <T>        the element type
     * @return a list of them all
     */
    public static <T> List<T> toList(T first, T... additional) {
        List<T> expressions = new ArrayList<>();
        expressions.add(first);
        expressions.addAll(asList(additional));
        return expressions;
    }

    /**
     * Returns a value without parsing. Note that this is different from {@link #literal(Object)} in that the given value will dropped
     * directly in to the pipeline for use/evaluation in whatever context the value is used.
     *
     * @param value the value
     * @return the new expression
     */
    public static Expression value(Object value) {
        return new ValueExpression(value);
    }
}
