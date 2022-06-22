package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.AddFields;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class AddFieldsCodec extends StageCodec< AddFields > {
    public AddFieldsCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<AddFields> getEncoderClass() {
        return AddFields.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, AddFields value, EncoderContext encoderContext) {
        value.getDocument().encode(getMapper(), writer, encoderContext);
    }
}
