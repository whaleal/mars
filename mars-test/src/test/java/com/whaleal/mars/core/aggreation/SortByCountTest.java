package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.util.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Unwind;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;

/**
 * @author lyz
 * @desc
 * @date 2022-05-18 0:06
 */
public class SortByCountTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, \"title\" : \"The Pillars of Society\", \"artist\" : \"Grosz\", \"year\" : 1926, \"tags\" : [ \"painting\", \"satire\", \"Expressionism\", \"caricature\" ] }\n" +
                "{ \"_id\" : 2, \"title\" : \"Melancholy III\", \"artist\" : \"Munch\", \"year\" : 1902, \"tags\" : [ \"woodcut\", \"Expressionism\" ] }\n" +
                "{ \"_id\" : 3, \"title\" : \"Dancer\", \"artist\" : \"Miro\", \"year\" : 1925, \"tags\" : [ \"oil\", \"Surrealism\", \"painting\" ] }\n" +
                "{ \"_id\" : 4, \"title\" : \"The Great Wave off Kanagawa\", \"artist\" : \"Hokusai\", \"tags\" : [ \"woodblock\", \"ukiyo-e\" ] }\n" +
                "{ \"_id\" : 5, \"title\" : \"The Persistence of Memory\", \"artist\" : \"Dali\", \"year\" : 1931, \"tags\" : [ \"Surrealism\", \"painting\", \"oil\" ] }\n" +
                "{ \"_id\" : 6, \"title\" : \"Composition VII\", \"artist\" : \"Kandinsky\", \"year\" : 1913, \"tags\" : [ \"oil\", \"painting\", \"abstract\" ] }\n" +
                "{ \"_id\" : 7, \"title\" : \"The Scream\", \"artist\" : \"Munch\", \"year\" : 1893, \"tags\" : [ \"Expressionism\", \"painting\", \"oil\" ] }\n" +
                "{ \"_id\" : 8, \"title\" : \"Blue Flower\", \"artist\" : \"O'Keefe\", \"year\" : 1918, \"tags\" : [ \"abstract\", \"painting\" ] }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"exhibits");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("exhibits");
    }

    /**
     * db.exhibits.aggregate( [ { $unwind: "$tags" },  { $sortByCount: "$tags" } ] )
     */
    @Test
    public void testForSortByCount(){
        pipeline.unwind(Unwind.unwind("tags"));
        pipeline.sortByCount(field("tags"));

        QueryCursor exhibits = mars.aggregate(pipeline, "exhibits");
        while (exhibits.hasNext()){
            System.out.println(exhibits.next());
        }
    }
}
