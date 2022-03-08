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
package com.whaleal.mars.core.query.codec;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.ExpressionCodec;
import com.whaleal.mars.core.aggregation.codecs.stages.*;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.*;
import com.whaleal.mars.core.query.codec.stage.EachStage;
import com.whaleal.mars.core.query.codec.stage.PositionStage;
import com.whaleal.mars.core.query.codec.stage.SliceStage;
import com.whaleal.mars.core.query.codec.stage.SortStage;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class UpdateCodecs implements CodecProvider {

    private final Codec expressionCodec;
    private final MongoMappingContext mapper;
    private Map<Class, StageCodec> codecs;

    public UpdateCodecs(MongoMappingContext mapper) {
        this.mapper = mapper;
        expressionCodec = new ExpressionCodec(this.mapper);
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        System.out.println(clazz);
        Codec<T> codec = getCodecs().get(clazz);
        if (codec == null) {

            if(clazz.isAssignableFrom( EachStage.class)){
//                return getCodecs().get(EachStage.class);
                return (Codec<T>) new EachCodec(mapper);
            }else if(clazz.isAssignableFrom(SliceStage.class)){
                return (Codec<T>) new SliceCodec(mapper);
            }else if(clazz.isAssignableFrom(PositionStage.class)){
                return (Codec<T>) new PositionCodec(mapper);
            }else if(clazz.isAssignableFrom(SortStage.class)){
                return (Codec<T>) new SortCodec(mapper);
            }else {
                return null;
            }
        }
        return codec;
    }

    private Map<Class, StageCodec> getCodecs() {
        if (codecs == null) {
            codecs = new HashMap<>();

            // Stages
            codecs.put(SliceStage.class,new SliceCodec(mapper));
            codecs.put(SortStage.class,new SortCodec(mapper));
            codecs.put(EachStage.class,new EachCodec(mapper));
            codecs.put(PositionStage.class,new PositionCodec(mapper));
        }
        return codecs;
    }
}
