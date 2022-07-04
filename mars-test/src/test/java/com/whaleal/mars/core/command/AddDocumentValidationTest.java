package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/29 0029 10:31
 * FileName: AddDocumentValidationTest
 * Description:
 */
public class AddDocumentValidationTest {

    private Mars mars = new Mars(Constant.connectionStr);
    @Before
    public void createData(){
        mars.createCollection(Book.class);
    }

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
        Document document = mars.executeCommand("{ collMod: \"book\",\n" +
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
        Document result = Document.parse("{\"n\":1, \"ok\":1.0}");
        Assert.assertEquals(result,document1);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("book");
    }
}
