package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Lookup;
import com.whaleal.mars.core.aggregation.stages.Match;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.aggregation.stages.ReplaceRoot;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.value;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 14:17
 */
public class LookUpAndCorrelatedTest {


    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s1 = "{ \"_id\" : 1, \"student\" : \"Ann Aardvark\", sickdays: [ new ISODate (\"2018-05-01\"),new ISODate (\"2018-08-23\") ] },\n" +
                "   { \"_id\" : 2, \"student\" : \"Zoe Zebra\", sickdays: [ new ISODate (\"2018-02-01\"),new ISODate (\"2018-05-23\") ] },\n";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"absences");

        String s2 = "{ \"_id\" : 1, year: 2018, name: \"New Years\", date: new ISODate(\"2018-01-01\") },\n" +
                "   { \"_id\" : 2, year: 2018, name: \"Pi Day\", date: new ISODate(\"2018-03-14\") },\n" +
                "   { \"_id\" : 3, year: 2018, name: \"Ice Cream Day\", date: new ISODate(\"2018-07-15\") },\n" +
                "   { \"_id\" : 4, year: 2017, name: \"New Years\", date: new ISODate(\"2017-01-01\") },\n" +
                "   { \"_id\" : 5, year: 2017, name: \"Ice Cream Day\", date: new ISODate(\"2017-07-16\") }\n";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"holidays");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("absences");
        mars.dropCollection("holidays");
    }

    /**
     * db.absences.aggregate( [
     *    {
     *       $lookup:
     *          {
     *            from: "holidays",
     *            pipeline: [
     *               { $match: { year: 2018 } },
     *               { $project: { _id: 0, date: { name: "$name", date: "$date" } } },
     *               { $replaceRoot: { newRoot: "$date" } }
     *            ],
     *            as: "holidays"
     *          }
     *     }
     * ] )
     */
    @Test
    public void testForCorrelatedSubquer(){

        pipeline.lookup(Lookup.lookup("holidays")
                .pipeline(Match.match(Filters.eq("year",2018)),
                        Projection.project().exclude("_id").include("date",value(Document.parse("{ name: \"$name\", date: \"$date\" }")))
                ,ReplaceRoot.replaceRoot(field("date")))
                .as("holidays"));

        QueryCursor absences = mars.aggregate(pipeline, "absences");

        while (absences.hasNext()){
            System.out.println(absences.next());
        }

    }
}
