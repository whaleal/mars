package com.whaleal.mars.core.crud;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Car;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.result.DeleteResult;

import java.util.ArrayList;
import java.util.List;

public class LocalDeleteTest {
    Mars mars;


    List<Person> people = new ArrayList<>();


    @Before
    public void init() {
        mars = new Mars(Constant.server100);

        Assert.assertNotNull(mars);

        for (int i = 0; i < 999999; i++) {
            people.add(EntityGenerater.getPerson());
        }
    }


    @Test
    public void delete() {
        Query query = new Query();

        DeleteOptions options = new DeleteOptions();
        options.multi(true);


        DeleteResult result = mars.delete(query, Person.class, options);

        System.out.println(result);

    }


    @Test
    public void delete1() {
        Query query = new Query();

        DeleteResult result = mars.delete(query,"person");

        System.out.println(result);

    }

    @Test
    public void delete2(){

        Query query = new Query();

        DeleteOptions options = new DeleteOptions();
        options.multi(true);


        DeleteResult result = mars.delete(query,"person",options);

        System.out.println(result);

    }

    @Test
    public void delete3(){

        Query query = new Query();

        DeleteOptions options = new DeleteOptions();
        options.multi(true);


        DeleteResult result = mars.delete(query, Car.class,options,"person");

        System.out.println(result);

    }


    @Test
    public void delete4(){

        Query query = new Query(Criteria.where("_id").is(new ObjectId("60a363180de7e47bd42bb51c")));

        DeleteOptions options = new DeleteOptions();
        options.multi(true);


        DeleteResult result = mars.delete(query, Car.class,options,"person");

        System.out.println(result);

    }



}
