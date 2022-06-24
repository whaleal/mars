package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Redact;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;


public class RedactCodec extends StageCodec<Redact> {
    public RedactCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Redact> getEncoderClass() {
        return Redact.class;
    }

    @Override
    protected void encodeStage( BsonWriter writer, Redact value, EncoderContext encoderContext) {
        wrapExpression(getMapper(), writer, value.getExpression(), encoderContext);
    }
}
