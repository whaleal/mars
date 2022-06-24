package com.whaleal.mars.core.aggreation;


import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Orders;
import com.whaleal.mars.bean.Users;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.aggregation.stages.Unwind;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.avg;
import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.DateExpressions.dateToString;
import static com.whaleal.mars.core.aggregation.expressions.DateExpressions.month;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.MathExpressions.multiply;
import static com.whaleal.mars.core.aggregation.expressions.StringExpressions.toUpper;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author lyz
 * @desc
 * @date 2022-05-09 11:54
 */
public class AggregationTestByOrdersAndUsers {

    Mars mars;

    @Before
    public void createDatabases(){
        mars = new Mars(Constant.connectionStr);

        Orders orders0 = new Orders("0", "Pepperoni", "small", 19, 10, new Date(2021, Calendar.MARCH, 13));
        Orders orders1 = new Orders("1", "Pepperoni", "medium", 20, 20, new Date(2021, Calendar.MARCH, 13));
        Orders orders2 = new Orders("2", "Pepperoni", "large", 21, 30, new Date(2021, Calendar.MARCH, 17));
        Orders orders3 = new Orders("3", "Cheese", "small", 12, 15, new Date(2021, Calendar.MARCH, 13));
        Orders orders4 = new Orders("4", "Cheese", "medium", 13, 50, new Date(2022, Calendar.JANUARY, 12));
        Orders orders5 = new Orders("5", "Cheese", "large", 14, 10, new Date(2022, Calendar.JANUARY, 12));
        Orders orders6 = new Orders("6", "Vegan", "small", 17, 10, new Date(2021, Calendar.JANUARY, 13));
        Orders orders7 = new Orders("7", "Vegan", "medium", 18, 10, new Date(2021, Calendar.MARCH, 13));

        mars.insert(orders0);
        mars.insert(orders1);
        mars.insert(orders2);
        mars.insert(orders3);
        mars.insert(orders4);
        mars.insert(orders5);
        mars.insert(orders6);
        mars.insert(orders7);

        mars.insert(new Users("jane",new Date(2011,Calendar.MARCH,02), new String[]{"golf", "racquetball"}));
        mars.insert(new Users("joe",new Date(2012,Calendar.JULY,02), new String[]{"tennis", "golf", "swimming"}));
        mars.insert(new Users("ruth",new Date(2012,Calendar.JANUARY,02), new String[]{"tennis", "golf", "swimming"}));
        mars.insert(new Users("harold",new Date(2012,Calendar.JANUARY,02), new String[]{"tennis", "golf", "swimming"}));
        mars.insert(new Users("kate",new Date(2012,Calendar.JANUARY,02), new String[]{"tennis", "golf", "swimming"}));
        mars.insert(new Users("jill",new Date(2012,Calendar.FEBRUARY,02), new String[]{"tennis", "golf", "swimming"}));


    }

    @After
    public void dropCollection(){
        mars.dropCollection("orders");

        mars.dropCollection("users");


    }


    @Test
    public void testForMatchAndGroup(){

        AggregationPipeline<Orders> pipeline = AggregationPipeline.create(Orders.class);
        pipeline.match(Filters.eq("size", "medium"));

        pipeline.group(Group.group(id("name"))
                .field("quantity",sum(field("quantity"))));

//        pipeline.project(Projection.project().exclude("name").exclude("size").exclude("price").exclude("date"));
        pipeline.project(Projection.project().include("_id").include("totalQuantity"));

        QueryCursor<Orders> aggregate = mars.aggregate(pipeline);

        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }

    }

    /**
     * db.orders.aggregate( [
     *
     *    // Stage 1: Filter pizza order documents by date range
     *    {
     *       $match:
     *       {
     *          "date": { $gte: new ISODate( "2020-01-30" ), $lt: new ISODate( "2022-01-30" ) }
     *       }
     *    },
     *
     *    // Stage 2: Group remaining documents by date and calculate results
     *    {
     *       $group:
     *       {
     *          _id: { $dateToString: { format: "%Y-%m-%d", date: "$date" } },
     *          totalOrderValue: { $sum: { $multiply: [ "$price", "$quantity" ] } },
     *          averageOrderQuantity: { $avg: "$quantity" }
     *       }
     *    },
     *
     *    // Stage 3: Sort documents by totalOrderValue in descending order
     *    {
     *       $sort: { totalOrderValue: -1 }
     *    }
     *
     *  ] )
     */
    @Test
    public void testForDate(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();
        pipeline.match(Filters.gte("date",new Date(2020, Calendar.JANUARY,30)),
                Filters.lte("date",new Date(2022, Calendar.JANUARY,30)));

        pipeline.group(Group.group(id(dateToString().format("%Y-%m-%d").date(field("date"))))
                .field("totalOrderValue",sum(multiply(field("price"),field("quantity"))))
                .field("averageOrderQuantity",avg(field("quantity"))));

        pipeline.sort(Sort.sort().descending("totalOrderValue"));


        QueryCursor<Document> aggregate = mars.aggregate(pipeline,"orders");
        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }
    }

    @Test
    public void testForNormalize(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.project(Projection.project().exclude("_id").include("name",toUpper(field("_id"))));
        pipeline.sort(Sort.sort().ascending("name"));

        QueryCursor<Document> users = mars.aggregate(pipeline, "users");
        while(users.hasNext()){
            System.out.println(users.next());
        }
    }

    @Test
    public void testForJoinMonth(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.project(Projection.project().include("month_joined",month(field("joined")))
                .include("name",field("_id"))
                .exclude("_id"));

        pipeline.sort(Sort.sort().ascending("month_joined"));

        QueryCursor<Document> users = mars.aggregate(pipeline, "users");
        while (users.hasNext()){
            System.out.println(users.next());
        }
    }

    /**
     * db.users.aggregate(
     *   [
     *     { $project : { month_joined : { $month : "$joined" } } } ,
     *     { $group : { _id : {month_joined:"$month_joined"} , number : { $sum : 1 } } },
     *     { $sort : { "_id.month_joined" : 1 } }
     *   ]
     * )
     */
    @Test
    public void testForJoinSumByMonth(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.project(Projection.project().include("month_joined",month(field("joined"))));

        pipeline.group(Group.group(id(Expressions.value(new Document("month_joined","$month_joined")))).field("number",sum(Expressions.value(1))));

        pipeline.sort(Sort.sort().ascending("_id.month_joined"));

        QueryCursor<Document> users = mars.aggregate(pipeline, "users");

        while (users.hasNext()){
            System.out.println(users.next());
        }
    }


    /**
     * db.users.aggregate(
     *   [
     *     { $unwind : "$likes" },
     *     { $group : { _id : "$likes" , number : { $sum : 1 } } },
     *     { $sort : { number : -1 } },
     *     { $limit : 5 }
     *   ]
     * )
     */
    @Test
    public void testForMostLike(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.unwind(Unwind.on("likes"));
        pipeline.group(Group.group(id("likes")).field("number",sum(value(1))));

        pipeline.sort(Sort.on().descending("number"));
        pipeline.limit(5);
        QueryCursor<Document> users = mars.aggregate(pipeline, "users");
        while (users.hasNext()) {
            System.out.println(users.next());

        }
    }

    @Test
    public void testForSinglePurpose(){
        long orders = mars.estimatedCount("orders");
        Assert.assertEquals(orders,8);


        long orders1 = mars.count(new Query(), "orders");
        Assert.assertEquals(orders1,8);

        //todo 缺少db.collection().distinct
//        mars.distinct
    }

}
