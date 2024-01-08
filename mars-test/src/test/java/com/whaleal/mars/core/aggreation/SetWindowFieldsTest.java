package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.SetWindowFields;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.domain.ISort;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.WindowExpressions.shift;
import static com.whaleal.mars.core.aggregation.stages.SetWindowFields.Output.output;
import static com.whaleal.mars.core.aggregation.stages.SetWindowFields.setWindowFields;
import static com.whaleal.mars.core.query.Sort.ascending;
import static com.whaleal.mars.core.query.Sort.descending;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 23:49
 *
 * 链接  https://www.mongodb.com/docs/manual/reference/operator/aggregation/setWindowFields/
 */
public class SetWindowFieldsTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ _id: 0, type: \"chocolate\", orderDate: new ISODate(\"2020-05-18T14:10:30Z\"),state: \"CA\", price: 13, quantity: 120 },\n" +
                "{ _id: 1, type: \"chocolate\", orderDate: new ISODate(\"2021-03-20T11:30:05Z\"),state: \"WA\", price: 14, quantity: 140 },\n" +
                "{ _id: 2, type: \"vanilla\", orderDate: new ISODate(\"2021-01-11T06:31:15Z\"),state: \"CA\", price: 12, quantity: 145 },\n" +
                "{ _id: 3, type: \"vanilla\", orderDate: new ISODate(\"2020-02-08T13:13:23Z\"),state: \"WA\", price: 13, quantity: 104 },\n" +
                "{ _id: 4, type: \"strawberry\", orderDate: new ISODate(\"2019-05-18T16:09:01Z\"),state: \"CA\", price: 41, quantity: 162 },\n" +
                "{ _id: 5, type: \"strawberry\", orderDate: new ISODate(\"2019-01-08T06:12:03Z\"),state: \"WA\", price: 43, quantity: 134 }";
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
    @Test
    public void testForQuantityForEachState(){
//        pipeline
        pipeline.setWindowFields(setWindowFields().partitionBy(field("state"))
                .sortBy(ascending("orderDate"))
                .output(output("cumulativeQuantityForState")
                        .operator(sum(field("quantity")))
                        .window().documents("unbounded","current")));
        QueryCursor cakeSales = mars.aggregate(pipeline, "cakeSales");


        while (cakeSales.hasNext()){
            System.out.println(cakeSales.next());
        }

    }


    /**
     *
     * db.cakeSales.aggregate( [
     *    {
     *       $setWindowFields: {
     *          partitionBy: "state" ,
     *          sortBy: { orderDate: 1 },
     *          output: {
     *             cumulativeQuantityForYear: {
     *                $sum: "$quantity",
     *                window: {
     *                   documents: [ "unbounded", "current" ]
     *                }
     *             }
     *             }
     *             }}])
     *
     *
     */
    @Test
    public void testSetWindowFields() {

                pipeline.setWindowFields(setWindowFields()
                        .partitionBy(field("state"))
                        .sortBy(ascending("orderDate"))
                        .output(output("cumulativeQuantityForState")
                                .operator(sum(field("quantity")))
                                .window()
                                .documents("unbounded", "current")));

        List<Document> actual = mars.aggregate(pipeline,"cakeSales").toList();

        List<Document> expected = parseDocs(
                "{ '_id' : 4, 'type' : 'strawberry', 'orderDate' : ISODate('2019-05-18T16:09:01Z'), 'state' : 'CA', 'price' : 41, " +
                        "'quantity' : 162, 'cumulativeQuantityForState' : 162 }",
                "{ '_id' : 0, 'type' : 'chocolate', 'orderDate' : ISODate('2020-05-18T14:10:30Z'), 'state' : 'CA', 'price' : 13, " +
                        "'quantity' : 120, 'cumulativeQuantityForState' : 282 }",
                "{ '_id' : 2, 'type' : 'vanilla', 'orderDate' : ISODate('2021-01-11T06:31:15Z'), 'state' : 'CA', 'price' : 12, " +
                        "'quantity' : 145, 'cumulativeQuantityForState' : 427 }",
                "{ '_id' : 5, 'type' : 'strawberry', 'orderDate' : ISODate('2019-01-08T06:12:03Z'), 'state' : 'WA', 'price' : 43, " +
                        "'quantity' : 134, 'cumulativeQuantityForState' : 134 }",
                "{ '_id' : 3, 'type' : 'vanilla', 'orderDate' : ISODate('2020-02-08T13:13:23Z'), 'state' : 'WA', 'price' : 13, " +
                        "'quantity' : 104, 'cumulativeQuantityForState' : 238 }",
                "{ '_id' : 1, 'type' : 'chocolate', 'orderDate' : ISODate('2021-03-20T11:30:05Z'), 'state' : 'WA', 'price' : 14, " +
                        "'quantity' : 140, 'cumulativeQuantityForState' : 378 }");

        System.out.println(actual);
        System.out.println(expected);
        Assert.assertEquals(actual, expected);
    }


    /**
     *
     *
     * [{"$setWindowFields": {"partitionBy": "$state", "sortBy": {"orderDate": 1}, "output": {"shiftQuantityForState": {"$shift": {"output": "$quantity", "by": 1, "default": "Not available"}}}}}]
     */

    @Test
    public void testShift() {

        pipeline.setWindowFields(setWindowFields()
                .partitionBy(field("state"))
                .sortBy(descending("quantity"))
                .output(output("shiftQuantityForState")
                        .operator(shift(field("quantity"), 1, value("Not available")))));

        List<Document> actual = mars.aggregate(pipeline,"cakeSales").toList();



        List<Document> expected = parseDocs(
                "{ '_id' : 4, 'type' : 'strawberry', 'orderDate' : ISODate('2019-05-18T16:09:01Z'), 'state' : 'CA', 'price' : 41, 'quantity' "
                        +
                        ": 162, 'shiftQuantityForState' : 145 }",
                "{ '_id' : 2, 'type' : 'vanilla', 'orderDate' : ISODate('2021-01-11T06:31:15Z'), 'state' : 'CA', 'price' : 12, 'quantity' : "
                        +
                        "145, 'shiftQuantityForState' : 120 }",
                "{ '_id' : 0, 'type' : 'chocolate', 'orderDate' : ISODate('2020-05-18T14:10:30Z'), 'state' : 'CA', 'price' : 13, 'quantity' :"
                        +
                        " 120, 'shiftQuantityForState' : 'Not available' }",
                "{ '_id' : 1, 'type' : 'chocolate', 'orderDate' : ISODate('2021-03-20T11:30:05Z'), 'state' : 'WA', 'price' : 14, 'quantity' :"
                        +
                        " 140, 'shiftQuantityForState' : 134 }",
                "{ '_id' : 5, 'type' : 'strawberry', 'orderDate' : ISODate('2019-01-08T06:12:03Z'), 'state' : 'WA', 'price' : 43, 'quantity' "
                        +
                        ": 134, 'shiftQuantityForState' : 104 }",
                "{ '_id' : 3, 'type' : 'vanilla', 'orderDate' : ISODate('2020-02-08T13:13:23Z'), 'state' : 'WA', 'price' : 13, 'quantity' : "
                        +
                        "104, 'shiftQuantityForState' : 'Not available' }");

        System.out.println(actual);
        System.out.println(expected);
        Assert.assertEquals(actual, expected);
    }

    protected List<Document> parseDocs(String... strings) {
        return stream(strings)
                .map(Document::parse)
                .collect(toList());
    }
}
