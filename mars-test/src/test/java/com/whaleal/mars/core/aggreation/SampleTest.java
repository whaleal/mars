package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.util.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 22:12
 */
public class SampleTest {


    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"name\" : \"dave123\", \"q1\" : true, \"q2\" : true }\n" +
                "{ \"_id\" : 2, \"name\" : \"dave2\", \"q1\" : false, \"q2\" : false  }\n" +
                "{ \"_id\" : 3, \"name\" : \"ahn\", \"q1\" : true, \"q2\" : true  }\n" +
                "{ \"_id\" : 4, \"name\" : \"li\", \"q1\" : true, \"q2\" : false  }\n" +
                "{ \"_id\" : 5, \"name\" : \"annT\", \"q1\" : false, \"q2\" : true  }\n" +
                "{ \"_id\" : 6, \"name\" : \"li\", \"q1\" : true, \"q2\" : true  }\n" +
                "{ \"_id\" : 7, \"name\" : \"ty\", \"q1\" : false, \"q2\" : true  }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"users");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("users");
    }

    /**
     * db.users.aggregate(
     *    [ { $sample: { size: 3 } } ]
     * )
     */
    @Test
    public void testFor(){
        pipeline.sample(3);

        Object users = mars.aggregate(pipeline, "users").tryNext();
        Assert.assertNotNull(users);



    }

}
