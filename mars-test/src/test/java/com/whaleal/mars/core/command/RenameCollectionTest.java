package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 13:24
 * FileName: RenameCollectionTest
 * Description:
 */
public class RenameCollectionTest {
    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { renameCollection: "<source_namespace>",
     *   to: "<target_namespace>",
     *   dropTarget: <true|false>,
     *   writeConcern: <document>,
     *   comment: <any> }
     */
    @Test
    public void testForRenameCollection(){
        Document document = mars.executeCommand("{ renameCollection: \"mars.document01\", to: \"mars.document\" }");
        System.out.println(document);
    }
}
