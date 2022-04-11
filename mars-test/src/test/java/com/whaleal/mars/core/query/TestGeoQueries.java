package com.whaleal.mars.core.query;

import com.mongodb.client.model.geojson.MultiPoint;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @user Lyz
 * @description
 * @date 2022/3/9 15:57
 */
public class TestGeoQueries {

    private MongoMappingContext context;

    private Query query;

    @BeforeMethod
    public void before() {
//        Mars mars = new Mars(Constant.connectionStr);
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());
        query = new Query();
    }

    @Test
    public void testGeo2d() {
        //right
        Point point = new Point(new Position(-73.93414657, 40.82302903));
        Criteria criteria = new Criteria("geometry").geointersects(point);
        Document document = context.toDocument(criteria.getCriteriaObject());
        System.out.println(document);

        ///
//        Criteria location = new Criteria("location").geowithin(new Geometry() {
//            @Override
//            public GeoJsonObjectType getType() {
//                return null;
//            }
//        });
//        System.out.println(context.toDocument(location));

        Criteria location1 = new Criteria("location").withinSphere(point, 5 / 3963.2);
        System.out.println(context.toDocument(location1.getCriteriaObject()));

        Criteria criteria1 = new Criteria("location").nearSphere(point).maxDistance(5);
        System.out.println(context.toDocument(criteria1.getCriteriaObject()));

        Point point1 = new Point(new Position(-73.9667, 40.78));
        Criteria criteria2 = new Criteria("location").near(point1).minDistance(1000).maxDistance(5000);
        System.out.println(context.toDocument(criteria2.getCriteriaObject()));

        Criteria location4 = new Criteria("location").withinCenter(point, 5 / 3963.2);

        System.out.println(context.toDocument(location1.getCriteriaObject()));


        Position position = new Position(0, 0);
        Position position1 = new Position(3, 6);
        Position position2 = new Position(6, 0);
        Position position3 = new Position(0, 0);
        List< Position > objects = new ArrayList<>();
        objects.add(position);
        objects.add(position1);
        objects.add(position2);
        //objects.add(position3);

        //Criteria loc = new Criteria("loc").geowithin(new Polygon(new PolygonCoordinates(objects)));
        // System.out.println(context.toDocument(loc.getCriteriaObject()));

        Document loc1 = new Criteria("loc").withinPolygon(new MultiPoint(objects)).getCriteriaObject();
        System.out.println(context.toDocument(loc1));

    }
}
