package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Set;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class SetStageCodec extends StageCodec< Set > {
    public SetStageCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Set> getEncoderClass() {
        return Set.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Set value, EncoderContext encoderContext) {
        value.getDocument().encode(getMapper(), writer, encoderContext);
    }
}
