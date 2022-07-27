package com.whaleal.mars.base;


import com.whaleal.mars.core.Mars;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContextTest {

    @Autowired
    Mars mars;

    @Resource
    Mars marsResouce;



    @Test
    public void test() {
        Assert.assertNotNull(mars);
        Assert.assertNotNull(mars.getDatabase());

        Assert.assertNotNull(marsResouce);
        Assert.assertNotNull(marsResouce.getDatabase());

    }

}
