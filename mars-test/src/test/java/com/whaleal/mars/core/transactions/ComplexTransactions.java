package com.whaleal.mars.core.transactions;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.util.StudentGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author wh
 *
 * 复杂事务测试
 */
public class ComplexTransactions {

    private Mars  mars  =new Mars(Constant.connectionStr) ;



    @Test
    public void test(){
        Student stu = StudentGenerator.getInstance(1001);



    }



    public  void  testMethod( Student stu ){
        // 基于事务的操作
        mars.save(stu);

        // 在事务体内 此时应该是查不到数据
        Assert.assertNull(new Mars(Constant.connectionStr).find(new Query(), Student.class).tryNext());


        Student stu2 = StudentGenerator.getInstance(1002);

        //  新的 会话插入
        new Mars(Constant.connectionStr).insert(stu2);


        assertEquals(mars.find(new Query(), Student.class).toList().size() , 1);

        assertNotNull(mars.find(new Query(), Student.class).tryNext());



    }
}
