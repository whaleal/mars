package com.whaleal.mars.core.crud;

import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * @author: lyz
 * @desc:
 * @create: 2022-08-22 15:58
 **/
public class SaveTest {

    private Mars mars = new Mars("mongodb://localhost:27017/mars");


//    @Before
//    public void createData(){
//        Animal animal = new Animal();
//        animal.setAge(12);
//        animal.setBirthday(new Date());
//        mars.insert(animal);
//    }

    @Test
    public void query(){
        QueryCursor<Animal> all = mars.findAll(new Query(), Animal.class);

        while (all.hasNext()){
            System.out.println(all.next());
        }

    }

    @Test
    public void save(){
        Animal animal = new Animal();
        animal.setId("6303387b47166d42e93af1wqsb");
        animal.setBirthday(new Date(1612202522L));

        mars.insert(animal);
    }

    @Test
    public void dropData(){
        mars.dropCollection(Animal.class);
    }
}
