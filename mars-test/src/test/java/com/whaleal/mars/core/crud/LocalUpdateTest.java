package com.whaleal.mars.core.crud;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.UpdateResult;

import java.util.ArrayList;
import java.util.List;

public class LocalUpdateTest {
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
    public void upsertWithEntity(){
        Person person = EntityGenerater.getPerson();

        Query query = Query.query(Criteria.where("a").is(99));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.update(query, person, options);


        System.out.println(result);

    }


    @Test
    public void upsertWithUpdateDefinition(){
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
    public void update() {

        Person person = EntityGenerater.getPerson();

        Query query = Query.query(Criteria.where("a").is(100));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.update(query, person, options,null);


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
        options.upsert(true);

        UpdateResult result = mars.update(query, entity, Person.class, options);

        System.out.println(result);

    }


    @Test
    public void testUpdateUpdateDefinition1(){

        Update update = Update.update("name", "updateDefinition后的id").set("department", 10086);

        Query query = Query.query(Criteria.where("name").is("cName"));

        UpdateResult result = mars.update(query,update,"person");

        System.out.println(result);

    }


    @Test
    public void testUpdateUpdateDefinition2(){

        Update update = Update.update("name", "updateDefinition后的id").set("department", 10087);

        Query query = Query.query(Criteria.where("name").is("cName"));


        UpdateResult result1 = mars.update(query,update,"person",new UpdateOptions().upsert(true));


        System.out.println(result1);
    }

}
