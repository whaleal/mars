package com.whaleal.mars.core.crud;

import com.whaleal.mars.bean.*;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.result.DeleteResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonCrudTest {
    Mars mars;



    @Before
    public void init() {
        //  创建连接
        mars = new Mars(Constant.server101);

        // 清空需要的表
        mars.dropCollection(Person.class);

    }


    @Test
    public void test01InsertOne(){
        Person person = EntityGenerater.getPerson();
        person.setAge("19");
        person.setHeight(2.12);
        person.setLastName("LastIsMars");
        person.setFirstName("FirstIsMars");
        person.setId("60dc738ffbd0bf3f4f7bc04c");
        Address  address = new Address();
        address.setStreetName( "南京路");
        address.setStreetNumber( 1228L);
        City city  = new City();
        city.setId( "1231321");
        city.setName( "湖南");
        city.setLat( 123f);
        city.setLon( 31f);
        String [] zipCodes = new String[4];
        zipCodes[0]=("11111");
        zipCodes[1] = ("2222");
        zipCodes[2] = ("333");
        zipCodes[3] = ("4444");
        city.setZipCodes( zipCodes);

        address.setCity( city);
        person.setAddress(address);

        InsertOneResult insertResult = mars.insert(person);

        Assert.assertTrue(insertResult.wasAcknowledged());
        Assert.assertEquals(insertResult.getInsertedId().asObjectId().getValue().toHexString(),"60dc738ffbd0bf3f4f7bc04c");

        Optional<Person> personFromDB = mars.findOne(Query.query(Criteria.where("_id").is(new ObjectId("60dc738ffbd0bf3f4f7bc04c"))), Person.class);

        Assert.assertNotNull(personFromDB.get());

        Person person1 = personFromDB.get();

        Assert.assertEquals(person.getAge(),person1.getAge());

        Assert.assertEquals(person.getId(),person1.getId());
        Assert.assertEquals(person.getFirstName(),person1.getFirstName());
        Assert.assertEquals(person.getLastName(),person1.getLastName());
        Assert.assertEquals(person.getHeight(),person1.getHeight());
        Assert.assertEquals(person.getBirthDate(),person1.getBirthDate());
        Assert.assertEquals(person.getAddress(),person1.getAddress());
        Assert.assertEquals(person.getCars().length,person1.getCars().length);
        Assert.assertEquals(person.getCars()[0].id,person1.getCars()[0].id);

    }



    @Test
    public void test02InsertMany(){

        int number = 999999 ;
        List<Person> personList = new ArrayList<>();
        for (int i = 1; i <=number; i++) {
            personList.add(EntityGenerater.getPerson());
        }

        Assert.assertEquals(personList.size() ,number);

        InsertManyResult insertManyResult = mars.insert(personList);

        Assert.assertTrue(insertManyResult.wasAcknowledged());

        long count = mars.count(Person.class);

        Assert.assertEquals(number, count);

    }


    @Test
    public void test03Count(){
        long count = mars.count(Person.class);
        System.out.println(count);
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
