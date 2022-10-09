package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.*;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.AggregationOptions;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.*;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.MathExpressions.round;

/**
 * @author lyz
 * @description
 * @date 2022-07-04 15:44
 **/
public class AggregateOptionTest {

    private Mars mars = new Mars("mongodb://192.168.3.200:47018/ops20220621");

    @Test
    public void test(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

//        pipeline.project(Projection.project())



    }


    @Test
    public void testFor(){
        long x = System.currentTimeMillis();
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.unwind(Unwind.on("diskInfoList"));

        pipeline.group(Group.of(Group.id("hostId"))
                .field("hostName",first(field("hostName")))
                .field("usage",first(field("diskInfoList.utilization"))));

        pipeline.limit(5);

        pipeline.sort(Sort.on().descending("usage"));

        pipeline.project(Projection.of().include("hostId",field("_id"))
                .include("hostName",field("hostName"))
                .include("usage",round(field("usage"),value(2))));

        List<Document> list = mars.aggregate(pipeline, "mongoDBCollections").toList();

        System.out.println("聚合时间" + (System.currentTimeMillis() - x));
        for (Document document : list){
            System.out.println(document);
        }

        System.out.println("一共花了" + (System.currentTimeMillis() - x));

    }


    @Test
    public void testForHint(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.sort(Sort.sort().ascending("aty"));
        pipeline.match(Filters.eq("category","cake"));
        pipeline.match(Filters.eq("qty",10));
        pipeline.sort(Sort.sort().descending("type"));
        AggregationOptions options = new AggregationOptions();


//        options.hint()
        options.hint(new Document().append("qty",1).append("category",1));
//        options.hint().append("qty",1).append("category",1);
        QueryCursor<Document> aggregate = mars.aggregate(pipeline, "tets",options);
        while(aggregate.hasNext()){
            System.out.println(aggregate.next());
        }

    }

    @Test
    public void testFor1(){
        long l = System.currentTimeMillis();
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.match(Filters.eq("log.msg","Slow query"));

        pipeline.group(Group.of(Group.id("nodeId"))
                .field("slow count", sum(value(1))));

        pipeline.limit(5);

        pipeline.sort(Sort.on().descending("slow count"));

        pipeline.lookup(Lookup.lookup("mongodbNodeMetrics")
                .foreignField("_id")
                .localField("_id")
                .as("nodeInfo"));

        pipeline.project(com.whaleal.mars.core.aggregation.stages.Projection.project().include("host",first(field("nodeInfo.hostName")))
                .include("port",first(field("nodeInfo.port")))
                .include("slow count",field("slow count")));
        List<Document> list = mars.aggregate(pipeline, "mongoDLog").toList();

        for (Document document : list){
            System.out.println(document);
            String instance = document.getString("host") + ":" + document.getString("port");
            document.put("instance",instance);
        }

        System.out.println("聚合用时" + (System.currentTimeMillis() - l));
        System.out.println(list.size());
        for (Document document : list){
            System.out.println(document.toJson());
        }
    }

    @Test
    public void test2(){
        Criteria slow_query = Criteria.where("log.msg").is("Slow query")
                .and("log.attr.durationMillis").gte("100");

        Query query = new Query(slow_query);

        QueryCursor<Document> mongoDLog = mars.findAll(query, Document.class, "mongoDLog");
        while (mongoDLog.hasNext()){
            System.out.println(mongoDLog.next());
        }
    }

    @Test
    public void test6(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();


        pipeline.unwind(Unwind.on("dbTables"));

        pipeline.sort(Sort.on().descending("dbTables.size"));

        pipeline.limit(5);

        QueryCursor<Document> mongoDBCollections = mars.aggregate(pipeline, "mongoDBCollections");

        List<Document> list = new ArrayList();
        while (mongoDBCollections.hasNext()){

            Document next = mongoDBCollections.next();
            System.out.println("" + next);

            Criteria criteria = Criteria.where("_id").is(next.getString("clusterId"));

            Query query = new Query(criteria);
            Document document = mars.findOne(query, Document.class, "mongoClusterInformation").get();

            next.put("clusterName",document.getString("clusterName"));
            next.put("dbName",next.get("dbTables",Document.class).getString("ns").split("\\.",2)[0]);
            next.put("collectionName",next.get("dbTables",Document.class).getString("ns").split("\\.",2)[1]);
            next.put("size", next.getInteger("dbTables.size"));
            list.add(next);
        }
    }
    }

