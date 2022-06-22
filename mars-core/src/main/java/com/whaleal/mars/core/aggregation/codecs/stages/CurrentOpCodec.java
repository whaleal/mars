package com.whaleal.mars.core.aggregation.codecs.stages;



import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.CurrentOp;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.value;


public class CurrentOpCodec extends StageCodec< CurrentOp > {
    public CurrentOpCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<CurrentOp> getEncoderClass() {
        return CurrentOp.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, CurrentOp value, EncoderContext encoderContext) {
        document(writer, () -> {
            writeBoolean(writer, "allUsers", value.isAllUsers());
            writeBoolean(writer, "idleConnections", value.isIdleConnections());
            writeBoolean(writer, "idleCursors", value.isIdleCursors());
            writeBoolean(writer, "idleSessions", value.isIdleSessions());
            writeBoolean(writer, "localOps", value.isLocalOps());
        });
    }

    private void writeBoolean(BsonWriter writer, String name, boolean value) {
        if (value) {
            value(writer, name, value);
        }
    }

}
