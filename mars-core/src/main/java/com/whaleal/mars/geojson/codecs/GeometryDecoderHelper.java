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
package com.whaleal.mars.geojson.codecs;


import com.mongodb.lang.Nullable;
import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

final class GeometryDecoderHelper {
    private static GeometryFactory  geometryFactory = new GeometryFactory();

    @SuppressWarnings("unchecked")
    static <T extends Geometry> T decodeGeometry(final BsonReader reader, final Class<T> clazz) {
        if (clazz.equals(Point.class)) {
            return (T) decodePoint(reader);
        } else if (clazz.equals(MultiPoint.class)) {
            return (T) decodeMultiPoint(reader);
        } else if (clazz.equals(Polygon.class)) {
            return (T) decodePolygon(reader);
        } else if (clazz.equals(MultiPolygon.class)) {
            return (T) decodeMultiPolygon(reader);
        } else if (clazz.equals(LineString.class)) {
            return (T) decodeLineString(reader);
        } else if (clazz.equals(MultiLineString.class)) {
            return (T) decodeMultiLineString(reader);
        } else if (clazz.equals(GeometryCollection.class)) {
            return (T) decodeGeometryCollection(reader);
        } else if (clazz.equals(Geometry.class)) {
            return (T) decodeGeometry(reader);
        }

        throw new CodecConfigurationException(format("Unsupported Geometry: %s", clazz));
    }

