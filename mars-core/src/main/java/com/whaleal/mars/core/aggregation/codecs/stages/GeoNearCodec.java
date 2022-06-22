package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.GeoNear;
import com.whaleal.mars.core.query.filters.Filter;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.value;


public class GeoNearCodec extends StageCodec< GeoNear > {
    public GeoNearCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<GeoNear> getEncoderClass() {
        return GeoNear.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, GeoNear value, EncoderContext encoderContext) {
        document(writer, () -> {
            value(getMapper(), writer, "near", value.getPoint(), encoderContext);
            value(getMapper(), writer, "near", value.getCoordinates(), encoderContext);
            value(writer, "key", value.getKey());
            value(writer, "distanceField", value.getDistanceField());
            value(writer, "spherical", value.getSpherical());
            value(getMapper(), writer, "maxDistance", value.getMaxDistance(), encoderContext);
            value(getMapper(), writer, "minDistance", value.getMinDistance(), encoderContext);
            Filter[] filters = value.getFilters();
            if (filters != null) {
                document(writer, "query", () -> {
                    for (Filter filter : filters) {
                        filter.encode(getMapper(), writer, encoderContext);
                    }
                });
            }
            value(getMapper(), writer, "distanceMultiplier", value.getDistanceMultiplier(), encoderContext);
            value(writer, "includeLocs", value.getIncludeLocs());
        });
    }
}
