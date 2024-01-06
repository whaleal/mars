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
package com.whaleal.mars.codecs.internal;

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

import java.util.concurrent.atomic.AtomicLong;

import static org.bson.BsonType.*;


/**
 * @author wh
 *
 * @see com.whaleal.mars.codecs.internal.LongCodec ;
 *
 */
public class AtomicLongCodec implements Codec<AtomicLong>, RepresentationConfigurable<AtomicLong> {

    private BsonType representation;

    public AtomicLongCodec() {
        this.representation = INT64;
    }


    private AtomicLongCodec(final BsonType representation) {
        this.representation = representation;
    }

    @Override
    public void encode(final BsonWriter writer, final AtomicLong value, final EncoderContext encoderContext) {
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
                writer.writeDecimal128(new Decimal128(value.longValue()));
                break;
            case STRING:
                writer.writeString(value.toString());
                break;
            default:
                throw new BsonInvalidOperationException("Cannot encode a  AtomicLong to a " + representation);
        }

    }

    @Override
    public AtomicLong decode(final BsonReader reader, final DecoderContext decoderContext) {
        return new AtomicLong(NumberCodecHelper.decodeLong(reader));
    }

    @Override
    public Class<AtomicLong> getEncoderClass() {
        return AtomicLong.class;
    }

    @Override
    public BsonType getRepresentation() {
        return representation;
    }

    @Override
    public Codec<AtomicLong> withRepresentation(BsonType bsonType) {

        if (representation != BsonType.STRING && representation != INT32 && representation != INT64 && representation != DOUBLE && representation != DECIMAL128) {
            throw new CodecConfigurationException(representation + " is not a supported representation for AtomicLongCodec");
        }
        return new AtomicLongCodec(bsonType);
    }
}
