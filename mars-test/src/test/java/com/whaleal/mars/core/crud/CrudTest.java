package com.whaleal.mars.core.crud;

import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Articles;
import com.whaleal.mars.bean.Child;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.InsertOneOptions;

import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.UpdateResult;
import com.whaleal.mars.util.StudentGenerator;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author cx
 * @date 2020/1/4
 * 测试crud基础功能
 */


@SpringBootTest
public class CrudTest {

    @Autowired
    Mars mars;

    Student student;


    Integer stuNo = 10000;

    @BeforeMethod
    public void isNull() {

        mars = new Mars(Constant.connectionStr);

        Assert.assertNotNull(this.mars);

        System.out.println(this.mars);
        student = StudentGenerator.getInstance(10000);
    }

    @After
    public void dropCollection(){
        mars.dropCollection(Student.class);
    }


    @Test
    public void findAll() {

        Query query = new Query();

        QueryCursor< Student > result = mars.findAll(query, Student.class);

        while (result.hasNext()) {
            System.out.println(result.next());
        }

    }

    @Test
    public void findOne() {

        Query query = Query.query(Criteria.where("_id").is(new ObjectId("6034c9c9e73be70704731635")));
        Optional< Student > one = mars.findOne(query, Student.class, null);

        System.out.println(one);
    }

    @Test
    public void insertOne() {

        Student student = StudentGenerator.getInstance(stuNo);

        mars.dropCollection(Student.class);
        mars.insert(student);
//        mars.dropCollection(Student.class);

    }

    @Test
    public void insertOption() {
        mars.dropCollection(Student.class);
        System.out.println("getTime: " + System.currentTimeMillis());
        mars.insert(student);
        System.out.println("endTime: " + System.currentTimeMillis());
    }

    @Test
    public void insertMany() {
        System.out.println("getTime: " + System.currentTimeMillis());
        mars.dropCollection(Student.class);
        mars.insert(student);
        System.out.println("endTime: " + System.currentTimeMillis());
    }

}
