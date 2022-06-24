package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.Unset;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.List;

public class UnsetCodec extends StageCodec< Unset > {
    public UnsetCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Unset> getEncoderClass() {
        return Unset.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Unset value, EncoderContext encoderContext) {
        List< Expression > fields = value.getFields();
        if (fields.size() == 1) {
            fields.get(0).encode(getMapper(), writer, encoderContext);
        } else if (fields.size() > 1) {
            Codec codec = getCodecRegistry().get(fields.getClass());
            encoderContext.encodeWithChildContext(codec, writer, fields);
        }
    }
}
