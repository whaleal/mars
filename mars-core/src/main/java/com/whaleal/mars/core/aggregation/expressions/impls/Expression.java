package com.whaleal.mars.core.aggregation.expressions.impls;

import com.mongodb.lang.Nullable;


import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.StringJoiner;

/**
 * Base class for all the expression types.
 */
public class Expression {
    private final String operation;
    private final Expression value;

    public Expression(String operation) {
        this.operation = operation;
        this.value = null;
    }

    /**
     * @param operation the expression name
     * @param value     the value
     */
    public Expression(String operation, Expression value) {
        this.operation = operation;
        this.value = value;
    }

    /**
     * @param operation the expression name
     * @param value     the value
     */
    public Expression(String operation, List<Expression> value) {
        this.operation = operation;
        this.value = new ExpressionList(value);
    }

    /**
     * @param mapper      the mapper
     * @param writer         the writer
     * @param encoderContext the context
     */

    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        ExpressionHelper.expression(mapper, writer, operation, value, encoderContext);
    }

    /**
     * @return the value
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @return the value
     */
    @Nullable
    public Expression getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Expression.class.getSimpleName() + "[", "]")
                   .add("operation='" + operation + "'")
                   .toString();
    }
}
