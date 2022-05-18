package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;

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
}
