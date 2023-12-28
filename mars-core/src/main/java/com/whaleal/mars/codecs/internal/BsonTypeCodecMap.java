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

import com.whaleal.mars.util.Assert;
import org.bson.BsonType;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.Iterator;

public class BsonTypeCodecMap {
    private final BsonTypeClassMap bsonTypeClassMap;
    private final Codec<?>[] codecs = new Codec[256];

    public BsonTypeCodecMap(BsonTypeClassMap bsonTypeClassMap, CodecRegistry codecRegistry) {
        this.bsonTypeClassMap =  Assert.notNull(bsonTypeClassMap);
        Assert.notNull(codecRegistry,"codecRegistry");
        Iterator var3 = bsonTypeClassMap.keys().iterator();

        while (var3.hasNext()) {
            BsonType cur = (BsonType) var3.next();
            Class<?> clazz = bsonTypeClassMap.get(cur);
            if (clazz != null) {
                try {
                    this.codecs[cur.getValue()] = codecRegistry.get(clazz);
                } catch (CodecConfigurationException var7) {
                }
            }
        }

    }

    public Codec<?> get(BsonType bsonType) {
        Codec<?> codec = this.codecs[bsonType.getValue()];
        if (codec == null) {
            Class<?> clazz = this.bsonTypeClassMap.get(bsonType);
            if (clazz == null) {
                throw new CodecConfigurationException(String.format("No class mapped for BSON type %s.", bsonType));
            } else {
                throw new CodecConfigurationException(String.format("Can't find a codec for %s.", clazz));
            }
        } else {
            return codec;
        }
    }
}
