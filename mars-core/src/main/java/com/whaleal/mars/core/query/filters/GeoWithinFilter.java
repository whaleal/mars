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
package com.whaleal.mars.core.query.filters;

import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.MultiPolygon;
import com.mongodb.client.model.geojson.Polygon;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

/**
 * Defines a $geoWithin filter.
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
    public final void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(path(mapper));
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
