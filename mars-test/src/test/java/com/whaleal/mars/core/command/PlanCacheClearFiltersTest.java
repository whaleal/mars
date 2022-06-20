package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 9:56
 * FileName: PlanCacheClearFiltersTest
 * Description:
 */
public class PlanCacheClearFiltersTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand(
     *    {
     *       planCacheClearFilters: <collection>,
     *       query: <query pattern>,
     *       sort: <sort specification>,
     *       projection: <projection specification>,
     *       comment: <any>
     *    }
     * )
     */
    @Test
    public void testForPlanCacheClearFilters(){
        Document document = mars.executeCommand("{\n" +
                "      planCacheClearFilters: \"document\",\n" +
                "      query: { \"status\" : \"A\" }\n" +
                "   }");
        System.out.println(document);

    }

    @Test
    public void testForPlanClearAllFilters(){
        Document document2 = mars.executeCommand("{\n" +
                "      planCacheClearFilters: \"document\"\n" +
                "   }");
        System.out.println(document2);
    }
}
