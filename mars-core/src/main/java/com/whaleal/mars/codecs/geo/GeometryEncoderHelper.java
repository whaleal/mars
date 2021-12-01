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

package com.whaleal.mars.codecs.geo;


import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;
import org.locationtech.jts.geom.*;
import static java.lang.String.format;

final class GeometryEncoderHelper {


    @SuppressWarnings("unchecked")
    static void encodeGeometry(final BsonWriter writer, final Geometry value, final EncoderContext encoderContext,
                               final CodecRegistry registry) {

        writer.writeStartDocument();
        writer.writeString("type", value.getGeometryType());

        if (value instanceof Point) {
            writer.writeName("coordinates");
            encodePoint(writer, (Point) value, encoderContext, registry);
        } else if (value instanceof MultiPoint) {
            writer.writeName("coordinates");
            encodeMultiPoint(writer, (MultiPoint) value, encoderContext, registry);
        } else if (value instanceof Polygon) {
            writer.writeName("coordinates");
            encodePolygon(writer, (Polygon) value, encoderContext, registry);
        } else if (value instanceof MultiPolygon) {
            writer.writeName("coordinates");
            encodeMultiPolygon(writer, (MultiPolygon) value, encoderContext, registry);
        } else if (value instanceof LineString) {
            writer.writeName("coordinates");
            encodeLineString(writer, (LineString) value, encoderContext, registry);
        } else if (value instanceof MultiLineString) {
            writer.writeName("coordinates");
            encodeMultiLineString(writer, (MultiLineString) value, encoderContext, registry);
        } else if (value instanceof GeometryCollection) {
            writer.writeName("geometries");
            encodeGeometryCollection(writer, (GeometryCollection) value, encoderContext, registry);
        } else {
            throw new CodecConfigurationException(format("Unsupported Geometry: %s", value));
        }


        //  todo  CRS
        //encodeCoordinateReferenceSystem(writer, value, encoderContext, registry);
        writer.writeEndDocument();
    }

    private static void encodePoint(final BsonWriter writer, final Point value,final EncoderContext encoderContext, final CodecRegistry registry) {

        encodeCoordinate(writer, value.getCoordinate(),encoderContext ,registry);
    }

    private static void encodeMultiPoint(final BsonWriter writer, final MultiPoint value ,final EncoderContext encoderContext,
                                         final CodecRegistry registry) {
        writer.writeStartArray();
        for (Coordinate position : value.getCoordinates()) {
            encodeCoordinate(writer, position,encoderContext ,registry);
        }
        writer.writeEndArray();
    }

    private static void encodePolygon(final BsonWriter writer, final Polygon value,final EncoderContext encoderContext,
                                      final CodecRegistry registry) {

        writer.writeStartArray();
        LinearRing exteriorRing = value.getExteriorRing();
        encodeLinearRing(writer,exteriorRing ,encoderContext ,registry);
        if(value.getNumInteriorRing() != 0){
            for(int i = 0 ; i< value.getNumInteriorRing() ;i++ ){
                LinearRing interiorRingN = value.getInteriorRingN(i);
                encodeLinearRing(writer,interiorRingN,encoderContext ,registry) ;
            }
        }

        writer.writeEndArray();

    }

    private static void encodeMultiPolygon(final BsonWriter writer, final MultiPolygon value,final EncoderContext encoderContext,
                                           final CodecRegistry registry) {
        writer.writeStartArray();
        //  todo  获取各个形状 然后 递归调用
        for(int i = 0 ; i <value.getNumGeometries();i++){

            Geometry geometry = value.getGeometryN(i);

            easyEncodeGeometry(writer, geometry, encoderContext, registry);

        }

        writer.writeEndArray();
    }

    /**
     *
     * [ [ 40, 5 ], [ 41, 6 ] ]
     * @param writer
     * @param value
     */
    private static void encodeLineString(final BsonWriter writer, final LineString value,final EncoderContext encoderContext,
                                         final CodecRegistry registry) {
        writer.writeStartArray();
        for (Coordinate position : value.getCoordinates()) {
            encodeCoordinate(writer, position,encoderContext ,registry);
        }
        writer.writeEndArray();
    }

    private static void encodeMultiLineString(final BsonWriter writer, final MultiLineString value,final EncoderContext encoderContext,
                                              final CodecRegistry registry) {
        writer.writeStartArray();

        for(int i = 0 ; i<value.getNumGeometries() ;i++){

            Geometry geometry  =  value.getGeometryN(i) ;

            easyEncodeGeometry(writer, geometry, encoderContext, registry);
        }

        writer.writeEndArray();
    }

    private static void encodeGeometryCollection(final BsonWriter writer, final GeometryCollection value,
                                                 final EncoderContext encoderContext, final CodecRegistry registry) {
        writer.writeStartArray();

        for(int i = 0 ; i< value.getNumGeometries();i++ ){
            Geometry geometry  =  value.getGeometryN(i) ;
            //  todo
            easyEncodeGeometry(writer, geometry, encoderContext, registry);
        }

        writer.writeEndArray();
    }





    private static void encodeLinearRing(final BsonWriter writer,final LinearRing ring ,final EncoderContext encoderContext,
                                         final CodecRegistry registry) {
        writer.writeStartArray();
        for (Coordinate position : ring.getCoordinates()) {
            encodeCoordinate(writer, position,encoderContext ,registry);
        }
        writer.writeEndArray();
    }



    /**
     *
     * 将 Coordinate  的 值按 x y  z  的顺序 依次写入 到数组中
     * 示例 [ -73.9814, 40.7681 ]
     * @param writer
     * @param value
     */
    static void encodeCoordinate(final BsonWriter writer, final Coordinate value,final EncoderContext encoderContext,
                                 final CodecRegistry registry) {
        writer.writeStartArray();


        writer.writeDouble(value.x);
        writer.writeDouble(value.y);

        writer.writeEndArray();
    }

    /**
     * 一个纯转换的内部方法
     * 不用 在 调用写 其他的东西
     * @param writer
     * @param value
     * @param encoderContext
     * @param registry
     */
    private static void  easyEncodeGeometry(final BsonWriter writer, final Geometry value, final EncoderContext encoderContext,
                                        final CodecRegistry registry){
        if (value instanceof Point) {

            encodePoint(writer, (Point) value, encoderContext, registry);
        } else if (value instanceof MultiPoint) {

            encodeMultiPoint(writer, (MultiPoint) value, encoderContext, registry);
        } else if (value instanceof Polygon) {

            encodePolygon(writer, (Polygon) value, encoderContext, registry);
        } else if (value instanceof MultiPolygon) {

            encodeMultiPolygon(writer, (MultiPolygon) value, encoderContext, registry);
        } else if (value instanceof LineString) {

            encodeLineString(writer, (LineString) value, encoderContext, registry);
        } else if (value instanceof MultiLineString) {

            encodeMultiLineString(writer, (MultiLineString) value, encoderContext, registry);
        } else if (value instanceof GeometryCollection) {

            encodeGeometryCollection(writer, (GeometryCollection) value, encoderContext, registry);
        } else {
            throw new CodecConfigurationException(format("Unsupported Geometry: %s", value));
        }

    }
    private GeometryEncoderHelper() {
    }
}
