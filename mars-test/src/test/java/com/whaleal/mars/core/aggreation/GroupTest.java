package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.AddFields;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.*;
import static com.whaleal.mars.core.aggregation.expressions.DateExpressions.dateToString;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.MathExpressions.multiply;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 11:04
 */
public class GroupTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "  { \"_id\" : 1, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : NumberInt(\"2\"), \"date\" : ISODate(\"2014-03-01T08:00:00Z\") },\n" +
                "  { \"_id\" : 2, \"item\" : \"jkl\", \"price\" : NumberDecimal(\"20\"), \"quantity\" : NumberInt(\"1\"), \"date\" : ISODate(\"2014-03-01T09:00:00Z\") },\n" +
                "  { \"_id\" : 3, \"item\" : \"xyz\", \"price\" : NumberDecimal(\"5\"), \"quantity\" : NumberInt( \"10\"), \"date\" : ISODate(\"2014-03-15T09:00:00Z\") },\n" +
                "  { \"_id\" : 4, \"item\" : \"xyz\", \"price\" : NumberDecimal(\"5\"), \"quantity\" :  NumberInt(\"20\") , \"date\" : ISODate(\"2014-04-04T11:21:39.736Z\") },\n" +
                "  { \"_id\" : 5, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : NumberInt(\"10\") , \"date\" : ISODate(\"2014-04-04T21:23:13.331Z\") },\n" +
                "  { \"_id\" : 6, \"item\" : \"def\", \"price\" : NumberDecimal(\"7.5\"), \"quantity\": NumberInt(\"5\" ) , \"date\" : ISODate(\"2015-06-04T05:08:13Z\") },\n" +
                "  { \"_id\" : 7, \"item\" : \"def\", \"price\" : NumberDecimal(\"7.5\"), \"quantity\": NumberInt(\"10\") , \"date\" : ISODate(\"2015-09-10T08:43:00Z\") },\n" +
                "  { \"_id\" : 8, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : NumberInt(\"5\" ) , \"date\" : ISODate(\"2016-02-06T20:20:13Z\") },\n";

