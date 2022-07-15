package com.whaleal.mars.core.aggreation;

import com.mongodb.ReadConcern;
import com.whaleal.mars.Constant;
import com.whaleal.mars.util.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.*;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.AggregationOptions;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author: cjq
 * @date: 2022/7/4 0004
 * @desc: 聚合选项测试
 */
public class OptionTest {

//    private Mars mars = new Mars("mongodb://192.168.3.200:47018/ops20220621");
    private Mars mars = new Mars(Constant.connectionStr);


    @Before
    public void createData(){
        String s = "{ _id: 1, category: \"cake\", type: \"chocolate\", qty: 10 },\n" +
                "   { _id: 2, category: \"cake\", type: \"ice cream\", qty: 25 },\n" +
                "   { _id: 3, category: \"pie\", type: \"boston cream\", qty: 20 },\n" +
                "   { _id: 4, category: \"pie\", type: \"blueberry\", qty: 15 }";
        List<Document> list = CreateDataUtil.parseString(s);
        mars.insert(list,"foodColl");
        Index index = new Index("qty", IndexDirection.ASC).on("type", IndexDirection.ASC);
        Index index1 = new Index("qty", IndexDirection.ASC).on("category", IndexDirection.ASC);
        mars.createIndex(index,"foodColl");
        mars.createIndex(index1,"foodColl");
    }


    @Test
    public void testForMaxTimeMS(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        AggregationOptions options = new AggregationOptions();
        pipeline.sort(Sort.sort().ascending("qty"));
        options.maxTimeMS(100);
        QueryCursor<Document> aggregate = mars.aggregate(pipeline, "foodColl",options);
        while(aggregate.hasNext()){
            System.out.println(aggregate.next());
        }

    }

//    @Test
//    public void testForApply(){
//        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
//        pipeline.sort(Sort.sort().ascending("qty"));
//        pipeline.match(Filters.eq("category","cake"),Filters.eq("qty",10));
//        pipeline.sort(Sort.sort().descending("type"));
//        AggregationOptions options = new AggregationOptions();
//        options.apply(new ArrayList<>(),mars.getCollection(Document.class),Document.class);
//        QueryCursor<Document> aggregate = mars.aggregate(pipeline, "foodColl",options);
//        while(aggregate.hasNext()){
//            System.out.println(aggregate.next());
//        }
//    }

    @Test
    public void testForHint(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.sort(Sort.sort().ascending("qty"));
        pipeline.match(Filters.eq("category","cake"),Filters.eq("qty",10));
        pipeline.sort(Sort.sort().descending("type"));
        AggregationOptions options = new AggregationOptions();
        options.hint(new Document().append("qty",1).append("category",1));
        QueryCursor<Document> aggregate = mars.aggregate(pipeline, "foodColl",options);
        while(aggregate.hasNext()){
            System.out.println(aggregate.next());
        }

    }


    @Test
    public void testForReadConcern(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.match(Filters.lt("rating",5));
        AggregationOptions options = new AggregationOptions();
        options.readConcern(ReadConcern.MAJORITY);
        QueryCursor<Document> aggregate = mars.aggregate(pipeline, "foodColl",options);
        while(aggregate.hasNext()){
            System.out.println(aggregate.next());
        }
    }
    @Test
    public void testForWriteConcern(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.match(Filters.lt("rating",5));
        AggregationOptions options = new AggregationOptions();
        options.writeConcern();
        QueryCursor<Document> aggregate = mars.aggregate(pipeline, "foodColl",options);
        while(aggregate.hasNext()){
            System.out.println(aggregate.next());
        }
    }


    @After
    public void dropCollection(){
        mars.dropCollection("foodColl");
    }
}
