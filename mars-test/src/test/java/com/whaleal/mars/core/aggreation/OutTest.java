package com.whaleal.mars.core.aggreation;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Group;
import com.whaleal.mars.core.aggregation.stages.Out;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.whaleal.mars.core.aggregation.expressions.AccumulatorExpressions.push;
import static com.whaleal.mars.core.aggregation.expressions.Expressions.field;
import static com.whaleal.mars.core.aggregation.stages.Group.id;

/**
 * @author lyz
 * @desc
 * @date 2022-05-17 14:34
 */
public class OutTest {

    Mars mars = new Mars(Constant.connectionStr);

    AggregationPipeline pipeline = AggregationPipeline.create();

    @Before
    public void createData(){
        String s = "   { \"_id\" : 8751, \"title\" : \"The Banquet\", \"author\" : \"Dante\", \"copies\" : 2 },\n" +
                "   { \"_id\" : 8752, \"title\" : \"Divine Comedy\", \"author\" : \"Dante\", \"copies\" : 1 },\n" +
                "   { \"_id\" : 8645, \"title\" : \"Eclogues\", \"author\" : \"Dante\", \"copies\" : 2 },\n" +
                "   { \"_id\" : 7000, \"title\" : \"The Odyssey\", \"author\" : \"Homer\", \"copies\" : 10 },\n" +
                "   { \"_id\" : 7020, \"title\" : \"Iliad\", \"author\" : \"Homer\", \"copies\" : 10 }";
        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"books");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("books");
    }

    @Test
    public void dropOutCollections(){
        mars.dropCollection("authors");
    }

    /**
     *db.getSiblingDB("test").books.aggregate( [
     *     { $group : { _id : "$author", books: { $push: "$title" } } },
     *     { $out : "authors" }
     * ] )
     */
    @Test
    public void testForSameDataBase(){
        pipeline.group(Group.group(id(field("author")))
                .field("books",push(field("title"))));

        pipeline.out(Out.to("authors"));

        mars.aggregate(pipeline,"books");
    }

    /**
     * db.getSiblingDB("test").books.aggregate( [
     *     { $group : { _id : "$author", books: { $push: "$title" } } },
     *     { $out : { db: "reporting", coll: "authors" } }
     * ] )
     */
    //todo 不支持插入到外部数据库中，暂时不考虑
    @Test
    public void testForOtherDatabase(){
        pipeline.group(Group.group(id(field("author")))
                .field("books",push(field("title"))));
        pipeline.out(Out.to(""));
    }
}
