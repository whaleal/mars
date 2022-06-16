package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 17:22
 * FileName: ProfileTest
 * Description:
 */
public class ProfileTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *   profile: <level>,
     *   slowms: <threshold>,
     *   sampleRate: <rate>,
     *   filter: <filter expression>
     * }
     */
    @Test
    public void testForProfile(){
        Document document = mars.executeCommand("{\n" +
                "     profile: 1,\n" +
                "     filter:\n" +
                "        {\n" +
                "           $or:\n" +
                "           [\n" +
                "              { millis: { $gte: 100 } },\n" +
                "              { user: \"root@admin\" }\n" +
                "           ]\n" +
                "        }\n" +
                "   }");
        System.out.println(document);
    }
}
