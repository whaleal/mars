package com.whaleal.mars.core.index;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.monitor.CollStatsMetrics;
import org.bson.Document;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

/**
 * @author lyz
 * @description
 * @date 2022-06-24 14:05
 **/
@SpringBootTest
public class AutoCreateIndexTest {

    @Autowired
    private Mars mars;

    @Test
    public void testFor(){
        mars.createCollection(Animal.class);


    }


    @Test
    public void testFor2(){

        List<Index> animal = mars.getIndexes("animal");
        animal.forEach(n -> {
            System.out.println(n.getIndexKeys().toJson());
            System.out.println(n.getIndexOptions());
        });
    }

}
