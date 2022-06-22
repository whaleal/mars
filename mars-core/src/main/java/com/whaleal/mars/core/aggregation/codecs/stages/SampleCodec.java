package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Sample;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public class SampleCodec extends StageCodec< Sample > {
    public SampleCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Sample> getEncoderClass() {
        return Sample.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Sample value, EncoderContext encoderContext) {
        document(writer, () -> writer.writeInt64("size", value.getSize()));
    }
}
