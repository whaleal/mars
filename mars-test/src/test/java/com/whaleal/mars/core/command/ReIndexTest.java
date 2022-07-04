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
 * Date: 2022/6/16 0016 13:21
 * FileName: ReIndexTest
 * Description:
 */
public class ReIndexTest {
    private Mars mars = new Mars(Constant.connectionStr);


    @Before
    public void createData(){
        mars.createCollection(Document.class);
        mars.insert(new Document().append("name","aaa"));
        mars.createIndex(new Index("name", IndexDirection.ASC),"document");
    }
    /**
     * { reIndex: <collection> }
     */
    @Test
    public void testForReIndex(){
        Document document = mars.executeCommand("{ reIndex: \"document\" }");
        Document result = Document.parse("{\n" +
                "\t\"nIndexesWas\" : 2,\n" +
                "\t\"nIndexes\" : 2,\n" +
                "\t\"indexes\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"v\" : 2,\n" +
                "\t\t\t\"key\" : {\n" +
                "\t\t\t\t\"_id\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"name\" : \"_id_\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"v\" : 2,\n" +
                "\t\t\t\"key\" : {\n" +
                "\t\t\t\t\"name\" : 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"name\" : \"name_1\"\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"ok\" : 1.0\n" +
                "}\n");
        Assert.assertEquals(result,document);
    }

    @After
    public void dropCollection(){
        mars.dropCollection("document");
    }

}
