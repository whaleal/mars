package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:53
 * FileName: GetAndSetFreeMonitoringStatusTest
 * Description:
 */
public class GetAndSetFreeMonitoringStatusTest {
    private Mars mars = new Mars("mongodb://root:123456@47.100.1.115:37001/admin?authSource=admin");

    /**
     * db.adminCommand( { getFreeMonitoringStatus: 1 } )
     */
    @Test
    public void testForGetFreeMonitoringStatus(){
        //todo 复制集环境没测
        Document document = mars.executeCommand("{ getFreeMonitoringStatus: 1 }");
        Document result = Document.parse("{\n" +
                "\t\"state\" : \"disabled\",\n" +
                "\t\"message\" : \"\",\n" +
                "\t\"url\" : \"\",\n" +
                "\t\"userReminder\" : \"\",\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForSetFreeMonitoringStatus(){
        //todo 复制集环境没测
        Document document = mars.executeCommand("{ setFreeMonitoring: 1, action: \"disable\" }");
        Document result = Document.parse("{ \"ok\" : 1.0 }\n");
        Assert.assertEquals(result,document);
    }
}
