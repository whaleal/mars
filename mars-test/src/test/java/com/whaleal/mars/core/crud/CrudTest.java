package com.whaleal.mars.core.crud;


import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.util.StudentGenerator;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Before
    public void isNull() {

        mars = new Mars(Constant.connectionStr);

        Assert.assertNotNull(this.mars);




    }

    @After
    public void dropCollection(){
        mars.dropCollection(Student.class);
    }


    @Test
    public void findAll() {

        Query query = new Query();

        QueryCursor< Student > result = mars.find(query, Student.class);

        while (result.hasNext()) {
            System.out.println(result.next());
        }

    }

    @Test
    public void findOne() {

        student = StudentGenerator.getInstance(10000);

        mars.insert(student);

        Query query = new Query();
        Optional< Student > one = mars.findOne(query, Student.class);
        Student student = one.get();

        Assert.assertNotNull(student);
    }

    @Test
    public void insertOne() {

        Student student = StudentGenerator.getInstance(stuNo);

        mars.dropCollection(Student.class);
        mars.insert(student);

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
