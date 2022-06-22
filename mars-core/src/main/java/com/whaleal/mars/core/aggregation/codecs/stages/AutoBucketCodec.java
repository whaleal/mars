package com.whaleal.mars.core.aggregation.codecs.stages;





import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.aggregation.expressions.impls.DocumentExpression;
import com.whaleal.mars.core.aggregation.stages.AutoBucket;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;


import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class AutoBucketCodec extends StageCodec< AutoBucket > {
    public AutoBucketCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class getEncoderClass() {
        return AutoBucket.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, AutoBucket value, EncoderContext encoderContext) {
        document(writer, () -> {
            expression(getMapper(), writer, "groupBy", value.getGroupBy(), encoderContext);
            ExpressionHelper.expression(getMapper(), writer, "buckets", value.getBuckets(), encoderContext);
            ExpressionHelper.expression(getMapper(), writer, "granularity", value.getGranularity(), encoderContext);
            DocumentExpression output = value.getOutput();
            if (output != null) {
                output.encode("output", getMapper(), writer, encoderContext);
            }
        });
    }
}
