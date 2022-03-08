package com.whaleal.mars.core.query.codec;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.stages.StageCodec;
import com.whaleal.mars.core.query.codec.stage.EachStage;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.value;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:27
 */
public class EachCodec extends StageCodec<EachStage> {

    public EachCodec(MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    protected void encodeStage(BsonWriter writer, EachStage value, EncoderContext encoderContext) {
        value(getMapper(),writer,value.getValue(),encoderContext);

    }

    @Override
    public Class<EachStage> getEncoderClass() {
        return EachStage.class;
    }
}
