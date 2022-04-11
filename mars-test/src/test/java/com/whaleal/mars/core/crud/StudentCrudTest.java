package com.whaleal.mars.core.crud;

import com.whaleal.icefrog.core.collection.CollUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.base.StudentGenerator;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.InsertManyOptions;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.avg;
import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;

@Slf4j
public class StudentCrudTest {

    private static final Mars mars = new Mars(Constant.connectionStr);
    private static final Integer defStuNo = 1000;
    private static final Integer initStuCount = 10;
    private static final Integer initClsCount = 10;
    private static final List< Student > stuList = new LinkedList<>();

    @BeforeMethod
    public void init() {

        for (int i = 1; i < initClsCount; i++) {
            for (int j = 0; j < initStuCount; j++) {
                Student student = StudentGenerator.getInstance(i * defStuNo + j);
                stuList.add(student);
            }
        }
        try {
            mars.dropCollection(Student.class);
        } catch (Exception e) {
        }
    }


    @Test
    public void test01del() {

        mars.insert(stuList, Student.class, new InsertManyOptions().ordered(false));
        DeleteResult deleteResult = mars.delete(new Query(), Student.class, new DeleteOptions().multi(true));

        Assert.assertEquals(stuList.size(), deleteResult.getDeletedCount());

    }

    @Test
    public void testInsertMany() {
        Student instance1 = StudentGenerator.getInstance(defStuNo);
        Student instance2 = StudentGenerator.getInstance(defStuNo + 1);
        Student instance3 = StudentGenerator.getInstance(defStuNo + 2);

        ArrayList< Student > students = CollUtil.newArrayList(instance1, instance2, instance3);

        InsertManyResult result = mars.insert(students, Student.class);
        Assert.assertEquals(students.size(), result.getOriginInsertManyResult().getInsertedIds().size());
    }

    @Test
    public void testDelete() {
        Student instance = StudentGenerator.getInstance(defStuNo);
        mars.insert(instance);
        DeleteResult deleteResult = mars.delete(new Query(), Student.class);

        Assert.assertEquals(1, deleteResult.getDeletedCount());
    }

    @Test
    public void testFind() {
        QueryCursor< Student > studentList = mars.findAll(new Query(), Student.class);
        Assert.assertNotNull(studentList);
    }

    @Test
    public void testInsertOne() {
        Student student = StudentGenerator.getInstance(defStuNo);

        InsertOneResult insert = mars.insert(student, "stu");
        Assert.assertNotNull(insert);

    }


    @Test
    public void testGroupBy() {
        //  求各班语文的平均成绩并排序

        AggregationPipeline pipeline = AggregationPipeline.create(Student.class);
        pipeline.group(Group.of(Group.id("classNo")).field("cscore", sum(field("cscore"))).field("mscore", avg(field("cscore"))));
        pipeline.sort(Sort.on().ascending("_id"));

        QueryCursor< Student > aggregate = mars.aggregate(pipeline);
        while (aggregate.hasNext()) {
            System.out.println(aggregate.next());
        }
    }


    @Test
    public void testInsertAndDrop() {
        LinkedList< Student > list = new LinkedList<>();
        int i;
        for (i = 0; i < 1000; i++) {
            Student student = StudentGenerator.getInstance(i);
            list.add(student);
        }
        mars.dropCollection(Student.class);
        InsertManyResult insert = mars.insert(list, Student.class);
        long count = mars.count(Student.class);
        Assert.assertEquals(i, count);
        mars.dropCollection(Student.class);

    }


}
