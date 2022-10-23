package com.whaleal.mars.core.crud;

import com.mongodb.client.result.DeleteResult;
import com.whaleal.icefrog.core.collection.CollUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.util.StudentGenerator;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.InsertManyOptions;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.avg;
import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;

@Slf4j
public class StudentCrudTest {

    private static final Mars mars = new Mars(Constant.connectionStr);
    private static final Integer defStuNo = 1000;
    private static final Integer initStuCount = 10;
    private static final Integer initClsCount = 10;
    private static final List<Student> stuList = new LinkedList<>();

//    @BeforeMethod
//    public void init() {
//
//        for (int i = 1; i < initClsCount; i++) {
//            for (int j = 0; j < initStuCount; j++) {
//                Student student = StudentGenerator.getInstance(i * defStuNo + j);
//                stuList.add(student);
//            }
//        }
//        try {
//            mars.dropCollection(Student.class);
//        } catch (Exception e) {
//        }
//    }


    @Test
    public void test01del() {

        mars.insert(stuList, Student.class, new InsertManyOptions().ordered(false));
        com.mongodb.client.result.DeleteResult deleteResult = mars.delete(new Query(), Student.class);

        Assert.assertEquals(stuList.size(), deleteResult.getDeletedCount());

    }

    @Test
    public void testInsertMany() {
        Student instance1 = StudentGenerator.getInstance(defStuNo);
        Student instance22 = StudentGenerator.getInstance(2002);
        Student instance2 = StudentGenerator.getInstance(defStuNo + 1);
        Student instance3 = StudentGenerator.getInstance(defStuNo + 2);

        ArrayList<Student> students = CollUtil.newArrayList(instance1, instance2, instance3, instance22);

        mars.insertAll(students);

/*        InsertManyResult result = mars.insert(students, Student.class);
        Assert.assertEquals(students.size(), result.getOriginInsertManyResult().getInsertedIds().size());*/
    }

    @Test
    public void testDelete() {

        Animal animal = new Animal();
        animal.setId("1001");

        System.out.println("++++++++++++");
        Query query = new Query(new Criteria("_id").is("1111"));

//        DeleteResult delete = mars.delete(animal,"cc");                       //false
//        DeleteResult delete = mars.delete(animal);                           //false
//        DeleteResult id = mars.delete(query, Student.class);
//        DeleteResult id = mars.delete(query, "student");                       //false
//        DeleteResult cc = mars.delete(query, "cc", new DeleteOptions());      //false
//        DeleteResult cc = mars.delete(query, Animal.class, "cc");
//        DeleteResult delete = mars.delete(query, Animal.class, new DeleteOptions(),"cc");
//        DeleteResult delete = mars.delete(query, Animal.class, new DeleteOptions());

//        DeleteResult deleteResult = mars.deleteMulti(query, Student.class);
//        DeleteResult deleteResult = mars.deleteMulti(query, Animal.class,"cc");
//        DeleteResult deleteResult = mars.deleteMulti(query, "cc");            //false



      /*  DeleteResult deleteResult = mars.delete(new Query(), Student.class);

        Assert.assertEquals(1, deleteResult.getDeletedCount());*/
    }

    @Test
    public void testFind() {
        QueryCursor<Student> studentList = mars.findAll(new Query(), Student.class);
        Assert.assertNotNull(studentList);
        System.out.println(studentList.toList().size());
    }

    @Test
    public void testFindOne() {

        Optional<Animal> marsOneEntity = mars.findOne(new Query(), Animal.class);
        Optional<Animal> marsOneCollection = mars.findOne(new Query(), Animal.class, "cc");

        Animal cc = mars.findOne(new Query(new Criteria("_id").is("1001")), Animal.class).orElse(new Animal());


        System.out.println("+++++++++++++++++++++++");
        System.out.println(marsOneEntity);
        System.out.println("========================");
        System.out.println(marsOneCollection);
        System.out.println("========================");
        System.out.println(cc);
        System.out.println("+++++++++++++++++++++++");


    }


    @Test
    public void testInsertOne() {
        Student student = StudentGenerator.getInstance(defStuNo);
/*
        InsertOneResult insert = mars.insert(student, "stu");
        Assert.assertNotNull(insert);*/

    }


    @Test
    public void testGroupBy() {
        //  求各班语文的平均成绩并排序

        AggregationPipeline pipeline = AggregationPipeline.create(Student.class);
        pipeline.group(Group.group(Group.id("classNo")).field("cscore", sum(field("cscore"))).field("mscore", avg(field("cscore"))));
        pipeline.sort(Sort.on().ascending("_id"));

        QueryCursor<Student> aggregate = mars.aggregate(pipeline);
        while (aggregate.hasNext()) {
            System.out.println(aggregate.next());
        }
    }


    @Test
    public void testInsertAndDrop() {
        LinkedList<Student> list = new LinkedList<>();
        int i;
        for (i = 0; i < 1000; i++) {
            Student student = StudentGenerator.getInstance(i);
            list.add(student);
        }
        mars.dropCollection(Student.class);
      /*  InsertManyResult insert = mars.insert(list, Student.class);
        long count = mars.estimatedCount(Student.class);
        Assert.assertEquals(i, count);
        mars.dropCollection(Student.class);
*/
    }


}
