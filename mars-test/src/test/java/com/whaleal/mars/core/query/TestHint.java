package com.whaleal.mars.core.query;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Members;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author cjq
 * @ClassName TestHint
 * @Description
 * @date 2022/7/14 14:09
 */
public class TestHint {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.insert(new Members(1,"abc123","P",0,null,null));
        mars.insert(new Members(2,"xyz123","A",60,"reminder: ping me at 100pts","Some random comment"));
        mars.insert(new Members(3,"lmn123","P",0,null,null));
        mars.insert(new Members(4,"pqr123","D",20,"Deactivated",null));
        mars.insert(new Members(5,"ijk123","P",0,null,null));
        mars.insert(new Members(6,"cde123","A",86,"reminder: ping me at 100pts","Some random comment"));
        mars.createIndex(new Index().on("status",IndexDirection.ASC),"members");
        mars.createIndex(new Index().on("points",IndexDirection.ASC),"members");
    }

    @Test
    public void testForHint(){
        Query query = new Query();
        query.addCriteria(Criteria.where("points").lte(20).and("status").is("P"));
        Document status = new Document().append("status", 1);
        query.withHint(status);
        mars.findAndModify(query,new Update().set("member","test"),Members.class);
        //explain
        Document document = mars.executeCommand(Document.parse("{\n" +
                "     explain: {\n" +
                "       findAndModify: \"members\",\n" +
                "       query: { \"points\": { $lte: 20 }, \"status\": \"P\" },\n" +
                "       update: {\"member\":\"test\"},\n" +
                "       hint: { status: 1 }\n" +
                "     },\n" +
                "     verbosity: \"queryPlanner\"\n" +
                "   }"));
        Document result = Document.parse("{\n" +
                "\t\"queryPlanner\" : {\n" +
                "\t\t\"plannerVersion\" : 1,\n" +
                "\t\t\"namespace\" : \"mars.members\",\n" +
                "\t\t\"indexFilterSet\" : false,\n" +
                "\t\t\"parsedQuery\" : {\n" +
                "\t\t\t\"$and\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"status\" : {\n" +
                "\t\t\t\t\t\t\"$eq\" : \"P\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"points\" : {\n" +
                "\t\t\t\t\t\t\"$lte\" : 20\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t\"queryHash\" : \"66BD2C41\",\n" +
                "\t\t\"planCacheKey\" : \"4D86FE4A\",\n" +
                "\t\t\"winningPlan\" : {\n" +
                "\t\t\t\"stage\" : \"UPDATE\",\n" +
                "\t\t\t\"inputStage\" : {\n" +
                "\t\t\t\t\"stage\" : \"FETCH\",\n" +
                "\t\t\t\t\"filter\" : {\n" +
                "\t\t\t\t\t\"points\" : {\n" +
                "\t\t\t\t\t\t\"$lte\" : 20\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"inputStage\" : {\n" +
                "\t\t\t\t\t\"stage\" : \"IXSCAN\",\n" +
                "\t\t\t\t\t\"keyPattern\" : {\n" +
                "\t\t\t\t\t\t\"status\" : 1\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"indexName\" : \"status_1\",\n" +
                "\t\t\t\t\t\"isMultiKey\" : false,\n" +
                "\t\t\t\t\t\"multiKeyPaths\" : {\n" +
                "\t\t\t\t\t\t\"status\" : [ ]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"isUnique\" : false,\n" +
                "\t\t\t\t\t\"isSparse\" : false,\n" +
                "\t\t\t\t\t\"isPartial\" : false,\n" +
                "\t\t\t\t\t\"indexVersion\" : 2,\n" +
                "\t\t\t\t\t\"direction\" : \"forward\",\n" +
                "\t\t\t\t\t\"indexBounds\" : {\n" +
                "\t\t\t\t\t\t\"status\" : [\n" +
                "\t\t\t\t\t\t\t\"[\\\"P\\\", \\\"P\\\"]\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"rejectedPlans\" : [ ]\n" +
                "\t},\n" +
                "\t\"serverInfo\" : {\n" +
                "\t\t\"host\" : \"photon.-app.\",\n" +
                "\t\t\"port\" : 27017,\n" +
                "\t\t\"version\" : \"4.4.15\",\n" +
                "\t\t\"gitVersion\" : \"bc17cf2c788c5dda2801a090ea79da5ff7d5fac9\"\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(document,result);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("members");
    }
}
