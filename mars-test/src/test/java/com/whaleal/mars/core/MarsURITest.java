package com.whaleal.mars.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;

import javax.annotation.Resource;


public class MarsURITest {

    @Resource
    Mars mars;

    @Before
    public void init() {

        mars = new Mars(Constant.server100);
    }


    @Test
    public void checkMarsNotNull() {

        Assert.assertNotNull(mars);
    }






}
