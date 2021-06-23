
package com.whaleal.mars.base;

import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
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
        for (int i = 1001; i < 1010; i++) {
            Student student = StudentGenerator.getInstance(i);
            list.add(student);
        }
        mars.insert(list);
    }

    
}
