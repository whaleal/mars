package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.bean.Book;
import com.whaleal.mars.bean.Num;
import com.whaleal.mars.bean.Status;
import com.whaleal.mars.core.Mars;

import com.whaleal.mars.session.QueryCursor;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyz
 * @desc
 * @create: 2022-08-01 18:14
 **/
public class TestForPropIgnore {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testFor(){
        Animal animal = new Animal();
        animal.setAge(24);

        Book book = new Book();
        book.setName("java");
        book.setPrice(99.99);

        animal.setBook(book);

        Num num = new Num();
        num.setNum(1);
        num.setName("test");

        List<Num> objects = new ArrayList<>();
        objects.add(num);

        animal.setNum(objects);

        animal.setStatus(Status.Incomplete);

        animal.setId(new ObjectId().toString());
        animal.setIsAlive(true);

         mars.insert(animal);

        QueryCursor< Animal > all = mars.findAll(Animal.class);
        while (all.hasNext()){
            Animal next = all.next();

            Assert.assertNotNull(next.getId());
            Assert.assertNotNull(next.getAge());
            Assert.assertNotNull(next.getIsAlive());

            Assert.assertNull(next.getBook());
            Assert.assertNull(next.getNum());
            Assert.assertNull(next.getArt());
            Assert.assertNull(next.getStatus());

            Assert.assertNull(next.getBirthday());

        }

    }

    @Test
    public void testForQuery(){
        //Query query = new Query(new Criteria());

        QueryCursor<Animal> all = mars.findAll( Animal.class);
        while (all.hasNext()){
            System.out.println(all.next());
        }
    }
}
