package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.QueryCursor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;


/**
 * 对查询操作中的排序功能进行测试
 *
 * @author cs
 * @date 2021/06/16
 */
public class SortTest {
    Mars mars = new Mars(Constant.connectionStr);


    @BeforeMethod
    public void init() {
        //准备数据准备一次就足够了，不能准备多次
        LinkedList< Student > list = new LinkedList<>();
        for (int i = 1001; i < 1010; i++) {
            Student student = StudentGenerator.getInstance(i);
            list.add(student);
        }
        mars.insert(list, Student.class);
    }


    @AfterMethod
    public void destory() {

        mars.dropCollection(Student.class);

    }


    @Test
    public void testSort() {
//        (Sort.on().ascending("sex")
        QueryCursor< Student > stuList = mars.findAll(new Query().with(Sort.ascending("sex")), Student.class);
        stuList.toList().forEach(System.out::println);
    }

}
