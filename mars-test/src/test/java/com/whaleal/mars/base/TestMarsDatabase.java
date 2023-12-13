package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.junit.Test;

/**
 * @author wh
 */
public class TestMarsDatabase {

    @Test
    public void test(){
        Mars mars = new Mars(Constant.connectionStr);
    }
}
