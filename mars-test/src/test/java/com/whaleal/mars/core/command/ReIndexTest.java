package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:21
 * FileName: ReIndexTest
 * Description:
 */
public class ReIndexTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { reIndex: <collection> }
     */
    @Test
    public void testForReIndex(){
        Document document = mars.executeCommand("{ reIndex: \"document\" }");
        System.out.println(document);
    }

}
