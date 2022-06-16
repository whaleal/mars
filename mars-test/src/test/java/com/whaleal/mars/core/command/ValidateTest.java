package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:39
 * FileName: ValidateTest
 * Description:
 */
public class ValidateTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( {
     *    validate: <string>,          // Collection name
     *    full: <boolean>,             // Optional
     *    repair: <boolean>,           // Optional, added in MongoDB 5.0
     *    metadata: <boolean>          // Optional, added in MongoDB 5.0.4
     * } )
     */
    @Test
    public void testForValidate(){
        Document document = mars.executeCommand("{ validate: \"person\" }");
        System.out.println(document);
        System.out.println("==========================================");
        //全验证
        Document document1 = mars.executeCommand("{ validate: \"person\", full: true }");
        System.out.println(document1);
        System.out.println("==========================================");
        //修复验证
        Document document2 = mars.executeCommand("{ validate: \"person\", repair: true } ");
        System.out.println(document2);
        System.out.println("==========================================");
        //元数据验证
        Document document3 = mars.executeCommand("{ validate: \"person\", metadata: true } ");
        System.out.println(document3);

    }
}
