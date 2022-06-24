package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 10:02
 * FileName: PlanCacheListFiltersTest
 * Description:
 */
public class PlanCacheListFiltersTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand( { planCacheListFilters: <collection> } )
     */
    @Test
    public void testForPlanCacheListFilters(){
        Document document = mars.executeCommand("{ planCacheListFilters: \"document\" }");
        System.out.println(document);
    }
}
