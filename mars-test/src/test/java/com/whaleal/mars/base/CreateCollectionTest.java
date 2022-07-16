package com.whaleal.mars.base;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Collation;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.bean.NumberBean;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.monitor.CollStatsMetrics;
import com.whaleal.mars.monitor.CollTimeSeriesMetrics;
import com.whaleal.mars.session.option.CollectionOptions;
import org.bson.Document;
import org.junit.After;
import org.junit.Test;
import org.testng.Assert;



/**
 * @author lyz
 * @desc 测试mars创建表的语句
 * @date 2022-05-18 15:52
 */
public class CreateCollectionTest {

    Mars mars = new Mars(Constant.connectionStr);

    @After
    public void dropCollection(){
        mars.dropCollection("book");
        mars.dropCollection("book1");
        mars.dropCollection("weather");
        mars.dropCollection("person");
    }

    @Test
    public void testForCreateCommon(){
        mars.createCollection("book");

        MongoCollection<Document> person = mars.getCollection(Document.class, "book");
        Assert.assertEquals(person.getNamespace().getCollectionName(),"book");
    }

    @Test
    public void testCreateCollectionByClass(){
        mars.createCollection(Book.class);

        CollStatsMetrics book = new CollStatsMetrics(mars.getMongoClient(), mars.getDatabase(), "book");

        Assert.assertEquals(book.getNS(),mars.getDatabase().getName() + "." + "book");
    }

    @Test
    public void testForCreateCollectionByAlias(){
        mars.createCollection(Book.class);

        MongoCollection<Book> collection = mars.getCollection(Book.class);

        Assert.assertEquals(collection.getNamespace().getCollectionName(),"book1");

    }

    @Test
    public void testForCreateByCapped(){
        mars.createCollection("person",new CollectionOptions(1024L,1000L,true));

        CollStatsMetrics book = new CollStatsMetrics(mars.getMongoClient(), mars.getDatabase(), "person");

        Assert.assertEquals(book.getMaxSize().longValue(),1024L);
        Assert.assertEquals(book.getMax().longValue(),1000L);
        Assert.assertEquals(book.isCapped().booleanValue(),true);

    }

    @Test
    public void testForCreateTimeSeries(){
        mars.createCollection(Weather.class);

        CollTimeSeriesMetrics weather = new CollTimeSeriesMetrics(mars.getMongoClient(), mars.getDatabase(), "weather");
        //根据集合getBucketsName是否为空来判断是否成功创建该时序集合
        Assert.assertNotNull(weather.getBucketsName());

    }

    @Test
    public void testForCreateWithCollation(){
        mars.createCollection("book",CollectionOptions.just(Collation.builder().locale("fr").build()));
    }

    @Test
    public void testForCappedWithAnno(){
        mars.createCollection(NumberBean.class);

        CollStatsMetrics numberBean = new CollStatsMetrics(mars.getMongoClient(), mars.getDatabase(), "numberBean");
        Assert.assertEquals(numberBean.isCapped().booleanValue(),true);
        Assert.assertEquals(numberBean.getMax().longValue(),1000L);
        Assert.assertEquals(numberBean.getMaxSize().longValue(),1024 * 1024);
    }


}
