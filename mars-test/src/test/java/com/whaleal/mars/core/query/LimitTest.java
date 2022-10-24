package com.whaleal.mars.core.query;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Num;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjq
 * @ClassName LimitTest
 * @Description
 * @date 2022/7/15 11:37
 */
public class LimitTest {

    Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
//        mars.createCollection(Num.class);
//        mars.insert(new Num(1,"aa",1));
//        mars.insert(new Num(3,"aa",3));
//        mars.insert(new Num(4,"aa",4));
//        mars.insert(new Num(2,"aa",2));
//        mars.insert(new Num(5,"aa",5));
    }

    @Test
    public void testForSort(){
        Query query = new Query(new Criteria());
        query.limit(2);
        QueryCursor<Num> queryCursor = mars.findAll(query, Num.class);
        while(queryCursor.hasNext()){
            System.out.println(queryCursor.next());
        }
    }

    @After
    public void dropCollection(){
        mars.dropCollection("num");
    }
}
