package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.util.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 16:41
 */
public class PlanCacheStatsTest {


    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"item\" : \"abc\", \"price\" : NumberDecimal(\"12\"), \"quantity\" : 2, \"type\": \"apparel\" },\n" +
                "   { \"_id\" : 2, \"item\" : \"jkl\", \"price\" : NumberDecimal(\"20\"), \"quantity\" : 1, \"type\": \"electronics\" },\n" +
                "   { \"_id\" : 3, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : 5, \"type\": \"apparel\" },\n" +
                "   { \"_id\" : 4, \"item\" : \"abc\", \"price\" : NumberDecimal(\"8\"), \"quantity\" : 10, \"type\": \"apparel\" },\n" +
                "   { \"_id\" : 5, \"item\" : \"jkl\", \"price\" : NumberDecimal(\"15\"), \"quantity\" : 15, \"type\": \"electronics\" }\n";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"orders");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("orders");
    }

    /**
     * db.orders.createIndex( { item: 1 } );
     * db.orders.createIndex( { item: 1, quantity: 1 } );
     * db.orders.createIndex( { quantity: 1 } );
     * db.orders.createIndex( { quantity: 1, type: 1 } );
     * db.orders.createIndex(
     *    { item: 1, price: 1 },
     *    { partialFilterExpression: { price: { $gte: NumberDecimal("10")} } }
     * );
     */
    @Test
    public void createIndex(){
        mars.createIndex(new Index("item",IndexDirection.ASC),"orders");
        mars.createIndex(new Index("item",IndexDirection.ASC).on("quantity",IndexDirection.ASC),"orders");
        mars.createIndex(new Index("quantity",IndexDirection.ASC),"orders");
        mars.createIndex(new Index("quantity",IndexDirection.ASC).on("type",IndexDirection.ASC),"orders");

        Index item = new Index("item", IndexDirection.ASC);
        IndexOptions indexOptions = new IndexOptions();
        IndexOptions indexOptions1 = indexOptions.partialFilterExpression(Document.parse("{ price: { $gte: NumberDecimal(\"10\")} }"));
        Index index = item.setOptions(indexOptions1);
        mars.createIndex(index,"orders");

    }

    /**
     * db.orders.find( { item: "abc", price: { $gte: NumberDecimal("10") } } )
     * db.orders.find( { item: "abc", price: { $gte: NumberDecimal("5") } } )
     * db.orders.find( { quantity: { $gte: 20 } } )
     * db.orders.find( { quantity: { $gte: 5 }, type: "apparel" } )
     */
    @Test
    public void queryCollection(){
//        Criteria criteria = Criteria.where("item").is("abc").and("price").gte(10);
//        Criteria criteria = Criteria.where("item").is("abc").and("price").gte(5);
//        Criteria criteria = Criteria.where("quantity").gte(20);
        Criteria criteria = Criteria.where("quantity").gte(5).and("type").is("apparel");
        QueryCursor<Document> orders = mars.findAll(new Query(criteria), Document.class, "orders");
        while (orders.hasNext()){
            System.out.println(orders.next());
        }
    }

    /**
     * db.orders.aggregate( [
     *    { $planCacheStats: { } }
     * ] )
     */
    @Test
    public void testForCache(){
        pipeline.planCacheStats();
        Object orders = mars.aggregate(pipeline, "orders").tryNext();
        Assert.assertNotNull(orders);
    }
}
