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


import com.whaleal.mars.codecs.Conversions;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;


public class EntityDecoder implements org.bson.codecs.Decoder<Object> {
    private final MarsCodec<?> marsCodec;
    private final EntityModel classModel;

    protected EntityDecoder(MarsCodec<?> marsCodec) {
        this.marsCodec = marsCodec;
        classModel = marsCodec.getEntityModel();
    }

    @Override
    public Object decode(BsonReader reader, DecoderContext decoderContext) {
        Object entity;
        if (decoderContext.hasCheckedDiscriminator()) {
            MarsInstanceCreator instanceCreator = getInstanceCreator();
            decodeProperties(reader, decoderContext, instanceCreator);
            return instanceCreator.getInstance();
        } else {
            entity = getCodecFromDocument(reader, classModel.useDiscriminator(), classModel.getDiscriminatorKey(),
                    marsCodec.getRegistry(), marsCodec.getDiscriminatorLookup(), marsCodec)
                    .decode(reader, DecoderContext.builder().checkedDiscriminator(true).build());
        }

        return entity;
    }

    protected MarsInstanceCreator getInstanceCreator() {
        return classModel.getInstanceCreator();
    }

    protected void decodeModel( BsonReader reader, DecoderContext decoderContext,
                                MarsInstanceCreator instanceCreator, PropertyModel model ) {

        if (model != null) {
            final BsonReaderMark mark = reader.getMark();
            try {
                if (reader.getCurrentBsonType() == BsonType.NULL) {
                    reader.readNull();
                } else {
                    Object value = decoderContext.decodeWithChildContext(model.getCachedCodec(), reader);

                    instanceCreator.set(model.deserialize(value), model);
                }
            } catch (BsonInvalidOperationException e) {
                mark.reset();
                final Object value = marsCodec.getMapper().getCodecRegistry().get(Object.class).decode(reader, decoderContext);
                instanceCreator.set(Conversions.convert(value, model.getTypeData().getType()), model);
            }
        } else {
            reader.skipValue();
        }
    }

    protected void decodeProperties(BsonReader reader, DecoderContext decoderContext,
                                    MarsInstanceCreator instanceCreator) {
        reader.readStartDocument();
        EntityModel classModel = marsCodec.getEntityModel();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (classModel.useDiscriminator() && classModel.getDiscriminatorKey().equals(name)) {
                reader.readString();
            } else
                decodeModel(reader, decoderContext, instanceCreator, classModel.getPropertyModelFromReadName(name));
        }

        reader.readEndDocument();
    }

    protected Codec<?> getCodecFromDocument(BsonReader reader, boolean useDiscriminator, String discriminatorKey,
                                            CodecRegistry registry, DiscriminatorLookup discriminatorLookup,
                                            Codec<?> defaultCodec) {
        Codec<?> codec = null;
        if (useDiscriminator) {
            BsonReaderMark mark = reader.getMark();
            try {
                reader.readStartDocument();
                while (codec == null && reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    if (discriminatorKey.equals(reader.readName())) {
                        codec = registry.get(discriminatorLookup.lookup(reader.readString()));
                    } else {
                        reader.skipValue();
                    }
                }
            } catch (Exception e) {
                throw new CodecConfigurationException(String.format("Failed to decode '%s'. Decoding errored with: %s",
                        marsCodec.getEntityModel().getName(), e.getMessage()), e);
            } finally {
                mark.reset();
            }
        }
        return codec != null ? codec : defaultCodec;
    }

    protected MarsCodec<?> getMarsCodec() {
        return marsCodec;
    }
}
