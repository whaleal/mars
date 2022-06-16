package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:31
 * FileName: DbHashTest
 * Description:
 */
public class DbHashTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand ( { dbHash: 1, collections: [ <collection1>, ... ] } )
     */
    @Test
    public void testForDbHash(){
        //数据库中所有集合的hash值
        Document document = mars.executeCommand(" { dbHash: 1 } ");
        System.out.println(document);
        //数据库中指定集合的hash值
        Document document1 = mars.executeCommand("{ dbHash: 1, collections: [ \"person\"] }");
        System.out.println(document1);
    }
}
