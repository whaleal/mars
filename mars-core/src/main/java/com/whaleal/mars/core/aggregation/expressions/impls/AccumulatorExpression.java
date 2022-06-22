package com.whaleal.mars.core.aggregation.expressions.impls;


import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.List;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.array;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;

public class AccumulatorExpression extends Expression {
    private final String initFunction;
    private final String accumulateFunction;
    private final List<Expression> accumulateArgs;
    private final String mergeFunction;
    private final String lang = "js";
    private List<Expression> initArgs;
    private String finalizeFunction;

    public AccumulatorExpression(String initFunction, String accumulateFunction, List<Expression> accumulateArgs, String mergeFunction) {
        super("$accumulator");
        this.initFunction = initFunction;
        this.accumulateFunction = accumulateFunction;
        this.accumulateArgs = accumulateArgs;
        this.mergeFunction = mergeFunction;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
            document(writer, getOperation(), () -> {
                writer.writeString("init", initFunction);
                array(mapper, writer, "initArgs", initArgs, encoderContext);
                writer.writeString("accumulate", accumulateFunction);
                array(mapper, writer, "accumulateArgs", accumulateArgs, encoderContext);
                writer.writeString("merge", mergeFunction);
                writer.writeString("finalize", finalizeFunction);
                writer.writeString("lang", lang);
            });
    }

    /**
     * 用于更新累加结果
     *
     * @param finalizeFunction the function body
     * @return this
     */
    public AccumulatorExpression finalizeFunction(String finalizeFunction) {
        this.finalizeFunction = finalizeFunction;
        return this;
    }

    public AccumulatorExpression initArgs(List<Expression> initArgs) {
        this.initArgs = initArgs;
        return this;
    }

}
