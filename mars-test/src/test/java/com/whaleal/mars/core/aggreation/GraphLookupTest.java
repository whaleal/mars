package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.GraphLookup;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;

/**
 * @author lyz
 * @desc
 * @date 2022-05-16 22:17
 */
public class GraphLookupTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"name\" : \"Dev\" }\n" +
                "{ \"_id\" : 2, \"name\" : \"Eliot\", \"reportsTo\" : \"Dev\" }\n" +
                "{ \"_id\" : 3, \"name\" : \"Ron\", \"reportsTo\" : \"Eliot\" }\n" +
                "{ \"_id\" : 4, \"name\" : \"Andrew\", \"reportsTo\" : \"Eliot\" }\n" +
                "{ \"_id\" : 5, \"name\" : \"Asya\", \"reportsTo\" : \"Ron\" }\n" +
                "{ \"_id\" : 6, \"name\" : \"Dan\", \"reportsTo\" : \"Andrew\" }";

        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"employees");


        String s1 = "{ \"_id\" : 0, \"airport\" : \"JFK\", \"connects\" : [ \"BOS\", \"ORD\" ] }\n" +
                "{ \"_id\" : 1, \"airport\" : \"BOS\", \"connects\" : [ \"JFK\", \"PWM\" ] }\n" +
                "{ \"_id\" : 2, \"airport\" : \"ORD\", \"connects\" : [ \"JFK\" ] }\n" +
                "{ \"_id\" : 3, \"airport\" : \"PWM\", \"connects\" : [ \"BOS\", \"LHR\" ] }\n" +
                "{ \"_id\" : 4, \"airport\" : \"LHR\", \"connects\" : [ \"PWM\" ] }";

        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"airports");

        String s2 = "{ \"_id\" : 1, \"name\" : \"Dev\", \"nearestAirport\" : \"JFK\" }\n" +
                "{ \"_id\" : 2, \"name\" : \"Eliot\", \"nearestAirport\" : \"JFK\" }\n" +
                "{ \"_id\" : 3, \"name\" : \"Jeff\", \"nearestAirport\" : \"BOS\" }";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"travelers");

        String people = "{\"_id\" : 1,\"name\" : \"Tanya Jordan\",\"friends\" : [ \"Shirley Soto\", \"Terry Hawkins\", \"Carole Hale\" ],\"hobbies\" : [ \"tennis\", \"unicycling\", \"golf\" ]}\n" +
                "{\"_id\" : 2,\"name\" : \"Carole Hale\",\"friends\" : [ \"Joseph Dennis\", \"Tanya Jordan\", \"Terry Hawkins\" ],\"hobbies\" : [ \"archery\", \"golf\", \"woodworking\" ]}\n" +
                "{\"_id\" : 3,\"name\" : \"Terry Hawkins\",\"friends\" : [ \"Tanya Jordan\", \"Carole Hale\", \"Angelo Ward\" ],\"hobbies\" : [ \"knitting\", \"frisbee\" ]}\n" +
                "{\"_id\" : 4,\"name\" : \"Joseph Dennis\",\"friends\" : [ \"Angelo Ward\", \"Carole Hale\" ],\"hobbies\" : [ \"tennis\", \"golf\", \"topiary\" ]}\n" +
                "{\"_id\" : 5,\"name\" : \"Angelo Ward\",\"friends\" : [ \"Terry Hawkins\", \"Shirley Soto\", \"Joseph Dennis\" ],\"hobbies\" : [ \"travel\", \"ceramics\", \"golf\" ]}\n" +
                "{\"_id\" : 6,\"name\" : \"Shirley Soto\",\"friends\" : [ \"Angelo Ward\", \"Tanya Jordan\", \"Carole Hale\" ],\"hobbies\" : [ \"frisbee\", \"set theory\" ] }";
        List<Document> documents3 = CreateDataUtil.parseString(people);
        mars.insert(documents3,"people");

    }

    @After
    public void dropCollections(){
        mars.dropCollection("employees");
        mars.dropCollection("airports");
        mars.dropCollection("travelers");
        mars.dropCollection("people");
    }

    @Test
    public void testForSelf(){
        pipeline.graphLookup(GraphLookup.from("employees")
                .startWith(field("reportsTo"))
                .connectFromField("reportsTo")
                .connectToField("name")
                .as("reportingHierarchy"));

        QueryCursor employees = mars.aggregate(pipeline, "employees");

        while (employees.hasNext()){
            System.out.println(employees.next());
        }
    }



    @Test
    public void testForOther(){
        pipeline.graphLookup(GraphLookup.from("airports")
                .startWith(field("nearestAirport"))
                .connectFromField("connects")
                .connectToField("airport")
                .maxDepth(2)
                .depthField("numConnections")
                .as("destinations"));

        QueryCursor travelers = mars.aggregate(pipeline, "travelers");
        while (travelers.hasNext()){
            System.out.println(travelers.next());
        }

    }

    @Test
    public void testForQueryFilter(){
        pipeline.match(Filters.eq("name","Tanya Jordan"));
        pipeline.graphLookup(GraphLookup.from("people")
                .startWith(field("friends"))
                .connectFromField("friends")
                .connectToField("name")
                .as("golfers")
                .restrict(Filters.eq("hobbies","golf")));
        pipeline.project(Projection.of().include("name").include("friends").include("connections who play golf",field("golfers.name")));

        QueryCursor people = mars.aggregate(pipeline, "people");
        while (people.hasNext()){
            System.out.println(people.next());
        }
    }
}
