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
import com.whaleal.mars.core.aggregation.expressions.impls.MathExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Defines helper methods for the math expressions
 *
 * @mongodb.driver.manual reference/operator/aggregation/#arithmetic-expression-operators Arithmetic Expressions
 */
public final class MathExpressions {
    protected MathExpressions() {
    }

    /**
     * Returns the absolute value of a number.
     *
     * @param value the value
     * @return the new expression
     *  $abs
     */
    public static Expression abs(Expression value) {
        return new MathExpression("$abs", value);
    }

    /**
     * Adds numbers together or adds numbers and a date. If one of the arguments is a date, $add treats the other arguments as
     * milliseconds to add to the date.
     *
     * @param first      the first expression to sum
     * @param additional any subsequent expressions to include in the sum
     * @return the new expression
     *  $add
     */
    public static MathExpression add(Expression first, Expression... additional) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(first);
        expressions.addAll(asList(additional));
        return new MathExpression("$add", expressions);
    }

    /**
     * Returns the smallest integer greater than or equal to the specified number.
     *
     * @param value the value
     * @return the new expression
     *  $ceil
     */
    public static Expression ceil(Expression value) {
        return new MathExpression("$ceil", value);
    }

    /**
     * Returns the result of dividing the first number by the second. Accepts two argument expressions.
     *
     * @param numerator the numerator
     * @param divisor   the divisor
     * @return the new expression
     *  $divide
     */
    public static Expression divide(Expression numerator, Expression divisor) {
        return new MathExpression("$divide", Arrays.asList(numerator, divisor));
    }

    /**
     * Raises e to the specified exponent.
     *
     * @param value the value
     * @return the new expression
     *  $exp
     */
    public static Expression exp(Expression value) {
        return new MathExpression("$exp", value);
    }

    /**
     * Returns the largest integer less than or equal to the specified number.
     *
     * @param value the value
     * @return the new expression
     *  $floor
     */
    public static Expression floor(Expression value) {
        return new MathExpression("$floor", value);
    }

    /**
     * Calculates the natural log of a number.
     *
     * @param value the value
     * @return the new expression
     *  $ln
     */
    public static Expression ln(Expression value) {
        return new MathExpression("$ln", value);
    }

    /**
     * Calculates the log of a number in the specified base.
     *
     * @param number the number to log
     * @param base   the base to use
     * @return the new expression
     *  $log
     */
    public static Expression log(Expression number, Expression base) {
        return new MathExpression("$log", Arrays.asList(number, base));
    }

    /**
     * Calculates the log base 10 of a number.
     *
     * @param value the value
     * @return the new expression
     *  $log10
     */
    public static Expression log10(Expression value) {
        return new MathExpression("$log10", value);
    }

    /**
     * Returns the remainder of the first number divided by the second. Accepts two argument expressions.
     *
     * @param dividend the dividend
     * @param divisor  the divisor
     * @return the new expression
     *  $mod
     */
    public static Expression mod(Expression dividend, Expression divisor) {
        return new MathExpression("$mod", Arrays.asList(dividend, divisor));
    }

    /**
     * Multiplies numbers together and returns the result. Pass the arguments to $multiply in an array.
     *
     * @param first      the first expression to add
     * @param additional any additional expressions
     * @return the new expression
     *  $multiply
     */
    public static Expression multiply(Expression first, Expression... additional) {
        List<Expression> expressions = new ArrayList<>(asList(first));
        expressions.addAll(asList(additional));
        return new MathExpression("$multiply", expressions);
    }

    /**
     * Raises a number to the specified exponent.
     *
     * @param number   the base name
     * @param exponent the exponent
     * @return the new expression
     *  $pow
     */
    public static Expression pow(Expression number, Expression exponent) {
        return new MathExpression("$pow", Arrays.asList(number, exponent));
    }

    /**
     * Rounds a number to to a whole integer or to a specified decimal place.
     *
     * @param number the value
     * @param place  the place to round to
     * @return the new expression
     *  $round
     */
    public static Expression round(Expression number, Expression place) {
        return new MathExpression("$round", asList(number, place));
    }

    /**
     * Calculates the square root.
     *
     * @param value the value
     * @return the new expression
     *  $sqrt
     */
    public static Expression sqrt(Expression value) {
        return new MathExpression("$sqrt", value);
    }

    /**
     * Returns the result of subtracting the second value from the first. If the two values are numbers, return the difference. If the two
     * values are dates, return the difference in milliseconds. If the two values are a date and a number in milliseconds, return the
     * resulting date. Accepts two argument expressions. If the two values are a date and a number, specify the date argument first as it
     * is not meaningful to subtract a date from a number.
     *
     * @param minuend    the number to subtract from
     * @param subtrahend the number to subtract
     * @return the new expression
     *  $subtract
     */
    public static Expression subtract(Expression minuend, Expression subtrahend) {
        return new MathExpression("$subtract", Arrays.asList(minuend, subtrahend));
    }

    /**
     * Truncates a number to a whole integer or to a specified decimal place.
     * <p>
     * NOTE:  Prior to 4.2, the place value wasn't available.  Pass null if your server is older than 4.2.
     *
     * @param number the value
     * @param place  the place to trunc to
     * @return the new expression
     *  $trunc
     */
    public static Expression trunc(Expression number, Expression place) {
        ArrayList<Expression> params = new ArrayList<>();
        params.add(number);
        if (place != null) {
            params.add(place);
        }
        return new MathExpression("$trunc", params);
    }
}
