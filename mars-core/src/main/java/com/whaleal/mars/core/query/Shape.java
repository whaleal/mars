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


import com.mongodb.client.model.geojson.Point;

/**
 * This encapsulates the data necessary to define a shape for queries.
 * @deprecated use the driver provide facilities instead.
 *
 *
 */
@Deprecated()
public class Shape {
    private final String geometry;
    private final Point[] points;

    Shape( String geometry, Point... points) {
        this.geometry = geometry;
        this.points = points;
    }

    /**
     * Specifies a rectangle for a geospatial $geoWithin query to return documents that are within the bounds of the rectangle,
     * according to their point-based location data.
     *
     * @param bottomLeft the bottom left bound
     * @param upperRight the upper right bound
     * @return the box
     * @mongodb.driver.manual reference/operator/query/box/ $box
     * @mongodb.driver.manual reference/operator/query/geoWithin/ $geoWithin
     */
    public static Shape box(Point bottomLeft, Point upperRight) {
        return new Shape("$box", bottomLeft, upperRight);
    }

    /**
     * Specifies a circle for a $geoWithin query.
     *
     * @param center the center of the circle
     * @param radius the radius circle
     * @return the box
     * @mongodb.driver.manual reference/operator/query/center/ $center
     * @mongodb.driver.manual reference/operator/query/geoWithin/ $geoWithin
     */
    public static Shape center(Point center, double radius) {
        return new Center("$center", center, radius);
    }

    /**
     * Specifies a circle for a geospatial query that uses spherical geometry.
     *
     * @param center the center of the circle
     * @param radius the radius circle
     * @return the box
     * @mongodb.driver.manual reference/operator/query/centerSphere/ $centerSphere
     */
    public static Shape centerSphere(Point center, double radius) {
        return new Center("$centerSphere", center, radius);
    }

    /**
     * Specifies a polygon for a geospatial $geoWithin query on legacy coordinate pairs.
     *
     * @param points the points of the polygon
     * @return the box
     * @mongodb.driver.manual reference/operator/query/polygon/ $polygon
     * @mongodb.driver.manual reference/operator/query/geoWithin/ $geoWithin
     */
    public static Shape polygon(Point... points) {
        return new Shape("$polygon", points);
    }

    /**
     * @return the geometry of the shape
     */
    public String getGeometry() {
        return geometry;
    }

    /**
     * @return the points of the shape
     */
    public Point[] getPoints() {
        return copy(points);
    }

    private Point[] copy(Point[] array) {
        Point[] copy = new Point[array.length];
        System.arraycopy(array, 0, copy, 0, array.length);
        return copy;
    }

    /**
     *
     */
    public static class Center extends Shape {
        private final Point center;
        private final double radius;

        Center(String geometry, Point center, double radius) {
            super(geometry);
            this.center = center;
            this.radius = radius;
        }

        /**
         * @return the center
         */
        public Point getCenter() {
            return center;
        }

        /**
         * @return the radius
         */
        public double getRadius() {
            return radius;
        }
    }
}
