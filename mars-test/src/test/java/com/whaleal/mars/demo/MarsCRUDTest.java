package com.whaleal.mars.demo;


import com.whaleal.mars.base.StudentGenerator;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.InsertOneResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

import java.util.Optional;

/**
 * @author wh
 */
@Slf4j
@SpringBootTest
public class MarsCRUDTest {
    @Autowired
    private Mars mars;

    //插入一个对象记录到数据库
    @Test
    public void testInsertOne() {
        //这个只是我自定义的一个创建对象的静态方法
        Student student = StudentGenerator.getInstance(10000);
        InsertOneResult insert = mars.insert(student);
    }

    //根据条件查找一个对象并返回
    @Test
    public void testFind() {
        String key = "1";
        Optional< Student > student = mars.findOne(Query.query(Criteria.where("_id").is(key)), Student.class);
    }

    //更新student表中_id为key的记录，修改它的stuName字段为yy
    @Test
    public void testUpdate() {
        String key = "1";
        mars.update(Query.query(Criteria.where("_id").is(key)), Update.update("stuName", "yy"), Student.class, new UpdateOptions().multi(false), "student");
    }

    //删除student集合中_id为key的记录
    @Test
    public void testDeleteOne() {
        String key = "1";
        mars.delete(Query.query(Criteria.where("_id").is(key)), Student.class);
    }

}
