package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Count;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class CountCodec extends StageCodec< Count > {
    public CountCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Count> getEncoderClass() {
        return Count.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Count value, EncoderContext encoderContext) {
        writer.writeString(value.getName());
    }
}
