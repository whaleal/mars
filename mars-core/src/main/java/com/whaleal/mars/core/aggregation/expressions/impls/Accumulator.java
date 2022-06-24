package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.List;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;

public class Accumulator extends Expression {

    /**
     * @param operation
     * @param values
     */
    public Accumulator(String operation, List<Expression> values) {
        super(operation, new ExpressionList(values));
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        writer.writeName(getOperation());
        ExpressionList values = getValue();
        if (values != null) {
            List<Expression> list = values.getValues();
            if (list.size() > 1) {
                writer.writeStartArray();
            }
            for (Expression expression : list) {
                wrapExpression(mapper, writer, expression, encoderContext);
            }
            if (list.size() > 1) {
                writer.writeEndArray();
            }
        } else {
            writer.writeNull();
        }
    }

    @Override
    public ExpressionList getValue() {
        return (ExpressionList) super.getValue();
    }
}
