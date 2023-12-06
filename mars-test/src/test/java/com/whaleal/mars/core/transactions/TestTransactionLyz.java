package com.whaleal.mars.core.transactions;

import com.whaleal.mars.Application;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.service.ITransactionService;

import com.whaleal.mars.session.MarsSessionImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lyz
 * @desc
 * @create: 2023-12-05 11:25
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TestTransactionLyz {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private Mars mars;

    @Test
    public void test(){

//        MarsSessionImpl marsSession = mars.startSession();

        transactionService.save();

    }
}
