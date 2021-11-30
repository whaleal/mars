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
 * Defines helper methods for the comparison expressions
 *
 * @mongodb.driver.manual reference/operator/aggregation/#comparison-expression-operators Comparison Expressions
 */
public final class ComparisonExpressions {
    private ComparisonExpressions() {
    }

    /**
     * Returns 0 if the two values are equivalent, 1 if the first value is greater than the second, and -1 if the first value is less than
     * the second.
     *
     * @param first  an expression for the value to compare
     * @param second an expression yielding the value to check against
     * @return the new expression
     *  $cmp
     */
    public static Expression cmp(Expression first, Expression second) {
        return new Expression("$cmp", Arrays.asList(first, second));
    }

    /**
     * Returns true if the values are equivalent.
     *
     * @param first  an expression for the value to compare
     * @param second an expression yielding the value to check against
     * @return the new expression
     *  $eq
     */
    public static Expression eq(Expression first, Expression second) {
        return new Expression("$eq", Arrays.asList(first, second));
    }

    /**
     * Compares two values and returns:
     *
     * <li>true when the first value is greater than the second value.
     * <li>false when the first value is less than or equivalent to the second value.
     *
     * @param first  an expression for the value to compare
     * @param second an expression yielding the value to check against
     * @return the new expression
     *  $gt
     */
    public static Expression gt(Expression first, Expression second) {
        return new Expression("$gt", Arrays.asList(first, second));
    }

    /**
     * Compares two values and returns:
     *
     * <li>true when the first value is greater than or equivalent to the second value.
     * <li>false when the first value is less than the second value.
     *
     * @param first  an expression for the value to compare
     * @param second an expression yielding the value to check against
     * @return the new expression
     *  $gte
     */
    public static Expression gte(Expression first, Expression second) {
        return new Expression("$gte", Arrays.asList(first, second));
    }

    /**
     * Returns true if the first value is less than the second.
     *
     * @param first  an expression for the value to compare
     * @param second an expression yielding the value to check against
     * @return the new expression
     *  $lt
     */
    public static Expression lt(Expression first, Expression second) {
        return new Expression("$lt", Arrays.asList(first, second));
    }

    /**
     * Compares two values and returns:
     *
     * <li>true when the first value is less than or equivalent to the second value.
     * <li>false when the first value is greater than the second value.
     *
     * @param first  an expression for the value to compare
     * @param second an expression yielding the value to check against
     * @return the new expression
     *  $lte
     */
    public static Expression lte(Expression first, Expression second) {
        return new Expression("$lte", Arrays.asList(first, second));
    }

    /**
     * Returns true if the values are not equivalent.
     *
     * @param first  an expression for the value to compare
     * @param second an expression yielding the value to check against
     * @return the new expression
     *  $ne
     */
    public static Expression ne(Expression first, Expression second) {
        return new Expression("$ne", Arrays.asList(first, second));
    }

}
