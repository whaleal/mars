package com.whaleal.mars.core.crud;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.bean.PersonEx;
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


    @Test
    public  void tryInsert(){

        PersonEx personEx = new PersonEx();
        Person person = people.get(0);

        personEx.setAddress(person.getAddress());
        personEx.setAge(person.getAge());
        personEx.setId(person.getId());
        personEx.setFirstName(person.getFirstName());
        personEx.setLastName(person.getLastName());


        mars.insert(personEx);
    }



}
