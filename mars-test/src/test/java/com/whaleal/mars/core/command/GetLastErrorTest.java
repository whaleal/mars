package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 14:04
 * FileName: GetLastErrorTest
 * Description:
 */
public class GetLastErrorTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { getLastError: 1 }
     */
    @Test
    public void testForGetLastError(){
        Document document = mars.executeCommand("{ getLastError: 1 }");
        Document result = Document.parse("{\n" +
                "\t\"connectionId\" : 212,\n" +
                "\t\"n\" : 0,\n" +
                "\t\"syncMillis\" : 0,\n" +
                "\t\"writtenTo\" : null,\n" +
                "\t\"writeConcern\" : {\n" +
                "\t\t\"w\" : 1,\n" +
                "\t\t\"wtimeout\" : 0\n" +
                "\t},\n" +
                "\t\"err\" : null,\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

}
