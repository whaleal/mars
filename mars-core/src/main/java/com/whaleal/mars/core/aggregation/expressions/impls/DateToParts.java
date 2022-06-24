package com.whaleal.mars.core.aggregation.expressions.impls;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


/**
 * Returns a document that contains the constituent parts of a given BSON Date value as individual properties. The properties returned
 * are year, month, day, hour, minute, second and millisecond.
 */
public class DateToParts extends Expression {
    private final Expression date;
    private Expression timeZone;

    private ValueExpression iso8601;

    public DateToParts(Expression date) {
        super("$dateToParts");
        this.date = date;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "date", date, encoderContext);
            expression(mapper, writer, "timezone", timeZone, encoderContext);
            ExpressionHelper.expression(mapper, writer, "iso8601", iso8601, encoderContext);
        });
    }

    /**
     * Optional. If set to true, modifies the output document to use ISO week date fields.
     * Defaults to false.
     *
     * @param iso8601 true to use ISO 8601
     * @return this
     */
    public DateToParts iso8601(boolean iso8601) {
        this.iso8601 = new ValueExpression(iso8601);
        return this;
    }

    /**
     * The optional timezone to use to format the date. By default, it uses UTC.
     *
     * @param timezone the expression
     * @return this
     */
    public DateToParts timezone(Expression timezone) {
        this.timeZone = timezone;
        return this;
    }
}
