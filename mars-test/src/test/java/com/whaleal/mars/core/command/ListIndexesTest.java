package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.bean.Orders;
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
 * Date: 2022/6/16 0016 13:05
 * FileName: ListIndexesTest
 * Description:
 */
public class ListIndexesTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Orders.class);
        mars.insert(new Orders("1","test","1",1,1,new Date()));
        Index index = new Index("name", IndexDirection.ASC);
        mars.createIndex(index,"orders");
        Index index1 = new Index("size", IndexDirection.ASC);
        mars.createIndex(index1,"orders");
        Index index2 = new Index("price", IndexDirection.ASC);
        mars.createIndex(index2,"orders");
    }
    /**
     * db.runCommand (
     *    {
     *       listIndexes: "<collection-name>",
     *       cursor: { batchSize: <int> },
     *       comment: <any>
     *    }
     * )
     */
    @Test
    public void testForListIndexes(){
        Document document = mars.executeCommand(Document.parse("{\n" +
                "     listIndexes: \"orders\"\n" +
                "  }"));
        Document result = Document.parse("{\n" +
                "\t\"cursor\" : {\n" +
                "\t\t\"id\" : NumberLong(0),\n" +
                "\t\t\"ns\" : \"mars.orders\",\n" +
                "\t\t\"firstBatch\" : [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"v\" : 2,\n" +
                "\t\t\t\t\"key\" : {\n" +
                "\t\t\t\t\t\"_id\" : 1\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"_id_\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"v\" : 2,\n" +
                "\t\t\t\t\"key\" : {\n" +
                "\t\t\t\t\t\"name\" : 1\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"name_1\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"v\" : 2,\n" +
                "\t\t\t\t\"key\" : {\n" +
                "\t\t\t\t\t\"size\" : 1\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"size_1\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"v\" : 2,\n" +
                "\t\t\t\t\"key\" : {\n" +
                "\t\t\t\t\t\"price\" : 1\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"price_1\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForListIndexesWithBatchSize(){
        Document document = mars.executeCommand(Document.parse("{\n" +
                "\n" +
                "      listIndexes: \"orders\", cursor: { batchSize: 1 }\n" +
                "\n" +
                "   }"));
        Document cursor = (Document) document.get("cursor");
        Document document1 = new Document().append("ns", cursor.get("ns"))
                .append("firstBatch", cursor.get("firstBatch"));
        Document result = Document.parse("{\n" +
                "\t\t\"ns\" : \"mars.orders\",\n" +
                "\t\t\"firstBatch\" : [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"v\" : 2,\n" +
                "\t\t\t\t\"key\" : {\n" +
                "\t\t\t\t\t\"_id\" : 1\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"_id_\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "}\n");
        Assert.assertEquals(result,document1);
    }
    @After
    public void dropCollection(){
        mars.dropCollection("orders");
    }
}
