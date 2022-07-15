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
 * @description
 * @date 2022-06-12 18:37
 **/
public class FindCommandTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        String s = "   { _id: 1, flavor: \"chocolate\" },\n" +
                "   { _id: 2, flavor: \"strawberry\" },\n" +
                "   { _id: 3, flavor: \"cherry\" }";
        List<Document> documents = CreateDataUtil.parseString(s);

        mars.insert(documents,"cakeFlavors");

        String s1 = "{ _id: 1, category: \"café\", status: \"A\" }\n" +
                "{ _id: 2, category: \"cafe\", status: \"a\" }\n" +
                "{ _id: 3, category: \"cafE\", status: \"a\" }";
        List<Document> documents1 = CreateDataUtil.parseString(s1);

        mars.insert(documents1,"myColl");

        String s2 = "   { \"_id\" : 1, \"grades\" : [ 95, 92, 90 ] },\n" +
                "   { \"_id\" : 2, \"grades\" : [ 98, 100, 102 ] },\n" +
                "   { \"_id\" : 3, \"grades\" : [ 95, 110, 100 ] }";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"students");

        String s3 = "   { \"_id\" : 1,\"grades\" : [{ \"grade\" : 80, \"mean\" : 75, \"std\" : 6 },{ \"grade\" : 85, \"mean\" : 90, \"std\" : 4 },{ \"grade\" : 85, \"mean\" : 85, \"std\" : 6 }]},\n" +
                "   { \"_id\" : 2,\"grades\" : [{ \"grade\" : 90, \"mean\" : 75, \"std\" : 6 },{ \"grade\" : 87, \"mean\" : 90, \"std\" : 3 },{ \"grade\" : 85, \"mean\" : 85, \"std\" : 4 }]}";
        List<Document> documents3 = CreateDataUtil.parseString(s3);
        mars.insert(documents3,"students2");
    }

    @After
    public void DropCollection(){
        mars.dropCollection("cakeFlavors");
        mars.dropCollection("myColl");
        mars.dropCollection("students");
        mars.dropCollection("students2");
    }

    /**
     * db.cakeFlavors.runCommand( {
     *    find: db.cakeFlavors.getName(),
     *    filter: { $expr: { $eq: [ "$flavor", "$$targetFlavor" ] } },
     *    let : { targetFlavor: "chocolate" }
     * } )
     */
    @Test
    public void testForFind(){
        Document document = mars.executeCommand(Document.parse("{\n" +
                "   find: 'cakeFlavors',\n" +
                "   filter: { $expr: { $eq: [ \"$flavor\", \"$$targetFlavor\" ] } },\n" +
                "   let : { targetFlavor: \"chocolate\" }\n" +
                "}"));

        System.out.println(document);
    }

    /**
     * db.runCommand(
     *    {
     *      findAndModify: "myColl",
     *      query: { category: "cafe", status: "a" },
     *      sort: { category: 1 },
     *      update: { $set: { status: "Updated" } },
     *      collation: { locale: "fr", strength: 1 }
     *    }
     * )
     */
    @Test
    public void testForFindAndModifyWithCollation(){
        Document document = mars.executeCommand(Document.parse("   {\n" +
                "     findAndModify: \"myColl\",\n" +
                "     query: { category: \"cafe\", status: \"a\" },\n" +
                "     sort: { category: 1 },\n" +
                "     update: { $set: { status: \"Updated\" } },\n" +
                "     collation: { locale: \"fr\", strength: 1 }\n" +
                "   }"));

        Document result = Document.parse("{\n" +
                "   \"lastErrorObject\" : {\n" +
                "      \"n\" : 1\n" +
                "      \"updatedExisting\" : true,\n" +
                "   },\n" +
                "   \"value\" : {\n" +
                "      \"_id\" : 1,\n" +
                "      \"category\" : \"café\",\n" +
                "      \"status\" : \"A\"\n" +
                "   },\n" +
                "   \"ok\" : 1.0\n" +
                "}");

        Assert.assertEquals(document,result);
    }

    /**
     * db.runCommand(
     *    {
     *      findAndModify: "students",
     *      query: { grades: { $gte: 100 } },
     *      update:  { $set: { "grades.$[element]" : 100 } },
     *      arrayFilters: [ { "element": { $gte: 100 } } ]
     *    }
     * )
     */
    @Test
    public void testForFindAndModifyWithArrayFilters(){
        Document document = mars.executeCommand(Document.parse("   {\n" +
                "     findAndModify: \"students\",\n" +
                "     query: { grades: { $gte: 100 } },\n" +
                "     update:  { $set: { \"grades.$[element]\" : 100 } },\n" +
                "     arrayFilters: [ { \"element\": { $gte: 100 } } ]\n" +
                "   }"));

        System.out.println(document);
//        Document.parse("");
    }

    /**
     * db.runCommand(
     *    {
     *      findAndModify: "students2",
     *      query: {  "_id" : 1 },
     *      update: [ { $set: { "total" : { $sum: "$grades.grade" } } } ],
     *      new: true
     *    }
     * )
     */
    @Test
    public void testForFindAndInsert(){
        Document document = mars.executeCommand(Document.parse("   {\n" +
                "     findAndModify: \"students2\",\n" +
                "     query: {  \"_id\" : 1 },\n" +
                "     update: [ { $set: { \"total\" : { $sum: \"$grades.grade\" } } } ],\n" +
                "     new: true\n" +
                "   }"));
        System.out.println(document);
    }
}
