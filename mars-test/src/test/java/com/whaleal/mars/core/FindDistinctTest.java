package com.whaleal.mars.core;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author: cjq
 * @date: 2022/7/6 0006
 * @desc:
 */
public class FindDistinctTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData(){
        mars.insert(new Book(1,"rrr",2.0,2));
        mars.insert(new Book(2,"rrr",3.0,3));
        mars.insert(new Book(3,"rrr",4.0,4));
        mars.insert(new Book(4,"rere",4.0,4));
        mars.insert(new Book(5,"eerr",4.0,4));
        mars.insert(new Book(6,"eerr",5.0,5));
        mars.insert(new Book(7,"eerr",5.0,5));
        mars.insert(new Book(8,"eerr",5.0,5));
    }

    @Test
    public  void test(){
        Query query = new Query();
        QueryCursor<Book> all = mars.findAll(query, Book.class);

        List<Double> price = mars.findDistinct(query, "price", Book.class, Double.class).toList();

        Double[] doubles = {2.0, 3.0, 4.0, 5.0};
        List<Double> doubles1 = Arrays.asList(doubles);

        Assert.assertEquals(price,doubles1);
    }

    @After
    public void drop(){
        mars.dropCollection("book");
    }
}
