package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:12
 * FileName: ConnectionStatusTest
 * Description:
 */
public class ConnectionStatusTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { connectionStatus: 1, showPrivileges: <boolean> }
     */
    @Test
    public void testForConnectionStatus(){
        Document document = mars.executeCommand(" { connectionStatus: 1, showPrivileges: true }");
        System.out.println(document);
    }
}
