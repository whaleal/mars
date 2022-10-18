//package com.whaleal.mars.base;
//
//import com.mongodb.MongoNamespace;
//import com.mongodb.client.ChangeStreamIterable;
//import com.mongodb.client.ListCollectionsIterable;
//import com.mongodb.client.MongoCollection;
//import com.whaleal.mars.Constant;
//import com.whaleal.mars.bean.Gender;
//import com.whaleal.mars.bean.Student;
//import com.whaleal.mars.core.Mars;
//import com.whaleal.mars.core.aggregation.AggregationPipeline;
//import com.whaleal.mars.core.aggregation.stages.CollectionStats;
//import com.whaleal.mars.session.AggregationImpl;
//import com.whaleal.mars.session.QueryCursor;
//import com.whaleal.mars.session.option.CollectionOptions;
//import org.bson.Document;
//import org.junit.After;
//import org.junit.Assert;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//public class EntityTest {
//    Mars mars;
//
//    @BeforeMethod
//    public void init() {
//        mars = new Mars(Constant.connectionStr);
//    }
//
//    @AfterMethod
//    public void testDropCollection() {
//        mars.dropCollection(Student.class);
//    }
//
//    @Test
//    public void testCreateCollection() {
//        CollectionOptions options = CollectionOptions.empty().capped().size(2000L).maxDocuments(2000L);
//
//        mars.createCollection(Student.class, options);
//
//        MongoCollection<Student> collection = mars.getCollection(Student.class);
//        Assert.assertNotNull(collection);
//
//        AggregationPipeline<Student> pipeline = AggregationPipeline.create(Student.class);
//        pipeline.collStats(CollectionStats.collStats());
//
//
//        QueryCursor<Student> aggregate = mars.aggregate(pipeline);
//        while (aggregate.hasNext()){
//            System.out.println(aggregate.next());
//        }
//
//
//    }
//
//    @Test
//    public void testProperty(){
//        Student student = new Student();
//        //id
//        student.setStuNo("1234");
//        //字段指定别名
//        student.setStuAge(25);
//        //指定字段值类型
//        student.setStuHeight(160D);
//        student.setStuSex(Gender.male);
//
//        mars.insert(student);
//    }
//
//    @Test
//    public void testFor(){
//        mars.getMongoClient();
//    }
//
//}
