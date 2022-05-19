package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @date 2022-05-18 13:56
 */
public class CommandTest {

    Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void test(){
        Document parse = Document.parse("");
        Document document = mars.executeCommand(parse);
    }

    @Test
    public void testForCreateCollection(){

    }
}
