package com.whaleal.mars.core.internal;

import com.mongodb.lang.Nullable;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.PropertyModel;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.StringJoiner;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.value;

public class OperationTarget {
    private final PathTarget target;
    private final Object value;

    /**
     * @param target the target
     * @param value  the value
     */
    public OperationTarget(@Nullable PathTarget target, @Nullable Object value) {
        this.target = target;
        this.value = value;
    }

    /**
     * Encodes this target
     *
     * @param mapper the datastore
     * @return the encoded form
     */
    public Object encode(MongoMappingContext mapper) {
        if (target == null) {
            if (value == null) {
                throw new NullPointerException();
            }
            return value;
        }
        PropertyModel mappedField = this.target.getTarget();
        Object mappedValue = value;

        PropertyModel model = mappedField != null
                              ? mappedField
                              : null;

        Codec cachedCodec = null;
//        if (model != null && !(mappedValue instanceof LegacyQuery)) {
//            cachedCodec = model.specializeCodec(mapper);
//        }
            DocumentWriter writer = new DocumentWriter(mapper.getMapper());
            Object finalMappedValue = mappedValue;
            document(writer, () -> value(mapper, writer, "mapped", finalMappedValue, EncoderContext.builder().build()));
            mappedValue = writer.getDocument().get("mapped");
        return new Document(target.translatedPath(), mappedValue);
    }

    /**
     * @return the PathTarget for this instance
     */
    @Nullable
    public PathTarget getTarget() {
        return target;
    }

    /**
     * @return the value
     */
    @Nullable
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OperationTarget.class.getSimpleName() + "[", "]")
            .add("target=" + target)
            .add("value=" + value)
            .toString();
    }
}
