package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.PlanCacheStats;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public class PlanCacheStatsCodec extends StageCodec< PlanCacheStats > {
    public PlanCacheStatsCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<PlanCacheStats> getEncoderClass() {
        return PlanCacheStats.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, PlanCacheStats value, EncoderContext encoderContext) {
        document(writer, () -> {
        });
    }
}
