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
package com.whaleal.mars.core.aggregation.stages;

import com.mongodb.client.model.geojson.Point;
import com.whaleal.mars.core.query.filters.Filter;


public class GeoNear extends Stage {
    private Point point;
    private double[] coordinates;
    private String distanceField;
    private Boolean spherical;
    private Number maxDistance;
    private Filter[] filters;
    private Number distanceMultiplier;
    private String includeLocs;
    private Number minDistance;
    private String key;

    protected GeoNear(Point point) {
        this();
        this.point = point;
    }

    protected GeoNear() {
        super("$geoNear");
    }

    protected GeoNear(double[] coordinates) {
        this();
        this.coordinates = coordinates;
    }


    public static GeoNear to(Point point) {
        return new GeoNear(point);
    }


    public static GeoNear to(double[] coordinates) {
        return new GeoNear(coordinates);
    }


    public GeoNear distanceField(String distanceField) {
        this.distanceField = distanceField;
        return this;
    }


    public GeoNear distanceMultiplier(Number distanceMultiplier) {
        this.distanceMultiplier = distanceMultiplier;
        return this;
    }


    public double[] getCoordinates() {
        return coordinates;
    }


    public String getDistanceField() {
        return distanceField;
    }

    public Number getDistanceMultiplier() {
        return distanceMultiplier;
    }


    public Filter[] getFilters() {
        return filters;
    }


    public String getIncludeLocs() {
        return includeLocs;
    }


    public String getKey() {
        return key;
    }


    public Number getMaxDistance() {
        return maxDistance;
    }


    public Number getMinDistance() {
        return minDistance;
    }


    public Point getPoint() {
        return point;
    }


    public Boolean getSpherical() {
        return spherical;
    }


    public GeoNear includeLocs(String includeLocs) {
        this.includeLocs = includeLocs;
        return this;
    }


    public GeoNear key(String key) {
        this.key = key;
        return this;
    }


    public GeoNear maxDistance(Number maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }


    public GeoNear minDistance(Number minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    public GeoNear query(Filter... filters) {
        this.filters = filters;
        return this;
    }


    public GeoNear spherical(Boolean spherical) {
        this.spherical = spherical;
        return this;
    }
}
