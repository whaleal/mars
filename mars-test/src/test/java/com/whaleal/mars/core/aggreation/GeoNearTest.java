package com.whaleal.mars.core.aggreation;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.GeoNear;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-16 16:47
 */
public class GeoNearTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();



    @Before
    public void createData(){
        String s = "{\"name\": \"Central Park\",\"location\": { \"type\": \"Point\", \"coordinates\": [ -73.97, 40.77 ] },\"category\": \"Parks\",\"legacy\" : [ -73.9375, 40.8303 ],}\n" +
                "{\"name\": \"Sara D. Roosevelt Park\",\"location\": { \"type\": \"Point\", \"coordinates\": [ -73.9928, 40.7193 ] },\"category\": \"Parks\",\"legacy\" : [ -73.9375, 40.8303 ],},\n" +
                "{\"name\": \"Polo Grounds\",\"location\": { \"type\": \"Point\", \"coordinates\": [ -73.9375, 40.8303 ] },\"category\": \"Stadiums\",\"legacy\" : [ -73.9375, 40.8303 ],}";

        List<Document> documents = CreateDataUtil.parseString(s);

        mars.insert(documents,"places");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("places");
    }

    @Test
    public void testForMaxDistance(){
        //给集合创建索引
        mars.createIndex(new Index().on("location", IndexDirection.GEO2DSPHERE),"places");

        pipeline.geoNear(GeoNear.geoNear(new Point(new Position(-73.99279 , 40.719296)))
                .distanceField("dist.calculated")
                .maxDistance(2)
                .query(Filters.eq("category","Parks"))
                .includeLocs("dist.location")
                .spherical(true));

        QueryCursor places = mars.aggregate(pipeline, "places");
        while (places.hasNext()){
            System.out.println(places.next());
        }
    }

    @Test
    public void testForMinDistance(){
        mars.createIndex(new Index().on("location", IndexDirection.GEO2DSPHERE),"places");

        pipeline.geoNear(GeoNear.to(new Point(new Position(-73.99279 , 40.719296)))
                .distanceField("dist.calculated")
                .minDistance(2)
                .query(Filters.eq("category","Parks"))
                .includeLocs("dist.location")
                .spherical(true));

        QueryCursor places = mars.aggregate(pipeline, "places");
        while (places.hasNext()){
            System.out.println(places.next());
        }
    }

    @Test
    public void testForTwoIndex(){
        mars.createIndex(new Index().on("location",IndexDirection.GEO2DSPHERE),"places");
        mars.createIndex(new Index().on("legacy",IndexDirection.GEO2D),"places");

        pipeline.geoNear(GeoNear.to(new Point(new Position(-73.98142 , 40.71782)))
                .key("location")
                .distanceField("dist.calculated")
                .query(Filters.eq("category","Parks"))
                );

        pipeline.limit(5);

        QueryCursor places = mars.aggregate(pipeline, "places");
        while (places.hasNext()){
            System.out.println(places.next());
        }

    }
}
