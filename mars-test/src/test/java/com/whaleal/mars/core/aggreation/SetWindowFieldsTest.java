package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 23:49
 */
public class SetWindowFieldsTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ _id: 0, type: \"chocolate\", orderDate: new Date(\"2020-05-18T14:10:30Z\"),state: \"CA\", price: 13, quantity: 120 },\n" +
                "{ _id: 1, type: \"chocolate\", orderDate: new Date(\"2021-03-20T11:30:05Z\"),state: \"WA\", price: 14, quantity: 140 },\n" +
                "{ _id: 2, type: \"vanilla\", orderDate: new Date(\"2021-01-11T06:31:15Z\"),state: \"CA\", price: 12, quantity: 145 },\n" +
                "{ _id: 3, type: \"vanilla\", orderDate: new Date(\"2020-02-08T13:13:23Z\"),state: \"WA\", price: 13, quantity: 104 },\n" +
                "{ _id: 4, type: \"strawberry\", orderDate: new Date(\"2019-05-18T16:09:01Z\"),state: \"CA\", price: 41, quantity: 162 },\n" +
                "{ _id: 5, type: \"strawberry\", orderDate: new Date(\"2019-01-08T06:12:03Z\"),state: \"WA\", price: 43, quantity: 134 }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"cakeSales");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("cakeSales");
    }

    /**
     * db.cakeSales.aggregate( [
     *    {
     *       $setWindowFields: {
     *          partitionBy: "$state",
     *          sortBy: { orderDate: 1 },
     *          output: {
     *             cumulativeQuantityForState: {
     *                $sum: "$quantity",
     *                window: {
     *                   documents: [ "unbounded", "current" ]
     *                }
     *             }
     *          }
     *       }
     *    }
     * ] )
     */
    //todo 未开发
    @Test
    public void testForQuantityForEachState(){
//        pipeline
    }
}
