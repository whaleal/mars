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
 * Defines helper methods for the trigonometry expressions
 *
 * @mongodb.driver.manual reference/operator/aggregation/#trigonometry-expression-operators Trigonometry Expressions
 */
public final class TrigonometryExpressions {
    private TrigonometryExpressions() {
    }

    /**
     * Returns the inverse cosine (arc cosine) of a value in radians.
     *
     * @param value the value
     * @return the new expression
     *  $acos
     */
    public static Expression acos(Expression value) {
        return new Expression("$acos", value);
    }

    /**
     * Returns the inverse hyperbolic cosine (hyperbolic arc cosine) of a value in radians.
     *
     * @param value the value
     * @return the new expression
     *  $acosh
     */
    public static Expression acosh(Expression value) {
        return new Expression("$acosh", value);
    }

    /**
     * Returns the inverse sin (arc sine) of a value in radians.
     *
     * @param value the value
     * @return the new expression
     *  $asin
     */
    public static Expression asin(Expression value) {
        return new Expression("$asin", value);
    }

    /**
     * Returns the inverse hyperbolic sine (hyperbolic arc sine) of a value in radians.
     *
     * @param value the value
     * @return the new expression
     *  $asinh
     */
    public static Expression asinh(Expression value) {
        return new Expression("$asinh", value);
    }

    /**
     * Returns the inverse tangent (arc tangent) of a value in radians.
     *
     * @param value the value
     * @return the new expression
     *  $atan
     */
    public static Expression atan(Expression value) {
        return new Expression("$atan", value);
    }

    /**
     * Returns the inverse tangent (arc tangent) of y / x in radians, where y and x are the first and second values passed to the
     * expression respectively.
     *
     * @param yValue the y value
     * @param xValue the x value
     * @return the new expression
     *  $atan2
     */
    public static Expression atan2(Expression yValue, Expression xValue) {
        return new Expression("$atan2", Arrays.asList(yValue, xValue));
    }

    /**
     * Returns the inverse hyperbolic tangent (hyperbolic arc tangent) of a value in radians.
     *
     * @param value the value
     * @return the new expression
     *  $atanh
     */
    public static Expression atanh(Expression value) {
        return new Expression("$atanh", value);
    }

    /**
     * Returns the cosine of a value that is measured in radians.
     *
     * @param value the value
     * @return the new expression
     *  $cos
     */
    public static Expression cos(Expression value) {
        return new Expression("$cos", value);
    }

    /**
     * Returns the hyperbolic cosine of a value that is measured in radians.
     *
     * @param value the value
     * @return the new expression
     *  $cosh
     */
    public static Expression cosh(Expression value) {
        return new Expression("$cosh", value);
    }

    /**
     * Converts a value from degrees to radians.
     *
     * @param value the value
     * @return the new expression
     *  $degreesToRadians
     */
    public static Expression degreesToRadians(Expression value) {
        return new Expression("$degreesToRadians", value);
    }

    /**
     * Converts a value from radians to degrees.
     *
     * @param value the value
     * @return the new expression
     *  $radiansToDegrees
     */
    public static Expression radiansToDegrees(Expression value) {
        return new Expression("$radiansToDegrees", value);
    }

    /**
     * Returns the sine of a value that is measured in radians.
     *
     * @param value the value
     * @return the new expression
     *  $sin
     */
    public static Expression sin(Expression value) {
        return new Expression("$sin", value);
    }

    /**
     * Returns the hyperbolic sine of a value that is measured in radians.
     *
     * @param value the value
     * @return the new expression
     *  $sinh
     */
    public static Expression sinh(Expression value) {
        return new Expression("$sinh", value);
    }

    /**
     * Returns the tangent of a value that is measured in radians.
     *
     * @param value the value
     * @return the new expression
     *  $tan
     */
    public static Expression tan(Expression value) {
        return new Expression("$tan", value);
    }

    /**
     * Returns the hyperbolic tangent of a value that is measured in radians.
     *
     * @param value the value
     * @return the new expression
     *  $tanh
     */
    public static Expression tanh(Expression value) {
        return new Expression("$tanh", value);
    }

}
