package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Lookup;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 14:06
 */
public class LookUpAndMultipleJoinsTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){

        String s1 =  "  { \"_id\" : 1, \"item\" : \"almonds\", \"price\" : 12, \"ordered\" : 2 },\n" +
                "  { \"_id\" : 2, \"item\" : \"pecans\", \"price\" : 20, \"ordered\" : 1 },\n" +
                "  { \"_id\" : 3, \"item\" : \"cookies\", \"price\" : 10, \"ordered\" : 60 }";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"orders");

        String s2 = "  { \"_id\" : 1, \"stock_item\" : \"almonds\", warehouse: \"A\", \"instock\" : 120 },\n" +
                "  { \"_id\" : 2, \"stock_item\" : \"pecans\", warehouse: \"A\", \"instock\" : 80 },\n" +
                "  { \"_id\" : 3, \"stock_item\" : \"almonds\", warehouse: \"B\", \"instock\" : 60 },\n" +
                "  { \"_id\" : 4, \"stock_item\" : \"cookies\", warehouse: \"B\", \"instock\" : 40 },\n" +
                "  { \"_id\" : 5, \"stock_item\" : \"cookies\", warehouse: \"A\", \"instock\" : 80 }";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"warehouses");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("orders");
        mars.dropCollection("warehouses");
    }

    /**
     * db.orders.aggregate( [
     *    {
     *       $lookup:
     *          {
     *            from: "warehouses",
     *            let: { order_item: "$item", order_qty: "$ordered" },
     *            pipeline: [
     *               { $match:
     *                  { $expr:
     *                     { $and:
     *                        [
     *                          { $eq: [ "$stock_item",  "$$order_item" ] },
     *                          { $gte: [ "$instock", "$$order_qty" ] }
     *                        ]
     *                     }
     *                  }
     *               },
     *               { $project: { stock_item: 0, _id: 0 } }
     *            ],
     *            as: "stockdata"
     *          }
     *     }
     * ] )
     */
    //todo 不会实现
    @Test
    public void testForMultipleJoins(){
        pipeline.lookup(Lookup.from("warehouses")
                .let("order_item",field("item"))
                .let("order_qty",field("ordered"))
                .as("stockdata"));
    }
}
