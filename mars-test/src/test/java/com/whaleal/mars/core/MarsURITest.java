package com.whaleal.mars.core;

import org.junit.Precondition;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;

import javax.annotation.Resource;


public class MarsURITest {

    @Resource
    Mars mars;

    @Before
    public void init() {

        Precondition.PreconditionNotNull(mars);
    }


    @Test
    public void checkMarsNotNull() {

        Precondition.PreconditionNotNull(mars);
    }






}
