package com.whaleal.mars.core.aggreation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 12:11
 */
public class IndexStatsTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"item\" : \"abc\", \"price\" : 12, \"quantity\" : 2, \"type\": \"apparel\" }\n" +
                "{ \"_id\" : 2, \"item\" : \"jkl\", \"price\" : 20, \"quantity\" : 1, \"type\": \"electronics\" }\n" +
                "{ \"_id\" : 3, \"item\" : \"abc\", \"price\" : 10, \"quantity\" : 5, \"type\": \"apparel\" }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"orders");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("orders");
    }

    /**
     * db.orders.createIndex( { item: 1, quantity: 1 } )
     * db.orders.createIndex( { type: 1, item: 1 } )
     *
     * db.orders.find( { type: "apparel"} )
     * db.orders.find( { item: "abc" } ).sort( { quantity: 1 } )
     *
     * db.orders.aggregate( [ { $indexStats: { } } ] )
     */

    //todo 测试时遇到问题/创建索引出错/查询有异议
    @Test
    public void testForIndexUsing(){
        mars.createIndex(new Index().on("item", IndexDirection.ASC).on("quantity",IndexDirection.ASC),"orders");
        mars.createIndex(new Index().on("type", IndexDirection.ASC).on("item",IndexDirection.ASC),"orders");

        MongoDatabase database = mars.getDatabase();
        FindIterable<Document> orders = database.getCollection("orders").find(Document.parse("{ type: \"apparel\"}"));
        FindIterable<Document> orders1 = database.getCollection("orders").find(Document.parse(" { item: \"abc\" } , sort( { quantity: 1 } "));
        //这种方式会进入死循环
//        while (orders.cursor().hasNext()){
//            System.out.println(orders.cursor().next());
//        }

//        Criteria criteria = Criteria.where("type").is("apparel");
//        mars.findAll(new Query(criteria),"")

        pipeline.indexStats();
        Object orders2 = mars.aggregate(pipeline, "orders").tryNext();
        System.out.println(orders2);


    }
}

