package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.PipelineField;
import com.whaleal.mars.core.aggregation.stages.Projection;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;


public class ProjectionCodec extends StageCodec< Projection > {

    public ProjectionCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Projection> getEncoderClass() {
        return Projection.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Projection projection, EncoderContext encoderContext) {
        document(writer, () -> {
            for (PipelineField field : projection.getFields()) {
                wrapExpression(getMapper(), writer, field.getName(), field.getValue(), encoderContext);
            }
        });
    }
}
