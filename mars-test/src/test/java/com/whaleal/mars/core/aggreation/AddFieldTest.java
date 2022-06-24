package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Scores;
import com.whaleal.mars.bean.Spces;
import com.whaleal.mars.bean.Vehicles;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.AddFields;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.ArrayExpressions.array;
import static com.whaleal.mars.core.aggregation.expressions.ArrayExpressions.concatArrays;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.MathExpressions.add;


/**
 * @author lyz
 * @desc
 * @date 2022-05-12 14:57
 */
public class AddFieldTest {

    Mars mars = new Mars(Constant.connectionStr);


    @Before
    public void createData(){
        mars.insert(new Scores(1,"Maya",new int[]{10, 5,10 },new int[]{10,8},0));
        mars.insert(new Scores(2,"Ryan",new int[]{5, 6,5 },new int[]{8,8},8));


        mars.insert(new Vehicles(1,"car",new Spces(4,4)));
        mars.insert(new Vehicles(2,"motorcycle",new Spces(0,2)));
        Vehicles vehicles = new Vehicles();
        vehicles.setId(3);
        vehicles.setType("jet ski");
        mars.insert(vehicles,"vehicles");

        mars.insert(new Document("_id","1").append("dogs",10).append("cats",15),"animals");

        mars.insert(new Document("_id","1").append("item","tangerine").append("type","citrus"),"fruit");
        mars.insert(new Document("_id","2").append("item","lemon").append("type","citrus"),"fruit");
        mars.insert(new Document("_id","3").append("item","grapefruit").append("type","citrus"),"fruit");


    }

    @After
    public void dropCollection(){
        mars.dropCollection("vehicles");
        mars.dropCollection("scores");
        mars.dropCollection("animals");
        mars.dropCollection("fruit");
    }

    @Test
    public void queryData(){
        QueryCursor<Scores> all = mars.findAll(new Query(), Scores.class);

        while (all.hasNext()){
            System.out.println(all.next());
        }
    }

    /**
     * db.scores.aggregate( [
     *    {
     *      $addFields: {
     *        totalHomework: { $sum: "$homework" } ,
     *        totalQuiz: { $sum: "$quiz" }
     *      }
     *    },
     *    {
     *      $addFields: { totalScore:
     *        { $add: [ "$totalHomework", "$totalQuiz", "$extraCredit" ] } }
     *    }
     * ] )
     */
    @Test
    public void testForSample(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.addFields(AddFields.addFields().field("totalHomework",sum(field("homework"))).field("totalQuiz",sum(field("quiz"))));

        pipeline.addFields(AddFields.addFields().field("totalScore",add(field("totalHomework"),field("totalQuiz"),field("extraCredit"))));

        Document document = mars.aggregate(pipeline, "scores").tryNext();
        Document parse = Document.parse("{\"_id\": 1, \"extraCredit\": 0, \"homework\": [10, 5, 10], \"quiz\": [10, 8], \"student\": \"Maya\", \"totalHomework\": 25, \"totalQuiz\": 18, \"totalScore\": 43}");
        Assert.assertEquals(document,parse);

    }


    @Test
    public void testForEmbed(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.addFields(AddFields.addFields().field("specs.fuel_type",value("unleaded")));

        Document document = mars.aggregate(pipeline, "vehicles").tryNext();
        Document parse = Document.parse("{\"_id\": 1, \"specs\": {\"doors\": 4, \"wheels\": 4, \"fuel_type\": \"unleaded\"}, \"type\": \"car\"}");

        Assert.assertEquals(document,parse);
    }

    @Test
    public void testForCover(){

        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.addFields(AddFields.addFields().field("cats",value(20)));

        Document document = mars.aggregate(pipeline, "animals").tryNext();
        System.out.println(document.toJson());
        Document parse = Document.parse("{\"_id\": 1, \"dogs\": 10, \"cats\": 20}");

        Assert.assertEquals(document,parse);

    }

    @Test
    public void testForReplace(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.addFields(AddFields.addFields().field("_id",field("item")).field("item",value("fruit")));

        Document document = mars.aggregate(pipeline, "fruit").tryNext();

        Document parse = Document.parse("{\"_id\": \"tangerine\", \"item\": \"fruit\", \"type\": \"citrus\"}");

        Assert.assertEquals(document,parse);
    }

    /**
     * 根据值生成数组表达式：array(value(7))
     */
    @Test
    public void testForConcatArrays(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.match(Filters.eq("_id",1));

        Document scores = mars.aggregate(pipeline, "scores").tryNext();
        System.out.println(scores);

        pipeline.addFields(AddFields.of().field("homework",concatArrays(field("homework"),array(value(7)))));


        QueryCursor<Document> aggregate = mars.aggregate(pipeline,"scores");

        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }


    }



}
