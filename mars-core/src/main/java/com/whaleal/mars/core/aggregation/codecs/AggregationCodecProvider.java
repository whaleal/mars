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
package com.whaleal.mars.core.aggregation.codecs;

import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.stages.*;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.*;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AggregationCodecProvider implements CodecProvider {

    private final Codec expressionCodec;
    private final MongoMappingContext mapper;
    private Map<Class, StageCodec> codecs;

    public AggregationCodecProvider(MongoMappingContext mapper) {
        this.mapper = mapper;
        expressionCodec = new ExpressionCodec(this.mapper);
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        Codec<T> codec = getCodecs().get(clazz);
        if (codec == null) {
            if (Expression.class.isAssignableFrom(clazz)) {
                codec = expressionCodec;
            }
        }
        return codec;
    }

    private Map<Class, StageCodec> getCodecs() {
        if (codecs == null) {
            codecs = new HashMap<>();

            // Stages
            codecs.put(AddFields.class, new AddFieldsCodec(mapper));
            codecs.put(AutoBucket.class, new AutoBucketCodec(mapper));
            codecs.put(Bucket.class, new BucketCodec(mapper));
            codecs.put(CollectionStats.class, new CollectionStatsCodec(mapper));
            codecs.put(Count.class, new CountCodec(mapper));
            codecs.put(CurrentOp.class, new CurrentOpCodec(mapper));
            codecs.put(Facet.class, new FacetCodec(mapper));
            codecs.put(GeoNear.class, new GeoNearCodec(mapper));
            codecs.put(GraphLookup.class, new GraphLookupCodec(mapper));
            codecs.put(Group.class, new GroupCodec(mapper));
            codecs.put(IndexStats.class, new IndexStatsCodec(mapper));
            codecs.put(Merge.class, new MergeCodec(mapper));
            codecs.put(PlanCacheStats.class, new PlanCacheStatsCodec(mapper));
            codecs.put(Limit.class, new LimitCodec(mapper));
            codecs.put(Lookup.class, new LookupCodec(mapper));
            codecs.put(Match.class, new MatchCodec(mapper));
            codecs.put(Out.class, new OutCodec(mapper));
            codecs.put(Projection.class, new ProjectionCodec(mapper));
            codecs.put(Redact.class, new RedactCodec(mapper));
            codecs.put(ReplaceRoot.class, new ReplaceRootCodec(mapper));
            codecs.put(ReplaceWith.class, new ReplaceWithCodec(mapper));
            codecs.put(Sample.class, new SampleCodec(mapper));
            codecs.put(Set.class, new SetStageCodec(mapper));
            codecs.put(SetWindowFields.class, new SetWindowFieldsCodec(mapper));
            codecs.put(Skip.class, new SkipCodec(mapper));
            codecs.put(Sort.class, new SortCodec(mapper));
            
            codecs.put(SortByCount.class, new SortByCountCodec(mapper));
            codecs.put(UnionWith.class, new UnionWithCodec(mapper));
            codecs.put(Unset.class, new UnsetCodec(mapper));
            codecs.put(Unwind.class, new UnwindCodec(mapper));


        }
        return codecs;
    }
}
