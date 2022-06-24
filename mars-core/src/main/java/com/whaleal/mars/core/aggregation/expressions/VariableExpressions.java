package com.whaleal.mars.core.aggregation.expressions;



import com.whaleal.mars.core.aggregation.expressions.impls.*;
/**
 * Defines helper methods for the variable expressions
 *
 */
public final class VariableExpressions {
    private VariableExpressions() {
    }

    /**
     * Binds variables for use in the specified expression, and returns the result of the expression.
     *
     * @param in the expression to evaluate.  variables can be defined using the {@link LetExpression#variable(String, Expression)} method
     * @return the new expression
     * @aggregation.expression $let
     */
    public static LetExpression let(Expression in) {
        return new LetExpression(in);
    }
}
