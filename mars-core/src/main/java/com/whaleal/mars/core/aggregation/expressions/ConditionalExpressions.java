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
import com.whaleal.mars.core.aggregation.expressions.impls.IfNull;
import com.whaleal.mars.core.aggregation.expressions.impls.SwitchExpression;

import java.util.Arrays;

/**
 * Defines helper methods for the conditional expressions
 *
 * @mongodb.driver.manual reference/operator/aggregation/#conditional-expression-operators Conditional Expressions
 */
public class ConditionalExpressions {
    private ConditionalExpressions() {
    }

    /**
     * Evaluates a boolean expression to return one of the two specified return expressions.
     *
     * @param condition the condition to evaluate
     * @param then      the expression for the true branch
     * @param otherwise the expresion for the else branch
     * @return the new expression
     * @aggregation.expression $cond
     */
    public static Expression condition(Expression condition, Expression then, Expression otherwise) {
        return new Expression("$cond", Arrays.asList(condition, then, otherwise));
    }

    /**
     * Evaluates an expression and returns the value of the expression if the expression evaluates to a non-null value. If the
     * expression evaluates to a null value, including instances of undefined values or missing fields, returns the value of the
     * replacement expression.
     *
     * @return the new expression
     * @aggregation.expression $ifNull
     */
    public static IfNull ifNull() {
        return new IfNull();
    }

    /**
     * Evaluates a series of case expressions. When it finds an expression which evaluates to true, $switch executes a specified
     * expression and breaks out of the control flow.
     *
     * @return the new expression
     * @aggregation.expression $switch
     */
    public static SwitchExpression switchExpression() {
        return new SwitchExpression();
    }
}
