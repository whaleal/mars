package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.ZipCodesUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.*;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author lyz
 * @desc
 * @date 2022-05-12 11:23
 */
public class AggregationTestByZipCodes {

    Mars mars = new Mars(Constant.connectionStr);


    @Before
    public void createData(){

        List<Document> document = new ArrayList<>();
        try {
            document = ZipCodesUtil.createDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mars.insert(document, "zipCodes");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("zipCodes");
    }

    @Test
    public void testForZipCode(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create(Document.class);
        pipeline.group(Group.of(id("state")).field("totalPop",sum(field("pop"))));

        pipeline.match(Filters.gte("totalPop",10*1000*1000));

        QueryCursor<Document> aggregate = mars.aggregate(pipeline, "zipCodes");

        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }
    }


    @Test
    public void testForAvgPop(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.group(Group.of(id(Expressions.value(new Document("state","$state").append("city","$city"))))
                .field("pop",sum(field("pop"))));
//        pipeline.group(Group.of(id().field("state",field("state")).field("city",field("city")))
//                .field("pop",sum(field("pop"))));

        pipeline.group(Group.of(id(field("_id.state"))).field("avgCityPop",avg(field("pop"))));

//        List<Stage> innerStage = pipeline.getInnerStage();
//
//        for (Stage stage : innerStage){
//            System.out.println(stage.getStageName());
//            System.out.println(stage.toString());
//            System.out.println(stage);
//        }

        QueryCursor<Document> zipCodes = mars.aggregate(pipeline, "zipCodes");
        while (zipCodes.hasNext()){
            System.out.println(zipCodes.next());
        }
    }

    /**
     * db.zipcodes.aggregate( [
     *    { $group:
     *       {
     *         _id: { state: "$state", city: "$city" },
     *         pop: { $sum: "$pop" }
     *       }
     *    },
     *    { $sort: { pop: 1 } },
     *    { $group:
     *       {
     *         _id : "$_id.state",
     *         biggestCity:  { $last: "$_id.city" },
     *         biggestPop:   { $last: "$pop" },
     *         smallestCity: { $first: "$_id.city" },
     *         smallestPop:  { $first: "$pop" }
     *       }
     *    },
     *
     *   // the following $project is optional, and
     *   // modifies the output format.
     *
     *   { $project:
     *     { _id: 0,
     *       state: "$_id",
     *       biggestCity:  { name: "$biggestCity",  pop: "$biggestPop" },
     *       smallestCity: { name: "$smallestCity", pop: "$smallestPop" }
     *     }
     *   }
     * ] )
     */
    @Test
    public void testForCity(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.group(Group.of(id(value(Document.parse("{ state: \"$state\", city: \"$city\" }"))))
                .field("pop",sum(field("pop"))));

        pipeline.sort(Sort.on().ascending("pop"));

        pipeline.group(Group.of(id("_id.state"))
                .field("biggestCity",last(field("_id.city")))
                .field("biggestPop",last(field("pop")))
                .field("smallestCity",first(field("_id.city")))
                .field("smallestPop",first(field("pop"))));

        pipeline.project(Projection.of().exclude("_id")
                .include("state",field("_id"))
                .include("biggestCity",value(Document.parse("{ name: \"$biggestCity\",  pop: \"$biggestPop\" }")))
                .include("smallestCity",value(Document.parse("{ name: \"$smallestCity\", pop: \"$smallestPop\" }"))));

        QueryCursor<Document> aggregate = mars.aggregate(pipeline,"zipCodes");
        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }

    }


}
