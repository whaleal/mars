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

import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

import java.util.Arrays;

/**
 * Defines helper methods for the boolean expressions
 *
 * @mongodb.driver.manual reference/operator/aggregation/#boolean-expression-operators Boolean Expressions
 */
public final class BooleanExpressions {
    private BooleanExpressions() {
    }

    /**
     * Evaluates one or more expressions and returns true if all of the expressions are true or if evoked with no argument expressions.
     * Otherwise, $and returns false.
     *
     * @param first      the first expression
     * @param additional any additional expressions
     * @return the new expression
     * @aggregation.expression $and
     */
    public static Expression and(Expression first, Expression... additional) {
        return new Expression("$and", Expressions.toList(first, additional));
    }

    /**
     * Evaluates a boolean and returns the opposite boolean value; i.e. when passed an expression that evaluates to true, $not returns
     * false; when passed an expression that evaluates to false, $not returns true.
     *
     * @param value the expression
     * @return the new expression
     * @aggregation.expression $not
     */
    public static Expression not(Expression value) {
        return new Expression("$not", Arrays.asList(value));
    }

    /**
     * Evaluates one or more expressions and returns true if any of the expressions are true. Otherwise, $or returns false.
     *
     * @param first      the first expression
     * @param additional any additional expressions
     * @return the new expression
     * @aggregation.expression $or
     */
    public static Expression or(Expression first, Expression... additional) {
        return new Expression("$or", Expressions.toList(first, additional));
    }

}
