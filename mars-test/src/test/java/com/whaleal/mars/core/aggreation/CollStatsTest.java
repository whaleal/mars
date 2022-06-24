package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.CollectionStats;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @date 2022-05-16 15:37
 */
public class CollStatsTest {
    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline<Document> pipeline = AggregationPipeline.create();

    @Before
    public void createData(){

    }

    @After
    public void dropData(){

    }

    @Test
    public void testFor(){
        pipeline.collStats(CollectionStats.collStats().histogram(true));

        Document document = mars.aggregate(pipeline, "inventory").tryNext();
        Assert.assertNotNull(document);

    }

    @Test
    public void testForStorageStats(){
        pipeline.collStats(CollectionStats.collStats());
        Document document = mars.aggregate(pipeline,"inventory").tryNext();
        System.out.println(document);
    }

}
