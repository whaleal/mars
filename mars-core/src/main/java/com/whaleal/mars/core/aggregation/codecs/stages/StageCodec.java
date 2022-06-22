package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Stage;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public abstract class StageCodec<T extends Stage > implements Codec<T> {
    private final MongoMappingContext mapper;

    protected StageCodec(MongoMappingContext mapper) {
        this.mapper = mapper;
    }

    @Override
    public final T decode(BsonReader reader, DecoderContext decoderContext) {
        throw new UnsupportedOperationException("encodingOnly");
    }

    @Override
    public final void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        document(writer, () -> {
            writer.writeName(value.stageName());
            encodeStage(writer, value, encoderContext);
        });
    }

    protected abstract void encodeStage(BsonWriter writer, T value, EncoderContext encoderContext);

    public MongoMappingContext getMapper() {
        return mapper;
    }

    protected CodecRegistry getCodecRegistry() {
        return mapper.getCodecRegistry();
    }
}
