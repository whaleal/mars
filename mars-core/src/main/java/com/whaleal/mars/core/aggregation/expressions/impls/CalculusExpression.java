package com.whaleal.mars.core.aggregation.expressions.impls;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.TimeUnit;
import com.whaleal.mars.core.aggregation.expressions.WindowExpressions;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.Locale;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


public class CalculusExpression extends Expression {
    private final Expression input;
    private TimeUnit unit;

    public CalculusExpression(String operation, Expression input) {
        super(operation);
        this.input = input;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "input", input, encoderContext);
            if (unit != null) {
                writer.writeString("unit", unit.name().toLowerCase(Locale.ROOT));
            }
        });
    }

    /**
     * Sets the time unit for the expression
     *
     * @param unit the unit
     * @return this
     */
    public CalculusExpression unit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }
}
