package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.domain.SortType;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public class SortCodec extends StageCodec< Sort > {
    public SortCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Sort> getEncoderClass() {
        return Sort.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Sort value, EncoderContext encoderContext) {
        document(writer, () -> {
            for (SortType sort : value.getSorts()) {
                writer.writeName(sort.getField());
                sort.getDirection().encode(writer);
            }
        });
    }
}
