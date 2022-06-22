package com.whaleal.mars.core.aggregation.codecs.stages;

import com.mongodb.lang.Nullable;


import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.TimeUnit;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.SetWindowFields;

import com.whaleal.mars.core.query.Sort;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.Locale;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class SetWindowFieldsCodec extends StageCodec< SetWindowFields > {

    private final Codec<Object> objectCodec;

    public SetWindowFieldsCodec( MongoMappingContext mapper) {
        super(mapper);
        objectCodec = getMapper().getCodecRegistry()
                                    .get(Object.class);
    }

    @Override
    public Class<SetWindowFields> getEncoderClass() {
        return SetWindowFields.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, SetWindowFields value, EncoderContext encoderContext) {
        document(writer, () -> {
            if (value.partition() != null) {
                writer.writeName("partitionBy");
                expression(getMapper(), writer, value.partition(), encoderContext);
            }
            Sort[] sorts = value.sorts();
            if (sorts != null) {
                document(writer, "sortBy", () -> {
                    for (Sort sort : sorts) {
                        writer.writeInt64(sort.getField(), sort.getOrder());
                    }
                });
            }
            document(writer, "output", () -> {
                for (SetWindowFields.Output output : value.outputs()) {
                    document(writer, output.name(), () -> {
                        operator(writer, encoderContext, output.operator());
                        window(writer, output, encoderContext);
                    });
                }
            });
        });
    }

    private void documents(BsonWriter writer, @Nullable List<Object> list, String documents,
                           EncoderContext encoderContext) {
        if (list != null) {
            array(writer, documents, () -> {
                for (Object document : list) {
                    objectCodec.encode(writer, document, encoderContext);
                }
            });
        }
    }

    @SuppressWarnings("rawtypes")
    private void operator(BsonWriter writer, EncoderContext encoderContext, @Nullable Expression operator) {
        if (operator != null) {
            expression(getMapper(), writer, operator, encoderContext);
        }
    }

    private void window( BsonWriter writer, SetWindowFields.Output output, EncoderContext encoderContext) {
        SetWindowFields.Window window = output.windowDef();
        if (window != null) {
            document(writer, "window", () -> {
                documents(writer, window.documents(), "documents", encoderContext);
                documents(writer, window.range(), "range", encoderContext);
                TimeUnit unit = window.unit();
                if (unit != null) {
                    writer.writeString("unit", unit.name().toLowerCase(Locale.ROOT));
                }
            });
        }
    }
}
