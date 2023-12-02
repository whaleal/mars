package com.whaleal.mars.core.aggregation.expressions;


import com.whaleal.icefrog.core.collection.ListUtil;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.*;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;
import java.util.List;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static java.util.Arrays.asList;

/**
 * Provides window specific operations.
 *
 */
public final class WindowExpressions {
    private WindowExpressions() {
    }

    /**
     * Returns the population covariance of two numeric expressions that are evaluated using documents in the $setWindowFields stage window.
     * <p>
     * $covariancePop is only available in the $setWindowFields stage.
     *
     * @param first  the first expression to evaluate
     * @param second the second expression to evaluate
     * @return the new expression
     * @mongodb.server.release 5.0
     * @aggregation.expression $covariancePop
     */
    public static Expression covariancePop(Expression first, Expression second) {
        return new MathExpression("$covariancePop", ListUtil.of(first, second));
    }

    /**
     * Returns the sample covariance of two numeric expressions that are evaluated using documents in the $setWindowFields stage window.
     * <p>
     * $covarianceSamp is only available in the $setWindowFields stage.
     *
     * @param first  the first expression to evaluate
     * @param second the second expression to evaluate
     * @return the new expression
     * @aggregation.expression $covarianceSamp
     */
    public static Expression covarianceSamp(Expression first, Expression second) {
        return new MathExpression("$covarianceSamp", ListUtil.of(first, second));
    }

    /**
     * Returns the document position (known as the rank) relative to other documents in the $setWindowFields stage partition.
     *
     * @return the expression
     * @mongodb.server.release 5.0
     * @aggregation.expression $denseRank
     */
    public static Expression denseRank() {
        return new Expression("$denseRank") {
            @Override
            public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
                document(writer, getOperation(), () -> {
                });
            }
        };
    }

    /**
     * Returns the average rate of change within the specified window.
     *
     * @param input Specifies the expression to evaluate. The expression must evaluate to a number.
     * @return the new expression
     * @aggregation.expression $derivative
     */
    public static CalculusExpression derivative(Expression input) {
        return new CalculusExpression("$derivative", input);
    }

    /**
     * Returns the position of a document (known as the document number) in the $setWindowFields stage partition.
     *
     * @return the new expression
     * @aggregation.expression $documentNumber
     */
    public static Expression documentNumber() {
        return new Expression("$documentNumber") {
            @Override
            public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
                document(writer, getOperation(), () -> {
                });
            }
        };
    }

    /**
     * Returns the exponential moving average of numeric expressions applied to documents in a partition defined in the $setWindowFields
     * stage.
     * <p>
     * $expMovingAvg is only available in the $setWindowFields stage.
     *
     * @param input Specifies the expression to evaluate. Non-numeric expressions are ignored.
     * @param n     An integer that specifies the number of historical documents that have a significant mathematical weight in the
     *              exponential moving average calculation, with the most recent documents contributing the most weight.
     * @return the new expression
     * @aggregation.expression $expMovingAvg
     */
    public static Expression expMovingAvg(Expression input, int n) {
        return new ExpMovingAvg(input, n);
    }

    /**
     * Returns the approximation of the area under a curve, which is calculated using the trapezoidal rule where each set of adjacent
     * documents form a trapezoid using the:
     *
     * @param input Specifies the expression to evaluate. The expression must evaluate to a number.
     * @return the new expression
     * @aggregation.expression $integral
     */
    public static CalculusExpression integral(Expression input) {
        return new CalculusExpression("$integral", input);
    }

    /**
     * Returns the document position (known as the rank) relative to other documents in the $setWindowFields stage partition.
     *
     * @return the new expression
     * @aggregation.expression $rank
     */
    public static Expression rank() {
        return new Expression("$rank") {
            @Override
            public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
                document(writer, getOperation(), () -> {
                });
            }
        };
    }

    /**
     * Returns the value from an expression applied to a document in a specified position relative to the current document in the
     * $setWindowFields stage partition.
     *
     * @param output       Specifies an expression to evaluate and return in the output.
     * @param by           Specifies an integer with a numeric document position relative to the current document in the output.
     * @param defaultValue Specifies an optional default expression to evaluate if the document position is outside of the implicit
     *                     $setWindowFields stage window. The implicit window contains all the documents in the partition.
     * @return the expression
     * @aggregation.expression $shift
     * @mongodb.server.release 5.0
     */
    public static ShiftExpression shift(Expression output, long by, Expression defaultValue) {
        return new ShiftExpression(output, by, defaultValue);
    }

    /**
     * Returns the population standard deviation of the input values.
     *
     * @param value      the value
     * @param additional any subsequent expressions to include in the expression
     * @return the new expression
     * @aggregation.expression $stdDevPop
     */
    public static Expression stdDevPop(Expression value, Expression... additional) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(value);
        expressions.addAll(asList(additional));
        return new Accumulator("$stdDevPop", expressions);
    }

    /**
     * Returns the sample standard deviation of the input values.
     *
     * @param value      the value
     * @param additional any subsequent expressions to include in the expression
     * @return the new expression
     * @aggregation.expression $stdDevSamp
     */
    public static Expression stdDevSamp(Expression value, Expression... additional) {
        List<Expression> expressions = new ArrayList<>();
        expressions.add(value);
        expressions.addAll(asList(additional));
        return new Accumulator("$stdDevSamp", expressions);
    }
}
