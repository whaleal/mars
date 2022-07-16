package com.whaleal.mars.core;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.bean.Child;
import com.whaleal.mars.monitor.CollStatsMetrics;
import org.junit.Test;



/**
 * Author: cjq
 * Date: 2022/6/27 0027 17:53
 * FileName: TestCappedAt
 * Description:
 */

public class TestCappedAt {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testForCappedAt(){
        //创建Capped集合
        mars.createCollection(Book.class);
    }

    @Test
    public void testDropCollection(){
        mars.dropCollection(Book.class);
    }

    @Test
    public void testForGetCount(){
        //查看最大文件数
        CollStatsMetrics collStats = new CollStatsMetrics(mars.getMongoClient(), mars.getDatabase(), "book");
        System.out.println(collStats.getMax());
    }

    @Test
    public void testForGetMaxSize(){
        //查看最大Size
        CollStatsMetrics collStats = new CollStatsMetrics(mars.getMongoClient(), mars.getDatabase(), "book");
        System.out.println(collStats.getMaxSize());
    }

    @Test
    public void testForIsCapped(){
        //查看是否是Capped
        CollStatsMetrics collStats = new CollStatsMetrics(mars.getMongoClient(), mars.getDatabase(), "book");
        System.out.println(collStats.isCapped());
    }
}
