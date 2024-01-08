package com.whaleal.mars.core.extendbean;


import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class CrudWithExtend {


    @Autowired
    Mars mars;


    List< Person > people = new ArrayList<>();


    @Before
    public void init() {
        mars = new Mars(Constant.connectionStr);

        Assert.assertNotNull(mars);

        for (int i = 0; i < 999; i++) {
            people.add(EntityGenerater.getPerson());
        }

        mars.insert(people,Person.class);
    }

    @Test
    public void test01() {


        QueryCursor< Person > all = mars.findAll( Person.class);

        while (all.hasNext()) {
            Person p = all.next();
            Assert.assertNotNull(p.getId());
        }
        mars.dropCollection(Person.class);
    }


    @After
    public void drop(){
        mars.dropCollection(Person.class);
    }

}
