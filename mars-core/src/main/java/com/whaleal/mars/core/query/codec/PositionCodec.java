package com.whaleal.mars.core.query.codec;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.stages.StageCodec;
import com.whaleal.mars.core.query.codec.stage.PositionStage;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:28
 */
public class PositionCodec extends StageCodec<PositionStage> {

    public PositionCodec(MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public void encodeStage(BsonWriter writer, PositionStage value, EncoderContext encoderContext) {
        writer.writeInt32(value.getValue());
    }

    @Override
    public Class<PositionStage> getEncoderClass() {
        return PositionStage.class;
    }
}
