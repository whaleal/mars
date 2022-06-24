package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static java.util.Arrays.asList;

public class ArrayLiteral extends ArrayExpression {

    public ArrayLiteral(Expression... values) {
        super("unused", asList(values));
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        Expression value = getValue();
        if (value != null) {
            value.encode(mapper, writer, encoderContext);
        }
    }
}
