package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.*;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.push;
import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;


/**
 * @author lyz
 * @desc
 * @date 2022-05-16 15:58
 */
public class FacetTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"title\" : \"The Pillars of Society\", \"artist\" : \"Grosz\", \"year\" : 1926,\"price\" : 199.99,\"tags\" : [ \"painting\", \"satire\", \"Expressionism\", \"caricature\" ] }\n" +
                "{ \"_id\" : 2, \"title\" : \"Melancholy III\", \"artist\" : \"Munch\", \"year\" : 1902,\"price\" : 280.00,\"tags\" : [ \"woodcut\", \"Expressionism\" ] }\n" +
                "{ \"_id\" : 3, \"title\" : \"Dancer\", \"artist\" : \"Miro\", \"year\" : 1925,\"price\" : 76.04,\"tags\" : [ \"oil\", \"Surrealism\", \"painting\" ] }\n" +
                "{ \"_id\" : 4, \"title\" : \"The Great Wave off Kanagawa\", \"artist\" : \"Hokusai\",\"price\" : 167.30,\"tags\" : [ \"woodblock\", \"ukiyo-e\" ] }\n" +
                "{ \"_id\" : 5, \"title\" : \"The Persistence of Memory\", \"artist\" : \"Dali\", \"year\" : 1931,\"price\" : 483.00,\"tags\" : [ \"Surrealism\", \"painting\", \"oil\" ] }\n" +
                "{ \"_id\" : 6, \"title\" : \"Composition VII\", \"artist\" : \"Kandinsky\", \"year\" : 1913,\"price\" : 385.00,\"tags\" : [ \"oil\", \"painting\", \"abstract\" ] }\n" +
                "{ \"_id\" : 7, \"title\" : \"The Scream\", \"artist\" : \"Munch\", \"year\" : 1893,\"tags\" : [ \"Expressionism\", \"painting\", \"oil\" ] }\n" +
                "{ \"_id\" : 8, \"title\" : \"Blue Flower\", \"artist\" : \"O'Keefe\", \"year\" : 1918,\"price\" : 118.42,\"tags\" : [ \"abstract\", \"painting\" ] }";

        List<Document> documents = CreateDataUtil.parseString(s);

        mars.insert(documents,"artwork");
    }

    @After
    public void dropCollections(){
        mars.dropCollection("artwork");
    }

    @Test
    public void testForFacet(){
        pipeline.facet(Facet.of().field("categorizedByTags", Unwind.unwind("tags"), SortByCount.sortByCount(field("tags")))
                .field("categorizedByPrice", Match.match(Filters.exists("price")), Bucket.bucket().groupBy(field("price"))
                                .boundaries(value(0), value(150), value(200), value(300), value(400))
                                .defaultValue("Other")
                                .outputField("count",sum(value(1)))
                                .outputField("titles",push(field("title"))))
                .field("categorizedByYears(Auto)",AutoBucket.autoBucket().groupBy(field("year")).buckets(4)));

        QueryCursor artwork = mars.aggregate(pipeline, "artwork");
        while (artwork.hasNext()){
            System.out.println(artwork.next());
        }
    }
}
