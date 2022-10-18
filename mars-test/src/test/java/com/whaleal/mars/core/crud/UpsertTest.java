package com.whaleal.mars.core.crud;

import com.mongodb.client.result.UpdateResult;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.*;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UpsertTest {

    Mars mars = new Mars(Constant.connectionStr);


    @Before
    public void creatData(){

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

    }

    @After
    public void dropCollection(){
        mars.dropCollection("animal");
    }



    @Test
    public void update(){

        Query query = new Query(new Criteria("_id").is("1001"));

        Update update = new Update();
        update.set("age",22);

        Optional<Animal> oldAnimal = mars.findOne(query, Animal.class);
        UpdateResult updateResult = mars.update(query, update, Animal.class);
        Optional<Animal> animal = mars.findOne(query, Animal.class);

        System.out.println("======================");
        System.out.println(animal);
        System.out.println("======================");
        System.out.println(oldAnimal);
        System.out.println("======================");

    }



}
