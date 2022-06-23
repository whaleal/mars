package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.expressions.SystemVariables;
import com.whaleal.mars.core.aggregation.stages.Lookup;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.aggregation.stages.ReplaceRoot;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.ArrayExpressions.elementAt;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.ObjectExpressions.mergeObjects;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 14:01
 */
public class LookUpAndMergeTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){

        String s1 = "   { \"_id\" : 1, \"item\" : \"almonds\", \"price\" : 12, \"quantity\" : 2 },\n" +
                "   { \"_id\" : 2, \"item\" : \"pecans\", \"price\" : 20, \"quantity\" : 1 }";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"orders");

        String s2 = "{ \"_id\" : 1, \"item\" : \"almonds\", description: \"almond clusters\", \"instock\" : 120 },\n" +
                "  { \"_id\" : 2, \"item\" : \"bread\", description: \"raisin and nut bread\", \"instock\" : 80 },\n" +
                "  { \"_id\" : 3, \"item\" : \"pecans\", description: \"candied pecans\", \"instock\" : 60 }";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"items");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("orders");
        mars.dropCollection("items");
    }

    /**
     * db.orders.aggregate( [
     *    {
     *       $lookup: {
     *          from: "items",
     *          localField: "item",    // field in the orders collection
     *          foreignField: "item",  // field in the items collection
     *          as: "fromItems"
     *       }
     *    },
     *    {
     *       $replaceRoot: { newRoot: { $mergeObjects: [ { $arrayElemAt: [ "$fromItems", 0 ] }, "$$ROOT" ] } }
     *    },
     *    { $project: { fromItems: 0 } }
     *
     * Document{{newRoot=Document{{_id=1, item=almonds, description=almond clusters, instock=120, price=12, quantity=2,
     *                  fromItems=[Document{{_id=1, item=almonds, description=almond clusters, instock=120}}]}}}}
     * Document{{newRoot=Document{{_id=2, item=pecans, description=candied pecans, instock=60, price=20, quantity=1, fromItems=[Document{{_id=3, item=pecans, description=candied pecans, instock=60}}]}}}}
     * ] )
     */
    @Test
    public void testForMergerObjects(){
        pipeline.lookup(Lookup.from("items")
                .localField("item")
                .foreignField("item")
                .as("fromItems"));

//        pipeline.replaceRoot(ReplaceRoot.with().field("newRoot",mergeObjects().add(elementAt(field("fromItems"),value(0))).add(value("$$ROOT"))));
        pipeline.replaceRoot(ReplaceRoot.replaceRoot(mergeObjects().add(elementAt(field("fromItems"),value(0))).add(SystemVariables.ROOT)));
        pipeline.project(Projection.project().exclude("fromItems"));

        QueryCursor orders = mars.aggregate(pipeline, "orders");
        while (orders.hasNext()){
            System.out.println(orders.next());
        }

    }
}
