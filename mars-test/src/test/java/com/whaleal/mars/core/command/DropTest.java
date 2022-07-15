package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.CollectionOptions;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 18:06
 * FileName: DropTest
 * Description:
 */
public class DropTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
//        mars.createCollection("test1", CollectionOptions.just(Collation.of("zh")));

        String s = "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffac\"), \"name\" : \"张七\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffa8\"), \"name\" : \"张三\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffa9\"), \"name\" : \"李四\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffaa\"), \"name\" : \"王五\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffab\"), \"name\" : \"马六\" }";

        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"test1");
    }

    /**
     * { drop: <collection_name>, writeConcern: <document>, comment: <any> }
     */
    @Test
    public void testForDrop(){

        Query query = new Query();
//        query.with(Sort.on().ascending("name"));

        QueryCursor<Document> person = mars.findAll(query, Document.class, "test1");
        while (person.hasNext()){
            System.out.println(person.next());
        }

        Collation person1 = Collation.parse("test1");
        System.out.println(person1.toDocument());
        System.out.println("===========================================");
        Document document = mars.executeCommand("{\n" +
                "      drop: \"test1\",\n" +
                "      comment: \"测试\",\n" +
                "      writeConcern: { w: \"majority\"}\n" +
                "   }");
        System.out.println(document);
        Query query1 = new Query();
//        query.with(Sort.on().ascending("name"));

        QueryCursor<Document> person2 = mars.findAll(query1, Document.class, "test1");
        while (person2.hasNext()){
            System.out.println(person2.next());
        }

        Collation person3 = Collation.parse("test1");
        System.out.println(person3.toDocument());
    }
}
