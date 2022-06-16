package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Gender;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.option.CollectionOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EntityTest {
    Mars mars;

    @BeforeMethod
    public void init() {
        mars = new Mars(Constant.connectionStr);
    }

    @Test
    public void testDropCollection() {
        mars.dropCollection(Student.class);
    }

    @Test
    public void testCreateCollection() {
        CollectionOptions options = CollectionOptions.empty().capped().size(2000L).maxDocuments(2000L);

        mars.createCollection(Student.class, options);

    }

    @Test
    public void testProperty(){
        Student student = new Student();
        //id
        student.setStuNo("1234");
        //字段指定别名
        student.setStuAge(25);
        //指定字段值类型
        student.setStuHeight(160D);
        student.setStuSex(Gender.male);

        mars.insert(student);
    }

}
