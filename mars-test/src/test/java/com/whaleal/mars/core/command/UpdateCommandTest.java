package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.util.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author lyz
 * @description
 * @date 2022-06-12 22:45
 **/
public class UpdateCommandTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        String s = "   { _id: 1, member: \"abc123\", status: \"Pending\", points: 0, misc1: \"note to self: confirm status\", misc2: \"Need to activate\" },\n" +
                "   { _id: 2, member: \"xyz123\", status: \"D\", points: 59, misc1: \"reminder: ping me at 100pts\", misc2: \"Some random comment\" },\n";

        String s1  = "";

        List<Document> documents = CreateDataUtil.parseString(s);
        mars.insert(documents,"members");


    }

    @After
    public void dropCollection(){
        mars.dropCollection("members");
    }

    /**
     * db.runCommand(
     *    {
     *       update: "members",
     *       updates: [
     *          {
     *            q: { member: "abc123" }, u: { $set: { status: "A" }, $inc: { points: 1 } }
     *          }
     *       ],
     *       ordered: false,
     *       writeConcern: { w: "majority", wtimeout: 5000 }
     *    }
     * )
     */
    @Test
    public void testForUpdate(){

        QueryCursor<Document> members1 = mars.findAll(new Query(), Document.class, "members");
        while (members1.hasNext()){
            System.out.println(members1.next());
        }
        Document document = mars.executeCommand("   {\n" +
                "      update: 'members',\n" +
                "      updates: [\n" +
                "         {\n" +
                "           q: { member: \"abc123\" }, u: { $set: { status: \"A\" }, $inc: { points: 1 } }\n" +
                "         }\n" +
                "      ],\n" +
                "      ordered: false,\n" +
                "      writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "   }");

        Document result = Document.parse("{ \"n\" : 1, \"nModified\" : 1, \"ok\" : 1}");

        Assert.assertEquals(document,result);

        QueryCursor<Document> members = mars.findAll(new Query(), Document.class, "members");
        while (members.hasNext()){
            System.out.println(members.next());
        }
    }
}
