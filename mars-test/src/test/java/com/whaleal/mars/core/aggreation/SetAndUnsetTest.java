package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.AddFields;
import com.whaleal.mars.core.aggregation.stages.Unset;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.MathExpressions.add;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 22:59
 */
public class SetAndUnsetTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = " { _id: 1, student: \"Maya\", homework: [ 10, 5, 10 ], quiz: [ 10, 8 ], extraCredit: 0 },\n" +
                "   { _id: 2, student: \"Ryan\", homework: [ 5, 6, 5 ], quiz: [ 8, 8 ], extraCredit: 8 }\n";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"scores");

        String s1 = "{ \"_id\" : 1, title: \"Antelope Antics\", isbn: \"0001122223334\", author: { last:\"An\", first: \"Auntie\" }, copies: [ { warehouse: \"A\", qty: 5 }, { warehouse: \"B\", qty: 15 } ] },\n" +
                "   { \"_id\" : 2, title: \"Bees Babble\", isbn: \"999999999333\", author: { last:\"Bumble\", first: \"Bee\" }, copies: [ { warehouse: \"A\", qty: 2 }, { warehouse: \"B\", qty: 5 } ] }\n";
        List<Document> documents1 = CreateDataUtil.parseString(s);
        mars.insert(documents1,"books");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("scores");
        mars.dropCollection("books");
    }

    /**
     * db.scores.aggregate( [
     *    {
     *      $set: {
     *         totalHomework: { $sum: "$homework" },
     *         totalQuiz: { $sum: "$quiz" }
     *      }
     *    },
     *    {
     *      $set: {
     *         totalScore: { $add: [ "$totalHomework", "$totalQuiz", "$extraCredit" ] } }
     *    }
     * ] )
     */
    @Test
    public void testForTwoSetStage(){
        pipeline.set(AddFields.of()
                .field("totalHomework",sum(field("homework")))
                .field("totalQuiz",sum(field("quiz"))));

        pipeline.set(AddFields.addFields().field("totalScore",add(field("totalHomework"),field("totalQuiz"),field("extraCredit"))));

        QueryCursor scores = mars.aggregate(pipeline, "scores");
        while (scores.hasNext()){
            System.out.println(scores.next());
        }
    }

    /**
     *db.books.aggregate([ { $unset: "copies" } ])
     * db.books.aggregate([ { $unset: [ "copies" ] } ])

     */
    @Test
    public void testForUnsetOne(){
        pipeline.unset(Unset.fields("copies"));
        QueryCursor books = mars.aggregate(pipeline, "books");
        while (books.hasNext()){
            System.out.println(books.next());
        }
    }

    /**
     *db.books.aggregate([
     *{ $unset: [ "isbn", "copies" ] }
     *])
     */
    @Test
    public void testForUnsetMore(){
       pipeline.unset(Unset.fields("isbn","copies"));
        QueryCursor books = mars.aggregate(pipeline, "books");
        while (books.hasNext()){
            System.out.println(books.next());
        }
    }

    /**
     *db.books.aggregate([
     *      { $unset: [ "isbn", "author.first", "copies.warehouse" ] }
     * ])
     */
    @Test
    public void testForUnsetEmbed(){
        pipeline.unset(Unset.fields("isbn","author.first","copies.warehouse"));
        QueryCursor books = mars.aggregate(pipeline, "books");
        while (books.hasNext()){
            System.out.println(books.next());
        }
    }
}
