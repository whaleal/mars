
package com.whaleal.mars.base;

import com.whaleal.mars.bean.Student;
import com.whaleal.mars.config.MongoProperties;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.result.InsertManyResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContextTest {

    @Autowired
    Mars mars;

    @Test
    public void testIOC(){
        LinkedList<Student> list = new LinkedList<>();
        int i  ;
        for (i =0; i <1000; i++) {
            Student student = StudentGenerator.getInstance(i);
            list.add(student);
        }
        mars.dropCollection(Student.class);
        InsertManyResult insert = mars.insert(list);
        long count = mars.count(Student.class);
        Assert.assertEquals(i,count);
        mars.dropCollection(Student.class);

    }

    @Autowired
    MongoProperties mongoProperties ;

    @Test
    public void testProperties(){

        Assert.assertNotNull(mongoProperties.getUri());

    }


    
}
