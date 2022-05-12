package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Scores;
import com.whaleal.mars.bean.Spces;
import com.whaleal.mars.bean.Vehicles;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.AddFields;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.MathExpressions.add;


/**
 * @author lyz
 * @desc
 * @date 2022-05-12 14:57
 */
public class AddToSetTest {

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
        mars.insert(vehicles);

        mars.insert(new Document("id",1).append("dogs",10).append("cats",15),"animals");



    }

    @After
    public void dropCollection(){
        mars.dropCollection("vehicles");
        mars.dropCollection("scores");
    }

    @Test
    public void queryData(){
        QueryCursor<Scores> all = mars.findAll(new Query(), Scores.class);

        while (all.hasNext()){
            System.out.println(all.next());
        }
    }

    //todo
    @Test
    public void testForSample(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.addFields(AddFields.of().field("totalHomework",sum(field("homework"))).field("totalQuiz",sum(field("quiz"))));

        pipeline.addFields(AddFields.of().field("totalScore",add(field("totalHomework"),field("totalQuiz"),field("extraCredit"))));

        QueryCursor<Document> scores = mars.aggregate(pipeline, "scores");
        while (scores.hasNext()){
            System.out.println(scores.next());
        }

    }


    @Test
    public void testForEmbed(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.addFields(AddFields.of().field("specs.fuel_type",value("unleaded")));

        QueryCursor<Document> vehicles = mars.aggregate(pipeline, "vehicles");
        while (vehicles.hasNext()){
            System.out.println(vehicles.next());
        }
    }

    @Test
    public void testForCover(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.addFields(AddFields.of().field("cats",value(20)));


    }
}
