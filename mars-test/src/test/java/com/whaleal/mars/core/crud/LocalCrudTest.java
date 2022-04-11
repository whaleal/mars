package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.ReplaceOptions;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
public class LocalCrudTest {


    Mars mars;


    List< Person > people = new ArrayList<>();


    @BeforeMethod
    public void init() {
        mars = new Mars(Constant.connectionStr);

        Assert.assertNotNull(mars);

        for (int i = 0; i < 999999; i++) {
            people.add(EntityGenerater.getPerson());
        }
    }


    @Test
    public void findAll() {

        Query query = new Query();
        QueryCursor< Person > result = mars.findAll(query, Person.class);

        while (result.hasNext()) {
            Person person = result.next();

            System.out.println(person);

        }

    }

    @Test
    public void findOne() {


        Query query = Query.query(Criteria.where("address.city.name").is("上海"));
        Optional< Person > one = mars.findOne(query, Person.class);

        Person person = one.get();

        System.out.println(person);
        Assert.assertNotNull(person);

        Assert.assertNotNull(person.getId());

        Assert.assertEquals(person.getAddress().getCity().getName(), "上海");


    }

    @Test
    public void insertOne() {

        Person p = EntityGenerater.getPerson();

        Assert.assertNull(p.getId());


        mars.insert(p);

        Assert.assertNotNull(p.getId());


    }

    @Test
    public void insertMany() {

        mars.dropCollection(Person.class);
        InsertManyResult insert = mars.insert(people, Person.class);

        int size = insert.getInsertedIds().size();


        Assert.assertEquals(size, people.size());

    }

    @Test
    public void update() {

        Person person = EntityGenerater.getPerson();

        Query query = Query.query(Criteria.where("a").is(100));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.updateEntity(query, person, options, null);
        //mars.update(query, person);

        System.out.println(result);

    }

    @Test
    public void updateByUpdateDefinition() {


        List zipCodes = new ArrayList();
        zipCodes.add("11111");
        zipCodes.add("2222");
        zipCodes.add("333");
        zipCodes.add("4444");

        Document city = new Document();
        city.put("id", "1231321");
        city.put("name", "湖南");
        city.put("lat", 123);
        city.put("lon", 31);
        city.put("zipCodes", zipCodes);


        Document address = new Document();
        address.put("streetName", "南京路");
        address.put("streetNumber", 1233);
        address.put("city", city);

        Document employee = new Document();
        employee.put("name", "尾田荣一郎");
        employee.put("age", 21);
        employee.put("sex", "男");
        employee.put("address", address);

        List employees = new ArrayList();
        employees.add(employee);

        Document document = new Document();
        document.put("name", "111111");
        document.put("Employees", employees);


        Update entity = Update.update("name", "updateDefinition后的id").set("department", document);

        Query query = Query.query(Criteria.where("name").is("cName"));

        UpdateOptions options = new UpdateOptions();
        options.multi(true);

        UpdateResult result = mars.update(query, entity, Person.class, options, null);

        System.out.println(result);

    }

    @Test
    public void delete() {
        Query query = new Query();

        DeleteOptions options = new DeleteOptions();
        options.multi(true);


        DeleteResult result = mars.delete(query, Person.class, options, null);

        System.out.println(result);

    }

    @Test
    public void replace() {

        Person replaceperson = EntityGenerater.getPerson();
        replaceperson.setFirstName("f49858");

        mars.insert(replaceperson);

        Person Person = EntityGenerater.getPerson();

        ReplaceOptions replaceOptions = new ReplaceOptions();

        //  修改并测试该参数
        Query query = Query.query(Criteria.where("fName").is("f49858"));
        UpdateResult replace = mars.replace(query, Person, replaceOptions);

        Assert.assertEquals(1, replace.getOriginUpdateResult().getModifiedCount());
    }

    @Test
    public void save() {

        Optional< Person > one = mars.findOne(new Query(), Person.class);

        Person p = one.get();

        Assert.assertNotNull(p);

        Assert.assertNotNull(p.getId());

        System.out.println(p.getId());

        Person per = EntityGenerater.getPerson();

        System.out.println(per);
        per.setId(p.getId());
        Person save = mars.save(per);

        System.out.println(save);

    }


    @Test
    public void upsertWithEntity() {
        Person person = EntityGenerater.getPerson();

        Query query = Query.query(Criteria.where("a").is(99));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.updateEntity(query, person, options, null);
        //mars.update(query, person);

        System.out.println(result);

    }


    @Test
    public void upsertWithUpdateDefinition() {
        List zipCodes = new ArrayList();
        zipCodes.add("11111");
        zipCodes.add("2222");
        zipCodes.add("333");
        zipCodes.add("4444");

        Document city = new Document();
        city.put("id", "1231321");
        city.put("name", "湖南");
        city.put("lat", 123);
        city.put("lon", 31);
        city.put("zipCodes", zipCodes);


        Document address = new Document();
        address.put("streetName", "南京路");
        address.put("streetNumber", 1233);
        address.put("city", city);

        Document employee = new Document();
        employee.put("name", "尾田荣一郎");
        employee.put("age", 21);
        employee.put("sex", "男");
        employee.put("address", address);

        List employees = new ArrayList();
        employees.add(employee);

        Document document = new Document();
        document.put("name", "111111");
        document.put("Employees", employees);


        Update entity = Update.update("name", "updateDefinition后的id").set("department", document);

        Query query = Query.query(Criteria.where("a").is(101));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.update(query, entity, Person.class, options, null);

        System.out.println(result);

    }


    @Test
    public void findOne2() {


        Query query = new Query();


        Optional< Person > one = mars.findOne(query, Person.class);
        Person person = one.get();
        System.out.println(person);
        Assert.assertNotNull(person);

        Assert.assertNotNull(person.getId());

        System.out.println(person.getAge());

        System.out.println(person.getAge());

        // Assert.assertEquals(person.getAddress().getCity().getName(), "上海");


    }

}
