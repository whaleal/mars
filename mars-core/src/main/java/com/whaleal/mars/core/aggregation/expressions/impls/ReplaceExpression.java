package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;


/**
 * Defines expressions for $replaceAll and $replaceOne
 */
public class ReplaceExpression extends Expression {
    private final Expression find;
    private final Expression replacement;
    private final Expression input;

    /**
     * @param operator    the operator name
     * @param input       the input value/source
     * @param find        the search expression
     * @param replacement the replacement value
     */
    public ReplaceExpression(String operator,
                             Expression input,
                             Expression find,
                             Expression replacement) {
        super(operator);
        this.input = input;
        this.find = find;
        this.replacement = replacement;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            wrapExpression(mapper, writer, "input", input, encoderContext);
            wrapExpression(mapper, writer, "find", find, encoderContext);
            wrapExpression(mapper, writer, "replacement", replacement, encoderContext);
        });
    }
}
