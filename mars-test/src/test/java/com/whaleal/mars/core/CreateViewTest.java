package com.whaleal.mars.core;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.CreateViewOptions;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Spces;
import com.whaleal.mars.bean.Vehicles;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-27 13:17
 **/
public class CreateViewTest {

    private Mars mars = new Mars(Constant.connectionStr);


    @Test
    public void createData(){

        List<Document> zip = CreateDataUtil.parseString("{ \"_id\" : \"01001\", \"city\" : \"AGAWAM\", \"loc\" : [ -72.622739, 42.070206 ], \"pop\" : 15338, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01002\", \"city\" : \"CUSHMAN\", \"loc\" : [ -72.51564999999999, 42.377017 ], \"pop\" : 36963, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01005\", \"city\" : \"BARRE\", \"loc\" : [ -72.10835400000001, 42.409698 ], \"pop\" : 4546, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01007\", \"city\" : \"BELCHERTOWN\", \"loc\" : [ -72.41095300000001, 42.275103 ], \"pop\" : 10579, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01008\", \"city\" : \"BLANDFORD\", \"loc\" : [ -72.936114, 42.182949 ], \"pop\" : 1240, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01010\", \"city\" : \"BRIMFIELD\", \"loc\" : [ -72.188455, 42.116543 ], \"pop\" : 3706, \"state\" : \"MA\" }");

        mars.insert(zip,"zip");

        List list = new ArrayList<>();
        for(int i =0; i < 10;i++){
            Vehicles vehicles = new Vehicles();
            vehicles.setId(i);
            vehicles.setType("a");
            Spces spec = new Spces();
            spec.setDoors(4);
            spec.setWheels(4);
            vehicles.setSpecs(spec);
            list.add(vehicles);
        }
        mars.insert(list,Vehicles.class);
    }

//    @After
//    public void dropData(){
//        mars.dropCollection("zip");
//        mars.dropCollection(Vehicles.class);
//
//    }

    @Test
    public void testForCreate() {
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.match(Filters.eq("state","MA"));
        MongoCollection<Document> view = mars.createView("test", "zip", pipeline, new CreateViewOptions());

        MongoCursor<Document> iterator = view.find().iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

    @Test
    public void createViewStudents(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.match(Filters.eq("year",1));

        mars.createView("firstYears","students",pipeline,new CreateViewOptions());
    }

    @Test
    public void createViewByClass(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.match(Filters.eq("type","a"));

        mars.createView("vehicles1",Vehicles.class,pipeline,new CreateViewOptions());
    }

    @Test
    public void dropView(){
        mars.dropCollection("test");

    }

}
