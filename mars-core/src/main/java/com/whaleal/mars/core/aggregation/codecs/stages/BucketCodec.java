package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.DocumentExpression;
import com.whaleal.mars.core.aggregation.stages.Bucket;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class BucketCodec extends StageCodec< Bucket > {
    public BucketCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class getEncoderClass() {
        return Bucket.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Bucket value, EncoderContext encoderContext) {
        document(writer, () -> {
            expression(getMapper(), writer, "groupBy", value.getGroupBy(), encoderContext);
            expression(getMapper(), writer, "boundaries", value.getBoundaries(), encoderContext);
            value(getMapper(), writer, "default", value.getDefaultValue(), encoderContext);
            DocumentExpression output = value.getOutput();
            if (output != null) {
                output.encode("output", getMapper(), writer, encoderContext);
            }
        });
    }
}