//        List<Document> documents = CreateDataUtil.parseString(s);
//        mars.insert(documents,"sales");

        String s1  = "{ \"_id\" : 1, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : NumberInt(\"2\"), \"date\" : ISODate(\"2014-03-01T08:00:00Z\") },\n" +
                "  { \"_id\" : 2, \"item\" : \"jkl\", \"price\" : NumberDecimal(\"20\"), \"quantity\" : NumberInt(\"1\"), \"date\" : ISODate(\"2014-03-01T09:00:00Z\") },\n" +
                "  { \"_id\" : 3, \"item\" : \"xyz\", \"price\" : NumberDecimal(\"5\"), \"quantity\" : NumberInt( \"10\"), \"date\" : ISODate(\"2014-03-15T09:00:00Z\") },\n" +
                "  { \"_id\" : 4, \"item\" : \"xyz\", \"price\" : NumberDecimal(\"5\"), \"quantity\" :  NumberInt(\"20\") , \"date\" : ISODate(\"2014-04-04T11:21:39.736Z\") },\n" +
                "  { \"_id\" : 5, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : NumberInt(\"10\") , \"date\" : ISODate(\"2014-04-04T21:23:13.331Z\") },\n" +
                "  { \"_id\" : 6, \"item\" : \"def\", \"price\" : NumberDecimal(\"7.5\"), \"quantity\": NumberInt(\"5\" ) , \"date\" : ISODate(\"2015-06-04T05:08:13Z\") },\n" +
                "  { \"_id\" : 7, \"item\" : \"def\", \"price\" : NumberDecimal(\"7.5\"), \"quantity\": NumberInt(\"10\") , \"date\" : ISODate(\"2015-09-10T08:43:00Z\") },\n" +
                "  { \"_id\" : 8, \"item\" : \"abc\", \"price\" : NumberDecimal(\"10\"), \"quantity\" : NumberInt(\"5\" ) , \"date\" : ISODate(\"2016-02-06T20:20:13Z\") },\n";


        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"sales");

        String s2  = "{ \"_id\" : 8751, \"title\" : \"The Banquet\", \"author\" : \"Dante\", \"copies\" : 2 },\n" +
                "  { \"_id\" : 8752, \"title\" : \"Divine Comedy\", \"author\" : \"Dante\", \"copies\" : 1 },\n" +
                "  { \"_id\" : 8645, \"title\" : \"Eclogues\", \"author\" : \"Dante\", \"copies\" : 2 },\n" +
                "  { \"_id\" : 7000, \"title\" : \"The Odyssey\", \"author\" : \"Homer\", \"copies\" : 10 },\n" +
                "  { \"_id\" : 7020, \"title\" : \"Iliad\", \"author\" : \"Homer\", \"copies\" : 10 }";
        List<Document> documents = CreateDataUtil.parseString(s2);
        mars.insert(documents,"books");
    }

    @After
    public void dropCollections(){
        mars.dropCollection("sales");
//        mars.dropCollection("books");
        mars.dropCollection("books");
    }

    @Test
    public void testForDocumentCount(){
        pipeline.group(Group.group(id("null")).field("count",sum(value(1))));

        Object sales = mars.aggregate(pipeline, "sales").tryNext();
        System.out.println(sales);
    }

    /**
     * db.sales.aggregate( [ { $group : { _id : "$item" } } ] )
     */
    @Test
    public void testForDiffValue(){
        pipeline.group(Group.group(id(field("item"))));

        QueryCursor sales = mars.aggregate(pipeline, "sales");

        while (sales.hasNext()){
            System.out.println(sales.next());
        }

    }


    /**
     * db.sales.aggregate(
     *   [
     *     {
     *       $group :
     *         {
     *           _id : "$item",
     *           totalSaleAmount: { $sum: { $multiply: [ "$price", "$quantity" ] } }
     *         }
     *      },
     *      {
     *        $match: { "totalSaleAmount": { $gte: 100 } }
     *      }
     *    ]
     *  )
     */
    @Test
    public void testForGroupByItem(){
        pipeline.group(Group.group(id(field("item"))).
                field("totalSaleAmount",sum(multiply(field("price"),field("quantity")))));

        pipeline.match(Filters.gte("totalSaleAmount",100));

        QueryCursor sales = mars.aggregate(pipeline, "sales");
        while(sales.hasNext()){
            System.out.println(sales.next());
        }
    }

    /**
     * db.sales.aggregate([
     *   // First Stage
     *   {
     *     $match : { "date": { $gte: new ISODate("2014-01-01"), $lt: new ISODate("2015-01-01") } }
     *   },
     *   // Second Stage
     *   {
     *     $group : {
     *        _id : { $dateToString: { format: "%Y-%m-%d", date: "$date" } },
     *        totalSaleAmount: { $sum: { $multiply: [ "$price", "$quantity" ] } },
     *        averageQuantity: { $avg: "$quantity" },
     *        count: { $sum: 1 }
     *     }
     *   },
     *   // Third Stage
     *   {
     *     $sort : { totalSaleAmount: -1 }
     *   }
     *  ])
     */
    //todo 有误
    @Test
    public void testForGroupByYear() throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = dateFormat.parse("2014-01-01");
        Date endDate = dateFormat.parse("2015-01-01");

        pipeline.match(Filters.gte("date",beginDate),Filters.lte("date",endDate));
        pipeline.group(Group.group(id(dateToString().format("%Y-%m-%d").date(field("date"))))
                .field("totalSaleAmount",sum(multiply(field("price"),field("quantity"))))
                .field("averageQuantity",avg(field("quantity")))
                .field("count",sum(value(1))));
        pipeline.sort(Sort.sort().descending("totalSaleAmount"));

        QueryCursor sales = mars.aggregate(pipeline, "sales");
        while (sales.hasNext()){
            System.out.println(sales.next());
        }
    }

    /**
     * db.sales.aggregate([
     *   {
     *     $group : {
     *        _id : null,
     *        totalSaleAmount: { $sum: { $multiply: [ "$price", "$quantity" ] } },
     *        averageQuantity: { $avg: "$quantity" },
     *        count: { $sum: 1 }
     *     }
     *   }
     *  ])
     */
    @Test
    public void testForGroupByNull(){
        pipeline.group(Group.group(id("null"))
                .field("totalSaleAmount",sum(multiply(field("price"),field("quantity"))))
                .field("averageQuantity",avg(field("quantity")))
                .field("count",sum(value(1))));
        QueryCursor sales = mars.aggregate(pipeline, "sales");
        while (sales.hasNext()){
            System.out.println(sales.next());
        }
    }

    /**
     * db.books.aggregate([
     *    { $group : { _id : "$author", books: { $push: "$title" } } }
     *  ])
     */
    @Test
    public void testForPivotData(){
        pipeline.group(Group.group(id(field("author")))
                .field("books",push(field("title"))));

        QueryCursor books = mars.aggregate(pipeline, "books");
        while (books.hasNext()){
            System.out.println(books.next());
        }
    }

    /**
     * db.books.aggregate([
     *    // First Stage
     *    {
     *      $group : { _id : "$author", books: { $push: "$$ROOT" } }
     *    },
     *    // Second Stage
     *    {
     *      $addFields:
     *        {
     *          totalCopies : { $sum: "$books.copies" }
     *        }
     *    }
     *  ])
     */
    @Test
    public void testForGroupByAuthor(){
        pipeline.group(Group.group(id(field("author")))
//                .field("books",push(field(field("ROOT").toString()))));
                .field("books",push(value("$$ROOT"))));

        pipeline.addFields(AddFields.of().field("totalCopies",sum(value("$books.copies"))));

        QueryCursor books = mars.aggregate(pipeline, "books");
        while (books.hasNext()){
            System.out.println(books.next());
        }
    }

}
