package com.whaleal.mars.core.crud;


import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.core.Mars;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CrudWithExtend {


    @Autowired
    Mars mars;


    List<Person> people = new ArrayList<>();


    @Before
    public void init() {
        mars = new Mars(Constant.connectingStr);

        Assert.assertNotNull(mars);

        for(int i = 0 ;i<999 ;i++){
            people.add(EntityGenerater.getPerson());
        }

        mars.insert(people);
    }

    @Test
    public void test01(){

        Query query = new Query();
        QueryCursor< Person > all = mars.findAll(query, Person.class);

        while (all.hasNext()){
            Person p = all.next();
            Assert.assertNotNull(p.getId());
        }
        mars.dropCollection(Person.class);
    }



}
