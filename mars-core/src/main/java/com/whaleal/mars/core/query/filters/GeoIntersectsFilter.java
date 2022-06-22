package com.whaleal.mars.core.query.filters;

import com.mongodb.client.model.geojson.Geometry;

import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 *
 */
public class GeoIntersectsFilter extends Filter {
    GeoIntersectsFilter(String field, Geometry val) {
        super("$geoIntersects", field, val);
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(path(mapper.getMapper()));
        if (isNot()) {
            writer.writeStartDocument("$not");
        }
        writer.writeStartDocument(getName());
        writer.writeName("$geometry");
        writeUnnamedValue(getValue(mapper), mapper, writer, context);
        writer.writeEndDocument();
        if (isNot()) {
            writer.writeEndDocument();
        }
        writer.writeEndDocument();
    }
}
