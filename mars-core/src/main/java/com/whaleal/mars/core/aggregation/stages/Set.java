package com.whaleal.mars.core.aggregation.stages;

import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.expressions.impls.DocumentExpression;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

/**
 * @author wh
 *
 */
public class Set extends Stage {
    private final DocumentExpression document = Expressions.of();

    protected Set() {
        super("$set");
    }

    /**
     * Creates a new Set stage
     *
     * @return the new stage
     */
    public static Set set() {
        return new Set();
    }

    /**
     * Add a field to the stage
     *
     * @param name  the name of the new field
     * @param value the value expression
     * @return this
     */
    public Set field(String name, Expression value) {
        document.field(name, value);
        return this;
    }

    /**
     * @return the fields
     * 
     */
    public DocumentExpression getDocument() {
        return document;
    }
}
