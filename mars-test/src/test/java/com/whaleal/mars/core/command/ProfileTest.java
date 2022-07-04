package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
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
        Document result = Document.parse("{\n" +
                "\t\"was\" : 1,\n" +
                "\t\"slowms\" : 100,\n" +
                "\t\"sampleRate\" : 1.0,\n" +
                "\t\"filter\" : {\n" +
                "\t\t\"$or\" : [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"millis\" : {\n" +
                "\t\t\t\t\t\"$gte\" : 100\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"user\" : {\n" +
                "\t\t\t\t\t\"$eq\" : \"root@admin\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"note\" : \"When a filter expression is set, slowms and sampleRate are not used for profiling and slow-query log lines.\",\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
}
