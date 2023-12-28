package com.whaleal.mars.service.impl;

import com.whaleal.mars.bean.Articles;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lyz
 * @desc
 * @create: 2023-12-05 11:18
 **/
@Slf4j
@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private Mars mars;

    @Override
    @Transactional
    public void  save(){

        int id = 2001;
        Articles articles = new Articles(id, "test", "lyz", 1);

        mars.insert(articles);

        log.info("开始插入错误数据");

        mars.insert(articles);
    }

    public static void main(String[] args) {
        int id = 2001;
        Articles articles = new Articles(id, "test", "lyz", 1);

        System.out.println(articles);
    }
}


