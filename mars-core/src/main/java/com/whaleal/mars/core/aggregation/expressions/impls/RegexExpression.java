package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonRegularExpression;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.regex.Pattern;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class RegexExpression extends Expression {
    private final Expression input;
    private String regex;
    private String options;

    public RegexExpression(String operation, Expression input) {
        super(operation);
        this.input = input;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            expression(mapper, writer, "input", input, encoderContext);
            value(mapper, writer, "regex", new BsonRegularExpression(regex), encoderContext);
            value(writer, "options", options);
        });
    }

    /**
     * Optional options to apply to the regex
     *
     * @param options the options
     * @return this
     */
    public RegexExpression options(String options) {
        this.options = options;
        return this;
    }

    /**
     * The regular expression
     *
     * @param pattern the regular expression
     * @return this
     */
    public RegexExpression pattern(String pattern) {
        this.regex = pattern;
        return this;
    }

    /**
     * The regular expression
     *
     * @param pattern the regular expression
     * @return this
     */
    public RegexExpression pattern(Pattern pattern) {
        this.regex = pattern.pattern();
        return this;
    }
}
