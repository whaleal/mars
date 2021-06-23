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

import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.aggregation.expressions.impls.*;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * Defines helper methods for the date expressions
 *
 * @mongodb.driver.manual reference/operator/aggregation/#date-expression-operators Date Expressions
 */
public final class DateExpressions {
    private DateExpressions() {
    }

    /**
     * Constructs and returns a Date object given the date’s constituent properties.
     *
     * @return the new expression
     * @aggregation.expression $dateFromParts
     */
    public static DateFromParts dateFromParts() {
        return new DateFromParts();
    }

    /**
     * Converts a date/time string to a date object.
     *
     * @return the new expression
     * @aggregation.expression $dateFromString
     */
    public static DateFromString dateFromString() {
        return new DateFromString();
    }

    /**
     * Constructs and returns a Date object given the date’s constituent properties.
     *
     * @param date The input date for which to return parts.
     * @return the new expression
     * @aggregation.expression $dateToParts
     */
    public static DateToParts dateToParts(Expression date) {
        return new DateToParts(date);
    }

    /**
     * Returns the date as a formatted string.
     *
     * @return the new expression
     * @aggregation.expression $dateToString
     */
    public static DateToString dateToString() {
        return new DateToString();
    }

    /**
     * Returns the day of the month for a date as a number between 1 and 31.
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $dayOfMonth
     */
    public static DateExpression dayOfMonth(Expression value) {
        return new DateExpression("$dayOfMonth", value);
    }

    /**
     * Returns the day of the week for a date as a number between 1 (Sunday) and 7 (Saturday).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $dayOfWeek
     */
    public static DateExpression dayOfWeek(Expression value) {
        return new DateExpression("$dayOfWeek", value);
    }

    /**
     * Returns the day of the year for a date as a number between 1 and 366 (leap year).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $dayOfYear
     */
    public static DateExpression dayOfYear(Expression value) {
        return new DateExpression("$dayOfYear", value);
    }

    /**
     * Returns the hour for a date as a number between 0 and 23.
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $hour
     */
    public static DateExpression hour(Expression value) {
        return new DateExpression("$hour", value);
    }

    /**
     * Returns the weekday number in ISO 8601 format, ranging from 1 (for Monday) to 7 (for Sunday).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $isoDayOfWeek
     */
    public static IsoDates isoDayOfWeek(Expression value) {
        return new IsoDates("$isoDayOfWeek", value);
    }

    /**
     * Returns the week number in ISO 8601 format, ranging from 1 to 53. Week numbers start at 1 with the week (Monday through Sunday) that
     * contains the year’s first Thursday.
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $isoWeek
     */
    public static IsoDates isoWeek(Expression value) {
        return new IsoDates("$isoWeek", value);
    }

    /**
     * Returns the year number in ISO 8601 format. The year starts with the Monday of week 1 (ISO 8601) and ends with the Sunday of the
     * last
     * week (ISO 8601).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $isoWeekYear
     */
    public static IsoDates isoWeekYear(Expression value) {
        return new IsoDates("$isoWeekYear", value);
    }

    /**
     * Returns the milliseconds of a date as a number between 0 and 999.
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $millisecond
     */
    public static DateExpression milliseconds(Expression value) {
        return new DateExpression("$millisecond", value);
    }

    /**
     * Returns the minute for a date as a number between 0 and 59.
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $minute
     */
    public static DateExpression minute(Expression value) {
        return new DateExpression("$minute", value);
    }

    /**
     * Returns the month for a date as a number between 1 (January) and 12 (December).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $month
     */
    public static DateExpression month(Expression value) {
        return new DateExpression("$month", value);
    }

    /**
     * Returns the seconds for a date as a number between 0 and 60 (leap seconds).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $second
     */
    public static DateExpression second(Expression value) {
        return new DateExpression("$second", value);
    }

    /**
     * Converts a value to a date. If the value cannot be converted to a date, $toDate errors. If the value is null or missing,
     * $toDate returns null.
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $toDate
     */
    public static DateExpression toDate(Expression value) {
        return new DateExpression("$toDate", value);
    }

    /**
     * Returns the week number for a date as a number between 0 (the partial week that precedes the first Sunday of the year) and 53
     * (leap year).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $week
     */
    public static DateExpression week(Expression value) {
        return new DateExpression("$week", value);
    }

    /**
     * Returns the year for a date as a number (e.g. 2014).
     *
     * @param value the expression containing the date value
     * @return the new expression
     * @aggregation.expression $year
     */
    public static DateExpression year(Expression value) {
        return new DateExpression("$year", value);
    }

    /**
     * Base class for the date expressions
     *
     * @mongodb.driver.manual reference/operator/aggregation/#date-expression-operators Date Expressions
     */
    public static class DateExpression extends Expression {
        protected DateExpression(String operation, Expression value) {
            super(operation, value);
        }

        @Override
        public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
            ExpressionHelper.document(writer, () -> ExpressionHelper.expression(mapper, writer, getOperation(), (Expression) getValue(), encoderContext));
        }
    }
}
