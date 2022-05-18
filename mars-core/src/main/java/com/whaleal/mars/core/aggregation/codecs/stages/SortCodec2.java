package com.whaleal.mars.core.aggregation.codecs.stages;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;

import com.whaleal.mars.core.query.Sort;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * @author wh
 */
public class SortCodec2 extends StageCodec< Sort > {
    public SortCodec2( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Sort> getEncoderClass() {
        return Sort.class;
    }

    @Override
    protected void encodeStage( BsonWriter writer, Sort value, EncoderContext encoderContext) {
        ExpressionHelper.document(writer, () -> {
            for (Sort.SortType sort : value.getSorts()) {
                writer.writeName(sort.getField());
                sort.getDirection().encode(writer);
            }
        });
    }
}
