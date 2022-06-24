package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 11:53
 * FileName: ListCollectionsTest
 * Description:
 */
public class ListCollectionsTest {
    private Mars mars = new Mars(Constant.connectionStr);


    /**
     * { listCollections: 1, filter: <document>, nameOnly: <boolean>, authorizedCollections: <boolean>, comment: <any> }
     */
    @Test
    public void testForListCollections(){
        Document document = mars.executeCommand(" { listCollections: 1.0, authorizedCollections: true, nameOnly: true }");
        System.out.println(document);
    }
}
