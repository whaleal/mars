package com.whaleal.mars.core.command;


import com.whaleal.icefrog.core.lang.UUID;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.bson.types.Binary;
import org.junit.Test;


/**
 * Author: cjq
 * Date: 2022/6/14 0014 17:14
 * FileName: StartSessionTest
 * Description:
 */
public class StartSessionTest {

    private Mars mars = new Mars(Constant.connectionStr);

    Binary binary;
    /**
     * { startSession: 1 }
     */
    @Test
    public void testForStartSession(){
        Document document = mars.executeCommand("{ startSession: 1 } ");
        System.out.println(document);
        byte[] data = document.get("id", Document.class).get("id", Binary.class).getData();
        binary = document.get("id", Document.class).get("id", Binary.class);
        System.out.println(binary);
        System.out.println(data);
        UUID uuid = UUID.nameUUIDFromBytes(data);
        System.out.println(uuid);

    }
}
