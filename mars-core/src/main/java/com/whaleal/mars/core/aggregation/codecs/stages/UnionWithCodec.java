package com.whaleal.mars.core.aggregation.codecs.stages;




import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.UnionWith;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;
import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.value;


/**
 * Encodes a UnionWith stage
 *
 */
public class UnionWithCodec extends StageCodec< UnionWith > {
    /**
     * Creates the codec
     *
     * @param mapper the mapper to use
     */

    public UnionWithCodec( MongoMappingContext mapper) {
        super(mapper);
    }

    @Override
    public Class<UnionWith> getEncoderClass() {
        return UnionWith.class;
    }

    @Override
    protected void encodeStage(BsonWriter writer, UnionWith unionWith, EncoderContext encoderContext) {
        String name = unionWith.getCollectionName();
        String collectionName = name != null ? name
                                             : getMapper().getMapper().getEntityModel(unionWith.getCollectionType()).getCollectionName();

        document(writer, () -> {
            value(writer, "coll", collectionName);
            value(getMapper(), writer, "pipeline", unionWith.getStages(), encoderContext);
        });
    }
}
