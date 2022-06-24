package com.whaleal.mars.core.query.filters;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.lang.NonNull;

import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

class CenterFilter extends Filter {
    private final double radius;

    protected CenterFilter(String filterName, String field, Point value, double radius) {
        super(filterName, field, value);
        this.radius = radius;
    }

    @Override
    public void encode( MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(path(mapper));
        writer.writeStartDocument("$geoWithin");

        writer.writeStartArray(getName());
        Point center = getValue();
        writer.writeStartArray();
        for (Double value : center.getPosition().getValues()) {
            writer.writeDouble(value);
        }
        writer.writeEndArray();
        writer.writeDouble(radius);
        writer.writeEndArray();

        writer.writeEndDocument();
        writer.writeEndDocument();
    }

    @Override
    @NonNull
    public Point getValue() {
        Object value = super.getValue();
        if (value != null) {
            return (Point) value;
        }
        throw new NullPointerException();

    }
}
