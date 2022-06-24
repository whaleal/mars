package com.whaleal.mars.core.aggreation;

import com.mongodb.client.model.MergeOptions;
import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Merge;
import com.whaleal.mars.core.aggregation.stages.Projection;
import com.whaleal.mars.core.query.filters.Filters;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.push;
import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.sum;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 14:24
 */
public class MergeTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "{ \"_id\" : 1, employee: \"Ant\", dept: \"A\", salary: 100000, fiscal_year: 2017 },\n" +
                "   { \"_id\" : 2, employee: \"Bee\", dept: \"A\", salary: 120000, fiscal_year: 2017 },\n" +
                "   { \"_id\" : 3, employee: \"Cat\", dept: \"Z\", salary: 115000, fiscal_year: 2017 },\n" +
                "   { \"_id\" : 4, employee: \"Ant\", dept: \"A\", salary: 115000, fiscal_year: 2018 },\n" +
                "   { \"_id\" : 5, employee: \"Bee\", dept: \"Z\", salary: 145000, fiscal_year: 2018 },\n" +
                "   { \"_id\" : 6, employee: \"Cat\", dept: \"Z\", salary: 135000, fiscal_year: 2018 },\n" +
                "   { \"_id\" : 7, employee: \"Gecko\", dept: \"A\", salary: 100000, fiscal_year: 2018 },\n" +
                "   { \"_id\" : 8, employee: \"Ant\", dept: \"A\", salary: 125000, fiscal_year: 2019 },\n" +
                "   { \"_id\" : 9, employee: \"Bee\", dept: \"Z\", salary: 160000, fiscal_year: 2019 },\n" +
                "   { \"_id\" : 10, employee: \"Cat\", dept: \"Z\", salary: 150000, fiscal_year: 2019 }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"salaries");



        String s1 = "{ \"_id\" : 11,  employee: \"Wren\", dept: \"Z\", salary: 100000, fiscal_year: 2019 },\n" +
                "   { \"_id\" : 12,  employee: \"Zebra\", dept: \"A\", salary: 150000, fiscal_year: 2019 },\n" +
                "   { \"_id\" : 13,  employee: \"headcount1\", dept: \"Z\", salary: 120000, fiscal_year: 2020 },\n" +
                "   { \"_id\" : 14,  employee: \"headcount2\", dept: \"Z\", salary: 120000, fiscal_year: 2020 }\n";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"salaries");

        String s2 = "{ \"_id\" : { \"fiscal_year\" : 2017, \"dept\" : \"A\" }, \"salaries\" : 220000 }\n" +
                "{ \"_id\" : { \"fiscal_year\" : 2017, \"dept\" : \"Z\" }, \"salaries\" : 115000 }\n" +
                "{ \"_id\" : { \"fiscal_year\" : 2018, \"dept\" : \"A\" }, \"salaries\" : 215000 }\n" +
                "{ \"_id\" : { \"fiscal_year\" : 2018, \"dept\" : \"Z\" }, \"salaries\" : 280000 }\n" +
                "{ \"_id\" : { \"fiscal_year\" : 2019, \"dept\" : \"A\" }, \"salaries\" : 125000 }\n" +
                "{ \"_id\" : { \"fiscal_year\" : 2019, \"dept\" : \"Z\" }, \"salaries\" : 310000 }";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"budgets");

        String s3 = "{ \"_id\" : ObjectId(\"5cd8c68261baa09e9f3622be\"), \"employees\" : [ \"Ant\", \"Gecko\" ], \"dept\" : \"A\", \"fiscal_year\" : 2018 }\n" +
                "{ \"_id\" : ObjectId(\"5cd8c68261baa09e9f3622bf\"), \"employees\" : [ \"Ant\", \"Bee\" ], \"dept\" : \"A\", \"fiscal_year\" : 2017 }\n" +
                "{ \"_id\" : ObjectId(\"5cd8c68261baa09e9f3622c0\"), \"employees\" : [ \"Bee\", \"Cat\" ], \"dept\" : \"Z\", \"fiscal_year\" : 2018 }\n" +
                "{ \"_id\" : ObjectId(\"5cd8c68261baa09e9f3622c1\"), \"employees\" : [ \"Cat\" ], \"dept\" : \"Z\", \"fiscal_year\" : 2017 }";
        List<Document> documents3 = CreateDataUtil.parseString(s3);
        mars.insert(documents3,"orgArchive");


    }

    @After
    public void dropCollection(){
        mars.dropCollection("salaries");
        mars.dropCollection("budgets");
//        mars.dropCollection("orgArchive");
    }


    /**
     * db.getSiblingDB("zoo").salaries.aggregate( [
     *    { $group: { _id: { fiscal_year: "$fiscal_year", dept: "$dept" }, salaries: { $sum: "$salary" } } },
     *    { $merge : { into: { db: "reporting", coll: "budgets" }, on: "_id",  whenMatched: "replace", whenNotMatched: "insert" } }
     * ] )
     */
    @Test
    public void testForInit(){
        pipeline.group(Group.group(id(Expressions.value(new Document("fiscal_year","$fiscal_year").append("dept","$dept"))))
                .field("salaries",sum(field("salary"))));
        pipeline.merge(Merge.into("budgets")
                .on("_id")
                .whenMatched(MergeOptions.WhenMatched.REPLACE)
                .whenNotMatched(MergeOptions.WhenNotMatched.INSERT));

        QueryCursor salaries = mars.aggregate(pipeline, "salaries");
        while (salaries.hasNext()){
            System.out.println(salaries.next());
        }
    }

    /**
     * db.getSiblingDB("zoo").salaries.aggregate( [
     *    { $match : { fiscal_year:  { $gte : 2019 } } },
     *    { $group: { _id: { fiscal_year: "$fiscal_year", dept: "$dept" }, salaries: { $sum: "$salary" } } },
     *    { $merge : { into: { db: "reporting", coll: "budgets" }, on: "_id",  whenMatched: "replace", whenNotMatched: "insert" } }
     * ] )
     */

    @Test
    public void testForReplace(){
        pipeline.match(Filters.gte("fiscal_year",2019));
        pipeline.group(Group.group(id(Expressions.value(new Document("fiscal_year","$fiscal_year").append("dept","$dept"))))
                .field("salaries",sum(field("salary"))));
        pipeline.merge(Merge.into("budgets")
                .on("_id")
                .whenMatched(MergeOptions.WhenMatched.REPLACE)
                .whenNotMatched(MergeOptions.WhenNotMatched.INSERT));
        mars.aggregate(pipeline,"salaries");

//        MongoDatabase database = mars.getDatabase();
//        MongoCollection<Document> budgets = database.getCollection("budgets");
//        FindIterable<Document> documents = budgets.find();

    }

    /**
     * db.getSiblingDB("reporting").orgArchive.createIndex ( { fiscal_year: 1, dept: 1 }, { unique: true } )
     *
     * db.getSiblingDB("zoo").salaries.aggregate( [
     *     { $match: { fiscal_year: 2019 }},
     *     { $group: { _id: { fiscal_year: "$fiscal_year", dept: "$dept" }, employees: { $push: "$employee" } } },
     *     { $project: { _id: 0, dept: "$_id.dept", fiscal_year: "$_id.fiscal_year", employees: 1 } },
     *     { $merge : { into : { db: "reporting", coll: "orgArchive" }, on: [ "dept", "fiscal_year" ], whenMatched: "fail" } }
     * ] )
     */
    @Test
    public void testForOnlyInsert(){
        mars.createIndex(new Index().on("fiscal_year", IndexDirection.ASC).on("dept",IndexDirection.ASC).on("unique",IndexDirection.fromValue(true)),"orgArchive");
        pipeline.match(Filters.eq("fiscal_year",2019));
        pipeline.group(Group.group(id(Expressions.value(new Document("fiscal_year","$fiscal_year").append("dept","$dept"))))
                .field("employees",push(field("employee"))));
        pipeline.project(Projection.of().exclude("_id")
                .include("dept",field("_id.dept"))
                .include("fiscal_year",field("fiscal_year"))
                .include("employees"));
        pipeline.merge(Merge.into("orgArchive")
                .on("dept","fiscal_year")
                .whenMatched(MergeOptions.WhenMatched.FAIL));

        mars.aggregate(pipeline,"salaries");
    }


}
