package com.whaleal.mars.core.transactions;

import com.whaleal.mars.service.ITransactionService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lyz
 * @desc
 * @create: 2023-12-05 11:25
 **/
@SpringBootTest
public class TestTransactionLyz {

    @Autowired
    private ITransactionService transactionService;

    @Test
    public void test(){

        transactionService.save();

    }
}
