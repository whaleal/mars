package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Sort;
import com.whaleal.mars.session.QueryCursor;
import org.junit.Before;
import org.junit.Test;


/**
 * 对查询操作中的排序功能进行测试
 *
 * @author cs
 * @date 2021/06/16
 */
public class SortTest {
    Mars mars;

    @Before
    public void init() {
        mars = new Mars(Constant.server101);
        //准备数据准备一次就足够了，不能准备多次
        /*LinkedList<Student> list = new LinkedList<>();
        for (int i = 1001; i < 1010; i++) {
            Student student = StudentGenerator.getInstance(i);
            list.add(student);
        }
        mars.insert(list);*/
    }


    @Test
    public void testSort() {
        QueryCursor<Student> stuList = mars.findAll(new Query().with(Sort.by("stuName").descending()), Student.class);
        stuList.toList().forEach(System.out::println);
    }

}
