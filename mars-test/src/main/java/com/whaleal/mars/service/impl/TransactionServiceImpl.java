package com.whaleal.mars.service.impl;

import com.whaleal.mars.bean.Articles;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.service.ITransactionService;
import com.whaleal.mars.session.MarsSessionImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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

    @Transactional
    @Override
    public void save(){
        int id = 1001;
        Articles articles = new Articles(id, "test", "lyz", 1);

        mars.deleteMulti(new Query(),Articles.class);
        mars.insert(articles);
        mars.insert(articles);


    }
}
