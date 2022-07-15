package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Lookup;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 13:58
 */
public class LookUpAndArrayTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData() {
        String s1 = "{ _id: 1, title: \"Reading is ...\", enrollmentlist: [ \"giraffe2\", \"pandabear\", \"artie\" ], days: [\"M\", \"W\", \"F\"] },\n" +
                "   { _id: 2, title: \"But Writing ...\", enrollmentlist: [ \"giraffe1\", \"artie\" ], days: [\"T\", \"F\"] }\n";

        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1, "classes");

        String s2 = "   { _id: 1, name: \"artie\", joined: new ISODate(\"2016-05-01\"), status: \"A\" },\n" +
                "   { _id: 2, name: \"giraffe\", joined: new ISODate(\"2017-05-01\"), status: \"D\" },\n" +
                "   { _id: 3, name: \"giraffe1\", joined: new ISODate(\"2017-10-01\"), status: \"A\" },\n" +
                "   { _id: 4, name: \"panda\", joined: new ISODate(\"2018-10-11\"), status: \"A\" },\n" +
                "   { _id: 5, name: \"pandabear\", joined: new ISODate(\"2018-12-01\"), status: \"A\" },\n" +
                "   { _id: 6, name: \"giraffe2\", joined: new ISODate(\"2018-12-01\"), status: \"D\" }";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2, "members");
    }

    @After
    public void dropCollections(){
        mars.dropCollection("classes");
        mars.dropCollection("members");
    }

    /**
     *db.classes.aggregate( [
     *    {
     *       $lookup:
     *          {
     *             from: "members",
     *             localField: "enrollmentlist",
     *             foreignField: "name",
     *             as: "enrollee_info"
     *         }
     *    }
     * ] )
     */
    @Test
    public void testForArray(){

        pipeline.lookup(Lookup.lookup("members")
                .localField("enrollmentlist")
                .foreignField("name")
                .as("enrollee_info"));

        QueryCursor classes = mars.aggregate(pipeline, "classes");
        while (classes.hasNext()){
            System.out.println(classes.next());
        }
    }

}
