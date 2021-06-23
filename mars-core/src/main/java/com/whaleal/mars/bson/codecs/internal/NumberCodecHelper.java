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
import org.bson.types.Decimal128;

import java.math.BigDecimal;

import static java.lang.String.format;

final class NumberCodecHelper {

    static int decodeInt(final BsonReader reader) {
        int intValue;
        BsonType bsonType = reader.getCurrentBsonType();
        switch (bsonType) {
            case INT32:
                intValue = reader.readInt32();
                break;
            case INT64:
                long longValue = reader.readInt64();
                intValue = (int) longValue;
                if (longValue != (long) intValue) {
                    throw invalidConversion(Integer.class, longValue);
                }
                break;
            case DOUBLE:
                double doubleValue = reader.readDouble();
                intValue = (int) doubleValue;
                if (doubleValue != (double) intValue) {
                    throw invalidConversion(Integer.class, doubleValue);
                }
                break;
            case DECIMAL128:
                Decimal128 decimal128 = reader.readDecimal128();
                intValue = decimal128.intValue();
                if (!decimal128.equals(new Decimal128(intValue))) {
                    throw invalidConversion(Integer.class, decimal128);
                }
                break;
            case STRING:
                String string = reader.readString();
                intValue = Integer.valueOf(string);
                break;
            default:
                throw new BsonInvalidOperationException(format("Invalid numeric type, found: %s", bsonType));
        }
        return intValue;
    }

    static long decodeLong(final BsonReader reader) {
        long longValue;
        BsonType bsonType = reader.getCurrentBsonType();
        switch (bsonType) {
            case INT64:
                longValue = reader.readInt64();
                break;
            case INT32:
                longValue = reader.readInt32();
                break;

            case DOUBLE:
                double doubleValue = reader.readDouble();
                longValue = (long) doubleValue;
                if (doubleValue != (double) longValue) {
                    throw invalidConversion(Long.class, doubleValue);
                }
                break;
            case DECIMAL128:
                Decimal128 decimal128 = reader.readDecimal128();
                longValue = decimal128.longValue();
                if (!decimal128.equals(new Decimal128(longValue))) {
                    throw invalidConversion(Long.class, decimal128);
                }
                break;

            case STRING:
                String string = reader.readString();
                longValue = Long.valueOf(string);
                break;
            default:
                throw new BsonInvalidOperationException(format("Invalid numeric type, found: %s", bsonType));
        }
        return longValue;
    }

    static double decodeDouble(final BsonReader reader) {
        double doubleValue;
        BsonType bsonType = reader.getCurrentBsonType();
        switch (bsonType) {
            case DOUBLE:
                doubleValue = reader.readDouble();
                break;
            case INT32:
                doubleValue = reader.readInt32();
                break;
            case INT64:
                long longValue = reader.readInt64();
                doubleValue = longValue;
                if (longValue != (long) doubleValue) {
                    throw invalidConversion(Double.class, longValue);
                }
                break;

            case DECIMAL128:
                Decimal128 decimal128 = reader.readDecimal128();
                try {
                    doubleValue = decimal128.doubleValue();
                    if (!decimal128.equals(new Decimal128(new BigDecimal(doubleValue)))) {
                        throw invalidConversion(Double.class, decimal128);
                    }
                } catch (NumberFormatException e) {
                    throw invalidConversion(Double.class, decimal128);
                }
                break;
            case STRING:
                String string = reader.readString();
                doubleValue = Double.valueOf(string);
                break;

            default:
                throw new BsonInvalidOperationException(format("Invalid numeric type, found: %s", bsonType));
        }
        return doubleValue;
    }

    private static <T extends Number> BsonInvalidOperationException invalidConversion(final Class<T> clazz, final Number value) {
        return new BsonInvalidOperationException(format("Could not convert `%s` to a %s without losing precision", value, clazz));
    }

    private NumberCodecHelper() {
    }
}
