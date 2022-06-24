package com.whaleal.mars.core.aggregation.codecs;

import com.mongodb.lang.Nullable;


import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.expressions.impls.*;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 *
 */
public final class ExpressionHelper {
    private ExpressionHelper() {
    }

    public static void array(BsonWriter writer, Runnable body) {
        writer.writeStartArray();
        body.run();
        writer.writeEndArray();
    }

    public static void array(BsonWriter writer, String name, Runnable body) {
        writer.writeStartArray(name);
        body.run();
        writer.writeEndArray();
    }

    public static void array( MongoMappingContext mapper, BsonWriter writer, String name, @Nullable List<Expression> list,
                              EncoderContext encoderContext) {
        if (list != null) {
            array(writer, name, () -> {
                for (Expression expression : list) {
                    wrapExpression(mapper, writer, expression, encoderContext);
                }
            });
        }
    }

    public static void document(BsonWriter writer, Runnable body) {
        writer.writeStartDocument();
        body.run();
        writer.writeEndDocument();
    }

    public static void document(BsonWriter writer, String name, Runnable body) {
        writer.writeStartDocument(name);
        body.run();
        writer.writeEndDocument();
    }

    public static Document document(MongoMappingContext mapper, Document seed, Consumer<BsonWriter> body) {
        DocumentWriter writer = new DocumentWriter(mapper, seed);
        writer.writeStartDocument();
        body.accept(writer);
        writer.writeEndDocument();

        return writer.getDocument();
    }

    /**
     * @param mapper
     * @param writer
     * @param name
     * @param expression
     * @param encoderContext
     */

    public static void expression(MongoMappingContext mapper, BsonWriter writer, String name, @Nullable Expression expression,
                                  EncoderContext encoderContext) {
        if (expression != null) {
            writer.writeName(name);
            wrapExpression(mapper, writer, expression, encoderContext);
        }
    }

    /**
     * @param mapper
     * @param writer
     * @param expression
     * @param encoderContext
     */

    public static void expression(MongoMappingContext mapper, BsonWriter writer, @Nullable Expression expression, EncoderContext encoderContext) {
        if (expression != null) {
            expression.encode(mapper, writer, encoderContext);
        }
    }

    /**
     * @param mapper
     * @param writer
     * @param name
     * @param value
     * @param encoderContext
     */

    public static void value(MongoMappingContext mapper, BsonWriter writer, String name, @Nullable Object value, EncoderContext encoderContext) {
        if (value != null) {
            if (value instanceof List) {
                List<Object> list = (List<Object>) value;
                array(writer, name, () -> {
                    for (Object object : list) {
                        Codec codec = mapper.getCodecRegistry().get(object.getClass());
                        encoderContext.encodeWithChildContext(codec, writer, object);
                    }
                });
            } else {
                writer.writeName(name);
                Codec codec = mapper.getCodecRegistry().get(value.getClass());
                encoderContext.encodeWithChildContext(codec, writer, value);
            }
        }
    }

    public static void value(BsonWriter writer, String name, @Nullable Boolean value) {
        if (value != null) {
            writer.writeBoolean(name, value);
        }
    }

    public static void value(BsonWriter writer, String name, @Nullable Integer value) {
        if (value != null) {
            writer.writeInt32(name, value);
        }
    }

    public static void value(BsonWriter writer, String name, @Nullable Double value) {
        if (value != null) {
            writer.writeDouble(name, value);
        }
    }

    public static void value(BsonWriter writer, String name, @Nullable Long value) {
        if (value != null) {
            writer.writeInt64(name, value);
        }
    }

    public static void value(BsonWriter writer, String name, @Nullable String value) {
        if (value != null) {
            writer.writeString(name, value);
        }
    }

    /**
     * @param mapper
     * @param writer
     * @param value
     * @param encoderContext
     */

    public static void value(MongoMappingContext mapper, BsonWriter writer, @Nullable Object value, EncoderContext encoderContext) {
        if (value != null) {
            Codec codec = mapper.getCodecRegistry().get(value.getClass());
            encoderContext.encodeWithChildContext(codec, writer, value);
        }
    }

    /**
     * @param mapper
     * @param writer
     * @param expression
     * @param encoderContext
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void wrapExpression(MongoMappingContext mapper, BsonWriter writer, @Nullable Expression expression,
                                      EncoderContext encoderContext) {
        if (expression != null) {
            if (expression instanceof SingleValuedExpression) {
                expression.encode(mapper, writer, encoderContext);
            } else {
                document(writer, () -> {
                    expression.encode(mapper, writer, encoderContext);
                });
            }
        }
    }

    /**
     * @param mapper
     * @param writer
     * @param name
     * @param expression
     * @param encoderContext
     */

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void wrapExpression(MongoMappingContext mapper, BsonWriter writer, String name, @Nullable Expression expression,
                                      EncoderContext encoderContext) {
        if (expression != null) {
            writer.writeName(name);
            if (expression instanceof ValueExpression || expression instanceof ArrayLiteral || expression instanceof ExpressionList) {
                expression.encode(mapper, writer, encoderContext);
            } else {
                wrapExpression(mapper, writer, expression, encoderContext);
            }
        }
    }
}
