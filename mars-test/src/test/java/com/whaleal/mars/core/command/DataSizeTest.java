package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:16
 * FileName: DataSizeTest
 * Description:
 */
public class DataSizeTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *    dataSize: <string>,
     *    keyPattern: <document>,
     *    min: <document>,
     *    max: <document>,
     *    estimate: <boolean>
     * }
     */
    @Test
    public void testForDataSize(){
//        Document document = mars.executeCommand("{ dataSize: \"database.collection\", keyPattern: { field: 1 }, min: { field: 10 }, max: { field: 100 } }");
        Document document = mars.executeCommand("{ dataSize: \"mars.document\", keyPattern: { price: 1 }, min: { price: 10 }, max: { price: 100 } }");
        System.out.println(document);
    }
}
