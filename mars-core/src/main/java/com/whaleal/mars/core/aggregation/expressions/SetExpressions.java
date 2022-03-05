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


public final class SetExpressions {
    private SetExpressions() {
    }

    /**
     * Returns true if no element of a set evaluates to false, otherwise, returns false. Accepts a single argument expression.
     *
     * @param first      an expression to evaluate
     * @param additional any additional expressions
     * @return the new expression
     *  $allElementsTrue
     */
    public static Expression allElementsTrue(Expression first, Expression... additional) {
        return new Expression("$allElementsTrue", Expressions.toList(first, additional));
    }

    /**
     * Returns true if any elements of a set evaluate to true; otherwise, returns false. Accepts a single argument expression.
     *
     * @param first      an expression to evaluate
     * @param additional any additional expressions
     * @return the new expression
     *  $anyElementTrue
     */
    public static Expression anyElementTrue(Expression first, Expression... additional) {
        return new Expression("$anyElementTrue", Expressions.toList(first, additional));
    }

    /**
     * Returns a set with elements that appear in the first set but not in the second set; i.e. performs a relative complement of the
     * second set relative to the first. Accepts exactly two argument expressions.
     *
     * @param first  the first array expression
     * @param second the second expression
     * @return the new expression
     *  $setDifference
     */
    public static Expression setDifference(Expression first, Expression second) {
        return new Expression("$setDifference", Arrays.asList(first, second));
    }

    /**
     * Returns true if the input sets have the same distinct elements. Accepts two or more argument expressions.
     *
     * @param first      the first array expression
     * @param additional additional expressions
     * @return the new expression
     *  $setEquals
     */
    public static Expression setEquals(Expression first, Expression... additional) {
        return new Expression("$setEquals", Expressions.toList(first, additional));
    }

    /**
     * Returns a set with elements that appear in all of the input sets. Accepts any number of argument expressions.
     *
     * @param first      the first array expression
     * @param additional additional expressions
     * @return the new expression
     *  $setIntersection
     */
    public static Expression setIntersection(Expression first, Expression... additional) {
        return new Expression("$setIntersection", Expressions.toList(first, additional));
    }

    /**
     * Returns true if all elements of the first set appear in the second set, including when the first set equals the second set; i.e.
     * not a strict subset. Accepts exactly two argument expressions.
     *
     * @param first  the first array expression
     * @param second the second expression
     * @return the new expression
     *  $setIsSubset
     */
    public static Expression setIsSubset(Expression first, Expression second) {
        return new Expression("$setIsSubset", Arrays.asList(first, second));
    }

    /**
     * Returns a set with elements that appear in any of the input sets.
     *
     * @param first      the first array expression
     * @param additional additional expressions
     * @return the new expression
     *  $setUnion
     */
    public static Expression setUnion(Expression first, Expression... additional) {
        return new Expression("$setUnion", Expressions.toList(first, additional));
    }

}
