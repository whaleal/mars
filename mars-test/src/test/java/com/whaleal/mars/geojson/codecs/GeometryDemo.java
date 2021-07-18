package com.whaleal.mars.geojson.codecs;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;


public class GeometryDemo {

    private GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * create a point
     * @return
     */
    public Point createPoint(){
        Coordinate coord = new Coordinate(109.013388, 32.715519);
        Point point = geometryFactory.createPoint( coord );

        return point;
    }

    /**
     * create a point by WKT
     * @return
     * @throws ParseException
     */
    public Point createPointByWKT() throws ParseException {
        WKTReader reader = new WKTReader( geometryFactory );
        Point point = (Point) reader.read("POINT (109.013388 32.715519)");
        return point;
    }

    /**
     * create multiPoint by wkt
     * @return
     */
    public MultiPoint createMulPointByWKT(){
        WKTReader reader = new WKTReader( geometryFactory );
        try {

            MultiPoint mpoint = (MultiPoint) reader.read("MULTIPOINT(109.013388 32.715519,119.32488 31.435678)");

            return mpoint;

        }catch (ParseException e){
            e.printStackTrace();
        }

        return null ;

    }
    /**
     *
     * create a line
     * @return
     */
    public LineString createLine(){
        Coordinate[] coords  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
        LineString line = geometryFactory.createLineString(coords);

        return line;
    }

    /**
     * create a line by WKT
     * @return
     * @throws ParseException
     */
    public LineString createLineByWKT() throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        LineString line = (LineString) reader.read("LINESTRING(0 0, 2 0)");
        return line;
    }

    /**
     * create multiLine
     * @return
     */
    public MultiLineString createMLine(){
        Coordinate[] coords1  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
        LineString line1 = geometryFactory.createLineString(coords1);
        Coordinate[] coords2  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
        LineString line2 = geometryFactory.createLineString(coords2);
        LineString[] lineStrings = new LineString[2];
        lineStrings[0]= line1;
        lineStrings[1] = line2;
        MultiLineString ms = geometryFactory.createMultiLineString(lineStrings);
        return ms;
    }

    /**
     * create multiLine by WKT
     * @return
     * @throws ParseException
     */
    public MultiLineString createMLineByWKT(){
        WKTReader reader = new WKTReader( geometryFactory );
        try {
            MultiLineString line = (MultiLineString) reader.read("MULTILINESTRING((0 0, 2 0),(1 1,2 2))");

            return line;
        }catch (ParseException e){

        }

        return null ;

    }

    /**
     * create a polygon(多边形) by WKT
     * @return
     * @throws ParseException
     */
    public Polygon createPolygonByWKT(){
        WKTReader reader = new WKTReader( geometryFactory );
        try {
            Polygon polygon = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
            return polygon;
        }catch (ParseException e){
            e.printStackTrace();
        }

        return  null ;

    }

    /**
     * create multi polygon by wkt
     * @return
     * @throws ParseException
     */
    public MultiPolygon createMulPolygonByWKT() {
        WKTReader reader = new WKTReader( geometryFactory );
        try {
            MultiPolygon mpolygon = (MultiPolygon) reader.read("MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10),(30 10, 30 0, 40 10, 30 20, 30 10)))");
            return mpolygon;
        }catch (ParseException e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * create GeometryCollection  contain point or multiPoint or line or multiLine or polygon or multiPolygon
     * @return
     * @throws ParseException
     */
    public GeometryCollection createGeoCollect() throws ParseException{
        LineString line = createLine();
        Polygon poly =  createPolygonByWKT();
        Geometry g1 = geometryFactory.createGeometry(line);
        Geometry g2 = geometryFactory.createGeometry(poly);
        Geometry[] garray = new Geometry[]{g1,g2};
        GeometryCollection gc = geometryFactory.createGeometryCollection(garray);
        return gc;
    }

    /**
     * create a Circle  创建一个圆，圆心(x,y) 半径RADIUS
     * @param x
     * @param y
     * @param RADIUS
     * @return
     */
    public Polygon createCircle(double x, double y, final double RADIUS){
        final int SIDES = 32;//圆上面的点个数
        Coordinate coords[] = new Coordinate[SIDES+1];
        for( int i = 0; i < SIDES; i++){
            double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
            double dx = Math.cos( angle ) * RADIUS;
            double dy = Math.sin( angle ) * RADIUS;
            coords[i] = new Coordinate( (double) x + dx, (double) y + dy );
        }
        coords[SIDES] = coords[0];
        LinearRing ring = geometryFactory.createLinearRing( coords );
        Polygon polygon = geometryFactory.createPolygon( ring, null );
        return polygon;
    }

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        GeometryDemo gt = new GeometryDemo();
        Polygon p = gt.createCircle(0, 1, 2);
        //圆上所有的坐标(32个)
        Coordinate coords[] = p.getCoordinates();
        for(Coordinate coord:coords){
            System.out.println(coord.x+","+coord.y);
        }
    }
}
