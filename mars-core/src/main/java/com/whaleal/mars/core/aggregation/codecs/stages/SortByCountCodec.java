package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.SortByCount;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class SortByCountCodec extends StageCodec< SortByCount > {
    public SortByCountCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<SortByCount> getEncoderClass() {
        return SortByCount.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, SortByCount value, EncoderContext encoderContext) {
        value.getExpression().encode(getMapper(), writer, encoderContext);
    }
}
