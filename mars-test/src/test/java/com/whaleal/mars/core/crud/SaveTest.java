package com.whaleal.mars.core.crud;

import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.InsertManyOptions;
import com.whaleal.mars.session.option.InsertOneOptions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    public void test(){

        System.out.println("hello");
    }

    @Test
    public void save(){
        Animal animal = new Animal();
        animal.setId("6303387b47166d42e93af1wqsb");
        animal.setBirthday(new Date(1612202522L));

        Student student = new Student();
        student.setStuNo("1001");
        student.setStuName("cc");

        Animal save = mars.save(animal,"animal");
        System.out.println(save);

        System.out.println("==============");

        Animal insert = mars.insert(animal,"animal");
        System.out.println(insert);


        Animal insert2 = mars.insert(animal);
        System.out.println(insert2);

        InsertOneOptions options = new InsertOneOptions();
        InsertManyOptions optionsMany = new InsertManyOptions();

        List<Object> list = new ArrayList();
        list.add(animal);

        mars.insert(animal,"animal");

        mars.insert(list,options,"animal");
        mars.insert(list,"animal",optionsMany);



    }

    @Test
    public void dropData(){
        mars.dropCollection(Animal.class);
    }
}
