package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
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


        /**
         * db.orders.createIndex( { item: 1, quantity: 1 } )
         * db.orders.createIndex( { type: 1, item: 1 } )
         */
        mars.createIndex(new Index("item",IndexDirection.ASC).on("quantity",IndexDirection.ASC),"orders");
        mars.createIndex(new Index("type",IndexDirection.ASC).on("item",IndexDirection.ASC),"orders");



        /**
         * db.orders.find( { type: "apparel"} )
         * db.orders.find( { item: "abc" } ).sort( { quantity: 1 } )
         */

        Criteria criteria = Criteria.where("type").is("apparel");
        Query query = new Query(criteria);
        QueryCursor<Document> orders = mars.findAll(query, Document.class, "orders");
        while(orders.hasNext()){
            System.out.println(orders.next());
        }

        System.out.println("------------------------------------");

        Criteria criteria1 = Criteria.where("item").is("abc")
                .and("quantity").is(1);
        Query query1 = new Query(criteria1);
        QueryCursor<Document> orders1 = mars.findAll(query1, Document.class, "orders");
        while(orders1.hasNext()){
            System.out.println(orders1.next());
        }
    }

    @After
    public void dropCollection(){
        mars.dropCollection("orders");
    }



    /**
     * db.orders.aggregate( [ { $indexStats: { } } ] )
     */
    @Test
    public void testForIndexUsing(){

        pipeline.indexStats();
        QueryCursor orders = mars.aggregate(pipeline, "orders");
        while (orders.hasNext()){
            System.out.println(orders.next());
        }


    }


}

