package com.whaleal.mars.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wh
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoPropertiesTest {

    @Autowired
    MongoProperties properties;

    @Before
    public void init() {
        Assert.assertNotNull(properties);
    }


    @Test
    public void test() {

        Assert.assertNotNull(properties.getUri());
        Assert.assertNotNull(properties.getUri(), "uri is not null");
        System.out.println(properties.getUri());
    }


}
