package com.whaleal.mars.core;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import javax.annotation.Resource;


public class MarsURITest {

    @Resource
    Mars mars;

    @Before
    public void init() {

        Assert.assertNotNull(mars);
    }


    @Test
    public void checkMarsNotNull() {

        Assert.assertNotNull(mars);
    }






}
