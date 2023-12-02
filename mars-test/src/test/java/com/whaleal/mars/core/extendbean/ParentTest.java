package com.whaleal.mars.core.extendbean;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.whaleal.icefrog.core.collection.CollUtil;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Parent;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.QueryCursor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wh
 */


public class ParentTest {


    Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void init() {


    }


    @After
    public void destory() {

        try {
            mars.dropCollection(Parent.class);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }


    @Test
    public void test00Entity() {
        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");

        MongoCollection< Parent > collection = mars.getCollection(Parent.class);
        String collectionName = collection.getNamespace().getCollectionName();
        Assert.assertEquals("parent", collectionName);
    }

    @Test
    public void test01Insert() {
        Parent p1 = new Parent();
        p1.setAge(18);
        p1.setName("person");
        Parent p2 = new Parent();
        p2.setAge(18);
        p2.setName("person");
        ArrayList< Parent > parents = CollUtil.newArrayList(p1, p2);

       /* InsertManyResult insert = mars.insert(parents, Parent.class);

        Assert.assertEquals(2, insert.getInsertedIds().size());
*/

    }

    @Test
    public void test02Insert() {
        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");
        p.setId("10");
     /*   InsertOneResult insert = mars.insert(p);
        Assert.assertEquals("10", insert.getInsertedId().asString().getValue());
        //-----------------
        p.setId(null);
        mars.insert(p);
        Assert.assertNotNull(p.getId());
        System.out.println(p.getId());*/

    }

    @Test
    public void test03Countt() {
        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");
        p.setId("10");

        mars.insert(p);
        long count = mars.estimatedCount(Parent.class);
        Assert.assertEquals(1, count);

    }


    @Test
    public void test04ctById() {

        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");
        p.setId("10");

        mars.insert(p);
        Criteria id = Criteria.where("_id").is("10");

        long l = mars.count(new Query(id), Parent.class);

        Assert.assertEquals(1, l);
    }

    @Test
    public void test05delete() {
        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");
        p.setId("10");
        mars.insert(p);
        Criteria id = Criteria.where("_id").is("10");
        DeleteResult delete = mars.delete(new Query(id), Parent.class);
        Assert.assertEquals(1, delete.getDeletedCount());

    }

    @Test
    public void test06delete() {
        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");
        p.setId("10");


        mars.insert(p);
        p.setId("15");
        mars.insert(p);

        Criteria id = Criteria.where("age").is(18);
        com.mongodb.client.result.DeleteResult delete = mars.delete(new Query(id), Parent.class);
        Assert.assertEquals(2, delete.getDeletedCount());

    }

    @Test
    public void test07find1() {
        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");
        p.setId("10");

        mars.insert(p);
        Criteria id = Criteria.where("age").is(18);
        Optional< Parent > one = mars.findOne(new Query(id), Parent.class);
        Parent parent = one.get();

        Assert.assertEquals(p.getId(), parent.getId());
        Assert.assertEquals(p.getAge(), parent.getAge());
        Assert.assertEquals(p.getName(), parent.getName());
    }

    @Test
    public void test08findA() {
        Parent p = new Parent();
        p.setAge(33);
        p.setName("person");
        p.setId("15");


        Parent p2 = new Parent();
        p2.setAge(19);
        p2.setName("person10");
        p2.setId("10");

        mars.insert(p2);
        mars.insert(p);
        Criteria age = Criteria.where("age").lte(100);
//        with(Sort.on().descending("age"))
        QueryCursor< Parent > all = mars.findAll(new Query(age).with(Sort.descending("age")), Parent.class);
        List< Parent > parents = all.toList();
        Parent first = parents.get(0);

        Assert.assertEquals(p.getId(), first.getId());
        Assert.assertEquals(p.getAge(), first.getAge());
        Assert.assertEquals(p.getName(), first.getName());

        Parent secondy = parents.get(1);

        Assert.assertEquals(p2.getId(), secondy.getId());
        Assert.assertEquals(p2.getAge(), secondy.getAge());
        Assert.assertEquals(p2.getName(), secondy.getName());

    }

    @Test
    public void test07Save1() {
        Parent p = new Parent();
        p.setAge(18);
        p.setName("person");
        p.setId("10");
        Parent save = mars.save(p);

        p.setName("save");

        mars.save(p);
        Criteria id = Criteria.where("_id").lte("10");
        Optional< Parent > one = mars.findOne(new Query(id), Parent.class);

        Parent first = one.get();

        Assert.assertEquals(p.getId(), first.getId());
        Assert.assertEquals(p.getAge(), first.getAge());
        Assert.assertEquals(p.getName(), first.getName());
    }
}
