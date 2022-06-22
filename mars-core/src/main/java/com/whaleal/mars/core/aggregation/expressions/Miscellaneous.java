package com.whaleal.mars.core.aggregation.expressions;





import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.*;


import com.whaleal.mars.core.query.filters.Filter;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * Defines miscellaneous operators for aggregations.
 *
 */
public final class Miscellaneous {
    private Miscellaneous() {
    }

    /**
     * Returns the value of a specified field from a document. If you don't specify an object, $getField returns the value of the field
     * from $$CURRENT.
     *
     * @param field the field name
     * @return the new expression
     * @aggregation.expression $getField
     */
    public static Expression getField(String field) {
        return getField(new ValueExpression(field));
    }

    /**
     * Returns the value of a specified field from a document. If you don't specify an object, $getField returns the value of the field
     * from $$CURRENT.
     *
     * @param field the expression yielding the field name
     * @return the new expression
     * @aggregation.expression $getField
     */
    public static Expression getField(Expression field) {
        return new Expression("$getField", field);
    }

    /**
     * Returns a random float between 0 and 1.
     *
     * @return the filter
     * @aggregation.expression $rand
     */
    public static Expression rand() {
        return new Expression("$rand", new DocumentExpression());
    }

    /**
     * Matches a random selection of input documents. The number of documents selected approximates the sample rate expressed as a
     * percentage of the total number of documents.
     *
     * @param rate the rate to check against
     * @return the filter
     * @aggregation.expression $sampleRate
     */
    public static Filter sampleRate( double rate) {
        return new Filter("$sampleRate", null, rate) {
            @Override
            public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
                writeNamedValue(getName(), getValue(), mapper, writer, context);
            }
        };
    }

    /**
     * Adds, updates, or removes a specified field in a document.
     *
     * @param field Field in the input object that you want to add, update, or remove. field can be any valid expression that resolves to
     *              a string constant.
     * @param input Document that contains the field that you want to add or update. input must resolve to an object, missing, null, or
     *              undefined.
     * @param value The value that you want to assign to field. value can be any valid expression. Set to $$REMOVE to remove field from
     *              the input document.
     * @return the new expression
     * @aggregation.expression $setField
     */
    public static Expression setField(String field, Object input, Expression value) {
        return setField(new ValueExpression(field), input, value);
    }

    /**
     * Adds, updates, or removes a specified field in a document.
     *
     * @param field Field in the input object that you want to add, update, or remove. field can be any valid expression that resolves to
     *              a string constant.
     * @param input Document that contains the field that you want to add or update. input must resolve to an object, missing, null, or
     *              undefined.
     * @param value The value that you want to assign to field. value can be any valid expression. Set to $$REMOVE to remove field from
     *              the input document.
     * @return the new expression
     * @aggregation.expression $setField
     */
    public static Expression setField(Expression field, Object input, Expression value) {
        return new SetFieldExpression(field, input, value);
    }

    /**
     * Removes a specified field in a document.
     *
     * @param field the field name
     * @param input Document that contains the field that you want to add or update. input must resolve to an object, missing, null, or
     *              undefined.
     * @return the new expression
     * @aggregation.expression $unsetField
     */
    public static Expression unsetField(String field, Object input) {
        return unsetField(new ValueExpression(field), input);
    }

    /**
     * Removes a specified field in a document.
     *
     * @param field the expression yielding the field name
     * @param input Document that contains the field that you want to add or update. input must resolve to an object, missing, null, or
     *              undefined.
     * @return the new expression
     * @aggregation.expression $unsetField
     */
    public static Expression unsetField(Expression field, Object input) {
        return new UnsetFieldExpression(field, input);
    }
}
