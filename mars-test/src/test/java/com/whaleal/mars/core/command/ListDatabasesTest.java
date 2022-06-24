package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 12:03
 * FileName: ListDatabasesTest
 * Description:
 */
public class ListDatabasesTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.adminCommand( { listDatabases: 1 } )
     */
    @Test
    public void testForListDatabases(){
        Document document = mars.executeCommand("{ listDatabases: 1 }");
        System.out.println(document);
    }

    @Test
    public void testForListDatabasesNameOnly(){
        Document document = mars.executeCommand("{ listDatabases: 1, nameOnly: true}");
        System.out.println(document);
    }

    @Test
    public void testForListDatabaseWithFilter(){
        Document document = mars.executeCommand(" { listDatabases: 1, filter: { \"name\": /^rep/ } }");
        System.out.println(document);
    }
}
