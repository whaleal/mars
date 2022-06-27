package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.CollectionOptions;
import org.bson.Document;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc 测试mars创建表的语句
 * @date 2022-05-18 15:52
 */
public class CreateCollectionTest {

    Mars mars = new Mars(Constant.connectionStr);

//    @Before


    @Test
    public void testForCreateCommon(){
        mars.createCollection("person", CollectionOptions.just(Collation.of("zh")));

        String s = "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffac\"), \"name\" : \"张七\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffa8\"), \"name\" : \"张三\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffa9\"), \"name\" : \"李四\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffaa\"), \"name\" : \"王五\" }\n" +
                "{ \"_id\" : ObjectId(\"586b98980cec8d86881cffab\"), \"name\" : \"马六\" }";

        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"person");
    }

    @Test
    public void testForQuery(){
        Query query = new Query();
//        query.with(Sort.on().ascending("name"));

        QueryCursor<Document> person = mars.findAll(query, Document.class, "person");
        while (person.hasNext()){
            System.out.println(person.next());
        }

        Collation person1 = Collation.parse("person");
        System.out.println(person1.toDocument());
    }

    @Test
    public void deleteCollection(){
//        mars.dropCollection("testCreate");
        mars.dropCollection("weather");
    }


    @Test
    public void testForCreateCollection(){
        mars.createCollection(Weather.class);
    }

    @Test
    public void testFor(){


    }

}
