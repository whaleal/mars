package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.Merge;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


@SuppressWarnings({"rawtypes", "unchecked"})
public class MergeCodec extends StageCodec<Merge> {
    public MergeCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class< Merge > getEncoderClass() {
        return Merge.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Merge merge, EncoderContext encoderContext) {
        document(writer, () -> {
            String collection = merge.getType() != null
                                ? getMapper().getMapper().getEntityModel(merge.getType()).getCollectionName()
                                : merge.getCollection();
            String database = merge.getDatabase();

            if (database == null) {
                writer.writeString("into", collection);
            } else {
                document(writer, "into", () -> {
                    writer.writeString("db", database);
                    writer.writeString("coll", collection);
                });
            }

            List<String> on = merge.getOn();
            if (on != null) {
                if (on.size() == 1) {
                    writer.writeString("on", on.get(0));
                } else {
                    array(writer, "on", () -> on.forEach(writer::writeString));
                }
            }
            Map<String, Expression > variables = merge.getVariables();
            if (variables != null) {
                document(writer, "let", () -> {
                    for (Entry<String, Expression> entry : variables.entrySet()) {
                        expression(getMapper(), writer, entry.getKey(), entry.getValue(), encoderContext);
                    }
                });
            }
            writeEnum(writer, "whenMatched", merge.getWhenMatched());
            value(getMapper(), writer, "whenMatched", merge.getWhenMatchedPipeline(), encoderContext);
            writeEnum(writer, "whenNotMatched", merge.getWhenNotMatched());
        });
    }

    private void writeEnum(BsonWriter writer, String name, Enum<?> value) {
        if (value != null) {
            writer.writeString(name, value.name().toLowerCase());
        }
    }
}
