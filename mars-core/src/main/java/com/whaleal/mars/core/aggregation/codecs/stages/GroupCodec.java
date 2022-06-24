package com.whaleal.mars.core.aggregation.codecs.stages;





import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.expressions.impls.Fields;
import com.whaleal.mars.core.aggregation.stages.Group;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.wrapExpression;


public class GroupCodec extends StageCodec< Group > {

    public GroupCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Group> getEncoderClass() {
        return Group.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, Group group, EncoderContext encoderContext) {
        document(writer, () -> {
            Group.GroupId id = group.getId();
            if (id != null) {
                writer.writeName("_id");
                if (id.getDocument() != null) {
                    id.getDocument().encode(getMapper(), writer, encoderContext);
                } else {
                    if (id.getField() != null) {
                        wrapExpression(getMapper(), writer, id.getField(), encoderContext);
                        //                        document(writer, () -> {
                        //                            id.getField().encode(getMapper(), writer, encoderContext);
                        //                        });
                    }
                }
            } else {
                writer.writeNull("_id");
            }

            Fields<Group> fields = group.getFields();
            if (fields != null) {
                fields.encode(getMapper(), writer, encoderContext);
            }

        });
    }
}
