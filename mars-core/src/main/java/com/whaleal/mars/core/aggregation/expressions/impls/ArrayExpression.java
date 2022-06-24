package com.whaleal.mars.core.aggregation.expressions.impls;

import java.util.List;

/**
 * Base class for the array expressions
 */
public class ArrayExpression extends Expression implements SingleValuedExpression {
    public ArrayExpression(String operation, List<Expression> value) {
        super(operation, value);
    }
}
