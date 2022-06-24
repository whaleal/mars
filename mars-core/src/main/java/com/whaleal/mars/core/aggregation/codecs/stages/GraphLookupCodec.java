package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.GraphLookup;
import com.whaleal.mars.core.query.filters.Filter;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.*;


public class GraphLookupCodec extends StageCodec< GraphLookup > {
    public GraphLookupCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<GraphLookup> getEncoderClass() {
        return GraphLookup.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, GraphLookup value, EncoderContext encoderContext) {
        document(writer, () -> {
            if (value.getFrom() != null) {
                value(writer, "from", value.getFrom());
            } else {
                writer.writeString("from", getMapper().getMapper().getEntityModel(value.getFromType()).getCollectionName());
            }
            expression(getMapper(), writer, "startWith", value.getStartWith(), encoderContext);
            value(writer, "connectFromField", value.getConnectFromField());
            value(writer, "connectToField", value.getConnectToField());
            value(writer, "as", value.getAs());
            value(writer, "maxDepth", value.getMaxDepth());
            value(writer, "depthField", value.getDepthField());
            Filter[] restriction = value.getRestriction();
            if (restriction != null) {
                document(writer, "restrictSearchWithMatch", () -> {
                    for (Filter filter : restriction) {
                        filter.encode(getMapper(), writer, encoderContext);
                    }
                });
            }
        });
    }
}
