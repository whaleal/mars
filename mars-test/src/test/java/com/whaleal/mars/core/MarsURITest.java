package com.whaleal.mars.core;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
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
