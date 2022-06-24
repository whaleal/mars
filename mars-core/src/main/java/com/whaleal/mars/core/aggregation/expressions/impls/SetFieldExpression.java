package com.whaleal.mars.core.aggregation.expressions.impls;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


public class SetFieldExpression extends Expression {
    private final Expression field;
    private final Object input;
    private final Expression value;

    public SetFieldExpression(Expression field, Object input, Expression value) {
        super("$setField");
        this.field = field;
        this.input = input;
        this.value = value;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "field", field, encoderContext);
            ExpressionHelper.value(mapper, writer, "input", input, encoderContext);
            expression(mapper, writer, "value", value, encoderContext);
        });
    }
}
