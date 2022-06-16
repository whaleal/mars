package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @desc
 * @date 2022-06-10 14:50
 */
public class DeleteTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void createCollections(){
        String s = "{ \"_id\" : 1, \"item\" : \"abc\", \"price\" : 12, \"quantity\" : 2, \"type\": \"apparel\",\"status\" : \"A\" }\n" +
                "{ \"_id\" : 2, \"item\" : \"jkl\", \"price\" : 20, \"quantity\" : 1, \"type\": \"electronics\",\"status\" : \"D\" }\n" +
                "{ \"_id\" : 3, \"item\" : \"abc\", \"price\" : 10, \"quantity\" : 5, \"type\": \"apparel\",\"status\" : \"D\" }";

        List<Document> documents = CreateDataUtil.parseString(s);

        mars.insert(documents,"orders");

        String s1 = "{ _id: 1, category: \"café\", status: \"A\" }\n" +
                "{ _id: 2, category: \"cafe\", status: \"a\" }\n" +
                "{ _id: 3, category: \"cafE\", status: \"a\" }";
        List<Document> documents1 = CreateDataUtil.parseString(s1);
        mars.insert(documents1,"myColl");

        String s2 = "{ \"_id\" : 1, \"member\" : \"abc123\", \"status\" : \"P\", \"points\" :  0,  \"misc1\" : null, \"misc2\" : null },\n" +
                "   { \"_id\" : 2, \"member\" : \"xyz123\", \"status\" : \"A\", \"points\" : 60,  \"misc1\" : \"reminder: ping me at 100pts\", \"misc2\" : \"Some random comment\" },\n" +
                "   { \"_id\" : 3, \"member\" : \"lmn123\", \"status\" : \"P\", \"points\" :  0,  \"misc1\" : null, \"misc2\" : null },\n" +
                "   { \"_id\" : 4, \"member\" : \"pqr123\", \"status\" : \"D\", \"points\" : 20,  \"misc1\" : \"Deactivated\", \"misc2\" : null },\n" +
                "   { \"_id\" : 5, \"member\" : \"ijk123\", \"status\" : \"P\", \"points\" :  0,  \"misc1\" : null, \"misc2\" : null },\n" +
                "   { \"_id\" : 6, \"member\" : \"cde123\", \"status\" : \"A\", \"points\" : 86,  \"misc1\" : \"reminder: ping me at 100pts\", \"misc2\" : \"Some random comment\" }\n";
        List<Document> documents2 = CreateDataUtil.parseString(s2);
        mars.insert(documents2,"members");
    }


    @Test
    public void dropCollections(){
        mars.dropCollection("orders");
        mars.dropCollection("myColl");
        mars.dropCollection("members");
    }



    /**
     * db.runCommand(
     *    {
     *       delete: "orders",
     *       deletes: [ { q: { status: "D" }, limit: 1 } ]
     *    }
     * )
     */
    @Test
    public void testForDelete(){

        Document document = mars.executeCommand("   {\n" +
                "      delete: \"orders\",\n" +
                "      deletes: [ { q: { status: \"D\" }, limit: 1 } ]\n" +
                "   }");

        Document parse = Document.parse("{ \"ok\" : 1, \"n\" : 1 }");
        Assert.assertEquals(document,parse);
    }

    @Test
    public void testForDeleteAll(){
        Document document = mars.executeCommand("{\n" +
                "      delete: \"orders\",\n" +
                "      deletes: [ { q: { }, limit: 0 } ],\n" +
                "      writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "   }");

        System.out.println(document);
    }

    /**
     * db.runCommand({
     *    delete: "myColl",
     *    deletes: [
     *      { q: { category: "cafe", status: "a" }, limit: 0, collation: { locale: "fr", strength: 1 } }
     *    ]
     * })
     */
    @Test
    public void testForDeleteWithCollation(){
        Document document = mars.executeCommand("{delete: \"myColl\",\n" +
                "   deletes: [\n" +
                "     { q: { category: \"cafe\", status: \"a\" }, limit: 0, collation: { locale: \"fr\", strength: 1 } }\n" +
                "   ]\n" +
                "}");
        System.out.println(document);
    }


    /**
     * db.members.createIndex( { status: 1 } )
     * db.members.createIndex( { points: 1 } )
     */
    @Test
    public void createIndexOnMembers(){
        mars.createIndex(new Index("status", IndexDirection.ASC),"members");
        mars.createIndex(new Index("points", IndexDirection.ASC),"members");
    }


    /**
     *db.runCommand({
     *    delete: "members",
     *    deletes: [
     *      { q: { "points": { $lte: 20 }, "status": "P" }, limit: 0, hint: { status: 1 } }
     *    ]
     * })
     */
    @Test
    public void testForHint(){
        Document document = mars.executeCommand("{\n" +
                "   delete: \"members\",\n" +
                "   deletes: [\n" +
                "     { q: { \"points\": { $lte: 20 }, \"status\": \"P\" }, limit: 0, hint: { status: 1 } }\n" +
                "   ]\n" +
                "}");
        System.out.println(document);

    }

    /**
     * 查看上述删除操作的执行效果
     */
    @Test
    public void testForExplain(){
        Document document = mars.executeCommand("{\n" +
                "     explain: {\n" +
                "       delete: \"members\",\n" +
                "       deletes: [\n" +
                "         { q: { \"points\": { $lte: 20 }, \"status\": \"P\" }, limit: 0, hint: { status: 1 } }\n" +
                "       ]\n" +
                "     },\n" +
                "     verbosity: \"queryPlanner\"\n" +
                "   }");
        System.out.println(document);
    }
}
