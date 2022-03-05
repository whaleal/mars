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
package com.whaleal.mars.core.aggregation.stages.filters;

import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.Point;
import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.Map;

/**
 * Defines a filter for $near and $nearSphere queries
 */
public class NearFilter extends Filter {
    private Double maxDistance;
    private Double minDistance;
    private CoordinateReferenceSystem crs;

    NearFilter(String filterName, String field, Point point) {
        super(filterName, field, point);
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(path(mapper));
        if (isNot()) {
            writer.writeStartDocument("$not");
        }
        writer.writeStartDocument(getName());
        writer.writeName("$geometry");
        writeUnnamedValue(getValue(mapper), mapper, writer, context);
        if (maxDistance != null) {
            writeNamedValue("$maxDistance", maxDistance, mapper, writer, context);
        }
        if (minDistance != null) {
            writeNamedValue("$minDistance", minDistance, mapper, writer, context);
        }
        if (crs != null) {
            writeNamedValue("crs", crs, mapper, writer, context);
        }
        writer.writeEndDocument();
        if (isNot()) {
            writer.writeEndDocument();
        }
        writer.writeEndDocument();
    }

    /**
     * Sets the max distance to consider
     *
     * @param maxDistance the max
     * @return this
     */
    public NearFilter maxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    /**
     * Sets the min distance to consider
     *
     * @param minDistance the min
     * @return this
     */
    public NearFilter minDistance(Double minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    /**
     * Sets the coordinate reference system to use
     *
     * @param crs the crs
     * @return this
     */
    public NearFilter crs(CoordinateReferenceSystem crs) {
        this.crs = crs;
        return this;
    }

    /**
     * @param opts the options to apply
     */
    public void applyOpts(Map opts) {
        maxDistance = (Double) opts.get("$maxDistance");
        minDistance = (Double) opts.get("$minDistance");
    }

}
