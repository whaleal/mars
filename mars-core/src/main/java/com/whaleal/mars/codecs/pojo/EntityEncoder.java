/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package com.whaleal.mars.codecs.pojo;


import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.IdGenerator;
import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.Map;


class EntityEncoder implements org.bson.codecs.Encoder<Object> {
    public static final ObjectIdGenerator OBJECT_ID_GENERATOR = new ObjectIdGenerator();
    private final MarsCodec MarsCodec;
    private IdGenerator idGenerator;

    protected EntityEncoder(MarsCodec MarsCodec) {
        this.MarsCodec = MarsCodec;
    }

    @Override
    public void encode(BsonWriter writer, Object value, EncoderContext encoderContext) {
        encodeEntity(writer, value, encoderContext);
    }

    @Override
    public Class<Object> getEncoderClass() {
        return MarsCodec.getEncoderClass();
    }

    @SuppressWarnings("unchecked")
    protected void encodeEntity(BsonWriter writer, Object value, EncoderContext encoderContext) {
        EntityModel model = MarsCodec.getEntityModel();
        if (areEquivalentTypes(value.getClass(), model.getType())) {
            ExpressionHelper.document(writer, () -> {

                PropertyModel idModel = model.getIdProperty();
                encodeIdProperty(writer, value, encoderContext, idModel);

                if (model.useDiscriminator()) {
                    writer.writeString(model.getDiscriminatorKey(),
                            model.getDiscriminator());
                }

                for (Object property : model.getPropertyModels()) {
                    PropertyModel<?> propertyModel = (PropertyModel<?>) property;
                    if (propertyModel.equals(idModel)) {
                        continue;
                    }
                    encodeProperty(writer, value, encoderContext, propertyModel);
                }
            });
        } else {
            MarsCodec.getRegistry()
                    .get((Class<? super Object>) value.getClass())
                    .encode(writer, value, encoderContext);
        }
    }

    protected MarsCodec getMarsCodec() {
        return MarsCodec;
    }

    private <S, V> boolean areEquivalentTypes(Class<S> t1, Class<V> t2) {
        return t1.equals(t2)
                || Collection.class.isAssignableFrom(t1) && Collection.class.isAssignableFrom(t2)
                || Map.class.isAssignableFrom(t1) && Map.class.isAssignableFrom(t2);
    }

    private void encodeIdProperty(BsonWriter writer, Object instance, EncoderContext encoderContext,
                                  PropertyModel idModel) {
        if (idModel != null) {
            IdGenerator generator = getIdGenerator();
            if (generator == null) {
                encodeProperty(writer, instance, encoderContext, idModel);
            } else {
                Object id = idModel.getPropertyAccessor().get(instance);
                if (id == null && encoderContext.isEncodingCollectibleDocument()) {
                    id = generator.generate();
                    idModel.getPropertyAccessor().set(instance, id);
                }
                encodeValue(writer, encoderContext, idModel, id);
            }
        }
    }

    private void encodeProperty(BsonWriter writer, Object instance, EncoderContext encoderContext, PropertyModel model) {
        Object value = model.getPropertyAccessor().get(instance);
        encodeValue(writer, encoderContext, model, value);
    }

    private void encodeValue( BsonWriter writer, EncoderContext encoderContext, PropertyModel model,
                              Object propertyValue ) {
        if (model.shouldSerialize(propertyValue)) {
            writer.writeName(model.getWriteName());
            if (propertyValue == null) {
                writer.writeNull();
            } else {
                Codec<? super Object> cachedCodec = model.getCachedCodec();
                encoderContext.encodeWithChildContext(cachedCodec, writer, model.serialize(propertyValue));
            }
        }else{
           //  既然不需要序列化 那么就直接丢弃
        }
    }


    private IdGenerator getIdGenerator() {
        if (idGenerator == null) {
            PropertyModel idModel = MarsCodec.getEntityModel().getIdProperty();
            if (idModel != null && idModel.getNormalizedType().isAssignableFrom(ObjectId.class)) {
                idGenerator = OBJECT_ID_GENERATOR;
            }
        }

        return idGenerator;
    }
}
