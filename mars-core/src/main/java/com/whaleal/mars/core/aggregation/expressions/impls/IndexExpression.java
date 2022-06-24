package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


/**
 * Defines the $indexOfBytes expression
 */
public class IndexExpression extends Expression {
    private final Expression string;
    private final Expression substring;
    private Integer end;
    private Integer start;

    /**
     * Creates the new expression
     *
     * @param operation the index operation name
     * @param string    the string to search
     * @param substring the target string
     */
    public IndexExpression(String operation, Expression string, Expression substring) {
        super(operation);
        this.string = string;
        this.substring = substring;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        array(writer, getOperation(), () -> {
            expression(mapper, writer, string, encoderContext);
            expression(mapper, writer, substring, encoderContext);
            value(mapper, writer, start, encoderContext);
            value(mapper, writer, end, encoderContext);
        });
    }

    /**
     * Sets the end boundary for searching
     *
     * @param end the end
     * @return this
     */
    public IndexExpression end(int end) {
        this.end = end;
        return this;
    }

    /**
     * Sets the start boundary for searching
     *
     * @param start the start
     * @return this
     */
    public IndexExpression start(int start) {
        this.start = start;
        return this;
    }
}
