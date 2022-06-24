package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;
import java.util.List;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


/**
 * Evaluates a series of case expressions. When it finds an expression which evaluates to true, $switch executes a specified expression
 * and breaks out of the control flow.
 */
public class SwitchExpression extends Expression {
    private final List<Pair> branches = new ArrayList<>();
    private Expression defaultCase;

    public SwitchExpression() {
        super("$switch");
    }

    /**
     * Adds a new branch to the switch
     *
     * @param caseExpression Can be any valid expression that resolves to a boolean. If the result is not a boolean, it is coerced to a
     *                       boolean value.
     * @param then           the expression to evaluate if the case is true
     * @return this
     */
    public SwitchExpression branch(Expression caseExpression, Expression then) {
        branches.add(new Pair(caseExpression, then));
        return this;
    }

    /**
     * Adds a default case if nothing is matched.
     *
     * @param caseExpression the default case
     * @return this
     */
    public SwitchExpression defaultCase(Expression caseExpression) {
        this.defaultCase = caseExpression;
        return this;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        document(writer, getOperation(), () -> {
            array(writer, "branches", () -> {
                for (Pair branch : branches) {
                    document(writer, () -> {
                        wrapExpression(mapper, writer, "case", branch.caseExpression, encoderContext);
                        wrapExpression(mapper, writer, "then", branch.then, encoderContext);
                    });
                }
            });
            expression(mapper, writer, "default", defaultCase, encoderContext);
        });
    }

    private static class Pair {
        private final Expression caseExpression;
        private final Expression then;

        Pair(Expression caseExpression, Expression then) {
            this.caseExpression = caseExpression;
            this.then = then;
        }
    }
}
