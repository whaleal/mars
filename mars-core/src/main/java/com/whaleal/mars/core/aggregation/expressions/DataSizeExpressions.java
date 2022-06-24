package com.whaleal.mars.core.aggregation.expressions;

import com.whaleal.mars.core.aggregation.expressions.impls.*;

/**
 * Defines helper methods for the data size expressions
 *
 */
public final class DataSizeExpressions {
    private DataSizeExpressions() {
    }

    /**
     * Returns the size of a given string or binary data value’s content in bytes.
     *
     * @param expression the binary size expression
     * @return the new expression
     * @aggregation.expression $binarySize
     */
    public static Expression binarySize(Expression expression) {
        return new Expression("$binarySize", expression);
    }

    /**
     * Returns the size in bytes of a given document (i.e. bsontype Object) when encoded as BSON.
     *
     * @param expression the bson size expression
     * @return the new expression
     * @aggregation.expression $bsonSize
     */
    public static Expression bsonSize(Expression expression) {
        return new Expression("$bsonSize", expression);
    }
}
