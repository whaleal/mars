package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 10:07
 * FileName: PlanCacheSetFilterTest
 * Description:
 */
public class PlanCacheSetFilterTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * db.runCommand(
     *    {
     *       planCacheSetFilter: <collection>,
     *       query: <query>,
     *       sort: <sort>,
     *       projection: <projection>,
     *       indexes: [ <index1>, <index2>, ...],
     *       comment: <any>
     *    }
     * )
     */
    @Test
    public void testForPlanCacheSetFilter(){
        Document document = mars.executeCommand("{\n" +
                "      planCacheSetFilter: \"document\",\n" +
                "      query: { status: \"A\" },\n" +
                "      indexes: [\n" +
                "         { cust_id: 1, status: 1 },\n" +
                "         { status: 1, order_date: -1 }\n" +
                "      ]\n" +
                "   }");
        System.out.println(document);
        Document document1 = mars.executeCommand("{\n" +
                "      planCacheSetFilter: \"document\",\n" +
                "      query: { item: \"ABC\" },\n" +
                "      projection: { quantity: 1, _id: 0 },\n" +
                "      sort: { order_date: 1 },\n" +
                "      indexes: [\n" +
                "         { item: 1, order_date: 1 , quantity: 1 }\n" +
                "      ]\n" +
                "   }");
        System.out.println(document1);
    }
}
