package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.Match;
import com.whaleal.mars.core.query.filters.Filter;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;


public class MatchCodec extends StageCodec< Match > {

    public MatchCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<Match> getEncoderClass() {
        return Match.class;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void encodeStage(BsonWriter writer, Match value, EncoderContext encoderContext) {
        document(writer, () -> {
            for (Filter filter : value.getFilters()) {
                filter.encode(getMapper(), writer, encoderContext);
            }
        });
    }
}
