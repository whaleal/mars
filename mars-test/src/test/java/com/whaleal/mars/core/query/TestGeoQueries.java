package com.whaleal.mars.core.query;

import com.mongodb.client.model.geojson.*;
import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

/**
 * @user Lyz
 * @description
 * @date 2022/3/9 15:57
 */
public class TestGeoQueries {

    private MongoMappingContext context;

    private Query query ;

    @Before
    public void before(){
//        Mars mars = new Mars(Constant.connectionStr);
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());
        query = new Query();
    }

    @Test
    public void testGeo2d(){
        //right
        Point point = new Point(new Position( -73.93414657,40.82302903));
        Criteria criteria = new Criteria("geometry").geointersects(point);
        Document document = context.toDocument(criteria);
        System.out.println(document);

        ///
//        Criteria location = new Criteria("location").geowithin(new Geometry() {
//            @Override
//            public GeoJsonObjectType getType() {
//                return null;
//            }
//        });
//        System.out.println(context.toDocument(location));

        Criteria location1 = new Criteria("location").geowithin(new Shape.Center("centerSphere",point,5 / 3963).getCenter());
        System.out.println(context.toDocument(location1));

        Criteria criteria1 = new Criteria("location").nearSphere(point).maxDistance(5);
        System.out.println(context.toDocument(criteria1));

        Point point1 = new Point(new Position(-73.9667, 40.78));
        Criteria criteria2 = new Criteria("location").near(point1).minDistance(1000).maxDistance(5000);
        System.out.println(context.toDocument(criteria2));


    }
}
