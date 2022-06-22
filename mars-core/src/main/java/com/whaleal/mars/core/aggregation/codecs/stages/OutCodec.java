package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Out;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class OutCodec extends StageCodec< Out > {
    public OutCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Out> getEncoderClass() {
        return Out.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void encodeStage(BsonWriter writer, Out value, EncoderContext encoderContext) {
        Class<?> type = value.getType();
        if (type != null) {
            writer.writeString(getMapper().getMapper().getEntityModel(type).getCollectionName());
        } else {
            writer.writeString(value.getCollection());
        }
    }
}
