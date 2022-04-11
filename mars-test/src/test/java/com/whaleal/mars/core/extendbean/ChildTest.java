package com.whaleal.mars.core.extendbean;

import com.mongodb.client.MongoCollection;
import com.whaleal.icefrog.core.collection.CollUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.bean.Child;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wh
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChildTest {

    @Autowired
    Mars mars ;

    @Before
    public void init(){
        try {
            mars.dropCollection(Child.class);
        }catch (Exception e){
            // e.printStackTrace();
        }

    }


    @Test
    public void test00Entity(){
        Child p = new Child();
        p.setAge(18);
        p.setName("child");

        MongoCollection< Child > collection = mars.getCollection(Child.class);
        String collectionName = collection.getNamespace().getCollectionName();
        Assert.assertEquals("child",collectionName);
    }
    @Test
    public void test01Insert(){
        Child p1 = new Child();
        p1.setAge(18);
        p1.setName("child");
        Child p2 = new Child();
        p2.setAge(18);
        p2.setName("child");
        ArrayList< Child > Childs = CollUtil.newArrayList(p1, p2);

        InsertManyResult insert = mars.insert(Childs,Child.class);

        Assert.assertEquals(2,insert.getInsertedIds().size());


    }

    @Test
    public void test02Insert(){
        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");
        InsertOneResult insert = mars.insert(p);
        Assert.assertEquals("10",insert.getInsertedId().asString().getValue());
        //-----------------
        p.setId(null);
        mars.insert(p);
        Assert.assertNotNull(p.getId());
        System.out.println(p.getId());

    }

    @Test
    public void test03Countt(){
        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");

        mars.insert(p);
        long count = mars.count(Child.class);
        Assert.assertEquals(1,count);

    }


    @Test
    public void test04ctById(){

        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");

        mars.insert(p);
        Criteria id = Criteria.where("_id").is("10");

        long l = mars.countById(new Query(id), Child.class);

        Assert.assertEquals(1,l);
    }

    @Test
    public void  test05delete(){
        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");
        mars.insert(p);
        Criteria id = Criteria.where("_id").is("10");
        DeleteResult delete = mars.delete(new Query(id), Child.class);
        Assert.assertEquals(1,delete.getDeletedCount());

    }
    @Test
    public void  test06delete(){
        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");


        mars.insert(p);
        p.setId("15");
        mars.insert(p);

        Criteria id = Criteria.where("age").is(18);
        DeleteResult delete = mars.delete(new Query(id), Child.class,new DeleteOptions().multi(true));
        Assert.assertEquals(2,delete.getDeletedCount());

    }

    @Test
    public void test07findOn(){
        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");

        mars.insert(p);
        Criteria id = Criteria.where("age").is(18);
        Optional< Child > one = mars.findOne(new Query(id), Child.class);
        Child Child = one.get();

        Assert.assertEquals(p.getId(),Child.getId());
        Assert.assertEquals(p.getAge(),Child.getAge());
        Assert.assertEquals(p.getName(),Child.getName());
    }

    @Test
    public void test08findAl(){
        Child p = new Child();
        p.setAge(33);
        p.setName("child");
        p.setId("15");


        Child p2 = new Child();
        p2.setAge(19);
        p2.setName("child10");
        p2.setId("10");

        mars.insert(p2);
        mars.insert(p);
        Criteria age = Criteria.where("age").lte(100);
        QueryCursor< Child > all = mars.findAll(new Query(age).with(Sort.on().descending("age")), Child.class);
        List< Child > Childs = all.toList();
        Child first = Childs.get(0);

        Assert.assertEquals(p.getId(),first.getId());
        Assert.assertEquals(p.getAge(),first.getAge());
        Assert.assertEquals(p.getName(),first.getName());

        Child secondy = Childs.get(1);

        Assert.assertEquals(p2.getId(),secondy.getId());
        Assert.assertEquals(p2.getAge(),secondy.getAge());
        Assert.assertEquals(p2.getName(),secondy.getName());

    }

    @Test
    public void test09Save1(){
        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");
        Child save = mars.save(p);

        p.setName("save");

        mars.save(p);
        Criteria id = Criteria.where("_id").lte("10");
        Optional< Child > one = mars.findOne(new Query(id), Child.class);

        Child first = one.get();

        Assert.assertEquals(p.getId(),first.getId());
        Assert.assertEquals(p.getAge(),first.getAge());
        Assert.assertEquals(p.getName(),first.getName());
    }

    @Test
    public void test10field(){

        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");
        p.setWeight(3.3);
        Child first = mars.save(p);


        Assert.assertEquals(p.getId(),first.getId());
        Assert.assertEquals(p.getAge(),first.getAge());
        Assert.assertEquals(p.getName(),first.getName());
        Assert.assertEquals(p.getWeight(),first.getWeight());
    }

    @Test
    public void test11field(){

        Child p = new Child();
        p.setAge(18);
        p.setName("child");
        p.setId("10");
        p.setWeight(3.3);
        mars.insert(p);
        Child first = mars.findOne(new Query(), Child.class).get();

        Assert.assertEquals(p.getId(),first.getId());
        Assert.assertEquals(p.getAge(),first.getAge());
        Assert.assertEquals(p.getName(),first.getName());
        Assert.assertEquals(p.getWeight(),first.getWeight());

    }



}
