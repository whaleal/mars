package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.AutoBucket;
import com.whaleal.mars.core.aggregation.stages.Facet;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.push;
import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;
import static com.whaleal.mars.core.aggregation.expressions.MathExpressions.multiply;

/**
 * @author lyz
 * @desc
 * @date 2022-05-16 14:02
 */
public class BucketAutoTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline<Document> pipeline = AggregationPipeline.create();


    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"title\" : \"The Pillars of Society\", \"artist\" : \"Grosz\", \"year\" : 1926,\"price\" : 199.99,\"dimensions\" : { \"height\" : 39, \"width\" : 21, \"units\" : \"in\" } }\n" +
                "{ \"_id\" : 2, \"title\" : \"Melancholy III\", \"artist\" : \"Munch\", \"year\" : 1902,\"price\" : 280.00,\"dimensions\" : { \"height\" : 49, \"width\" : 32, \"units\" : \"in\" } }\n" +
                "{ \"_id\" : 3, \"title\" : \"Dancer\", \"artist\" : \"Miro\", \"year\" : 1925,\"price\" : 76.04,\"dimensions\" : { \"height\" : 25, \"width\" : 20, \"units\" : \"in\" } }\n" +
                "{ \"_id\" : 4, \"title\" : \"The Great Wave off Kanagawa\", \"artist\" : \"Hokusai\",\"price\" : 167.30,\"dimensions\" : { \"height\" : 24, \"width\" : 36, \"units\" : \"in\" } }\n" +
                "{ \"_id\" : 5, \"title\" : \"The Persistence of Memory\", \"artist\" : \"Dali\", \"year\" : 1931,\"price\" : 483.00,\"dimensions\" : { \"height\" : 20, \"width\" : 24, \"units\" : \"in\" } }\n" +
                "{ \"_id\" : 6, \"title\" : \"Composition VII\", \"artist\" : \"Kandinsky\", \"year\" : 1913,\"price\" : 385.00,\"dimensions\" : { \"height\" : 30, \"width\" : 46, \"units\" : \"in\" } }\n" +
                "{ \"_id\" : 7, \"title\" : \"The Scream\", \"artist\" : \"Munch\",\"price\" : 159.00,\"dimensions\" : { \"height\" : 24, \"width\" : 18, \"units\" : \"in\" } }\n" +
                "{ \"_id\" : 8, \"title\" : \"Blue Flower\", \"artist\" : \"O'Keefe\", \"year\" : 1918,\"price\" : 118.42,\"dimensions\" : { \"height\" : 24, \"width\" : 20, \"units\" : \"in\" } }";

        List<Document> documents = CreateDataUtil.parseString(s);

        mars.insert(documents,"artwork");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("artwork");
    }

    @Test
    public void testForSingleFacet(){
        pipeline.autoBucket(AutoBucket.autoBucket().groupBy(field("price"))
                .buckets(4));

        QueryCursor<Document> artwork = mars.aggregate(pipeline, "artwork");

        while (artwork.hasNext()){
            System.out.println(artwork.next());
        }
    }

    @Test
    public void testForMultiFaceted(){
        pipeline.facet(Facet.facet().field("price",AutoBucket.autoBucket().groupBy(field("price")).buckets(4))
                .field("year",AutoBucket.autoBucket().groupBy(field("year")).buckets(3).outputField("count",sum(value(1))).outputField("years",push(field("year"))))
                .field("area",AutoBucket.autoBucket().groupBy(multiply(field("dimensions.height"),field("dimensions.width"))).buckets(4).outputField("count",sum(value(1))).outputField("titles",push(field("title")))));

        QueryCursor<Document> artwork = mars.aggregate(pipeline, "artwork");
        while (artwork.hasNext()){
            System.out.println(artwork.next());
        }
    }
}
