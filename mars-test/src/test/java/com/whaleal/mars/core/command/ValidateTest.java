package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:39
 * FileName: ValidateTest
 * Description:
 */
public class ValidateTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void createData(){
        mars.createCollection(Document.class);
    }

    /**
     * db.runCommand( {
     *    validate: <string>,          // Collection name
     *    full: <boolean>,             // Optional
     *    repair: <boolean>,           // Optional, added in MongoDB 5.0
     *    metadata: <boolean>          // Optional, added in MongoDB 5.0.4
     * } )
     */
    @Test
    public void testForValidate(){

        Document document = mars.executeCommand(Document.parse("{ validate: \"document\" }"));
        Document result = Document.parse("{\n" +
                "\t\"ns\" : \"mars.document\",\n" +
                "\t\"nInvalidDocuments\" : 0,\n" +
                "\t\"nrecords\" : 0,\n" +
                "\t\"nIndexes\" : 1,\n" +
                "\t\"keysPerIndex\" : {\n" +
                "\t\t\"_id_\" : 0\n" +
                "\t},\n" +
                "\t\"indexDetails\" : {\n" +
                "\t\t\"_id_\" : {\n" +
                "\t\t\t\"valid\" : true\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"valid\" : true,\n" +
                "\t\"warnings\" : [ ],\n" +
                "\t\"errors\" : [ ],\n" +
                "\t\"extraIndexEntries\" : [ ],\n" +
                "\t\"missingIndexEntries\" : [ ],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
        //全验证
        Document document1 = mars.executeCommand(Document.parse("{ validate: \"document\", full: true }"));
        Document result1 = Document.parse("{\n" +
                "\t\"ns\" : \"mars.document\",\n" +
                "\t\"nInvalidDocuments\" : 0,\n" +
                "\t\"nrecords\" : 0,\n" +
                "\t\"nIndexes\" : 1,\n" +
                "\t\"keysPerIndex\" : {\n" +
                "\t\t\"_id_\" : 0\n" +
                "\t},\n" +
                "\t\"indexDetails\" : {\n" +
                "\t\t\"_id_\" : {\n" +
                "\t\t\t\"valid\" : true\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"valid\" : true,\n" +
                "\t\"warnings\" : [ ],\n" +
                "\t\"errors\" : [ ],\n" +
                "\t\"extraIndexEntries\" : [ ],\n" +
                "\t\"missingIndexEntries\" : [ ],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result1,document1);
        //修复验证
        Document document2 = mars.executeCommand(Document.parse("{ validate: \"document\", repair: true } "));
        Document result2 = Document.parse("{\n" +
                "\t\"ns\" : \"mars.document\",\n" +
                "\t\"nInvalidDocuments\" : 0,\n" +
                "\t\"nrecords\" : 0,\n" +
                "\t\"nIndexes\" : 1,\n" +
                "\t\"keysPerIndex\" : {\n" +
                "\t\t\"_id_\" : 0\n" +
                "\t},\n" +
                "\t\"indexDetails\" : {\n" +
                "\t\t\"_id_\" : {\n" +
                "\t\t\t\"valid\" : true\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"valid\" : true,\n" +
                "\t\"warnings\" : [ ],\n" +
                "\t\"errors\" : [ ],\n" +
                "\t\"extraIndexEntries\" : [ ],\n" +
                "\t\"missingIndexEntries\" : [ ],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result2,document2);
        //元数据验证
        Document document3 = mars.executeCommand(Document.parse("{ validate: \"document\", metadata: true } "));
        Document result3 = Document.parse("{\n" +
                "\t\"ns\" : \"mars.document\",\n" +
                "\t\"nInvalidDocuments\" : 0,\n" +
                "\t\"nrecords\" : 0,\n" +
                "\t\"nIndexes\" : 1,\n" +
                "\t\"keysPerIndex\" : {\n" +
                "\t\t\"_id_\" : 0\n" +
                "\t},\n" +
                "\t\"indexDetails\" : {\n" +
                "\t\t\"_id_\" : {\n" +
                "\t\t\t\"valid\" : true\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"valid\" : true,\n" +
                "\t\"warnings\" : [ ],\n" +
                "\t\"errors\" : [ ],\n" +
                "\t\"extraIndexEntries\" : [ ],\n" +
                "\t\"missingIndexEntries\" : [ ],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result3,document3);

    }

    @Test
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
