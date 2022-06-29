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

    private Mars mars = new Mars("mongodb://192.168.200.139:27017/admin");

    /**
     * {
     *    getParameter: <value>,
     *    <parameter> : <value>,
     *    comment: <any>
     * }
     */
    @Test
    public void testForGetSingleParameter(){
        Document document = mars.executeCommand("{ getParameter : 1, \"saslHostName\" : 1 }");
        Document result = Document.parse("{ \"saslHostName\" : \"test\", \"ok\" : 1.0 }");
        Assert.assertEquals(document,result);
    }

    @Test
    public void testForGetAllParameter(){
        Document document = mars.executeCommand("{ getParameter : '*' }");
        System.out.println(document);
    }

    @Test
    public void testForGetSingleParameterWithDetail(){
        Document document = mars.executeCommand("{ getParameter : { showDetails: true }, \"saslHostName\" : 1 }");
        System.out.println(document);
    }

    @Test
    public void testForGetAllParameterWithDetail(){
        Document document = mars.executeCommand("{ getParameter : { showDetails: true, allParameters: true }}");
        System.out.println(document);
    }
}
