package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.query.filters.Filters;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-16 15:46
 */
public class CountTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline<Document> pipeline = AggregationPipeline.create();


    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"subject\" : \"History\", \"score\" : 88 }\n" +
                "{ \"_id\" : 2, \"subject\" : \"History\", \"score\" : 92 }\n" +
                "{ \"_id\" : 3, \"subject\" : \"History\", \"score\" : 97 }\n" +
                "{ \"_id\" : 4, \"subject\" : \"History\", \"score\" : 71 }\n" +
                "{ \"_id\" : 5, \"subject\" : \"History\", \"score\" : 79 }\n" +
                "{ \"_id\" : 6, \"subject\" : \"History\", \"score\" : 83 }";

        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"scores");

    }

    @After
    public void dropCollections(){
        mars.dropCollection("scores");
    }

    @Test
    public void testForCount(){
        pipeline.match(Filters.gt("score",80));

        pipeline.count("passing_scores");

        Document scores = mars.aggregate(pipeline, "scores").tryNext();
        System.out.println(scores);
    }
}
