package com.whaleal.mars.core;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.Optional;

/**
 * @author: cjq
 * @date: 2022/7/6 0006
 * @desc:
 */
public class FindDistinctTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void createData(){
        mars.insert(new Book(new ObjectId(),"rrr",2.0,2),"book");
        mars.insert(new Book(new ObjectId(),"rrr",3.0,3),"book");
        mars.insert(new Book(new ObjectId(),"rrr",4.0,4),"book");
        mars.insert(new Book(new ObjectId(),"rere",4.0,4),"book");
        mars.insert(new Book(new ObjectId(),"eerr",4.0,4),"book");
        mars.insert(new Book(new ObjectId(),"eerr",5.0,5),"book");
        mars.insert(new Book(new ObjectId(),"eerr",5.0,5),"book");
        mars.insert(new Book(new ObjectId(),"eerr",5.0,5),"book");
    }

    @Test
    public  void test(){
//        QueryCursor<Double> cursor = mars.findDistinct(new Query(), "price", Book.class, Double.class);
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("rrr"));
        QueryCursor<Book> all = mars.findAll(query, Book.class);
        while(all.hasNext()){
            System.out.println(all.next());
        }
        QueryCursor<Double> cursor = mars.findDistinct(query,"price", Book.class, Double.class);
        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }

    }

    @Test
    public void drop(){
        mars.dropCollection("book");
    }
}
