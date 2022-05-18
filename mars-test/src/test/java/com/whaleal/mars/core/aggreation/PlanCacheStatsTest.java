package com.whaleal.mars.core.aggreation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;
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

    @Test
    public void createData(){
        String s = "{ \"_id\" : 1, \"item\" : \"abc\", \"price\" : NumberDecimal(\"12\"), \"quantity\" : 2, \"type\": \"apparel\" },\n" +
                "   { \"_id\" : 2, \"item\" : \"jkl\", \"price\" : NumberDecimal(\"20\"), \"quantity\" : 1, \"type\": \"electronics\" },\n" +
                "   { \"_id\" : 3, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : 5, \"type\": \"apparel\" },\n" +
                "   { \"_id\" : 4, \"item\" : \"abc\", \"price\" : NumberDecimal(\"8\"), \"quantity\" : 10, \"type\": \"apparel\" },\n" +
                "   { \"_id\" : 5, \"item\" : \"jkl\", \"price\" : NumberDecimal(\"15\"), \"quantity\" : 15, \"type\": \"electronics\" }\n";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"orders");
    }

    @Test
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
        mars.createIndex(new Index().on("item", IndexDirection.ASC),"orders");
        mars.createIndex(new Index().on("item",IndexDirection.ASC).on("quantity",IndexDirection.ASC),"orders");
        mars.createIndex(new Index().on("quantity",IndexDirection.ASC),"orders");
        mars.createIndex(new Index().on("quantity",IndexDirection.ASC).on("type",IndexDirection.ASC),"orders");
        Index item = new Index().on("item", IndexDirection.ASC);
        IndexOptions indexOptions = new IndexOptions();
        IndexOptions indexOptions1 = indexOptions.partialFilterExpression(Document.parse("{ price: { $gte: NumberDecimal(\"10\")} }"));
        Index index = item.setOptions(indexOptions1);
        mars.createIndex(index,"orders");

    }

    @Test
    public void queryCollection(){
        MongoCollection<Document> orders = mars.getDatabase().getCollection("orders");
        FindIterable<Document> documents = orders.find();
        Document explain = documents.explain();
        System.out.println(explain);
    }

    /**
     * db.orders.aggregate( [
     *    { $planCacheStats: { } }
     * ] )
     */
    //todo 无结果
    @Test
    public void testForCache(){
        pipeline.planCacheStats();
        QueryCursor aggregate = mars.aggregate(pipeline,"orders");
        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }
    }
}
