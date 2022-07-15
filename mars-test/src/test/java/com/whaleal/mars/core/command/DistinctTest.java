package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-06-10 13:26
 */
public class DistinctTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createCollections(){
        String s1 = "{ \"_id\": 1, \"dept\": \"A\", \"item\": { \"sku\": \"111\", \"color\": \"red\" }, \"sizes\": [ \"S\", \"M\" ] }\n" +
                "{ \"_id\": 2, \"dept\": \"A\", \"item\": { \"sku\": \"111\", \"color\": \"blue\" }, \"sizes\": [ \"M\", \"L\" ] }\n" +
                "{ \"_id\": 3, \"dept\": \"B\", \"item\": { \"sku\": \"222\", \"color\": \"blue\" }, \"sizes\": \"S\" }\n" +
                "{ \"_id\": 4, \"dept\": \"A\", \"item\": { \"sku\": \"333\", \"color\": \"black\" }, \"sizes\": [ \"S\" ] }";

        List<Document> documents = CreateDataUtil.parseString(s1);
        mars.insert(documents,"inventory");

        String s2 = "{ _id: 1, category: \"café\", status: \"A\" }\n" +
                "{ _id: 2, category: \"cafe\", status: \"a\" }\n" +
                "{ _id: 3, category: \"cafE\", status: \"a\" }";
        List<Document> documents1 = CreateDataUtil.parseString(s2);
        mars.insert(documents1,"myColl");
    }

    @After
    public void dropCollections(){
        mars.dropCollection("inventory");
        mars.dropCollection("myColl");

    }


    /**
     * db.runCommand ( { distinct: "inventory", key: "dept" } )
     *
     * db.runCommand ( { distinct: "inventory", key: "item.sku" } )
     */
    @Test
    public void testForDistinct(){
        Document append = new Document("distinct", "inventory").append("key", "dept");

        Document document = mars.executeCommand(append);
        Document result = Document.parse("{\n" +
                "   \"values\" : [ \"A\", \"B\" ],\n" +
                "   \"ok\" : 1.0\n" +
                "}");
        Assert.assertEquals(document,result);
        System.out.println(document);

        Document append1 = new Document("distinct", "inventory").append("key", "item.sku");
        Document document1 = mars.executeCommand(append1);
        Document result1 = Document.parse("{\n" +
                "  \"values\" : [ \"111\", \"222\", \"333\" ],\n" +
                "  \"ok\" : 1.0\n" +
                "}");

        Assert.assertEquals(document1,result1);
    }

    /**
     * db.runCommand ( { distinct: "inventory", key: "sizes" } )
     */
    @Test
    public void testForSize(){
        Document document = mars.executeCommand(Document.parse("{ distinct: \"inventory\", key: \"sizes\" } "));
        Document result = Document.parse("{\n" +
                "  \"values\" : [ \"L\", \"M\", \"S\" ],\n" +
                "  \"ok\" : 1.0\n" +
                "}");

        Assert.assertEquals(document,result);
    }

    /**
     * db.runCommand ( { distinct: "inventory", key: "item.sku", query: { dept: "A"} } )
     */
    @Test
    public void testForDistinctAndQuery(){
        Document result = mars.executeCommand(Document.parse(" { distinct: \"inventory\", key: \"item.sku\", query: { dept: \"A\"} }"));

        Document parse = Document.parse("{\n" +
                "  \"values\" : [ \"111\", \"333\" ],\n" +
                "  \"ok\" : 1.0\n" +
                "}");

        Assert.assertEquals(result,parse);
    }

    /**
     * db.runCommand(
     *    {
     *       distinct: "myColl",
     *       key: "category",
     *       collation: { locale: "fr", strength: 1 }
     *    }
     * )
     */
    @Test
    public void testForCollation(){
        Document document = mars.executeCommand(Document.parse("   {\n" +
                "      distinct: \"myColl\",\n" +
                "      key: \"category\",\n" +
                "      collation: { locale: \"fr\", strength: 1 }\n" +
                "   }"));

        System.out.println(document);
//        Document result = Document.parse("");

    }

    /**
     * db.runCommand(
     *    {
     *      distinct: "restaurants",
     *      key: "rating",
     *      query: { cuisine: "italian" },
     *      readConcern: { level: "majority" }
     *    }
     * )
     */
    @Test
    public void testForReadConcern(){
        Document document = mars.executeCommand(Document.parse("   {\n" +
                "     distinct: \"restaurants\",\n" +
                "     key: \"rating\",\n" +
                "     query: { cuisine: \"italian\" },\n" +
                "     readConcern: { level: \"majority\" }\n" +
                "   }"));

        System.out.println(document);
    }
}
