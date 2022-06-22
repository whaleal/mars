package com.whaleal.mars.core.aggregation.expressions.impls;

public class LiteralExpression extends Expression {
    public LiteralExpression(Object value) {
        super("$literal", new ValueExpression(value));
    }
}
