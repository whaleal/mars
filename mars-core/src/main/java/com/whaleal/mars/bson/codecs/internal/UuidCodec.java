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

import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.internal.UuidHelper;

import java.util.UUID;

import static org.bson.assertions.Assertions.notNull;


public class UuidCodec implements Codec<UUID> {

    private final UuidRepresentation uuidRepresentation;

    public UuidCodec(final UuidRepresentation uuidRepresentation) {
        notNull("uuidRepresentation", uuidRepresentation);
        this.uuidRepresentation = uuidRepresentation;
    }

    public UuidCodec() {
        this.uuidRepresentation = UuidRepresentation.UNSPECIFIED;
    }

    public UuidRepresentation getUuidRepresentation() {
        return uuidRepresentation;
    }

    @Override
    public void encode(final BsonWriter writer, final UUID value, final EncoderContext encoderContext) {
        if (uuidRepresentation == UuidRepresentation.UNSPECIFIED) {
            throw new CodecConfigurationException("The uuidRepresentation has not been specified, so the UUID cannot be encoded.");
        }
        byte[] binaryData = UuidHelper.encodeUuidToBinary(value, uuidRepresentation);
        if (uuidRepresentation == UuidRepresentation.STANDARD) {
            writer.writeBinaryData(new BsonBinary(BsonBinarySubType.UUID_STANDARD, binaryData));
        } else {
            writer.writeBinaryData(new BsonBinary(BsonBinarySubType.UUID_LEGACY, binaryData));
        }
    }

    @Override
    public UUID decode(final BsonReader reader, final DecoderContext decoderContext) {
        byte subType = reader.peekBinarySubType();

        if (subType != BsonBinarySubType.UUID_LEGACY.getValue() && subType != BsonBinarySubType.UUID_STANDARD.getValue()) {
            throw new BSONException("Unexpected BsonBinarySubType");
        }

        byte[] bytes = reader.readBinaryData().getData();

        return UuidHelper.decodeBinaryToUuid(bytes, subType, uuidRepresentation);
    }

    @Override
    public Class<UUID> getEncoderClass() {
        return UUID.class;
    }

    @Override
    public String toString() {
        return "UuidCodec{"
                + "uuidRepresentation=" + uuidRepresentation
                + '}';
    }
}
