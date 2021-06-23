package com.whaleal.mars.session;

import com.whaleal.mars.bean.Corporation;
import com.whaleal.mars.bean.EntityGenerater;
import com.whaleal.mars.bean.IndexSome;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.ReplaceOptions;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.UpdateResult;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataStoreTest {


    Datastore datastore;

    @Before
    public void init() {
        ConnectionString connectionString = new ConnectionString(Constant.server100);
        MongoClient client = MongoClients.create(connectionString);
        datastore = new DatastoreImpl(client, connectionString.getDatabase());

    }

    public void getSession(){
        MarsSession marsSession = datastore.startSession();


    }


    @After
    public void cancel() {
        datastore = null;
    }


    @Test
    public void checkNotNull() {
        Assert.assertNotNull(datastore);

    }

    @Test
    public void insertOne() {


        Corporation corporation = EntityGenerater.getCorporation(false);

        datastore.insert(corporation);

    }

    @Test
    public void insertMany() {

        List<Corporation> corporations = EntityGenerater.getCorporation(true);

        datastore.insert(corporations);


    }


    @Test
    public void findAll() {

        Query query = new Query();


        QueryCursor<Corporation> result = datastore.findAll(query, Corporation.class, null);

        int count = 0;

        while (result.hasNext()) {
            System.out.println(result.next());
            count++;

            if (count > 1000) {
                break;
            }
        }

    }

    @Test
    public void findOne() {

        Query query = Query.query(Criteria.where("department.name").is("销售部"));
        Optional<Corporation> one = datastore.findOne(query, Corporation.class, null);

        Assert.assertNotNull(one);
    }


    @Test
    public void update() {
        Corporation corporation = null ; // BeanUtil.getCorporation(false);
        corporation.setName("cName");

        Query query = Query.query(Criteria.where("_id").is("JinMu"));

        UpdateOptions options = new UpdateOptions();
        options.multi(true);

        UpdateResult result = datastore.update(query, corporation, options, null);

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

        UpdateResult result = datastore.update(query, entity, Corporation.class, options, null);

        System.out.println(result);

    }

    @Test
    public void delete() {
        Query query = new Query();

        DeleteOptions options = new DeleteOptions();
        options.multi(false);


        DeleteResult result = datastore.delete(query, Corporation.class, options, null);

        System.out.println(result);

    }

    @Test
    public void replace() throws InterruptedException {

        Corporation corporation = EntityGenerater.getCorporation(false);
        corporation.setName("replace1");

        // 插入
        datastore.insert(corporation);
        Thread.sleep(1000);
        corporation.setName("repace&replace");

        ReplaceOptions replaceOptions = new ReplaceOptions();

        Query query = Query.query(Criteria.where("name").is("replace1"));


        // 查询,替换 后校验

        Optional<Corporation> one = datastore.findOne(query, Corporation.class, null);

        //  执行替换操作
        datastore.replace(query, corporation, replaceOptions);
        //  再次查询
        Query query2 = Query.query(Criteria.where("name").is("repace&replace"));

        Optional<Corporation> two = datastore.findOne(query2, Corporation.class, null);

        //Department departmentOne = one.get().getDepartment();

        //Department departmentTwo = two.get().getDepartment();


       /* Assert.assertEquals(departmentOne.getName(), (departmentTwo.getName()));


        Assert.assertEquals(departmentOne.getEmployees().size(), departmentTwo.getEmployees().size());
*/

    }


    /**
     * 测SAVE  方法
     */
    @Test
    public void save() {

        Corporation corporation = EntityGenerater.getCorporation(false);

        //corporation.set_id("600fb1868c2e1106f7192502");
        corporation.setName("testforSave");

        datastore.save(corporation);

    }


    /**
     * 索引相关
     */

    @Test
    public void getIndexes() {
        List<Index> corporationIndexes = datastore.getIndexes("corporation");

        Index index = corporationIndexes.get(0);
        Assert.assertEquals(index.getIndexKeys(), new Document("_id", 1));
    }

    @Test
    public void createIndex() {
        //  测试 读取 并根据读取的结果 再次创建 Indexes

        List<Index> addr = datastore.getIndexes("addr");

        System.out.println(addr);
        datastore.createIndexes(addr, "addr2");


    }


    @Test
    public void ensureIndexes() {

        datastore.ensureIndexes(IndexSome.class, "indexCollection");

    }


}
