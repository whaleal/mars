package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public class DocumentExpression extends Expression implements SingleValuedExpression, FieldHolder<DocumentExpression> {
    private final Fields<DocumentExpression> fields = Fields.on(this);

    public DocumentExpression() {
        super("unused");
    }

    public void encode(String name, MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, name, () -> fields.encode(mapper, writer, encoderContext));
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, () -> fields.encode(mapper, writer, encoderContext));
    }

    @Override
    public DocumentExpression field(String name, Expression expression) {
        return fields.add(name, expression);
    }
}
