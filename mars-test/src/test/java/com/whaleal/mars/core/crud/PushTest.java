package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.PushOptions;
import com.whaleal.mars.session.result.UpdateResult;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testng.annotations.AfterTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyz
 * @description
 * @date 2022-06-22 15:38
 **/
public class PushTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createCollection(){
        mars.insert(Document.parse("{ _id: 1, scores: [ 44, 78, 38, 80 ] } "),"students");
        mars.insert(Document.parse("   {\n" +
                "      \"_id\" : 5,\n" +
                "      \"quizzes\" : [\n" +
                "         { \"wk\": 1, \"score\" : 10 },\n" +
                "         { \"wk\": 2, \"score\" : 8 },\n" +
                "         { \"wk\": 3, \"score\" : 5 },\n" +
                "         { \"wk\": 4, \"score\" : 6 }\n" +
                "      ]\n" +
                "   }"),"students");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("students");
    }

    @Test
    public void testFor(){
        Query id = new Query(Criteria.where("_id").is(5));

        Update update = new Update();


        update.push("quizzes").each(new Document("wk","5").append("score","8"),new Document("wk","6").append("score","7"),new Document("wk","7").append("score","6"));
        update.push("quizzes").sort(Sort.descending("score"));
//        update.push("quizzes").slice(3);

        System.out.println(update.getUpdateObject());

        UpdateResult students = mars.update(id, update, "students");
        System.out.println(students.getMatchedCount() + students.getModifiedCount());

        QueryCursor<Document> students1 = mars.findAll(new Query(), Document.class, "students");
        while (students1.hasNext()){
            System.out.println(students1.next());
        }


    }

    @Test
    public void testForParse(){
        String s = "Document{{$push=Document{{quizzes=Document{{$each=[Ljava.lang.Object;@62010f5c, $sort=Document{{score=1}}, $slice=3}}}}}}";

        System.out.println(Document.parse(s).toJson());
    }
}
