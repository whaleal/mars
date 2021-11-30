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

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates a series of case expressions. When it finds an expression which evaluates to true, $switch executes a specified expression
 * and breaks out of the control flow.
 * 评估一系列 case 表达式。 当它找到计算结果为真的表达式时，$switch 执行指定的表达式并跳出控制流。
 */
public class SwitchExpression extends Expression {
    private final List<Pair> branches = new ArrayList<>();
    private Expression defaultCase;


    public SwitchExpression() {
        super("$switch");
    }


    public SwitchExpression branch(Expression caseExpression, Expression then) {
        branches.add(new Pair(caseExpression, then));
        return this;
    }


    public SwitchExpression defaultCase(Expression caseExpression) {
        this.defaultCase = caseExpression;
        return this;
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        ExpressionHelper.document(writer, () -> {
            ExpressionHelper.document(writer, getOperation(), () -> {
                ExpressionHelper.array(writer, "branches", () -> {
                    for (Pair branch : branches) {
                        ExpressionHelper.document(writer, () -> {
                            ExpressionHelper.expression(mapper, writer, "case", branch.caseExpression, encoderContext);
                            ExpressionHelper.expression(mapper, writer, "then", branch.then, encoderContext);
                        });
                    }
                });
                ExpressionHelper.expression(mapper, writer, "default", defaultCase, encoderContext);
            });
        });
    }

    private static class Pair {
        private final Expression caseExpression;
        private final Expression then;

        public Pair(Expression caseExpression, Expression then) {
            this.caseExpression = caseExpression;
            this.then = then;
        }
    }
}
