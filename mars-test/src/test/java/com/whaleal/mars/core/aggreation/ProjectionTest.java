package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-25 10:47
 **/
public class ProjectionTest {

    private Mars mars = new Mars(Constant.connectionStr);


    @After
    public void  after(){
        mars.getDatabase().drop();
    }


    @Before
    public void createData(){
        String s = "{\n" +
                "  \"_id\" : 1,\n" +
                "  title: \"abc123\",\n" +
                "  isbn: \"0001122223334\",\n" +
                "  author: { last: \"zzz\", first: \"aaa\" },\n" +
                "  copies: 5\n" +
                "}";

        mars.insert(Document.parse(s),"test");
    }


    @Test
    public void testForProjection(){
        AggregationPipeline<Document> pipeline = AggregationPipeline.create();

        pipeline.project(Projection.project().include("title").include("author").exclude("_id"));

        QueryCursor<Document> aggregate = mars.aggregate(pipeline,"test");
        while (aggregate.hasNext()){
            System.out.println(aggregate.next());
        }
    }
}
