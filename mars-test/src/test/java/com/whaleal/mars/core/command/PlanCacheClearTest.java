package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Assert;
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
        Document document = mars.executeCommand(Document.parse("{\n" +
                "      planCacheClear: \"book\",\n" +
                "      query: { \"qty\" : { \"$gt\" : 10 } },\n" +
                "      sort: { \"ord_date\" : 1 }\n" +
                "   }"));

        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
        //清除所有的缓存
        Document document1 = mars.executeCommand(Document.parse("{\n" +
                "      planCacheClear: \"book\"\n" +
                "   }"));
        Document result1 = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result1,document1);
    }
}
