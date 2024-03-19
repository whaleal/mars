package com.whaleal.mars.base;


import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Child;
import com.whaleal.mars.bean.Gender;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.CollectionStats;
import com.whaleal.mars.session.QueryCursor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EntityTest {
    Mars mars;

    @Before
    public void init() {
        mars = new Mars(Constant.connectionStr);
    }

    @After
    public void testDropCollection() {
        mars.dropCollection(Student.class);
    }

    @Test
    public void testCreateCollection() {
        mars.createCollection(Student.class, new CreateCollectionOptions().capped(true).sizeInBytes(2000).maxDocuments(2000l));

        MongoCollection<Student> collection = mars.getCollection(Student.class);
        Assert.assertNotNull(collection);

        AggregationPipeline<Student> pipeline = AggregationPipeline.create(Student.class);
        pipeline.collStats(CollectionStats.collStats());


        QueryCursor<Student> aggregate = mars.aggregate(pipeline);
        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }


    }

    @Test
    public void testProperty(){
        Student student = new Student();
        //id
        student.setStuNo("1234");
        //字段指定别名
        student.setStuAge(25);
        //指定字段值类型
        student.setStuHeight(160D);
        student.setStuSex(Gender.male);

        mars.insert(student);
    }

    @Test
    public void testFor(){
        mars.getMongoClient();
    }



    @Test
    public void testEntityAnnotation(){




    }

}
