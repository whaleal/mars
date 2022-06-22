package com.whaleal.mars.core.aggregation.codecs;


import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.codecs.stages.AddFieldsCodec;
import com.whaleal.mars.core.aggregation.codecs.stages.*;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AggregationCodecProvider implements CodecProvider {

    private final Codec expressionCodec;
    private final MongoMappingContext mapper;
    private Map<Class, StageCodec > codecs;

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public AggregationCodecProvider( MongoMappingContext mapper) {
        this.mapper = mapper;
        expressionCodec = new ExpressionCodec(mapper);
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
