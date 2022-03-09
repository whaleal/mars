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
package com.whaleal.mars.core.query;

import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.lang.Nullable;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.EntityModel;

import com.whaleal.mars.codecs.writer.DocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

/**
 * Creates queries for GeoJson geo queries on MongoDB. These queries generally require MongoDB 2.4 and above, and usually work on 2d sphere
 * indexes.
 */
@SuppressWarnings("removal")
final class Geo2dSphereCriteria extends FieldCriteria {

    private final Geometry geometry;
    private Document options;
    private CoordinateReferenceSystem crs;

    private Geo2dSphereCriteria( MongoMappingContext mapper, String field, FilterOperator operator,
                                 Geometry geometry, EntityModel model, boolean validating) {
        super(mapper, field, operator, geometry, model, validating);
        this.geometry = geometry;
    }

    static Geo2dSphereCriteria geo( MongoMappingContext mapper, String field, FilterOperator operator,
                                    Geometry value, EntityModel model, boolean validating) {
        return new Geo2dSphereCriteria(mapper, field, operator, value, model, validating);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Document toDocument() {
        Document query;
        FilterOperator operator = getOperator();
        DocumentWriter writer = new DocumentWriter();
        ((Codec) getMapper().getCodecRegistry().get(geometry.getClass()))
            .encode(writer, geometry, EncoderContext.builder().build());
        Document document = new Document("$geometry", writer.getDocument());

        if (operator == FilterOperator.NEAR || operator == FilterOperator.NEAR_SPHERE) {
            if (options != null) {
                document.putAll(options);
            }
            query = new Document(FilterOperator.NEAR.val(), document);
        } else if (operator == FilterOperator.GEO_WITHIN || operator == FilterOperator.INTERSECTS) {
            query = new Document(operator.val(), document);
            if (crs != null) {
                ((Document) document.get("$geometry")).put("crs", crs);
            }
        } else {
            throw new UnsupportedOperationException(String.format("Operator %s not supported for geo-query", operator.val()));
        }

        return new Document(getField(), query);
    }

    Geo2dSphereCriteria addCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        this.crs = crs;
        return this;
    }

    private Geo2dSphereCriteria manageOption(String key, @Nullable Object value) {
        if (options == null) {
            options = new Document();
        }
        if (value == null) {
            options.remove(key);
        } else {
            options.put(key, value);
        }

        return this;
    }

    Geo2dSphereCriteria maxDistance(@Nullable Double maxDistance) {
        return manageOption("$maxDistance", maxDistance);
    }

    Geo2dSphereCriteria minDistance(@Nullable Double minDistance) {
        return manageOption("$minDistance", minDistance);
    }
}
