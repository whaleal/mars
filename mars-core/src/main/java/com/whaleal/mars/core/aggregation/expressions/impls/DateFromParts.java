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
package com.whaleal.mars.core.aggregation.expressions.impls;

import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class DateFromParts extends Expression {
    private Expression year;
    private Expression month;
    private Expression day;
    private Expression hour;
    private Expression minute;
    private Expression second;
    private Expression millisecond;

    private Expression isoWeekYear;
    private Expression isoWeek;
    private Expression isoDayOfWeek;

    private Expression timezone;

    public DateFromParts() {
        super("$dateFromParts");
    }


    public DateFromParts day(int value) {
        return day(Expressions.value(value));
    }

    public DateFromParts day(Expression value) {
        this.day = value;
        return this;
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        ExpressionHelper.document(writer, () -> {
            ExpressionHelper.document(writer, getOperation(), () -> {
                ExpressionHelper.expression(mapper, writer, "year", year, encoderContext);
                ExpressionHelper.expression(mapper, writer, "month", month, encoderContext);
                ExpressionHelper.expression(mapper, writer, "day", day, encoderContext);
                ExpressionHelper.expression(mapper, writer, "hour", hour, encoderContext);
                ExpressionHelper.expression(mapper, writer, "minute", minute, encoderContext);
                ExpressionHelper.expression(mapper, writer, "second", second, encoderContext);
                ExpressionHelper.expression(mapper, writer, "millisecond", millisecond, encoderContext);
                ExpressionHelper.expression(mapper, writer, "isoWeekYear", isoWeekYear, encoderContext);
                ExpressionHelper.expression(mapper, writer, "isoWeek", isoWeek, encoderContext);
                ExpressionHelper.expression(mapper, writer, "isoDayOfWeek", isoDayOfWeek, encoderContext);
                ExpressionHelper.expression(mapper, writer, "timezone", timezone, encoderContext);
            });
        });
    }


    public DateFromParts hour(int value) {
        return hour(Expressions.value(value));
    }


    public DateFromParts hour(Expression value) {
        this.hour = value;
        return this;
    }


    public DateFromParts isoDayOfWeek(int value) {
        return isoDayOfWeek(Expressions.value(value));
    }


    public DateFromParts isoDayOfWeek(Expression value) {
        this.isoDayOfWeek = value;
        return this;
    }


    public DateFromParts isoWeek(int value) {
        return isoWeek(Expressions.value(value));
    }


    public DateFromParts isoWeek(Expression value) {
        this.isoWeek = value;
        return this;
    }


    public DateFromParts isoWeekYear(int value) {
        return isoWeekYear(Expressions.value(value));
    }


    public DateFromParts isoWeekYear(Expression value) {
        this.isoWeekYear = value;
        return this;
    }


    public DateFromParts millisecond(int value) {
        return millisecond(Expressions.value(value));
    }


    public DateFromParts millisecond(Expression value) {
        this.millisecond = value;
        return this;
    }


    public DateFromParts minute(int value) {
        return minute(Expressions.value(value));
    }


    public DateFromParts minute(Expression value) {
        this.minute = value;
        return this;
    }


    public DateFromParts month(int value) {
        return month(Expressions.value(value));
    }

    public DateFromParts month(Expression value) {
        this.month = value;
        return this;
    }


    public DateFromParts second(int value) {
        return second(Expressions.value(value));
    }


    public DateFromParts second(Expression value) {
        this.second = value;
        return this;
    }

    public DateFromParts timezone(Expression value) {
        this.timezone = value;
        return this;
    }


    public DateFromParts timezone(String value) {
        return timezone(Expressions.value(value));
    }


    public DateFromParts year(int value) {
        return year(Expressions.value(value));
    }


    public DateFromParts year(Expression value) {
        this.year = value;
        return this;
    }
}
