package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;

import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.ReplaceWith;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;


import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;



public class ReplaceWithCodec extends StageCodec< ReplaceWith > {
    public ReplaceWithCodec(MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<ReplaceWith> getEncoderClass() {
        return ReplaceWith.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, ReplaceWith replace, EncoderContext encoderContext) {
        Expression value = replace.getValue();
        if (value != null) {
            wrapExpression(getMapper(), writer, value, encoderContext);
        } else {
            replace.getDocument().encode(getMapper(), writer, encoderContext);
        }
    }
}
