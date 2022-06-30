package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 10:07
 * FileName: PlanCacheSetFilterTest
 * Description:
 */
public class PlanCacheSetFilterTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Document.class);
        mars.insert(new Document()
                .append("cust_id",1)
                .append("status","A")
                .append("order_date",new Date())
                .append("item","ABC")
                .append("quantity",5));
        mars.insert(new Document()
                .append("cust_id",2)
                .append("status","C")
                .append("order_date",new Date())
                .append("item","ABCD")
                .append("quantity",1));
        mars.insert(new Document()
                .append("cust_id",3)
                .append("status","A")
                .append("order_date",new Date())
                .append("item","AC")
                .append("quantity",10));
        mars.createIndex(new Index()
                .on("cust_id", IndexDirection.ASC)
                .on("status",IndexDirection.ASC),"document");
        mars.createIndex(new Index()
                .on("item", IndexDirection.ASC)
                .on("order_date",IndexDirection.ASC)
                .on("quantity",IndexDirection.ASC),"document");

    }
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
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
        Document document1 = mars.executeCommand("{\n" +
                "      planCacheSetFilter: \"document\",\n" +
                "      query: { item: \"ABC\" },\n" +
                "      projection: { quantity: 1, _id: 0 },\n" +
                "      sort: { order_date: 1 },\n" +
                "      indexes: [\n" +
                "         { item: 1, order_date: 1 , quantity: 1 }\n" +
                "      ]\n" +
                "   }");
        Document result1 = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result1,document1);
    }
    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
