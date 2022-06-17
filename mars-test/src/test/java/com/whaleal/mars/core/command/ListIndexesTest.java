package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:05
 * FileName: ListIndexesTest
 * Description:
 */
public class ListIndexesTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand (
     *    {
     *       listIndexes: "<collection-name>",
     *       cursor: { batchSize: <int> },
     *       comment: <any>
     *    }
     * )
     */
    @Test
    public void testForListIndexes(){
        Document document = mars.executeCommand("{\n" +
                "\n" +
                "     listIndexes: \"document\"\n" +
                "\n" +
                "  }");
        System.out.println(document);
    }

    @Test
    public void testForListIndexesWithBatchSize(){
        Document document = mars.executeCommand("{\n" +
                "\n" +
                "      listIndexes: \"document\", cursor: { batchSize: 1 }\n" +
                "\n" +
                "   }");
        System.out.println(document);
    }
}
