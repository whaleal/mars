package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Lookup;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


/**
 * @author lyz
 * @desc
 * @date 2022-05-17 13:09
 */
public class LookUpSingleTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "   { \"_id\" : 1, \"item\" : \"almonds\", \"price\" : 12, \"quantity\" : 2 },\n" +
                "   { \"_id\" : 2, \"item\" : \"pecans\", \"price\" : 20, \"quantity\" : 1 },\n" +
                "   { \"_id\" : 3  }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"orders");

        String s1 = "  { \"_id\" : 1, \"sku\" : \"almonds\", \"description\": \"product 1\", \"instock\" : 120 },\n" +
                "   { \"_id\" : 2, \"sku\" : \"bread\", \"description\": \"product 2\", \"instock\" : 80 },\n" +
                "   { \"_id\" : 3, \"sku\" : \"cashews\", \"description\": \"product 3\", \"instock\" : 60 },\n" +
                "   { \"_id\" : 4, \"sku\" : \"pecans\", \"description\": \"product 4\", \"instock\" : 70 },\n" +
                "   { \"_id\" : 5, \"sku\": null, \"description\": \"Incomplete\" },\n" +
                "   { \"_id\" : 6 }";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"inventory");

    }

    @After
    public void dropCollection(){
        mars.dropCollection("orders");
        mars.dropCollection("inventory");
    }

    /**
     * db.orders.aggregate( [
     *    {
     *      $lookup:
     *        {
     *          from: "inventory",
     *          localField: "item",
     *          foreignField: "sku",
     *          as: "inventory_docs"
     *        }
     *   }
     * ] )
     */
    @Test
    public void testForSingle(){
        pipeline.lookup(Lookup.lookup("inventory")
                .localField("item")
                .foreignField("sku")
                .as("inventory_docs"));

        QueryCursor orders = mars.aggregate(pipeline, "orders");
        while (orders.hasNext()){
            System.out.println(orders.next());
        }
    }



}
