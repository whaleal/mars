package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.*;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InsertTest {

    Mars mars = new Mars(Constant.connectionStr);



    @Test
    public void insert(){

        String [] code = {"20000"};

        Book book = new Book();
        book.setId(new ObjectId());
        book.setName("月亮与六便士");
        book.setPrice(32.15);
        book.setStocks(100);

        City city = new City();
        city.setId(new ObjectId().toString());
        city.setName("上海");
        city.setLat((float) 120.52);
        city.setLon((float) 30.40);
        city.setZipCodes(code);

        Address address = new Address();
        address.setCity(city);
        address.setStreetName("静安区");
        address.setStreetNumber(021L);

        Person person = new Person();
        person.setId("007");
        person.setAge("18");
        person.setAddress(address);
        person.setBirthDate(LocalDate.now());
        person.setHeight(175.2);
        person.setFirstName("张");
        person.setLastName("三");
        person.setCars(null);

        Map<String,Person> map = new HashMap<>();
        map.put("张三",person);

        Animal animal = new Animal();
        animal.setId("1001");
        animal.setAge(12);
        animal.setBook(book);
        animal.setArt(map);
        animal.setStatus(null);
        animal.setBirthday(new Date());
        animal.setIsAlive(true);

        Animal insert = mars.insert(animal);
        System.out.println("======================");
        System.out.println(insert);
        Animal resultAnimal = mars.findOne(new Query(), Animal.class).orElse(null);

        mars.delete(new Query(new Criteria("_id").is("1001")),Animal.class);

        Assert.assertEquals(animal,resultAnimal);

    }

}
