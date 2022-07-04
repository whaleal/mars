package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 12:03
 * FileName: ListDatabasesTest
 * Description:
 */
public class ListDatabasesTest {
    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    /**
     * db.adminCommand( { listDatabases: 1 } )
     */
    @Test
    public void testForListDatabases(){
        Document document = mars.executeCommand("{ listDatabases: 1 }");
        Document result = Document.parse("{\n" +
                "\t\"databases\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"admin\",\n" +
                "\t\t\t\"sizeOnDisk\" : NumberLong(385024),\n" +
                "\t\t\t\"empty\" : false\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"config\",\n" +
                "\t\t\t\"sizeOnDisk\" : NumberLong(110592),\n" +
                "\t\t\t\"empty\" : false\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"local\",\n" +
                "\t\t\t\"sizeOnDisk\" : NumberLong(73728),\n" +
                "\t\t\t\"empty\" : false\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"mars\",\n" +
                "\t\t\t\"sizeOnDisk\" : NumberLong(8192),\n" +
                "\t\t\t\"empty\" : false\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"totalSize\" : NumberLong(577536),\n" +
                "\t\"totalSizeMb\" : NumberLong(0),\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForListDatabasesNameOnly(){
        Document document = mars.executeCommand("{ listDatabases: 1, nameOnly: true}");
        Document result = Document.parse("{\n" +
                "\t\"databases\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"admin\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"config\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"local\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\" : \"mars\"\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForListDatabaseWithFilter(){
        Document document = mars.executeCommand(" { listDatabases: 1, filter: { \"name\": /^rep/ } }");
        Document result = Document.parse("{\n" +
                "\t\"databases\" : [ ],\n" +
                "\t\"totalSize\" : NumberLong(0),\n" +
                "\t\"totalSizeMb\" : NumberLong(0),\n" +
                "\t\"ok\" : 1.0\n" +
                "}");
        Assert.assertEquals(result,document);
    }
}
