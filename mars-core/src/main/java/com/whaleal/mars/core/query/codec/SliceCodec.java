package com.whaleal.mars.core.query.codec;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.stages.StageCodec;
import com.whaleal.mars.core.query.codec.stage.SliceStage;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:23
 */
public class SliceCodec extends StageCodec<SliceStage> {

    public SliceCodec(MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    protected void encodeStage(BsonWriter writer, SliceStage value, EncoderContext encoderContext) {
        writer.writeInt32(value.getValue());
    }

    @Override
    public Class<SliceStage> getEncoderClass() {
        return SliceStage.class;
    }
}
