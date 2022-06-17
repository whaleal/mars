package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/16 0016 10:39
 * FileName: DropIndexesTest
 * Description:
 */
public class DropIndexesTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { dropIndexes: <string>, index: <string|document|arrayofstrings>, writeConcern: <document>, comment: <any> }
     */
    @Test
    public void testForDropIndexes(){
        //删除所有非主键索引
        mars.executeCommand(" { dropIndexes: \"document\", index: \"*\" } ");
    }

    @Test
    public void testForDropSpecifyIndex(){
        //指定索引名删除
        mars.executeCommand("{ dropIndexes: \"document\", index: \"test\" }");
    }

    @Test
    public void testForDropMultipleIndexes(){
        //删除多个
        mars.executeCommand("{ dropIndexes: \"document\", index: [ \"test01\", \"test02\" ] }");
    }
}
