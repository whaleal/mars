package com.whaleal.mars.core.query.filters;

import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.MultiPolygon;
import com.mongodb.client.model.geojson.Polygon;

import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

/**
 * Defines a $geoWithin filter.
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GeoWithinFilter extends Filter {
    private CoordinateReferenceSystem crs;

    GeoWithinFilter(String field, Polygon value) {
        super("$geoWithin", field, value);
    }

    GeoWithinFilter(String field, MultiPolygon value) {
        super("$geoWithin", field, value);
    }

    /**
     * @param crs the CoordinateReferenceSystem to use
     * @return this
     */
    public GeoWithinFilter crs(CoordinateReferenceSystem crs) {
        this.crs = crs;
        return this;
    }

    @Override
    public final void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(path(mapper.getMapper()));
        writer.writeStartDocument(getName());
        writer.writeName("$geometry");

        Object shape = getValue();
        if (shape != null) {
            Codec codec = mapper.getCodecRegistry().get(shape.getClass());
            codec.encode(writer, shape, context);
        }

        writer.writeEndDocument();
        writer.writeEndDocument();
    }
}
