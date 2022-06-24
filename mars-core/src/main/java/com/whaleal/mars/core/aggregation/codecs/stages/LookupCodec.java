package com.whaleal.mars.core.aggregation.codecs.stages;





import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.aggregation.stages.Lookup;
import com.whaleal.mars.core.aggregation.stages.Stage;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.List;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.array;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public class LookupCodec extends StageCodec< Lookup > {

    public LookupCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Lookup> getEncoderClass() {
        return Lookup.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void encodeStage(BsonWriter writer, Lookup value, EncoderContext encoderContext) {
        document(writer, () -> {
            if (value.getFrom() != null) {
                writer.writeString("from", value.getFrom());
            } else {
                writer.writeString("from", getMapper().getMapper().getEntityModel(value.getFromType()).getCollectionName());
            }

            List<Stage> pipeline = value.getPipeline();
            if (pipeline == null) {
                writer.writeString("localField", value.getLocalField());
                writer.writeString("foreignField", value.getForeignField());
            } else {
                ExpressionHelper.expression(getMapper(), writer, "let", value.getVariables(), encoderContext);
                array(writer, "pipeline", () -> {
                    for (Stage stage : pipeline) {
                        Codec< Stage > codec = (Codec<Stage>) getCodecRegistry().get(stage.getClass());
                        codec.encode(writer, stage, encoderContext);
                    }
                });
            }
            writer.writeString("as", value.getAs());
        });
    }
}
