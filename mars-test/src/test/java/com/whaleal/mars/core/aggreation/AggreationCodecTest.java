package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.MarsCursor;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.AggregationOptions;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

public class AggreationCodecTest {

    Mars mars;


    @Before
    public void init() {
        mars = new Mars(Constant.connectionStr);

        Assert.assertNotNull(mars);
    }


    @Test
    public void testAggFilters() {

        AggregationPipeline< Student > pipeline = AggregationPipeline.create(Student.class);
        pipeline.match(Filters.eq("stuName", "6"));

        MarsCursor< Student > aggregate = mars.aggregate(pipeline);

        while (aggregate.hasNext()) {
            System.out.println("得到的结果");
            System.out.println(aggregate.next());
        }

    }


    @Test
    public void testAggPtoject() {

        AggregationPipeline< Person > pipeline = AggregationPipeline.create(Person.class);


        pipeline.project(Projection.of().exclude("age"));
        AggregationOptions aggregationOptions = new AggregationOptions();
//        aggregationOptions.collation()

        QueryCursor< Person > aggregate = mars.aggregate(pipeline);

        while (aggregate.hasNext()) {
            System.out.println(aggregate.next());
        }

    }


    @Test
    public void testGroupCount() {
        AggregationPipeline pipeline = AggregationPipeline.create();
        pipeline.group(Group.group(id("address.city.name"))
                .field("counter", sum(field("age"))));
        QueryCursor< Document > aggregate = mars.aggregate(pipeline, "person");


        while (aggregate.hasNext()) {
            System.out.println(aggregate.next());
        }
    }


}
