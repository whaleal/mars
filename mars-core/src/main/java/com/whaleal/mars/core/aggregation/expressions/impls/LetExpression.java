package com.whaleal.mars.core.aggregation.expressions.impls;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class LetExpression extends Expression {
    private final Expression in;
    private final DocumentExpression variables = Expressions.of();

    public LetExpression(Expression in) {
        super("$let");
        this.in = in;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "vars", variables, encoderContext);
            wrapExpression(mapper, writer, "in", in, encoderContext);
        });
    }

    /**
     * Defines a new variable
     *
     * @param name       the variable name
     * @param expression the value expression
     * @return this
     */
    public LetExpression variable(String name, Expression expression) {
        variables.field(name, expression);
        return this;
    }
}