    private static Point decodePoint(final BsonReader reader) {

        String type = null;
        Coordinate Coordinate = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
            } else if (key.equals("Coordinates")) {
                Coordinate = decodeCoordinate(reader);
            } else if (key.equals("crs")) {
                continue;
            } else {
                throw new CodecConfigurationException(format("Unexpected key '%s' found when decoding a GeoJSON point", key));
            }
        }
        reader.readEndDocument();

        if (type == null) {
            throw new CodecConfigurationException("Invalid Point, document contained no type information.");
        } else if (!type.equals("Point")) {
            throw new CodecConfigurationException(format("Invalid Point, found type '%s'.", type));
        } else if (Coordinate == null) {
            throw new CodecConfigurationException("Invalid Point, missing Coordinate Coordinates.");
        }
        return null ;
    }

    private static MultiPoint decodeMultiPoint(final BsonReader reader) {
        String type = null;
        List<Coordinate> Coordinates = null;
        Object crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
            } else if (key.equals("Coordinates")) {
                Coordinates = decodeCoordinates(reader);
            } else if (key.equals("crs")) {
                crs = decodeObject(reader);
            } else {
                throw new CodecConfigurationException(format("Unexpected key '%s' found when decoding a GeoJSON point", key));
            }
        }
        reader.readEndDocument();

        if (type == null) {
            throw new CodecConfigurationException("Invalid MultiPoint, document contained no type information.");
        } else if (!type.equals("MultiPoint")) {
            throw new CodecConfigurationException(format("Invalid MultiPoint, found type '%s'.", type));
        } else if (Coordinates == null) {
            throw new CodecConfigurationException("Invalid MultiPoint, missing Coordinate Coordinates.");
        }
        //return crs != null ? new MultiPoint(crs, Coordinates) : new MultiPoint(Coordinates);
        return null ;
    }

    private static Polygon decodePolygon(final BsonReader reader) {
        String type = null;
        Polygon Coordinates = null;
        Object crs = null;

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
            } else if (key.equals("Coordinates")) {
                Coordinates = decodePolygonCoordinates(reader);

            } else if (key.equals("crs")) {
                crs = decodeObject(reader);

            } else {
                throw new CodecConfigurationException(format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
            }
        }
        reader.readEndDocument();

        if (type == null) {
            throw new CodecConfigurationException("Invalid Polygon, document contained no type information.");
        } else if (!type.equals("Polygon")) {
            throw new CodecConfigurationException(format("Invalid Polygon, found type '%s'.", type));
        } else if (Coordinates == null) {
            throw new CodecConfigurationException("Invalid Polygon, missing Coordinates.");
        }
        //return crs != null ? new Polygon(crs, Coordinates) : new Polygon(Coordinates);



        return null ;
    }

    private static MultiPolygon decodeMultiPolygon(final BsonReader reader) {
        String type = null;
        List<Polygon> Coordinates = null;
        Object crs = null;

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
            } else if (key.equals("Coordinates")) {
                Coordinates = decodeMultiPolygonCoordinates(reader);

            } else if (key.equals("crs")) {
                crs = decodeObject(reader);
            } else {
                throw new CodecConfigurationException(format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
            }
        }
        reader.readEndDocument();

        if (type == null) {
            throw new CodecConfigurationException("Invalid MultiPolygon, document contained no type information.");
        } else if (!type.equals("MultiPolygon")) {
            throw new CodecConfigurationException(format("Invalid MultiPolygon, found type '%s'.", type));
        } else if (Coordinates == null) {
            throw new CodecConfigurationException("Invalid MultiPolygon, missing Coordinates.");
        }

        return geometryFactory.createMultiPolygon((Polygon []) Coordinates.toArray());

    }

    private static LineString decodeLineString(final BsonReader reader) {
        String type = null;
        List<Coordinate> Coordinates = null;
        Object crs = null;

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
            } else if (key.equals("Coordinates")) {
                Coordinates = decodeCoordinates(reader);
            } else if (key.equals("crs")) {
                crs = decodeObject(reader);
            } else {
                throw new CodecConfigurationException(format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
            }
        }
        reader.readEndDocument();

        if (type == null) {
            throw new CodecConfigurationException("Invalid LineString, document contained no type information.");
        } else if (!type.equals("LineString")) {
            throw new CodecConfigurationException(format("Invalid LineString, found type '%s'.", type));
        } else if (Coordinates == null) {
            throw new CodecConfigurationException("Invalid LineString, missing Coordinates.");
        }


        return  geometryFactory.createLineString((Coordinate[]) Coordinates.toArray());


    }

    private static MultiLineString decodeMultiLineString(final BsonReader reader) {
        String type = null;
        List<List<Coordinate>> Coordinates = null;
        Object crs = null;

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
            } else if (key.equals("Coordinates")) {
                Coordinates = decodeMultiCoordinates(reader);
            } else if (key.equals("crs")) {
                crs = decodeObject(reader);
            } else {
                throw new CodecConfigurationException(format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
            }
        }
        reader.readEndDocument();

        if (type == null) {
            throw new CodecConfigurationException("Invalid MultiLineString, document contained no type information.");
        } else if (!type.equals("MultiLineString")) {
            throw new CodecConfigurationException(format("Invalid MultiLineString, found type '%s'.", type));
        } else if (Coordinates == null) {
            throw new CodecConfigurationException("Invalid MultiLineString, missing Coordinates.");
        }

        LineString [] lineStrings = new LineString[Coordinates.size()] ;

        for(int i = 0 ; i< Coordinates.size() ; i++){
            List<Coordinate> coordinates = Coordinates.get(i);

            lineStrings[i] =  geometryFactory.createLineString((Coordinate[]) coordinates.toArray());

        }


        return   geometryFactory.createMultiLineString(lineStrings) ;

    }

    private static GeometryCollection decodeGeometryCollection(final BsonReader reader) {
        String type = null;
        List<? extends Geometry> geometries = null;
        Object crs = null;

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
            } else if (key.equals("geometries")) {
                geometries = decodeGeometries(reader);
            } else if (key.equals("crs")) {
                crs = decodeObject(reader);
            } else {
                throw new CodecConfigurationException(format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
            }
        }
        reader.readEndDocument();

        if (type == null) {
            throw new CodecConfigurationException("Invalid GeometryCollection, document contained no type information.");
        } else if (!type.equals("GeometryCollection")) {
            throw new CodecConfigurationException(format("Invalid GeometryCollection, found type '%s'.", type));
        } else if (geometries == null) {
            throw new CodecConfigurationException("Invalid GeometryCollection, missing geometries.");
        }
        //return crs != null ? new GeometryCollection(crs, geometries) : new GeometryCollection(geometries);

        return  null ;

    }

    private static List<? extends Geometry> decodeGeometries(final BsonReader reader) {
        validateIsArray(reader);
        reader.readStartArray();
        List<Geometry> values = new ArrayList<Geometry>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            Geometry geometry = decodeGeometry(reader);
            values.add(geometry);
        }
        reader.readEndArray();


        return values;
    }

    private static Geometry decodeGeometry(final BsonReader reader) {
        String type = null;
        BsonReaderMark mark = reader.getMark();
        validateIsDocument(reader);
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                break;
            } else {
                reader.skipValue();
            }
        }
        mark.reset();

        if (type == null) {
            throw new CodecConfigurationException("Invalid Geometry item, document contained no type information.");
        }
        Geometry geometry = null;
        if (type.equals("Point")) {
            geometry = decodePoint(reader);
        } else if (type.equals("MultiPoint")) {
            geometry = decodeMultiPoint(reader);
        } else if (type.equals("Polygon")) {
            geometry = decodePolygon(reader);
        } else if (type.equals("MultiPolygon")) {
            geometry = decodeMultiPolygon(reader);
        } else if (type.equals("LineString")) {
            geometry = decodeLineString(reader);
        } else if (type.equals("MultiLineString")) {
            geometry = decodeMultiLineString(reader);
        } else if (type.equals("GeometryCollection")) {
            geometry = decodeGeometryCollection(reader);
        } else {
            throw new CodecConfigurationException(format("Invalid Geometry item, found type '%s'.", type));
        }
        return geometry;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Polygon decodePolygonCoordinates(final BsonReader reader) {
        validateIsArray(reader);
        reader.readStartArray();
        List<List<Coordinate>> values = new ArrayList<List<Coordinate>>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(decodeCoordinates(reader));
        }
        reader.readEndArray();

        if (values.isEmpty()) {
            throw new CodecConfigurationException("Invalid Polygon no Coordinates.");
        }

        List<Coordinate> exterior = values.remove(0);
        ArrayList[] holes = values.toArray(new ArrayList[values.size()]);

        try {
            //return new Polygon(exterior, holes);
            return  null ;
        } catch (IllegalArgumentException e) {
            throw new CodecConfigurationException(format("Invalid Polygon: %s", e.getMessage()));
        }
    }

    private static List<Polygon> decodeMultiPolygonCoordinates(final BsonReader reader) {
        validateIsArray(reader);
        reader.readStartArray();
        List<Polygon> values = new ArrayList<Polygon>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(decodePolygonCoordinates(reader));
        }
        reader.readEndArray();

        if (values.isEmpty()) {
            throw new CodecConfigurationException("Invalid MultiPolygon no Coordinates.");
        }
        return values;
    }

    private static List<Coordinate> decodeCoordinates(final BsonReader reader) {
        validateIsArray(reader);
        reader.readStartArray();
        List<Coordinate> values = new ArrayList<Coordinate>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(decodeCoordinate(reader));
        }
        reader.readEndArray();
        return values;
    }

    private static List<List<Coordinate>> decodeMultiCoordinates(final BsonReader reader) {
        validateIsArray(reader);
        reader.readStartArray();
        List<List<Coordinate>> values = new ArrayList<List<Coordinate>>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(decodeCoordinates(reader));
        }
        reader.readEndArray();
        return values;
    }

    private static Coordinate decodeCoordinate(final BsonReader reader) {
        validateIsArray(reader);
        reader.readStartArray();
        List<Double> values = new ArrayList<Double>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(readAsDouble(reader));
        }
        reader.readEndArray();

        try {
            if(values.size() ==2){
                return new Coordinate(values.get(0),values.get(1));
            }else {
                throw  new IllegalArgumentException("value  in array  is :  %s" +values);
            }

        } catch (IllegalArgumentException e) {
            throw new CodecConfigurationException(format("Invalid Coordinate: %s", e.getMessage()));
        }
    }

    private static double readAsDouble(final BsonReader reader) {
        if (reader.getCurrentBsonType() == BsonType.DOUBLE) {
           return reader.readDouble();
        } else if (reader.getCurrentBsonType() == BsonType.INT32) {
            return reader.readInt32();
        } else if (reader.getCurrentBsonType() == BsonType.INT64) {
            return reader.readInt64();
        }

        throw new CodecConfigurationException("A GeoJSON Coordinate value must be a numerical type, but the value is of type "
                + reader.getCurrentBsonType());
    }

    @Nullable
    static Object decodeObject(final BsonReader reader) {
        String crsName = null;
        validateIsDocument(reader);
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals("type")) {
                String type = reader.readString();
                if (!type.equals("name")) {
                    throw new CodecConfigurationException(format("Unsupported Object '%s'.", type));
                }
            } else if (name.equals("properties")) {
                crsName = decodeObjectProperties(reader);
            } else {
                throw new CodecConfigurationException(format("Found invalid key '%s' in the Object.", name));
            }
        }
        reader.readEndDocument();

        return null ;
    }

    private static String decodeObjectProperties(final BsonReader reader) {
        String crsName = null;
        validateIsDocument(reader);
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals("name")) {
                crsName = reader.readString();
            } else {
                throw new CodecConfigurationException(format("Found invalid key '%s' in the Object.", name));
            }
        }
        reader.readEndDocument();

        if (crsName == null) {
            throw new CodecConfigurationException("Found invalid properties in the Object.");
        }
        return crsName;
    }

    private static void validateIsDocument(final BsonReader reader) {
        BsonType currentType = reader.getCurrentBsonType();
        if (currentType == null) {
            currentType = reader.readBsonType();
        }
        if (!currentType.equals(BsonType.DOCUMENT)) {
            throw new CodecConfigurationException("Invalid BsonType expecting a Document");
        }
    }

    private static void validateIsArray(final BsonReader reader) {
        if (reader.getCurrentBsonType() != BsonType.ARRAY) {
            throw new CodecConfigurationException("Invalid BsonType expecting an Array");
        }
    }

    private GeometryDecoderHelper() {
    }
}
