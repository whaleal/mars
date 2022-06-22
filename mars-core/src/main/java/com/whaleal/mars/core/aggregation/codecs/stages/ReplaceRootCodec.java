package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.ReplaceRoot;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;


public class ReplaceRootCodec extends StageCodec< ReplaceRoot > {
    public ReplaceRootCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<ReplaceRoot> getEncoderClass() {
        return ReplaceRoot.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, ReplaceRoot replace, EncoderContext encoderContext) {
        document(writer, () -> {
            writer.writeName("newRoot");
            if (replace.getValue() != null) {
                wrapExpression(getMapper(), writer, replace.getValue(), encoderContext);
            } else {
                replace.getDocument().encode(getMapper(), writer, encoderContext);
            }
        });
    }
}
