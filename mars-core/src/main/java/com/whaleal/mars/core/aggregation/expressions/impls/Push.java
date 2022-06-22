package com.whaleal.mars.core.aggregation.expressions.impls;





import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.AggregationException;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

public class Push extends Expression implements FieldHolder<Push> {
    private Expression field;
    private DocumentExpression document;

    public Push() {
        super("$push");
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        writer.writeName(getOperation());
        if (field != null) {
            field.encode(mapper, writer, encoderContext);
        } else if (document != null) {
            document.encode(mapper, writer, encoderContext);
        }
    }

    @Override
    public Push field(String name, Expression expression) {
        if (field != null) {
            throw new AggregationException("mixedModesNotAllowed "+ (getOperation()));
        }
        if (document == null) {
            document = Expressions.of();
        }
        document.field(name, expression);

        return this;
    }

    public Push single(Expression source) {
        if (document != null) {
            throw new AggregationException("mixedModesNotAllowed " +(getOperation()));
        }
        this.field = source;
        return this;
    }
}
