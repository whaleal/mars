package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.ReplaceWith;
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
    }

    @After
    public void dropCollection(){
        mars.dropCollection("people");
    }

    /**
     * db.people.aggregate( [
     *    { $replaceWith: { $mergeObjects:  [ { dogs: 0, cats: 0, birds: 0, fish: 0 }, "$pets" ] } }
     * ] )
     */
    @Test
    public void testForEmbeddedDocumentation(){
        pipeline.replaceWith(ReplaceWith.with(mergeObjects().add(value(Document.parse("{ dogs: 0, cats: 0, birds: 0, fish: 0 }")))
                .add(field("pets"))));

        QueryCursor people = mars.aggregate(pipeline, "people");
        while (people.hasNext()){
            System.out.println(people.next());
        }
    }

}
