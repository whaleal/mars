package com.whaleal.mars.base;

import com.whaleal.mars.bean.Student;
import com.whaleal.mars.session.option.InsertManyOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;

import java.util.LinkedList;
import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.avg;
import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;

@Slf4j
public class StudentTest {

    Mars mars;
    private static Integer defStuNo = 1800;
    private static Integer initStuCount = 100;
    private static Integer initClsCount = 10;
    private static List<Student> stuList = new LinkedList<>();

    @Before
    public void init() {
        mars = new Mars(Constant.connectingStr);
        for (int i = 1; i < initClsCount; i++) {
            for (int j = 0; j < initStuCount; j++) {
                Student student = StudentGenerator.getInstance(i * 1000 + j);
                stuList.add(student);
            }
        }
    }


    @Test
    public void test01del() {
        mars.dropCollection(Student.class);
        mars.insert(stuList ,new InsertManyOptions().ordered(false));
        DeleteResult deleteResult = mars.delete(new Query(), Student.class, new DeleteOptions().multi(true));
        Assert.assertEquals("删除异常", stuList.size(), deleteResult.getDeletedCount());
        mars.dropCollection(Student.class);
    }

    @Test
    public void testInsertMany() {

        InsertManyResult result = null ;

        try {
            result = mars.insert(stuList,new InsertManyOptions().ordered(false)) ;
        }catch (Exception e){
            e.printStackTrace();
        }

        Assert.assertEquals("insert exception", stuList.size(), result.getOriginInsertManyResult().getInsertedIds().size());
    }

    @Test
    public void testDelete() {
        DeleteResult deleteResult = mars.delete(new Query(), Student.class);
        Assert.assertEquals("delete Exception", 1, deleteResult.getDeletedCount());
    }

    @Test
    public void testFind() {
        QueryCursor<Student> studentList = mars.findAll(new Query(), Student.class);
        Assert.assertNotNull(studentList);
    }

    @Test
    public void testInsertOne() {
        Student student = StudentGenerator.getInstance(defStuNo);
        mars.dropCollection("stu");
        InsertOneResult insert = mars.insert(student,"stu");
        Assert.assertNotNull("insert exception",insert);
        mars.dropCollection("stu");
    }


    @Test
    public void testGroupBy(){
        //  求各班语文的平均成绩并排序

        AggregationPipeline pipeline = new AggregationPipeline();
        pipeline.group(Group.of(Group.id("classNo")).field("cscore",sum(field("cscore"))).field("mscore",avg(field("cscore"))));
        pipeline.sort(Sort.on().ascending("_id"));

        QueryCursor<Student> aggregate = mars.aggregate(pipeline, Student.class);
        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }
    }


}
