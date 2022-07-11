package com.whaleal.mars.core.command;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * @author: cjq
 * @date: 2022/7/6 0006
 * @desc:
 */
public class testlyz {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void createData(){
//       mars.createCollection(Book.class);
       //String 类型入参
//       mars.insert(new Book("aa","aa",2.0,2));
//       mars.insert(new Book("bb","bb",3.0,3));
//       mars.insert(new Book("cc","cc",4.0,4));
//       mars.insert(new Book("dd","dd",5.0,5));
//        InsertOneResult result = mars.insert(new Book("ee", "rr", 6.0, 6));
        //Integer
        mars.insert(new Book(1,"rrr",2.0,2),"book2");
        mars.insert(new Book(2,"rree",3.0,3),"book2");
        mars.insert(new Book(3,"rere",4.0,4),"book2");
        mars.insert(new Book(4,"eerr",5.0,5),"book2");
        //ObjectId
//        mars.insert(new Book(new ObjectId(),"aa",2.0,2));
//        mars.insert(new Book(new ObjectId(),"bb",3.0,3));
//        mars.insert(new Book(new ObjectId(),"cc",4.0,4));
//        mars.insert(new Book(new ObjectId(),"dd",5.0,5));
//        InsertOneResult result = mars.insert(new Book(new ObjectId(), "rr", 6.0, 6));
//        System.out.println(result.getInsertedId());


    }

    @Test
    public  void test(){
//        Optional<Book> book = mars.findById("62cbc2975b813772ae14f98c", Book.class);
//        Book book1 = book.get();
//        System.out.println(book1);
//        Optional<Book> book = mars.findById("dd", Book.class);
//        Book book1 = book.get();
//        System.out.println(book1);
        Optional<Book> book = mars.findById(3, Book.class);
        Book book1 = book.get();
        System.out.println(book1);
    }

    @Test
    public void drop(){
        mars.dropCollection("book2");
    }
}
