package com.whaleal.mars.core.query.codec;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.stages.StageCodec;
import com.whaleal.mars.core.query.codec.stage.SortStage;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:30
 */
public class SortCodec extends StageCodec<SortStage> {

    public SortCodec(MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    protected void encodeStage(BsonWriter writer, SortStage value, EncoderContext encoderContext) {
        writer.writeStartArray();
        //todo
        writer.writeEndArray();
    }

    @Override
    public Class<SortStage> getEncoderClass() {
        return SortStage.class;
    }
}
