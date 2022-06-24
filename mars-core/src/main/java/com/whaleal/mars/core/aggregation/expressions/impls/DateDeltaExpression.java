package com.whaleal.mars.core.aggregation.expressions.impls;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.TimeUnit;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.Locale;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


/**
 * Changes a Date object by a specified number of time units.
 */
public class DateDeltaExpression extends Expression {
    private final Expression startDate;
    private final long amount;
    private final TimeUnit unit;
    private Expression timezone;

    /**
     * @param operator
     * @param startDate
     * @param amount
     * @param unit
     */
    public DateDeltaExpression(String operator, Expression startDate, long amount, TimeUnit unit) {
        super(operator);
        this.startDate = startDate;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "startDate", startDate, encoderContext);
            writer.writeString("unit", unit.name().toLowerCase(Locale.ROOT));
            writer.writeInt64("amount", amount);
            expression(mapper, writer, "timezone", timezone, encoderContext);
        });
    }

    /**
     * The timezone to carry out the operation. <tzExpression> must be a valid expression that resolves to a string formatted as either
     * an Olson Timezone Identifier or a UTC Offset. If no timezone is provided, the result is displayed in UTC.
     *
     * @param timezone the timezone expression
     * @return this
     */
    public DateDeltaExpression timezone(Expression timezone) {
        this.timezone = timezone;
        return this;
    }
}
