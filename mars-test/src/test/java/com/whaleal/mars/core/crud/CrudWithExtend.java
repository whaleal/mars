package com.whaleal.mars.core.crud;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.core.Mars;

import java.util.ArrayList;
import java.util.List;

public class CrudWithExtend {


    Mars mars;


    List<Person> people = new ArrayList<>();



    @Before
    public void init() {
        mars = new Mars(Constant.server100);

        Assert.assertNotNull(mars);

        for(int i = 0 ;i<999999 ;i++){
            people.add(EntityGenerater.getPerson());
        }



    }



}
