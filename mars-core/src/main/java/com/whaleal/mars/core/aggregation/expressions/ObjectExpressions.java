package com.whaleal.mars.core.aggregation.expressions;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.*;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.expression;


/**
 * Defines helper methods for the object expressions
 *
 */
public final class ObjectExpressions {
    private ObjectExpressions() {
    }

    /**
     * Combines multiple documents into a single document.
     *
     * @return the new expression
     * @aggregation.expression $mergeObjects
     */
    public static MergeObjects mergeObjects() {
        return new MergeObjects();
    }

    /**
     * Defines the values to be merged.
     */
    public static class MergeObjects extends Expression {

        protected MergeObjects() {
            super("$mergeObjects", new ExpressionList());
        }

        /**
         * Adds an expression to be merged
         *
         * @param expression the expression
         * @return this
         */
        @SuppressWarnings("unchecked")
        public MergeObjects add(Expression expression) {
            ExpressionList value = (ExpressionList) getValue();
            if (value != null) {
                value.add(expression);
            }
            return this;
        }

        @Override
        public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
            expression(mapper, writer, getOperation(), getValue(), encoderContext);
        }
    }
}
