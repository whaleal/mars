package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:24
 * FileName: RenameCollectionTest
 * Description:
 */
public class RenameCollectionTest {
    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.createCollection(Document.class);
    }
    /**
     * { renameCollection: "<source_namespace>",
     *   to: "<target_namespace>",
     *   dropTarget: <true|false>,
     *   writeConcern: <document>,
     *   comment: <any> }
     */
    @Test
    public void testForRenameCollection(){
        Document document = mars.executeCommand(Document.parse("{ renameCollection: \"admin.document\", to: \"admin.document01\" }"));
        Document result = Document.parse("{\"ok\":1.0}");
        Assert.assertEquals(result,document);
    }
    @After
    public void dropCollection(){
        mars.dropCollection("document");
        mars.dropCollection("document01");
    }
}
