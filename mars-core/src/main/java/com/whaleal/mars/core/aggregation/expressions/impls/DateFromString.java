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

public class DateFromString extends Expression {
    private Expression dateString;
    private Expression format;
    private Expression timeZone;
    private Expression onError;
    private Expression onNull;

    public DateFromString() {
        super("$dateFromString");
    }

    public DateFromString dateString(String dateString) {
        return dateString(Expressions.value(dateString));
    }

    public DateFromString dateString(Expression dateString) {
        this.dateString = dateString;
        return this;
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        ExpressionHelper.document(writer, () -> {
            ExpressionHelper.document(writer, getOperation(), () -> {
                ExpressionHelper.expression(mapper, writer, "dateString", dateString, encoderContext);
                ExpressionHelper.expression(mapper, writer, "format", format, encoderContext);
                ExpressionHelper.expression(mapper, writer, "timezone", timeZone, encoderContext);
                ExpressionHelper.expression(mapper, writer, "onError", onError, encoderContext);
                ExpressionHelper.expression(mapper, writer, "onNull", onNull, encoderContext);
            });
        });
    }

    public DateFromString format(Expression format) {
        this.format = format;
        return this;
    }

    public DateFromString format(String format) {
        return format(Expressions.value(format));
    }

    public DateFromString onError(String onError) {
        return onError(Expressions.value(onError));
    }

    public DateFromString onError(Expression onError) {
        this.onError = onError;
        return this;
    }

    public DateFromString onNull(String onNull) {
        return onNull(Expressions.value(onNull));
    }

    public DateFromString onNull(Expression onNull) {
        this.onNull = onNull;
        return this;
    }

    public DateFromString timeZone(String timeZone) {
        return timeZone(Expressions.value(timeZone));
    }

    public DateFromString timeZone(Expression timeZone) {
        this.timeZone = timeZone;
        return this;
    }
}
