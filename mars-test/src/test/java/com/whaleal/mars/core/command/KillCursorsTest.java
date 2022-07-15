package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 11:37
 * FileName: KillCursorsTest
 * Description:
 */
public class KillCursorsTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { find: "restaurants",
     *      filter: { stars: 5 },
     *      projection: { name: 1, rating: 1, address: 1 },
     *      sort: { name: 1 },
     *      batchSize: 5
     *    }
     */
    @Test
    public void testForFindCursors(){
        Document document = mars.executeCommand(Document.parse("{ find: \"restaurants\",\n" +
                "     filter: { stars: 5 },\n" +
                "     projection: { name: 1, rating: 1, address: 1 },\n" +
                "     sort: { name: 1 },\n" +
                "     batchSize: 5\n" +
                "   }"));
        System.out.println(document);
    }

    @Test
    public void testForKillCursor(){
        Document document = mars.executeCommand(Document.parse("{ killCursors: \"restaurants\", cursors: [ NumberLong(\"18314637080\") ] } "));
        System.out.println(document);
    }
}
