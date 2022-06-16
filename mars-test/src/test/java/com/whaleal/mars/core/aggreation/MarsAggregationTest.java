package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.base.StudentGenerator;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.aggregation.stages.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.stages.Group.id;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MarsAggregationTest {
    @Autowired
    Mars mars;

    @Test
    public void testAggFilters() {
        Student instance = StudentGenerator.getInstance(1001);
        instance.setStuName("6");
        mars.insert(instance);

        AggregationPipeline< Student > pipeline = AggregationPipeline.create(Student.class);
        pipeline.match(Filters.eq("stuName", "6"));

        Student student = mars.aggregate(pipeline).tryNext();

        Assert.assertNotNull(student);
        Assert.assertEquals("6", student.getStuName());

    }


    @Test
    public void testAggProject() {

        Student student = StudentGenerator.getInstance(1001);
        mars.insert(student);

        AggregationPipeline< Student > pipeline = AggregationPipeline.create(Student.class);
        pipeline.project(Projection.of().exclude("_id").include("stuAge").include("classNo"));
        Student student1 = mars.aggregate(pipeline).tryNext();
        Assert.assertNotNull(student1);
        Assert.assertNull(student1.getStuNo());
        Assert.assertNotNull(student1.getStuAge());
        Assert.assertNotNull(student1.getClassNo());


    }


    @Test
    public void testGroupCount() {

        Student student = StudentGenerator.getInstance(1001);
        Student student1 = StudentGenerator.getInstance(1002);
        mars.insert(student);
        mars.insert(student1);

        AggregationPipeline< Student > pipeline = AggregationPipeline.create(Student.class);
        pipeline.group(Group.of(id("stuName"))
                .field("age", sum(field("age"))));
        QueryCursor< Student > aggregate = mars.aggregate(pipeline);
        while (aggregate.hasNext()) {
            System.out.println(aggregate.next());

        }


    }


    @After
    public void destory() {
        mars.dropCollection(Student.class);
    }

}
