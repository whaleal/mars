package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.util.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.*;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author lyz
 * @desc
 * @date 2022-05-18 0:11
 */
public class UnionWithTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ _id: 1, supplier: \"Aardvark and Sons\", state: \"Texas\" },\n" +
                "  { _id: 2, supplier: \"Bears Run Amok.\", state: \"Colorado\"},\n" +
                "  { _id: 3, supplier: \"Squid Mark Inc. \", state: \"Rhode Island\" }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"suppliers");

        String s1 = "{ _id: 1, warehouse: \"A\", region: \"West\", state: \"California\" },\n" +
                "  { _id: 2, warehouse: \"B\", region: \"Central\", state: \"Colorado\"},\n" +
                "  { _id: 3, warehouse: \"C\", region: \"East\", state: \"Florida\" }";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"warehouses");

        String s2 = "  { store: \"A\", item: \"Chocolates\", quantity: 150 },\n" +
                "  { store: \"B\", item: \"Chocolates\", quantity: 50 },\n" +
                "  { store: \"A\", item: \"Cookies\", quantity: 100 },\n" +
                "  { store: \"B\", item: \"Cookies\", quantity: 120 },\n" +
                "  { store: \"A\", item: \"Pie\", quantity: 10 },\n" +
                "  { store: \"B\", item: \"Pie\", quantity: 5 }";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"sales2019q1");

        String s3 = "  { store: \"A\", item: \"Cheese\", quantity: 30 },\n" +
                "  { store: \"B\", item: \"Cheese\", quantity: 50 },\n" +
                "  { store: \"A\", item: \"Chocolates\", quantity: 125 },\n" +
                "  { store: \"B\", item: \"Chocolates\", quantity: 150 },\n" +
                "  { store: \"A\", item: \"Cookies\", quantity: 200 },\n" +
                "  { store: \"B\", item: \"Cookies\", quantity: 100 },\n" +
                "  { store: \"B\", item: \"Nuts\", quantity: 100 },\n" +
                "  { store: \"A\", item: \"Pie\", quantity: 30 },\n" +
                "  { store: \"B\", item: \"Pie\", quantity: 25 }";
        List<Document> documents3 = CreateDataUtil.parseString(s3);
        mars.insert(documents3,"sales2019q2");

        String s4 = "  { store: \"A\", item: \"Cheese\", quantity: 50 },\n" +
                "  { store: \"B\", item: \"Cheese\", quantity: 20 },\n" +
                "  { store: \"A\", item: \"Chocolates\", quantity: 125 },\n" +
                "  { store: \"B\", item: \"Chocolates\", quantity: 150 },\n" +
                "  { store: \"A\", item: \"Cookies\", quantity: 200 },\n" +
                "  { store: \"B\", item: \"Cookies\", quantity: 100 },\n" +
                "  { store: \"A\", item: \"Nuts\", quantity: 80 },\n" +
                "  { store: \"B\", item: \"Nuts\", quantity: 30 },\n" +
                "  { store: \"A\", item: \"Pie\", quantity: 50 },\n" +
                "  { store: \"B\", item: \"Pie\", quantity: 75 }";
        List<Document> documents4 = CreateDataUtil.parseString(s4);
        mars.insert(documents4,"sales2019q3");

        String s5 = "  { store: \"A\", item: \"Cheese\", quantity: 100, },\n" +
                "  { store: \"B\", item: \"Cheese\", quantity: 100},\n" +
                "  { store: \"A\", item: \"Chocolates\", quantity: 200 },\n" +
                "  { store: \"B\", item: \"Chocolates\", quantity: 300 },\n" +
                "  { store: \"A\", item: \"Cookies\", quantity: 500 },\n" +
                "  { store: \"B\", item: \"Cookies\", quantity: 400 },\n" +
                "  { store: \"A\", item: \"Nuts\", quantity: 100 },\n" +
                "  { store: \"B\", item: \"Nuts\", quantity: 200 },\n" +
                "  { store: \"A\", item: \"Pie\", quantity: 100 },\n" +
                "  { store: \"B\", item: \"Pie\", quantity: 100 }";
        List<Document> documents5 = CreateDataUtil.parseString(s5);
        mars.insert(documents5,"sales2019q4");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("suppliers");
        mars.dropCollection("warehouses");
        mars.dropCollection("sales2019q1");
        mars.dropCollection("sales2019q2");
        mars.dropCollection("sales2019q3");
        mars.dropCollection("sales2019q4");
    }

    /**
     *db.suppliers.aggregate([
     *    { $project: { state: 1, _id: 0 } },
     *    { $unionWith: { coll: "warehouses", pipeline: [ { $project: { state: 1, _id: 0 } } ]} }
     *    { $group: { _id: "$state" } }
     * ])
     */

    @Test
    public void testForDuplicatesResults(){
        pipeline.project(Projection.project().include("state").exclude("_id"));
        pipeline.unionWith("warehouses",Projection.project().include("state").exclude("_id"));
        pipeline.group(Group.group(id(field("$state"))));

        QueryCursor suppliers = mars.aggregate(pipeline, "suppliers");
        while (suppliers.hasNext()){
            System.out.println(suppliers.next());
        }
    }

    /**
     * db.sales2019q1.aggregate( [
     *    { $set: { _id: "2019Q1" } },
     *    { $unionWith: { coll: "sales2019q2", pipeline: [ { $set: { _id: "2019Q2" } } ] } },
     *    { $unionWith: { coll: "sales2019q3", pipeline: [ { $set: { _id: "2019Q3" } } ] } },
     *    { $unionWith: { coll: "sales2019q4", pipeline: [ { $set: { _id: "2019Q4" } } ] } },
     *    { $sort: { _id: 1, store: 1, item: 1 } }
     * ] )
     */
    //todo 缺少pipeline类型的stage
    @Test
    public void testForReport1(){
        pipeline.set(AddFields.addFields().field("_id",value("2019Q1")));
        pipeline.unionWith("sales2019q2", Set.set().field("_id",value("2019Q2")));
        pipeline.unionWith("sales2019q3", Set.set().field("_id",value("2019Q3")));
        pipeline.unionWith("sales2019q4", Set.set().field("_id",value("2019Q4")));
        pipeline.sort(Sort.sort().ascending("_id").ascending("store").ascending("item"));
        QueryCursor sales2019q1 = mars.aggregate(pipeline, "sales2019q1");
        while (sales2019q1.hasNext()){
            System.out.println(sales2019q1.next());
        }
    }
}
