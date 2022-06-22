package com.whaleal.mars.core.aggregation.expressions.impls;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


public class DateFromString extends Expression {
    private Expression dateString;
    private Expression format;
    private Expression timeZone;
    private Expression onError;
    private Expression onNull;

    public DateFromString() {
        super("$dateFromString");
    }

    public DateFromString dateString(String dateString) {
        return dateString(Expressions.value(dateString));
    }

    public DateFromString dateString(Expression dateString) {
        this.dateString = dateString;
        return this;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "dateString", dateString, encoderContext);
            expression(mapper, writer, "format", format, encoderContext);
            expression(mapper, writer, "timezone", timeZone, encoderContext);
            expression(mapper, writer, "onError", onError, encoderContext);
            expression(mapper, writer, "onNull", onNull, encoderContext);
        });
    }

    public DateFromString format(Expression format) {
        this.format = format;
        return this;
    }

    public DateFromString format(String format) {
        return format(Expressions.value(format));
    }

    public DateFromString onError(String onError) {
        return onError(Expressions.value(onError));
    }

    public DateFromString onError(Expression onError) {
        this.onError = onError;
        return this;
    }

    public DateFromString onNull(String onNull) {
        return onNull(Expressions.value(onNull));
    }

    public DateFromString onNull(Expression onNull) {
        this.onNull = onNull;
        return this;
    }

    public DateFromString timeZone(String timeZone) {
        return timeZone(Expressions.value(timeZone));
    }

    public DateFromString timeZone(Expression timeZone) {
        this.timeZone = timeZone;
        return this;
    }
}
