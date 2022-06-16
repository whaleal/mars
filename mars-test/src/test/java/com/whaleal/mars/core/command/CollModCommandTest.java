package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 13:57
 * FileName: CollModCommandTest
 * Description:
 */
public class CollModCommandTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( { collMod: <collection or view>, <option1>: <value1>, <option2>: <value2> ... } )
     * 有索引的情况下，进行修改
     * db.runCommand( {
     *    collMod: <collection>,
     *    index: {
     *       keyPattern: <index_spec> || name: <index_name>,
     *       expireAfterSeconds: <number>,  // If changing the TTL expiration threshold
     *       hidden: <boolean>              // If changing the visibility of the index from the query planner
     *    }
     * } )
     */
    @Test
    public void testForChangeExpirationValue(){
        Document document = mars.executeCommand("{\n" +
                "   collMod: \"person\",\n" +
                "   index: {\n" +
                "      keyPattern: { lastAccess: 1 },\n" +
                "      expireAfterSeconds: 3600\n" +
                "   }\n" +
                "}");
        Document result = Document.parse("{ \"expireAfterSeconds_old\" : 1800, \"expireAfterSeconds_new\" : 3600, \"ok\" : 1 }");
        Assert.assertEquals(document,result);
    }

    @Test
    public void testForHideIndexFromTheQueryPlanner(){
        Document document = mars.executeCommand("{\n" +
                "   collMod: \"person\",\n" +
                "   index: {\n" +
                "      keyPattern: { shippedDate: 1 },\n" +
                "      hidden: true\n" +
                "   }\n" +
                "}");
        Document result = Document.parse("{ \"hidden_old\" : false, \"hidden_new\" : true, \"ok\" : 1 }");
        Assert.assertEquals(document,result);
    }

    //todo 查看mongodb日志验证是否有警告信息
    /**
     * db.runCommand( { collMod: "contacts",
     *    validator: { $jsonSchema: {
     *       bsonType: "object",
     *       required: [ "phone" ],
     *       properties: {
     *          phone: {
     *             bsonType: "string",
     *             description: "must be a string and is required"
     *          },
     *          email: {
     *             bsonType : "string",
     *             pattern : "@mongodb\.com$",
     *             description: "must be a string and match the regular expression pattern"
     *          },
     *          status: {
     *             enum: [ "Unknown", "Incomplete" ],
     *             description: "can only be one of the enum values"
     *          }
     *       }
     *    } },
     *    validationLevel: "moderate",
     *    validationAction: "warn"
     * } )
     */
    @Test
    public void testForAddDocumentValidation(){
        System.out.println("向person集合添加校验规则");
        Document document = mars.executeCommand("{ collMod: \"person\",\n" +
                "   validator: { $jsonSchema: {\n" +
                "      bsonType: \"object\",\n" +
                "      required: [ \"phone\" ],\n" +
                "      properties: {\n" +
                "         phone: {\n" +
                "            bsonType: \"string\",\n" +
                "            description: \"must be a string and is required\"\n" +
                "         },\n" +
                "         email: {\n" +
                "            bsonType : \"string\",\n" +
//                "            pattern : \"@mongodb\\.com$\",\n" +
                "            description: \"must be a string and match the regular expression pattern\"\n" +
                "         },\n" +
                "         status: {\n" +
                "            enum: [ \"Unknown\", \"Incomplete\" ],\n" +
                "            description: \"can only be one of the enum values\"\n" +
                "         }\n" +
                "      }\n" +
                "   } },\n" +
                "   validationLevel: \"moderate\",\n" +
                "   validationAction: \"warn\"\n" +
                "}");
        System.out.println("尝插入数据");
        Document document1 = mars.executeCommand("{\n" +
                "      insert: \"users\",\n" +
                "      documents: [ { name: \"Amanda\", status: \"Updated\" }]\n" +
                "   }");
        System.out.println(document1);
    }
}
