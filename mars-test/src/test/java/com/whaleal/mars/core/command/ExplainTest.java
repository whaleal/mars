package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 16:41
 * FileName: ExplainTest
 * Description:
 */
public class ExplainTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * {
     *    explain: <command>,
     *    verbosity: <string>,
     *    comment: <any>
     * }
     */
    @Test
    public void testForExplain(){
        //queryPlanner Mode
        System.out.println("queryPlanner Mode");
        Document document = mars.executeCommand(Document.parse("{\n" +
                "     explain: { count: \"products\", query: { quantity: { $gt: 50 } } },\n" +
                "     verbosity: \"queryPlanner\"\n" +
                "   }"));
        System.out.println(document);
        //executionStats Mode
        System.out.println("executionStats Mode");
        Document document1 = mars.executeCommand(Document.parse("{\n" +
                "      explain: { count: \"products\", query: { quantity: { $gt: 50 } } },\n" +
                "      verbosity: \"executionStats\"\n" +
                "   }"));
        System.out.println(document1);
        //allPlansExecution Mode
        System.out.println("allPlansExecution Mode");
        Document document2 = mars.executeCommand(Document.parse("{\n" +
                "     explain: {\n" +
                "        update: \"products\",\n" +
                "        updates: [\n" +
                "           {\n" +
                "               q: { quantity: 1057, category: \"apparel\" },\n" +
                "               u: { $set: { reorder: true } }\n" +
                "           }\n" +
                "        ]\n" +
                "     }\n" +
                "   }"));
        System.out.println(document2);

    }
}
