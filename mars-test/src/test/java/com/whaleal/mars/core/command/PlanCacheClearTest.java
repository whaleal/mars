package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 9:22
 * FileName: PlanCacheClearTest
 * Description:
 */
public class PlanCacheClearTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand(
     *    {
     *       planCacheClear: <collection>,
     *       query: <query>,
     *       sort: <sort>,
     *       projection: <projection>,
     *       comment: <any>
     *    }
     * )
     */
    @Test
    public void testForPlanCacheClear(){
        //按query shape清除
        System.out.println("按query shape清除");
        Document document = mars.executeCommand("{\n" +
                "      planCacheClear: \"orders\",\n" +
                "      query: { \"qty\" : { \"$gt\" : 10 } },\n" +
                "      sort: { \"ord_date\" : 1 }\n" +
                "   }");
        System.out.println(document);
        //清除所有的缓存
        System.out.println("清除所有");
        Document document1 = mars.executeCommand("{\n" +
                "      planCacheClear: \"orders\"\n" +
                "   }");
        System.out.println(document1);
    }
}
