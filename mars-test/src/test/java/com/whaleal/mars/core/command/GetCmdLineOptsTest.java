package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:47
 * FileName: GetCmdLineOptsTest
 * Description:
 */
public class GetCmdLineOptsTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.adminCommand( { getCmdLineOpts: 1  } )
     */
    @Test
    public void testForGetCmdLineOpts(){
        //只能在admin上测
        Document document = mars.executeCommand("{ getCmdLineOpts: 1  }");
        System.out.println(document);
    }
}
