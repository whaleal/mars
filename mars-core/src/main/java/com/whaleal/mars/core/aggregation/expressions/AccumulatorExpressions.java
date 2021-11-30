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


public final class AccumulatorExpressions {
    private AccumulatorExpressions() {
    }

    /**
     * Returns an array of unique expression values for each group. Order of the array elements is undefined.
     */
    public static AccumulatorExpression accumulator(String initFunction,
                                                    String accumulateFunction,
                                                    List<Expression> accumulateArgs,
                                                    String mergeFunction) {
        return new AccumulatorExpression(initFunction, accumulateFunction, accumulateArgs, mergeFunction);
    }

    /**
     * Returns an array of unique expression values for each group. Order of the array elements is undefined.
     */
    public static Expression addToSet(Expression value) {
        return new Expression("$addToSet", value);
    }

    /**
     * Returns an average of numerical values. Ignores non-numeric values.
     */
    public static Expression avg(Expression value, Expression... additional) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(value);
        expressions.addAll(asList(additional));
        return new Accumulator("$avg", expressions);
    }

    /**
     * Returns a value from the first document for each group. Order is only defined if the documents are in a defined order.
     */
    public static Expression first(Expression value) {
        return new Expression("$first", value);
    }

    /**
     * Defines a custom aggregation function or expression in JavaScript.
     */
    public static Expression function(String body, Expression... args) {
        return new FunctionExpression(body, asList(args));
    }

    /**
     * Returns a value from the last document for each group. Order is only defined if the documents are in a defined order.
     *
     * @param value the value
     * @return the new expression
     *  $last
     */
    public static Expression last(Expression value) {
        return new Expression("$last", value);
    }

    /**
     * Returns the highest expression value for each group.
     *
     * @param value the value
     * @return the new expression
     *  $max
     */
    public static Expression max(Expression value) {
        return new Expression("$max", value);
    }

    /**
     * Returns the lowest expression value for each group.
     *
     * @param value the value
     * @return the new expression
     *  $min
     */
    public static Expression min(Expression value) {
        return new Expression("$min", value);
    }

    /**
     * Returns an array of expression values for each group.
     *
     * @param value the value
     * @return the new expression
     *  $push
     */
    public static Expression push(Expression value) {
        return new Expression("$push", value);
    }

    /**
     * Returns an array of all values that result from applying an expression to each document in a group of documents that share the
     * same group by key.
     * <p>
     * $push is only available in the $group stage.
     *
     * @return the new expression
     *  $push
     */
    public static Push push() {
        return new Push();
    }

    /**
     * Returns the population standard deviation of the input values.
     *
     * @param value      the value
     * @param additional any subsequent expressions to include in the expression
     * @return the new expression
     *  $stdDevPop
     */
    public static Expression stdDevPop(Expression value, Expression... additional) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(value);
        expressions.addAll(asList(additional));
        return new Accumulator("$stdDevPop", expressions);
    }

    /**
     * Returns the sample standard deviation of the input values.
     *
     * @param value      the value
     * @param additional any subsequent expressions to include in the expression
     * @return the new expression
     *  $stdDevSamp
     */
    public static Expression stdDevSamp(Expression value, Expression... additional) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(value);
        expressions.addAll(asList(additional));
        return new Accumulator("$stdDevSamp", expressions);
    }

    /**
     * Calculates and returns the sum of numeric values. $sum ignores non-numeric values.
     *
     * @param first      the first expression to sum
     * @param additional any subsequent expressions to include in the sum
     * @return the new expression
     *  $sum
     */
    public static Expression sum(Expression first, Expression... additional) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(first);
        expressions.addAll(asList(additional));
        return new Accumulator("$sum", expressions);
    }
}

