package com.whaleal.mars.core.command;

import com.whaleal.icefrog.core.collection.ListUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 15:32
 * FileName: UsersInfoTest
 * Description:
 */
public class UsersInfoTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.executeCommand("{\n" +
                "       createUser: \"testUser\",\n" +
                "       pwd: \"testPwd\",\n" +
                "       customData: { employeeId: 12345 },\n" +
                "       roles: [\n" +
                "                \"readWrite\"\n" +
                "              ],\n" +
                "       writeConcern: { w: \"majority\" , wtimeout: 5000 }\n" +
                "}");
    }
    /**
     * {
     *   usersInfo: <various>,
     *   showCredentials: <Boolean>,
     *   showPrivileges: <Boolean>,
     *   showAuthenticationRestrictions: <Boolean>,
     *   filter: <document>,
     *   comment: <any>
     * }
     */
    @Test
    public void testForSpecificUsersInfo(){
        Document document = mars.executeCommand(Document.parse(" {\n" +
                "     usersInfo:  { user: \"testUser\", db: \"mars\" },\n" +
                "     showPrivileges: true\n" +
                "   }"));
        List<Document> users = (List<Document>) document.get("users");
        Document users1 = users.get(0);
        Document document1 = new Document()
                .append("user", users1.get("user"))
                .append("inheritedPrivileges", users1.get("inheritedPrivileges"));
        Document result = Document.parse("{\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"inheritedPrivileges\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"resource\" : {\n" +
                "\t\t\t\t\t\t\"db\" : \"mars\",\n" +
                "\t\t\t\t\t\t\"collection\" : \"system.js\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"actions\" : [\n" +
                "\t\t\t\t\t\t\"changeStream\",\n" +
                "\t\t\t\t\t\t\"collStats\",\n" +
                "\t\t\t\t\t\t\"convertToCapped\",\n" +
                "\t\t\t\t\t\t\"createCollection\",\n" +
                "\t\t\t\t\t\t\"createIndex\",\n" +
                "\t\t\t\t\t\t\"dbHash\",\n" +
                "\t\t\t\t\t\t\"dbStats\",\n" +
                "\t\t\t\t\t\t\"dropCollection\",\n" +
                "\t\t\t\t\t\t\"dropIndex\",\n" +
                "\t\t\t\t\t\t\"emptycapped\",\n" +
                "\t\t\t\t\t\t\"find\",\n" +
                "\t\t\t\t\t\t\"insert\",\n" +
                "\t\t\t\t\t\t\"killCursors\",\n" +
                "\t\t\t\t\t\t\"listCollections\",\n" +
                "\t\t\t\t\t\t\"listIndexes\",\n" +
                "\t\t\t\t\t\t\"planCacheRead\",\n" +
                "\t\t\t\t\t\t\"remove\",\n" +
                "\t\t\t\t\t\t\"renameCollectionSameDB\",\n" +
                "\t\t\t\t\t\t\"update\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\n" +
                "}");
        Assert.assertEquals(result,document1);
    }


    @Test
    public void testForAllUsersInfo(){
        Document document = mars.executeCommand(Document.parse(" { usersInfo: 1 }"));
        List<Document> users = (List<Document>) document.get("users");
        Document users1 = users.get(0);

        Document document1 = new Document()
                .append("user", users1.get("user"))
                .append("roles", users1.get("roles"));
        Document result = Document.parse("{\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"roles\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"role\" : \"readWrite\",\n" +
                "\t\t\t\t\t\"db\" : \"mars\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "}");
        Assert.assertEquals(result,document1);
    }

    @Test
    public void testForSpecificFiltersUsersInfo(){
        Document document = mars.executeCommand(Document.parse(" { usersInfo: 1, filter: { roles: { role: \"root\", db: \"admin\" } } }"));
        Document result = Document.parse("{ \"users\" : [ ], \"ok\" : 1.0 }");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForUsersInfoWithMechanisms(){
        Document document = mars.executeCommand(Document.parse("{ usersInfo: { forAllDBs: true}, filter: { mechanisms: \"SCRAM-SHA-1\" } } "));
        List<Document> users = (List<Document>) document.get("users");
        Document users1 = users.get(0);
        Document document1 = new Document()
                .append("user", users1.get("user"))
                .append("mechanisms", users1.get("mechanisms"));
        Document result = Document.parse("{\n" +
                "\t\t\t\"user\" : \"testUser\",\n" +
                "\t\t\t\"mechanisms\" : [\n" +
                "\t\t\t\t\"SCRAM-SHA-1\",\n" +
                "\t\t\t\t\"SCRAM-SHA-256\"\n" +
                "\t\t\t]\n" +
                "}");
        Assert.assertEquals(result,document1);
    }

    @After
    public void dropUser(){
        mars.executeCommand("{\n" +
                "   dropUser: \"testUser\",\n" +
                "   writeConcern: { w: \"majority\", wtimeout: 5000 }\n" +
                "} ");
    }
}
