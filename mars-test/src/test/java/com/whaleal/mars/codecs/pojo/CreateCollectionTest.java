package com.whaleal.mars.codecs.pojo;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.CollectionOptions;
import org.bson.Document;
import org.junit.Test;

import java.util.Date;


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
        mars.createCollection("person", CollectionOptions.just(Collation.of("zh").toMongoCollation()));
    }

    @Test
    public void testForQuery(){
        Query query = new Query();

        QueryCursor<Document> person = mars.findAll(query, Document.class, "person");
        while (person.hasNext()){
            System.out.println(person.next());
        }
    }

    @Test
    public void deleteCollection(){
        mars.dropCollection("weather");
        mars.dropCollection("book");
    }


    @Test
    public void testForCreateCollection(){
        mars.createCollection(Weather.class);
    }


    @Test
    public void testForProperty(){

        Weather weather = new Weather();
//        weather.setProp("test");
        weather.setSensorId(1);
        weather.setTimestamp(new Date());
        weather.setTemp("36.7C");
        weather.setType("nice");
        mars.insert(weather);

    }


}
