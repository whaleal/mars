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
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.query.filters.Filter;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * Defines miscellaneous operators for aggregations.
 */
public final class Miscellaneous {
    private Miscellaneous() {
    }

    /**
     * Returns a random float between 0 and 1.
     *
     * @return the filter
     * @aggregation.expression $rand
     */
    public static Expression rand() {
        return new Expression("$rand") {
            @Override
            public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
                ExpressionHelper.document(writer, () -> {
                    ExpressionHelper.document(writer, getOperation(), () -> {
                    });
                });
            }
        };
    }

    /**
     * Matches a random selection of input documents. The number of documents selected approximates the sample rate expressed as a
     * percentage of the total number of documents.
     *
     * @param rate the rate to check against
     * @return the filter
     * @aggregation.expression $sampleRate
     */
    public static Filter sampleRate(double rate) {
        return new Filter("$sampleRate", null, rate) {
            @Override
            public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
                writeNamedValue(getName(), getValue(), mapper, writer, context);
            }
        };
    }
}
