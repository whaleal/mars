package com.whaleal.mars.core.aggregation.codecs;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class ExpressionCodec<T extends Expression > implements Codec<T> {
    private final MongoMappingContext mapper;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ExpressionCodec( MongoMappingContext mapper) {
        this.mapper = mapper;
    }

    @Override
    public final T decode(BsonReader reader, DecoderContext decoderContext) {
        throw new UnsupportedOperationException("encodingOnly");
    }

    @Override
    public void encode(BsonWriter writer, T expression, EncoderContext encoderContext) {
        if (expression != null) {
            expression.encode(mapper, writer, encoderContext);
        } else {
            writer.writeNull();
        }
    }

    @Override
    public final Class<T> getEncoderClass() {
        return (Class<T>) Expression.class;
    }

    protected CodecRegistry getCodecRegistry() {
        return mapper.getCodecRegistry();
    }

    protected MongoMappingContext getMapper() {
        return mapper;
    }
}
