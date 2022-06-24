package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 16:43
 * FileName: CompactTest
 * Description:
 */
public class CompactTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForCompact(){
        Document document = mars.executeCommand("{compact:\"document\"}");
        System.out.println(document);
    }
}
