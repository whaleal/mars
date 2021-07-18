package com.whaleal.mars.core.query;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.core.aggregation.stages.filters.Filter;
import com.whaleal.mars.core.aggregation.stages.filters.Filters;
import com.whaleal.mars.geojson.codecs.GeometryDemo;
import org.bson.Document;
import org.junit.Test;

public class CriteriaTest {

    public void init(){

    }

    @Test
    public void test01(){

        Criteria criteria = Criteria.where("d").is("dad").and("m").is("mum").and("tall").gt("199").lt("220");

       //Criteria criteria = Criteria.where("d").lt("100").gt("200");

        Document criteriaObject = criteria.getCriteriaObject();

        System.out.println(criteriaObject);






    }


    @Test
    public void test02(){
        Filter a = Filters.eq("a", "bb");


        Filter b =Filters.eq("a","1100");

        Filter filter = Filters.elemMatch("a", Filters.eq("a", 10), Filters.lt("b", 15));


        System.out.println(filter);
    }



    @Test
    public void test11(){

        GeometryDemo  demo = new GeometryDemo();

        Criteria location = Criteria.where("location").geowithin(demo.createPolygonByWKT());

        Document criteriaObject = location.getCriteriaObject();

        System.out.println(criteriaObject);

    }

    @Test
    public void test12(){

        GeometryDemo  demo = new GeometryDemo();

        Criteria location = Criteria.where("location").geowithin(demo.createPolygonByWKT());

        Document criteriaObject = location.getCriteriaObject();


        MongoClient client = MongoClients.create(Constant.server101);


        MongoMappingContext mappingContext = new MongoMappingContext(client.getDatabase("mars"));
        MongoDatabase mars = client.getDatabase("mars").withCodecRegistry(mappingContext.getCodecRegistry());

        Document documents = mars.getCollection("places").find(criteriaObject).first();

        System.out.println(documents);

    }


    @Test
    public void test13(){

        GeometryDemo  demo = new GeometryDemo();

        Criteria location = Criteria.where("location").geowithin(demo.createPolygonByWKT());

        Document criteriaObject = location.getCriteriaObject();

        System.out.println(criteriaObject);

    }

    @Test
    public void test14(){

        GeometryDemo  demo = new GeometryDemo();

        Criteria location = Criteria.where("location").geowithin(demo.createPolygonByWKT());

        Document criteriaObject = location.getCriteriaObject();

        System.out.println(criteriaObject);

    }
}
