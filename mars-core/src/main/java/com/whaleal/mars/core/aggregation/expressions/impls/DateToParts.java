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

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * Returns a document that contains the constituent parts of a given BSON Date value as individual properties. The properties returned
 * are year, month, day, hour, minute, second and millisecond.
 * 返回包含给定 BSON 日期值的组成部分作为单独属性的文档。 返回的属性
 * 是年、月、日、小时、分钟、秒和毫秒。
 */
public class DateToParts extends Expression {
    private final Expression date;
    private Expression timeZone;

    private Boolean iso8601;

    public DateToParts(Expression date) {
        super("$dateToParts");
        this.date = date;
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        ExpressionHelper.document(writer, () -> {
            ExpressionHelper.document(writer, getOperation(), () -> {
                ExpressionHelper.expression(mapper, writer, "date", date, encoderContext);
                ExpressionHelper.expression(mapper, writer, "timezone", timeZone, encoderContext);
                ExpressionHelper.value(mapper, writer, "iso8601", iso8601, encoderContext);
            });
        });
    }

    public DateToParts iso8601(boolean iso8601) {
        this.iso8601 = iso8601;
        return this;
    }

    public DateToParts timezone(Expression timezone) {
        this.timeZone = timezone;
        return this;
    }
}
