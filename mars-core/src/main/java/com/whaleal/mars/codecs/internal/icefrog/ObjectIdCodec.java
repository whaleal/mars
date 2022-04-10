package com.whaleal.mars.codecs.internal.icefrog;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import com.whaleal.icefrog.core.lang.ObjectId;

/**
 *
 * 添加对自定义包
 * 内部特殊类型的支持
 *
 * @author wh
 *
 *
 */

public class ObjectIdCodec implements Codec< ObjectId > {
    @Override
    public void encode( final BsonWriter writer, final ObjectId value, final EncoderContext encoderContext) {
        writer.writeObjectId(new org.bson.types.ObjectId(value.toHexString()));
    }

    @Override
    public ObjectId decode( final BsonReader reader, final DecoderContext decoderContext) {
        return new ObjectId(reader.readObjectId().toHexString());
    }

    @Override
    public Class<ObjectId> getEncoderClass() {
        return com.whaleal.icefrog.core.lang.ObjectId.class;
    }
}