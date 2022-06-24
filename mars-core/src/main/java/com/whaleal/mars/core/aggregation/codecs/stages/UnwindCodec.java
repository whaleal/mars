package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Unwind;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class UnwindCodec extends StageCodec< Unwind > {
    public UnwindCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Unwind> getEncoderClass() {
        return Unwind.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Unwind value, EncoderContext encoderContext) {
        if (!value.optionsPresent()) {
            value.getPath().encode(getMapper(), writer, encoderContext);
        } else {
            document(writer, () -> {
                expression(getMapper(), writer, "path", value.getPath(), encoderContext);
                value(writer, "includeArrayIndex", value.getIncludeArrayIndex());
                value(writer, "preserveNullAndEmptyArrays", value.getPreserveNullAndEmptyArrays());
            });
        }
    }
}
