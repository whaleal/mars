package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
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
        System.out.println(document);
    }

}
