package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Skip;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class SkipCodec extends StageCodec< Skip > {
    public SkipCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Skip> getEncoderClass() {
        return Skip.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Skip value, EncoderContext encoderContext) {
        writer.writeInt64(value.getSize());
    }
}
