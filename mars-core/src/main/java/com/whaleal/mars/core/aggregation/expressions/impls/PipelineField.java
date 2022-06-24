package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static java.lang.String.format;

public class PipelineField {
    private final String name;
    private final Expression value;

    public PipelineField(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        writer.writeName(name);
        value.encode(mapper, writer, encoderContext);
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public String toString() {
        return format("PipelineField{name='%s', value=%s}", name, value);
    }
}
