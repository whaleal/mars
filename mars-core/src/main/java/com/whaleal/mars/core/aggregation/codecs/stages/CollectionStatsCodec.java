package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.CollectionStats;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public class CollectionStatsCodec extends StageCodec< CollectionStats > {
    public CollectionStatsCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<CollectionStats> getEncoderClass() {
        return CollectionStats.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, CollectionStats value, EncoderContext encoderContext) {
        document(writer, () -> {
            if (value.getHistogram()) {
                document(writer, "latencyStats", () -> writer.writeBoolean("histograms", true));
            }
            if (value.getScale() != null) {
                document(writer, "storageStats", () -> writer.writeInt32("scale", value.getScale()));
            }
            if (value.getCount()) {
                document(writer, "count", () -> {
                });
            }
        });
    }
}
