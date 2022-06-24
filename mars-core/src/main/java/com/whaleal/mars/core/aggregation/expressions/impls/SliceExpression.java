package com.whaleal.mars.core.aggregation.expressions.impls;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


public class SliceExpression extends Expression {
    private final Expression array;
    private final int size;
    private Integer position;

    public SliceExpression(Expression array, int size) {
        super("$slice");
        this.array = array;
        this.size = size;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        ExpressionHelper.array(writer, getOperation(), () -> {
            expression(mapper, writer, array, encoderContext);
            if (position != null) {
                writer.writeInt32(position);
            }
            writer.writeInt32(size);
        });
    }

    public SliceExpression position(Integer position) {
        this.position = position;
        return this;
    }
}
