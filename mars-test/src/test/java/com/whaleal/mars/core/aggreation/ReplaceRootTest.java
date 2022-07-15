package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.util.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.ReplaceRoot;
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
 * @date 2022-05-17 17:08
 */
public class ReplaceRootTest {


    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = " { \"_id\" : 1, \"name\" : \"Arlene\", \"age\" : 34, \"pets\" : { \"dogs\" : 2, \"cats\" : 1 } }\n" +
                "{ \"_id\" : 2, \"name\" : \"Sam\", \"age\" : 41, \"pets\" : { \"cats\" : 1, \"fish\" : 3 } }\n" +
                "{ \"_id\" : 3, \"name\" : \"Maria\", \"age\" : 25 }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"people");

        String s1 = "{\n" +
                "      \"_id\" : 1,\n" +
                "      \"grades\" : [\n" +
                "         { \"test\": 1, \"grade\" : 80, \"mean\" : 75, \"std\" : 6 },\n" +
                "         { \"test\": 2, \"grade\" : 85, \"mean\" : 90, \"std\" : 4 },\n" +
                "         { \"test\": 3, \"grade\" : 95, \"mean\" : 85, \"std\" : 6 }\n" +
                "      ]\n" +
                "   }";
        String s2 = "{\n" +
                "      \"_id\" : 2,\n" +
                "      \"grades\" : [\n" +
                "         { \"test\": 1, \"grade\" : 90, \"mean\" : 75, \"std\" : 6 },\n" +
                "         { \"test\": 2, \"grade\" : 87, \"mean\" : 90, \"std\" : 3 },\n" +
                "         { \"test\": 3, \"grade\" : 91, \"mean\" : 85, \"std\" : 4 }\n" +
                "      ]\n" +
                "   }";
        mars.insert(Document.parse(s1),"students");
        mars.insert(Document.parse(s2),"students");

        String s3 = "{ \"_id\" : 1, \"first_name\" : \"Gary\", \"last_name\" : \"Sheffield\", \"city\" : \"New York\" }\n" +
                "{ \"_id\" : 2, \"first_name\" : \"Nancy\", \"last_name\" : \"Walker\", \"city\" : \"Anaheim\" }\n" +
                "{ \"_id\" : 3, \"first_name\" : \"Peter\", \"last_name\" : \"Sumner\", \"city\" : \"Toledo\" }";
        List<Document> documents1 = CreateDataUtil.parseString(s3);
        mars.insert(documents1,"contacts");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("people");
        mars.dropCollection("students");
        mars.dropCollection("contacts");
    }


    /**
     * db.people.aggregate( [
     *    { $replaceRoot: { newRoot: { $mergeObjects:  [ { dogs: 0, cats: 0, birds: 0, fish: 0 }, "$pets" ] }} }
     * ] )
     */
    @Test
    public void testForEmbedDocument(){
        Document parse = Document.parse("{ dogs: 0, cats: 0, birds: 0, fish: 0 }");
        pipeline.replaceRoot(ReplaceRoot.with().field("newRoot",mergeObjects()
                .add(value(parse)).add(field("pets"))));

        QueryCursor collection = mars.aggregate(pipeline, "people");
        while (collection.hasNext()){
            System.out.println(collection.next());
        }
    }

    /**
     * db.students.aggregate( [
     *    { $unwind: "$grades" },
     *    { $match: { "grades.grade" : { $gte: 90 } } },
     *    { $replaceRoot: { newRoot: "$grades" } }
     * ] )
     */
    @Test
    public void testForDocumentNested(){
        pipeline.unwind(Unwind.on("grades"));
        pipeline.match(Filters.gte("grades.grade",90));
        pipeline.replaceRoot(ReplaceRoot.with().field("newRoot",field("grades")));

        QueryCursor people = mars.aggregate(pipeline, "students");
        while (people.hasNext()){
            System.out.println(people.next());
        }
    }

    /**
     * db.contacts.aggregate( [
     *    {
     *       $replaceRoot: {
     *          newRoot: {
     *             full_name: {
     *                $concat : [ "$first_name", " ", "$last_name" ]
     *             }
     *          }
     *       }
     *    }
     * ] )
     */
    @Test
    public void testForContactsDocument(){
        pipeline.replaceRoot(ReplaceRoot.replaceRoot(value(Document.parse("{full_name: {$concat : [ \"$first_name\", \" \", \"$last_name\" ]}}"))));
//        QueryCursor<Document> authors = mars.findAll(new Query(), Document.class, "authors");
//        while (authors.hasNext()){
//            System.out.println(authors.next());
//        }

        QueryCursor contacts = mars.aggregate(pipeline, "contacts");

        while (contacts.hasNext()){
            System.out.println(contacts.next());
        }
    }

}
