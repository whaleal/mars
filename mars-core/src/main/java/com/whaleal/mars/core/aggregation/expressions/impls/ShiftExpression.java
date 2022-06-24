package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


public class ShiftExpression extends Expression {
    private final Expression output;
    private final long by;
    private final Expression defaultValue;

    public ShiftExpression(Expression output, long by, Expression defaultValue) {
        super("$shift");
        this.output = output;
        this.by = by;
        this.defaultValue = defaultValue;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "output", output, encoderContext);
            writer.writeInt64("by", by);
            expression(mapper, writer, "default", defaultValue, encoderContext);
        });
    }
}
