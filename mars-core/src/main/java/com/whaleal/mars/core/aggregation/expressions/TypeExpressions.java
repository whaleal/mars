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

import com.whaleal.mars.core.aggregation.expressions.impls.ConvertExpression;
import com.whaleal.mars.core.aggregation.expressions.impls.ConvertType;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

/**
 * Defines helper methods for the type expressions
 *
 * @mongodb.driver.manual reference/operator/aggregation/#type-expression-operators Type Expressions
 */
public final class TypeExpressions {
    protected TypeExpressions() {
    }

    /**
     * Converts a value to a specified type.
     *
     * @param input the value to process
     * @param to    an expression giving the target type
     * @return the new expression
     *  $convert
     */
    public static Expression convert(Expression input, ConvertType to) {
        return new ConvertExpression(input, to);
    }

    /**
     * Checks if the specified expression resolves to one of the <a hre="https://docs.mongodb.com/manual/reference/bson-types/#bson-types">
     * numeric BSON types.</a>
     *
     * @param input the value to check
     * @return the new expression
     *  $isNumber
     */
    public static Expression isNumber(Expression input) {
        return new Expression("$isNumber", input);
    }

    /**
     * Converts value to a boolean.
     *
     * @param input the value to process
     * @return the new expression
     *  $toBool
     */
    public static Expression toBool(Expression input) {
        return new Expression("$toBool", input);
    }

    /**
     * Converts value to a Date.
     *
     * @param input the value to process
     * @return the new expression
     *  $toDate
     */
    public static Expression toDate(Expression input) {
        return new Expression("$toDate", input);
    }

    /**
     * Converts value to a Decimal128.
     *
     * @param input the value to process
     * @return the new expression
     *  $toDecimal
     */
    public static Expression toDecimal(Expression input) {
        return new Expression("$toDecimal", input);
    }

    /**
     * Converts value to a double.
     *
     * @param input the value to process
     * @return the new expression
     *  $toDouble
     */
    public static Expression toDouble(Expression input) {
        return new Expression("$toDouble", input);
    }

    /**
     * Converts value to an integer.
     *
     * @param input the value to process
     * @return the new expression
     *  $toInt
     */
    public static Expression toInt(Expression input) {
        return new Expression("$toInt", input);
    }

    /**
     * Converts value to a long.
     *
     * @param input the value to process
     * @return the new expression
     *  $toLong
     */
    public static Expression toLong(Expression input) {
        return new Expression("$toLong", input);
    }

    /**
     * Converts value to an ObjectId.
     *
     * @param input the value to process
     * @return the new expression
     *  $toObjectId
     */
    public static Expression toObjectId(Expression input) {
        return new Expression("$toObjectId", input);
    }

    /**
     * Converts value to a string.
     *
     * @param input the value to process
     * @return the new expression
     *  $toString
     */
    public static Expression toString(Expression input) {
        return StringExpressions.toString(input);
    }

    /**
     * Return the BSON data type of the field.
     *
     * @param input the value to process
     * @return the new expression
     *  $type
     */
    public static Expression type(Expression input) {
        return new Expression("$type", input);
    }
}
