package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:53
 * FileName: GetAndSetFreeMonitoringStatusTest
 * Description:
 */
public class GetAndSetFreeMonitoringStatusTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.adminCommand( { getFreeMonitoringStatus: 1 } )
     */
    @Test
    public void testForGetFreeMonitoringStatus(){
        //todo 复制集环境没测
        Document document = mars.executeCommand("{ getFreeMonitoringStatus: 1 }");
        System.out.println(document);
    }

    @Test
    public void testForSetFreeMonitoringStatus(){
        //todo 复制集环境没测
        Document document = mars.executeCommand("{ setFreeMonitoring: 1, action: \"disable\" }");
        System.out.println(document);
    }
}
