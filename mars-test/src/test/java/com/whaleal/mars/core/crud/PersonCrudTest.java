package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.base.SerializationUtil;
import com.whaleal.mars.bean.*;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import com.whaleal.mars.session.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonCrudTest {
    Mars mars;

    //

    /**
     * 关于注解的模式
     * 0 ：无注解 ；或 id  注解 ;
     * 1 ：
     */
    int model = 0;


    @BeforeMethod
    public void init() {
        //  创建连接
        mars = new Mars(Constant.connectionStr);

        // 清空需要的表
        mars.dropCollection(Person.class);


    }


    // InsertOne
    @Test
    public void test01() {
        Person person = EntityGenerater.getPerson();
        person.setAge("19");
        person.setHeight(2.12);
        person.setLastName("LastIsMars");
        person.setFirstName("FirstIsMars");
        person.setId("60dc738ffbd0bf3f4f7bc04c");
        Address address = new Address();
        address.setStreetName("南京路");
        address.setStreetNumber(1228L);
        City city = new City();
        city.setId("1231321");
        city.setName("湖南");
        city.setLat(123f);
        city.setLon(31f);
        String[] zipCodes = new String[4];
        zipCodes[0] = ("11111");
        zipCodes[1] = ("2222");
        zipCodes[2] = ("333");
        zipCodes[3] = ("4444");
        city.setZipCodes(zipCodes);

        address.setCity(city);
        person.setAddress(address);

        InsertOneResult insertResult = mars.insert(person);

        Assert.assertTrue(insertResult.wasAcknowledged());
        Assert.assertEquals(insertResult.getInsertedId().isObjectId() ? insertResult.getInsertedId().asObjectId().getValue().toHexString() : insertResult.getInsertedId().asString().getValue(), "60dc738ffbd0bf3f4f7bc04c");

        if (model == 0) {

            Optional< Person > personFromDB = mars.findOne(Query.query(Criteria.where("_id").is(new ObjectId("60dc738ffbd0bf3f4f7bc04c"))), Person.class);

            Assert.assertNotNull(personFromDB.get());

            Person person1 = personFromDB.get();

            Assert.assertEquals(person.getAge(), person1.getAge());

            Assert.assertEquals(person.getId(), person1.getId());
            Assert.assertEquals(person.getFirstName(), person1.getFirstName());
            Assert.assertEquals(person.getLastName(), person1.getLastName());
            Assert.assertEquals(person.getHeight(), person1.getHeight());
            Assert.assertEquals(person.getBirthDate(), person1.getBirthDate());
            Assert.assertEquals(person.getAddress(), person1.getAddress());
            Assert.assertEquals(person.getCars().length, person1.getCars().length);
            Assert.assertEquals(person.getCars()[0].id, person1.getCars()[0].id);
        }


    }


    //InsertMany
    @Test
    public void test02() {

        int number = 999999;
        List< Person > personList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            personList.add(EntityGenerater.getPerson());
        }

        Assert.assertEquals(personList.size(), number);

        InsertManyResult insertManyResult = mars.insert(personList, Person.class);

        Assert.assertTrue(insertManyResult.wasAcknowledged());

        long count = mars.estimatedCount(Person.class);

        Assert.assertEquals(number, count);

        QueryCursor< Person > all = mars.findAll(new Query(), Person.class);

        while (all.hasNext()) {
            Person next = all.next();

            Assert.assertNotNull(next);
            Assert.assertNotNull(next.getId());
            Assert.assertNotNull(next.getAge());
            Assert.assertNotNull(next.getAddress());
            Assert.assertNotNull(next.getBirthDate());
            Assert.assertNotNull(next.getFirstName());
            Assert.assertNotNull(next.getLastName());
            Assert.assertNotNull(next.getHeight());

            Address address = next.getAddress();

            Assert.assertNotNull(address.getCity());
            Assert.assertNotNull(address.getStreetName());
            Assert.assertNotNull(address.getStreetNumber());

            City city = address.getCity();

            Assert.assertNotNull(city.getId());
            Assert.assertNotNull(city.getLat());
            Assert.assertNotNull(city.getLon());
            Assert.assertNotNull(city.getName());

            Assert.assertNotNull(city.getZipCodes());
        }


    }


    //CountEntity
    @Test
    public void test03() {
        int number = 999;
        List< Person > personList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            personList.add(EntityGenerater.getPerson());
        }

        mars.insert(personList);
        long count = mars.estimatedCount(Person.class);

        Assert.assertEquals(count, number);

    }

    // CountEntityById
    @Test
    public void test04() {
        int number = 999;
        List< Person > personList = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            personList.add(EntityGenerater.getPerson());
        }

        mars.insert(personList);
        long count = mars.count(new Query(), Person.class);

        Assert.assertEquals(count, number);

    }


    // deleteOne
    @Test
    public void test05() {

        mars.createCollection(Person.class);

        long count = mars.estimatedCount(Person.class);

        Assert.assertEquals(count, 0);

        Person person = EntityGenerater.getPerson();

        InsertOneResult insert = mars.insert(person);

        Assert.assertTrue(insert.wasAcknowledged());

        if (model == 0) {

            Assert.assertNotNull(insert.getInsertedId().asObjectId().getValue().toHexString());

            String id = insert.getInsertedId().asObjectId().getValue().toHexString();

            long count1 = mars.estimatedCount(Person.class);
            Assert.assertEquals(count1, 1);

            Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(id)));
            DeleteResult deleteResult = mars.delete(query, Person.class);

            Assert.assertEquals(deleteResult.getDeletedCount(), 1);
        }


    }


    //deleteManay
    @Test
    public void test06() {


        List< Document > documents = SerializationUtil.getDocuments();

        com.mongodb.client.result.InsertManyResult insertManyResult = mars.getCollection(Document.class, "person").insertMany(documents);


        Assert.assertTrue(insertManyResult.wasAcknowledged());

        int number = insertManyResult.getInsertedIds().size();

        Query query = new Query();

        DeleteOptions options = new DeleteOptions();
        options.multi(true);

        DeleteResult result = mars.delete(query, Person.class, options);

        long deletedCount = result.getDeletedCount();

        Assert.assertEquals(number, deletedCount);


    }


    // delete with  "address" : { "city" : { "id" : "102101021"}}
    @Test
    public void test07() {


        Person person = EntityGenerater.getPerson();
        mars.insert(person);

        String value = person.getAddress().getCity().getId();

        Query query = new Query(Criteria.where("address.city.id").is(value));

        DeleteOptions options = new DeleteOptions();

        DeleteResult result = mars.delete(query, Person.class, options, "person");

        long deletedCount = result.getDeletedCount();

        Assert.assertEquals(deletedCount, 1);

        long count = mars.estimatedCount(Person.class);

        Assert.assertEquals(count, 0);

    }


    //  upsertWithEntity
    @Test
    public void test08() {
        Person person = EntityGenerater.getPerson();

        Query query = Query.query(Criteria.where("a").is(9999));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.updateEntity(query, person, options);


        Assert.assertTrue(result.wasAcknowledged());
        Assert.assertNotNull(result.getUpsertedId().asObjectId().getValue());


    }


    //upsertWithUpdateDefinition
    @Test
    public void test09() {
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

        UpdateResult result = mars.update(query, entity, Person.class, options);


        Assert.assertNotNull(result.getUpsertedId());
    }


    //update
    @Test
    public void test10() {

        Person person = EntityGenerater.getPerson();

        Query query = Query.query(Criteria.where("a").is(100));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.updateEntity(query, person, options);


        Assert.assertNotNull(result.getUpsertedId().asObjectId().getValue());

    }


    //updateByUpdateDefinition
    @Test
    public void test11() {


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

        Assert.assertNotNull(result.getUpsertedId().asObjectId().getValue());

    }


    //testUpdateUpdateDefinition1
    @Test
    public void test12() {

        Update update = Update.update("name", "updateDefinition后的id").set("department", 10086);

        Query query = Query.query(Criteria.where("name").is("cName"));

        UpdateResult result = mars.update(query, update, "person");


        Assert.assertEquals(result.getMatchedCount(), 0);

    }

    //testUpdateUpdateDefinition2

    @Test
    public void test13() {

        Update update = Update.update("name", "updateDefinition后的id").set("department", 10087);

        Query query = Query.query(Criteria.where("name").is("cName"));


        UpdateResult result = mars.update(query, update, "person", new UpdateOptions().upsert(true));


        Assert.assertEquals(result.getMatchedCount(), 0);

        Assert.assertNotNull(result.getUpsertedId().asObjectId().getValue());
    }


    // test findAll()
    @Test
    public void test14() {

        initWithStanderData();


        QueryCursor< Person > all = mars.findAll(new Query(), Person.class);


        List< Person > personList = all.toList();


        Assert.assertEquals(personList.size(), 1000);


        Person p = personList.get(0);

        Assert.assertNotNull(p.getId());


    }

    @Test
    public void test30() {

        mars.ensureIndexes(Student.class, "person");

    }


    @Test
    public void test31() {

        mars.ensureIndexes(Student.class, "person");
        List< Index > person = mars.getIndexes("person");

        Assert.assertEquals(person.size(), 4);

    }


    @Test
    public void test32() {
        mars.createCollection(Person.class);

        mars.dropCollection("person");
    }

    @Test
    public void test33() {
        mars.createCollection(Person.class);


    }


    public void initWithStanderData() {

        List< Document > documents = SerializationUtil.getDocuments();

        com.mongodb.client.result.InsertManyResult insertManyResult = mars.getCollection(Document.class, "person").insertMany(documents);


        Assert.assertTrue(insertManyResult.wasAcknowledged());

    }


}
