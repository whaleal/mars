
package com.whaleal.mars.core.crud;

import com.whaleal.mars.bean.Corporation;
import com.whaleal.mars.bean.Depart;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.Person;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.ReplaceOptions;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author cx
 * @date 2020/1/4
 * 测试crud基础功能
 */


@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudTest {

    @Autowired
    Mars mars;

    List<Corporation> corporations;

    @Before
    public void isNull() {
        Assert.assertNotNull(mars);

        System.out.println(mars);
        corporations =EntityGenerater.getCorporation(true);
    }

    @Test
    public void insertTest(){
        Depart depart = new Depart();
        //depart.setDepartName("12");
        Integer[] age = {12,12};
        //depart.setAge(13);
        mars.insert(depart);

    }

    @Test
    public void findAll() {

        Query query = new Query();

        QueryCursor<Corporation> result = mars.findAll(query, Corporation.class);

        while (result.hasNext()) {
            System.out.println(result.next());
        }

    }

    @Test
    public void findOne() {

        Query query = Query.query(Criteria.where("_id").is(new ObjectId("6034c9c9e73be70704731635")));
        Optional<Corporation> one = mars.findOne(query, Corporation.class, null);

        System.out.println(one);
    }

    @Test
    public void insertOne() {

       Corporation corporation = EntityGenerater.getCorporation(false);

        mars.insert(corporation);

    }

    @Test
    public void insertMany() {
        System.out.println("getTime: " +System.currentTimeMillis());
        mars.insert(corporations);
        System.out.println("endTime: " +System.currentTimeMillis());
    }

    @Test
    public void update() {
        Corporation corporation = EntityGenerater.getCorporation(false);
        corporation.setName("cName");

        //Query query = Query.query(Criteria.where("_id").is("JinMu"));

        Query query = Query.query(Criteria.where("a").is(100));

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        options.multi(true);

        UpdateResult result = mars.update(query, corporation, options, null);
        mars.update(query,corporation);

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

        UpdateResult result = mars.update(query, entity, Corporation.class, options, null);

        System.out.println(result);

    }

    @Test
    public void delete() {
        Query query = new Query();

        DeleteOptions options = new DeleteOptions();
        options.multi(true);


        DeleteResult result = mars.delete(query, Corporation.class, options, null);

        System.out.println(result);

    }

    @Test
    public void replace() {

        Corporation corporation =  EntityGenerater.getCorporation(false);
        corporation.setName("cName");

        ReplaceOptions replaceOptions = new ReplaceOptions();

        Query query = Query.query(Criteria.where("name").is("JinMu"));
        mars.replace(query, corporation, replaceOptions);
    }


    @Test
    public void deleteMany(){

        mars.delete(new Query(), Person.class,new DeleteOptions().multi(true));

    }


}
