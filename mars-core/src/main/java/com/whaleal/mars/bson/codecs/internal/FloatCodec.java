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
package com.whaleal.mars.bson.codecs.internal;

import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.RepresentationConfigurable;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.types.Decimal128;

import static java.lang.String.format;
import static org.bson.BsonType.*;


public class FloatCodec implements Codec<Float>, RepresentationConfigurable<Float> {


    private BsonType representation;

    public FloatCodec() {
        this.representation = INT64;
    }


    private FloatCodec(final BsonType representation) {
        this.representation = representation;
    }


    @Override
    public void encode(final BsonWriter writer, final Float value, final EncoderContext encoderContext) {
        switch (representation) {
            case INT32:
                writer.writeInt32(value.intValue());
                break;
            case INT64:
                writer.writeInt64(value.longValue());
                break;
            case DOUBLE:
                writer.writeDouble(value.doubleValue());
                break;
            case DECIMAL128:
                writer.writeDecimal128(Decimal128.parse(value.toString()));
                break;
            case STRING:
                writer.writeString(value.toString());
                break;
            default:
                throw new BsonInvalidOperationException("Cannot encode a  Float to a " + representation);
        }
    }

    @Override
    public Float decode(final BsonReader reader, final DecoderContext decoderContext) {
        Double value = NumberCodecHelper.decodeDouble(reader);
        if (value < -Float.MAX_VALUE || value > Float.MAX_VALUE) {
            throw new BsonInvalidOperationException(format("%s can not be converted into a Float.", value));
        }
        return value.floatValue();
    }

    @Override
    public Class<Float> getEncoderClass() {
        return Float.class;
    }

    @Override
    public BsonType getRepresentation() {
        return representation;
    }

    @Override
    public Codec<Float> withRepresentation(BsonType bsonType) {

        if (representation != BsonType.STRING && representation != INT32 && representation != INT64 && representation != DOUBLE && representation != DECIMAL128) {
            throw new CodecConfigurationException(representation + " is not a supported representation for Float");
        }
        return new FloatCodec(bsonType);

    }
}
