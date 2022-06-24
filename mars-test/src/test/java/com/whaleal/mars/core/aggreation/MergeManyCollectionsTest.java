package com.whaleal.mars.core.aggreation;

import com.mongodb.client.model.MergeOptions;
import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Merge;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 15:30
 */
public class MergeManyCollectionsTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ _id: 1, quarter: \"2019Q1\", region: \"A\", qty: 200, reportDate: new ISODate(\"2019-04-01\") },\n" +
                "   { _id: 2, quarter: \"2019Q1\", region: \"B\", qty: 300, reportDate: new ISODate(\"2019-04-01\") },\n" +
                "   { _id: 3, quarter: \"2019Q1\", region: \"C\", qty: 700, reportDate: new ISODate(\"2019-04-01\") },\n" +
                "   { _id: 4, quarter: \"2019Q2\", region: \"B\", qty: 300, reportDate: new ISODate(\"2019-07-01\") },\n" +
                "   { _id: 5, quarter: \"2019Q2\", region: \"C\", qty: 1000, reportDate: new ISODate(\"2019-07-01\") },\n" +
                "   { _id: 6, quarter: \"2019Q2\", region: \"A\", qty: 400, reportDate: new ISODate(\"2019-07-01\") },\n";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"purchaseorders");

        String s1 = "{ _id: 1, quarter: \"2019Q1\", region: \"A\", qty: 400, reportDate: new ISODate(\"2019-04-02\") },\n" +
                "   { _id: 2, quarter: \"2019Q1\", region: \"B\", qty: 550, reportDate: new ISODate(\"2019-04-02\") },\n" +
                "   { _id: 3, quarter: \"2019Q1\", region: \"C\", qty: 1000, reportDate: new ISODate(\"2019-04-05\") },\n" +
                "   { _id: 4, quarter: \"2019Q2\", region: \"B\", qty: 500, reportDate: new ISODate(\"2019-07-02\") },\n";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"reportedsales");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("purchaseorders");
        mars.dropCollection("reportedsales");
    }

    /**
     * db.purchaseorders.aggregate( [
     *    { $group: { _id: "$quarter", purchased: { $sum: "$qty" } } },  // group purchase orders by quarter
     *    { $merge : { into: "quarterlyreport", on: "_id",  whenMatched: "merge", whenNotMatched: "insert" } }
     * ])
     */
    @Test
    public void testForMerge(){
        pipeline.group(Group.group(id(field("quarter")))
                .field("purchased",sum(field("qty"))));
        pipeline.merge(Merge.into("quarterlyreport")
                .on("_id")
                .whenMatched(MergeOptions.WhenMatched.MERGE)
                .whenNotMatched(MergeOptions.WhenNotMatched.INSERT));
        QueryCursor purchaseorders = mars.aggregate(pipeline, "purchaseorders");
        while (purchaseorders.hasNext()){
            System.out.println(purchaseorders.next());
        }
    }
}
