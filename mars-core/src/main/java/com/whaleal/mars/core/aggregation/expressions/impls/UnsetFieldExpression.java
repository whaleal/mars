package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class UnsetFieldExpression extends Expression {
    private final Expression field;
    private final Object input;

    public UnsetFieldExpression(Expression field, Object input) {
        super("$unsetField");
        this.field = field;
        this.input = input;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "field", field, encoderContext);
            value(mapper, writer, "input", input, encoderContext);
        });
    }
}
