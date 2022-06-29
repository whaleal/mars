package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:07
 * FileName: ListCommandsTest
 * Description:
 */
public class ListCommandsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( { listCommands: 1 } )
     */
    @Test
    public void testForListCommands(){
        Document document = mars.executeCommand(" { listCommands: 1 }");
        System.out.println(document);
    }
}
