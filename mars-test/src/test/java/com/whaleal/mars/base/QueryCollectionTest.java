//package com.whaleal.mars.base;
//
//import com.whaleal.mars.bean.Book;
//import com.whaleal.mars.bean.Weather;
//import com.whaleal.mars.core.Mars;
//import com.whaleal.mars.monitor.CollStatsMetrics;
//import com.whaleal.mars.session.option.CollectionOptions;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author lyz
// * @description
// * @date 2022-07-14 10:23
// **/
//public class QueryCollectionTest {
//
//    @Autowired
//    private Mars mars;
//
//    @Before
//    public void createCollection(){
//        mars.createCollection(Book.class);
//        mars.createCollection("book1");
//
//        mars.createCollection(Weather.class,new CollectionOptions(1024L,1024L,true));
//        mars.createCollection("weather1",new CollectionOptions(1024L,1024L,true));
//    }
//
//    @After
//    public void dropCollection() {
//        mars.dropCollection("book");
//        mars.dropCollection("book1");
//        mars.dropCollection("weather");
//        mars.dropCollection("person");
//    }
//
//    @Test
//    public void testForDesc(){
//        CollStatsMetrics book1 = new CollStatsMetrics(mars.getMongoClient(), mars.getDatabase(), "book1");
//
//        Assert.assertEquals(book1.isCapped().booleanValue(),false);
//    }
//}
