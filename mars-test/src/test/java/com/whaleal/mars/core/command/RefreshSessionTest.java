package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * Author: cjq
 * Date: 2022/6/15 0015 9:17
 * FileName: RefreshSessionTest
 * Description:
 */
public class RefreshSessionTest {

    private Mars mars = new Mars(Constant.connectionStr);

    /**
     * { refreshSessions: [ { id : <UUID> }, ... ] } )
     */
    //todo endSession传入UUID出现错误，暂时未解决 不做测试
    @Test
    public void testForRefreshSession(){
        mars.executeCommand(" { refreshSessions: [ { id : <UUID> }, ... ] }");
    }
}
