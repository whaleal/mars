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
        mars.createCollection(Document.class);
        mars.insert(new Document().append("name","test").append("age",11).append("test","aa"));
        Index index = new Index("name", IndexDirection.ASC);
        mars.createIndex(index,"document");
        Index index1 = new Index("age", IndexDirection.ASC);
        mars.createIndex(index1,"document");
        Index index2 = new Index("test", IndexDirection.ASC);
        mars.createIndex(index2,"document");
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
        Document document = mars.executeCommand("{\n" +
                "     listIndexes: \"document\"\n" +
                "  }");
        Document result = Document.parse("{\n" +
                "\t\"cursor\" : {\n" +
                "\t\t\"id\" : NumberLong(0),\n" +
                "\t\t\"ns\" : \"mars.document\",\n" +
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
                "\t\t\t\t\t\"age\" : 1\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"age_1\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"v\" : 2,\n" +
                "\t\t\t\t\"key\" : {\n" +
                "\t\t\t\t\t\"test\" : 1\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\" : \"test_1\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @Test
    public void testForListIndexesWithBatchSize(){
        Document document = mars.executeCommand("{\n" +
                "\n" +
                "      listIndexes: \"document\", cursor: { batchSize: 1 }\n" +
                "\n" +
                "   }");
        Document result = Document.parse("{\n" +
                "\t\"cursor\" : {\n" +
                "\t\t\"id\" : NumberLong(\"4628176264269121931\"),\n" +
                "\t\t\"ns\" : \"mars.document\",\n" +
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
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }
    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }
}
