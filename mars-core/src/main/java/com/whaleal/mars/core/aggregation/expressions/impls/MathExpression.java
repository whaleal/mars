package com.whaleal.mars.core.aggregation.expressions.impls;


import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.List;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;


/**
 * Base class for the math expressions
 */
public class MathExpression extends Expression {

    /**
     * @param operation
     * @param operands
     */
    public MathExpression(String operation, List<Expression> operands) {
        super(operation, new ExpressionList(operands));
    }

    /**
     * @param operation
     * @param operand
     */
    public MathExpression(String operation, Expression operand) {
        super(operation, new ExpressionList(operand));
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        ExpressionList value = getValue();
        if (value != null) {
            final List<Expression> operands = value.getValues();
            writer.writeName(getOperation());
            if (operands.size() > 1) {
                writer.writeStartArray();
            }
            for (Expression operand : operands) {
                if (operand != null) {
                    wrapExpression(mapper, writer, operand, encoderContext);
                } else {
                    writer.writeNull();
                }
            }
            if (operands.size() > 1) {
                writer.writeEndArray();
            }
        }
    }

    @Override
    public ExpressionList getValue() {
        return (ExpressionList) super.getValue();
    }
}
