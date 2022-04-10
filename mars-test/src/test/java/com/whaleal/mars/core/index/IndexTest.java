package com.whaleal.mars.core.index;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.bean.Address;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexTest {


    @Autowired
    Mars mars;

    String coll = "coll";


    @Test
    public void testConnection() {

        Assert.assertNotNull(mars);
    }


    @Test
    public void testCreateIndex() {

        mars.dropIndexes(coll);
        Index index = new Index();
        index.on("c", IndexDirection.ASC);
        index.setOptions(new IndexOptions().background(true).expireAfter(3600l, TimeUnit.SECONDS));
        mars.createIndex(index, coll);

        List< Index > cool = mars.getIndexes(coll);
        Assert.assertEquals(cool.size(), 2);

        Assert.assertEquals(cool.get(1).getIndexKeys(), new Document("c", 1));

        Assert.assertEquals(cool.get(1).getIndexOptions().getExpireAfter(TimeUnit.SECONDS), Long.valueOf(3600));

        mars.dropIndexes(coll);

    }


    @Test
    public void testDropIndex() {
        Index index = new Index();
        index.on("a", IndexDirection.ASC).on("b", IndexDirection.DESC);
        index.setOptions(new IndexOptions().background(true));

        mars.dropIndexes(coll);

        mars.createIndex(index, coll);
        List< Index > indexes = mars.getIndexes(coll);
        Assert.assertEquals(indexes.size(), 2);

        Assert.assertEquals(indexes.get(1).getIndexKeys(), new Document("a", 1).append("b", -1));
        mars.dropIndex(index, coll);

    }

    @Test
    public void testEnsurseIndexes() {

        mars.dropIndexes(coll);
        mars.ensureIndexes(Student.class, coll);
        List< Index > indexes = mars.getIndexes(coll);
        Assert.assertEquals(indexes.size(), 4);


        Assert.assertEquals(indexes.get(1).getIndexKeys(), new Document("salary", 1).append("name", -1));

        Assert.assertEquals(indexes.get(2).getIndexKeys(), new Document("idcc", "hashed"));


        Assert.assertEquals(indexes.get(3).getIndexKeys(), new Document("idcc", 1));


        Long expireAfter = indexes.get(3).getIndexOptions().getExpireAfter(TimeUnit.SECONDS);

        Assert.assertEquals(expireAfter, Long.valueOf(10));

        mars.dropIndexes(coll);


    }


    @Test
    public void test() {

        Assert.assertNotNull(mars);


    }


    @Test
    public void testDBAndCollection() {
        MongoDatabase database = mars.getDatabase();
        MongoCollection< Document > tables = database.getCollection("tables");

        tables.insertOne(new Document());

    }


    @Test
    public void testEnsureIndexes() {

        mars.ensureIndexes(Address.class, "addr");

    }

    @Test
    public void testEnsureIndexesWithBigData() {

        System.out.println(System.currentTimeMillis());
        mars.ensureIndexes(Address.class, "POCCOLL");
        System.out.println(System.currentTimeMillis());

    }

    @Test
    public void testGetIndexes() {

        List< Index > addr = mars.getIndexes("cc");

        System.out.println(addr);
    }
}


