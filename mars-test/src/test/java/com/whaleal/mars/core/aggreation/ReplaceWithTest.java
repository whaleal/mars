package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.ReplaceWith;
import com.whaleal.mars.core.aggregation.stages.Unwind;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.ObjectExpressions.mergeObjects;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 20:48
 */
public class ReplaceWithTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "   { \"_id\" : 1, \"name\" : \"Arlene\", \"age\" : 34, \"pets\" : { \"dogs\" : 2, \"cats\" : 1 } },\n" +
                "   { \"_id\" : 2, \"name\" : \"Sam\", \"age\" : 41, \"pets\" : { \"cats\" : 1, \"fish\" : 3 } },\n" +
                "   { \"_id\" : 3, \"name\" : \"Maria\", \"age\" : 25 }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"people");

        mars.insert(Document.parse("   {\n" +
                "      \"_id\" : 1,\n" +
                "      \"grades\" : [\n" +
                "         { \"test\": 1, \"grade\" : 80, \"mean\" : 75, \"std\" : 6 },\n" +
                "         { \"test\": 2, \"grade\" : 85, \"mean\" : 90, \"std\" : 4 },\n" +
                "         { \"test\": 3, \"grade\" : 95, \"mean\" : 85, \"std\" : 6 }\n" +
                "      ]\n" +
                "   }"),"students");
        mars.insert(Document.parse("   {\n" +
                "      \"_id\" : 2,\n" +
                "      \"grades\" : [\n" +
                "         { \"test\": 1, \"grade\" : 90, \"mean\" : 75, \"std\" : 6 },\n" +
                "         { \"test\": 2, \"grade\" : 87, \"mean\" : 90, \"std\" : 3 },\n" +
                "         { \"test\": 3, \"grade\" : 91, \"mean\" : 85, \"std\" : 4 }\n" +
                "      ]\n" +
                "   }"),"students");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("people");

        mars.dropCollection("students");
    }

    /**
     * db.people.aggregate( [
     *    { $replaceWith: { $mergeObjects:  [ { dogs: 0, cats: 0, birds: 0, fish: 0 }, "$pets" ] } }
     * ] )
     */
    @Test
    public void testForEmbeddedDocumentation(){
        pipeline.replaceWith(ReplaceWith.replaceWith(mergeObjects().add(value(Document.parse("{ dogs: 0, cats: 0, birds: 0, fish: 0 }")))
                .add(field("pets"))));

        QueryCursor people = mars.aggregate(pipeline, "people");
        while (people.hasNext()){
            System.out.println(people.next());
        }
    }

    /**
     *db.students.aggregate( [
     *    { $unwind: "$grades" },
     *    { $match: { "grades.grade" : { $gte: 90 } } },
     *    { $replaceWith: "$grades" }
     * ] )
     */
    @Test
    public void testForEmbedArray(){
        pipeline.unwind(Unwind.on("grades"));
        pipeline.match(Filters.gte("grades.grade",90));
        pipeline.replaceWith(ReplaceWith.replaceWith(field("grades")));

        QueryCursor students = mars.aggregate(pipeline, "students");
        while (students.hasNext()){
            System.out.println(students.next());
        }
    }

}
