package com.whaleal.mars.geojson.codecs;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;


public class GeometryRelated {

    private GeometryFactory geometryFactory = new GeometryFactory();

    /**
     *  两个几何对象是否是重叠的
     * @return
     * @throws ParseException
     */
    public boolean equalsGeo() throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        LineString geometry1 = (LineString) reader.read("LINESTRING(0 0, 2 0, 5 0)");
        LineString geometry2 = (LineString) reader.read("LINESTRING(5 0, 0 0)");
        return geometry1.equals(geometry2);//true
    }

    /**
     * 几何对象没有交点(相邻)
     * @return
     * @throws ParseException
     */
    public boolean disjointGeo() throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        LineString geometry1 = (LineString) reader.read("LINESTRING(0 0, 2 0, 5 0)");
        LineString geometry2 = (LineString) reader.read("LINESTRING(0 1, 0 2)");
        return geometry1.disjoint(geometry2);
    }

    /**
     * 至少一个公共点(相交)
     * @return
     * @throws ParseException
     */
    public boolean intersectsGeo() throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        LineString geometry1 = (LineString) reader.read("LINESTRING(0 0, 2 0, 5 0)");
        LineString geometry2 = (LineString) reader.read("LINESTRING(0 0, 0 2)");
        Geometry interPoint = geometry1.intersection(geometry2);//相交点
        System.out.println(interPoint.toText());//输出 POINT (0 0)
        return geometry1.intersects(geometry2);
    }

    /**
     * 判断以x,y为坐标的点point(x,y)是否在geometry表示的Polygon中
     * @param x
     * @param y
     * @param geometry wkt格式
     * @return
     */
    public boolean withinGeo(double x,double y,String geometry) throws ParseException {

        Coordinate coord = new Coordinate(x,y);
        Point point = geometryFactory.createPoint( coord );

        WKTReader reader = new WKTReader( geometryFactory );
        Polygon polygon = (Polygon) reader.read(geometry);
        return point.within(polygon);
    }
    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        GeometryRelated gr = new GeometryRelated();
        System.out.println(gr.equalsGeo());
        System.out.println(gr.disjointGeo());
        System.out.println(gr.intersectsGeo());
        System.out.println(gr.withinGeo(5,5,"POLYGON((0 0, 10 0, 10 10, 0 10,0 0))"));
    }

}

