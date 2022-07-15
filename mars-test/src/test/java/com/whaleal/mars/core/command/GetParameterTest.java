package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 11:28
 * FileName: GetParameterTest
 * Description:
 */
public class GetParameterTest {

    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    /**
     * {
     *    getParameter: <value>,
     *    <parameter> : <value>,
     *    comment: <any>
     * }
     */
    @Test
    public void testForGetSingleParameter(){
        Document document = mars.executeCommand(Document.parse("{ getParameter : 1, \"saslHostName\" : 1 }"));
        Document result = Document.parse("{ \"saslHostName\" : \"WhaleFalls0807\", \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }

    @Test
    public void testForGetAllParameter(){
        Document document = mars.executeCommand(Document.parse("{ getParameter : '*' }"));
        System.out.println(document);
    }

    @Test
    public void testForGetSingleParameterWithDetail(){
        Document document = mars.executeCommand(Document.parse("{ getParameter : { showDetails: true }, \"saslHostName\" : 1 }"));
        Document result = Document.parse("{\n" +
                "\t\"saslHostName\" : {\n" +
                "\t\t\"value\" : \"WhaleFalls0807\",\n" +
                "\t\t\"settableAtRuntime\" : false,\n" +
                "\t\t\"settableAtStartup\" : true\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(document,result);
    }

    @Test
    public void testForGetAllParameterWithDetail(){
        Document document = mars.executeCommand(Document.parse("{ getParameter : { showDetails: true, allParameters: true }}"));
        System.out.println(document);
    }
}
