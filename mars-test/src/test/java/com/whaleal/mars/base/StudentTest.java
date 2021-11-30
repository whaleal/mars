package com.whaleal.mars.base;

import com.whaleal.mars.bean.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Precondition;
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
    private static Integer initStuCount = 1000;
    private static Integer initClsCount = 10;
    private static List<Student> stuList = new LinkedList<>();

    @Before
    public void init() {
        mars = new Mars(Constant.server100);
        for (int i = 1; i < initClsCount; i++) {
            for (int j = 0; j < initStuCount; j++) {
                Student student = StudentGenerator.getInstance(i * 1000 + j);
                stuList.add(student);
            }
        }
    }

    @Test
    public void testCRUD() {
        testInsertOne();
        testFind();
        testDelete();
        testInsertMany();
        testFind();
        testDelMany();
    }

    @Test
    public void testDelMany() {
        DeleteResult deleteResult = mars.delete(new Query(), Student.class, new DeleteOptions().multi(true));
        Precondition.PreconditionEquals("删除异常", 9000, deleteResult.getDeletedCount());
    }

    @Test
    public void testInsertMany() {
        InsertManyResult result = mars.insert(stuList);
        Precondition.PreconditionEquals("insert exception", 9000, result.getOriginInsertManyResult().getInsertedIds().size());
    }

    @Test
    public void testDelete() {
        DeleteResult deleteResult = mars.delete(new Query(), Student.class);
        Precondition.PreconditionEquals("delete Exception", 1, deleteResult.getDeletedCount());
    }

    @Test
    public void testFind() {
        QueryCursor<Student> studentList = mars.findAll(new Query(), Student.class);
        Precondition.PreconditionNotNull(studentList);
    }

    @Test
    public void testInsertOne() {
        Student student = StudentGenerator.getInstance(defStuNo);
        InsertOneResult insert = mars.insert(student,"stu");
        Precondition.PreconditionNotNull("insert exception",insert);
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
