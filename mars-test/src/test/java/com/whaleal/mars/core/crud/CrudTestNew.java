package com.whaleal.mars.core.crud;

import com.mongodb.ReadPreference;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.*;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.*;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.InsertManyOptions;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.util.CreateDataUtil;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-24 13:04
 **/
public class CrudTestNew {

    private Mars mars = new Mars(Constant.connectionStr);
    List<Num> nums = new ArrayList<>();


    @BeforeMethod
    public void createData(){
        for (int i = 0;i < 20; i++){
            Num num = new Num();
            num.setName(i + "hao");
            num.setNum(i);
            nums.add(num);
        }

        mars.insertAll(nums);



        List<Document> zip = CreateDataUtil.parseString("{ \"_id\" : \"01001\", \"city\" : \"AGAWAM\", \"loc\" : [ -72.622739, 42.070206 ], \"pop\" : 15338, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01002\", \"city\" : \"CUSHMAN\", \"loc\" : [ -72.51564999999999, 42.377017 ], \"pop\" : 36963, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01005\", \"city\" : \"BARRE\", \"loc\" : [ -72.10835400000001, 42.409698 ], \"pop\" : 4546, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01007\", \"city\" : \"BELCHERTOWN\", \"loc\" : [ -72.41095300000001, 42.275103 ], \"pop\" : 10579, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01008\", \"city\" : \"BLANDFORD\", \"loc\" : [ -72.936114, 42.182949 ], \"pop\" : 1240, \"state\" : \"MA\" }\n" +
                "{ \"_id\" : \"01010\", \"city\" : \"BRIMFIELD\", \"loc\" : [ -72.188455, 42.116543 ], \"pop\" : 3706, \"state\" : \"MA\" }");

        mars.insert(zip,"zip");

        List list = new ArrayList<>();
        for(int i =0; i < 10;i++){
            Vehicles vehicles = new Vehicles();
            vehicles.setId(i);
            vehicles.setType("a");
            Spces spec = new Spces();
            spec.setDoors(4);
            spec.setWheels(4);
            vehicles.setSpecs(spec);
            list.add(vehicles);
        }
        mars.insert(list,Vehicles.class);
        mars.insert(list,"vehicle");
    }


    @AfterMethod
    public void drop(){
        mars.dropCollection("zip");
        mars.dropCollection("cc");
        mars.dropCollection(Num.class);
        mars.dropCollection(Vehicles.class);
        mars.dropCollection("vehicle");
        mars.dropCollection(Articles.class);
        mars.dropCollection("articles1");
        mars.dropCollection("articles_chen");
    }

    @Test
    public void testForInsertOne(){
        Articles articles = new Articles(100,"ceshi","lyz",5);
        Articles insert = mars.insert(articles);
        System.out.println("==========================");
        System.out.println(insert);

        Articles articles1 = mars.insert(articles, "articles1");
        System.out.println(articles1);
        System.out.println("==========================");


        Articles articles2 = mars.findAll(Articles.class).tryNext();
        Assert.assertEquals(insert,articles2);

        Object articles11 = mars.findAll(null, "articles1").tryNext();
        Assert.assertEquals(articles1,articles11);
    }

    @Test
    public void testForInsertMany(){

        ArrayList<Object> objects = new ArrayList<>();
        for(int i = 0; i < 3 ; i++ ){

            Articles articles = new Articles(i,"ceshi","lyz",5);
            objects.add(articles);
        }

        ArrayList<Object> objects1 = new ArrayList<>();
        for(int i = 3; i < 6 ; i++ ){

            Articles articles = new Articles(i,"ceshi","lyz",5);
            objects1.add(articles);
        }
        Collection<Object> insert = mars.insert(objects, Articles.class);
        for (Object o : insert){
            System.out.println(o);
        }
        Assert.assertEquals(insert,objects);

        Collection<Object> insert1 = mars.insert(objects1, Articles.class,new InsertManyOptions());
        for (Object o : insert1){
            System.out.println(o);
        }
        Assert.assertEquals(insert1,objects);

        Collection<Object> articles1 = mars.insert(objects, "articles1");
        for (Object o : articles1) {
            System.out.println(o);
        }
        Assert.assertEquals(articles1,objects);

        Collection<Object> articles11 = mars.insert(objects1, "articles1",new InsertManyOptions());
        for (Object o : articles11) {
            System.out.println(o);
        }
        Assert.assertEquals(articles11,objects);

    }

    @Test
    public void testForInsertAll(){
        ArrayList<Object> objects = new ArrayList<>();
        for(int i = 0; i < 3 ; i++ ){

            Articles articles = new Articles(i,"ceshi","lyz",5);
            objects.add(articles);
        }
        for(int i = 0; i < 3 ; i++ ){
            Parent parent = new Parent();
            parent.setAge(40);
            parent.setName(i + "");
            objects.add(parent);
        }

        Collection<Object> objects1 = mars.insertAll(objects);
        for (Object o : objects1){
            System.out.println(o);
        }
        Assert.assertEquals(objects,objects1);
    }

    @Test
    public void testForSave(){

        Articles articles = new Articles(1001,"test","chen",6);
        Articles save = mars.save(articles);
        System.out.println(save);

        Articles articles_chen = mars.save(articles, "articles_chen");
        System.out.println(articles_chen);

        Articles tryNext = mars.findAll(Articles.class).tryNext();
        Assert.assertEquals(save,tryNext);

        Object next = mars.findAll(null, "articles_chen").tryNext();
        Assert.assertEquals(articles_chen,next);

    }

    @Test
    public void testForSaveMany(){

        ArrayList<Object> objects = new ArrayList<>();
        for(int i = 0; i < 3 ; i++ ){

            Articles articles = new Articles(i,"ceshi","chen",5);
            objects.add(articles);
        }

        List<Object> save = mars.save(objects);
        for (Object o : save){
            System.out.println(o);
        }
        Assert.assertEquals(objects,save);

        List<Object> cc = mars.save(objects,"cc");
        for (Object o : cc){
            System.out.println(o);
        }
        Assert.assertEquals(objects,cc);

    }

    @Test
    public void testForFindAll() {
        QueryCursor<Num> all = mars.findAll(Num.class);
        List<Num> nums1 = mars.findAll(Num.class).toList();

        while (all.hasNext()) {
            System.out.println(all.next());
        }

        System.out.println("************");

//        Assert.assertEquals(nums1,nums);

        QueryCursor<Object> num = mars.findAll(null, "num");
        while (num.hasNext()) {
            System.out.println(num.next());
        }

    }

    @Test
    public void testForFindByClass(){
        Criteria num1 = Criteria.where("num").gte(10);
        Query query = Query.query(num1);
        query.with(Sort.descending("num"));

        System.out.println("-------------------");
        QueryCursor<Num> numQueryCursor = mars.find(query, Num.class);
        while (numQueryCursor.hasNext()) {
            System.out.println(numQueryCursor.next());
        }
        System.out.println("-------------------");

        Optional<Num> one = mars.findOne(query, Num.class);
        System.out.println(one.get());

    }

    @Test
    public void testForFindByName(){
        Criteria num2 = Criteria.where("num").lt(10);
        Query query = Query.query(num2);
        query.setReadPreference(ReadPreference.nearest());
        QueryCursor<Document> num = mars.find(Query.query(num2), Document.class, "num");

        while (num.hasNext()){
            System.out.println(num.next());
        }

        Optional<Document> one = mars.findOne(Query.query(num2), Document.class,"num");
        System.out.println(one.get());
    }

    @Test
    public void testForProjection(){
        Criteria num = Criteria.where("num").lt(10);

        Query query = Query.query(num);

//        Projection projection = new Projection();
//        query.withProjection(projection.include("num").exclude("_id"));

        Projection projection = new Projection();
        query.withProjection(projection.include("name","_id"));

        QueryCursor<Document> num1 = mars.find(query, Document.class, "num");

        while (num1.hasNext()){
            System.out.println(num1.next());
        }

    }

    @Test
    public void testForExtendProjection(){
        Criteria criteria = Criteria.where("loc").is(-72.622739D);

        Query query = Query.query(criteria);

        //positional projection cannot be used with exclusion
//        query.withProjection(Projection.projection().exclude("loc.$"));

        query.withProjection(Projection.projection().include("loc.$").include("state"));

        QueryCursor<Document> zip = mars.find(query, Document.class, "zip");
        while (zip.hasNext()){
            System.out.println(zip.next());
        }
    }

    @Test
    public void testForInnerClassProjection(){
        Criteria criteria = new Criteria().and("specs.doors").is(4);

        Query query = Query.query(criteria);
        QueryCursor<Vehicles> all = mars.findAll(Vehicles.class);
        while (all.hasNext()){
            System.out.println(all.next());
        }

        query.withProjection(Projection.projection().include("type").include("spces.doors"));
        QueryCursor<Vehicles> vehiclesQueryCursor = mars.find(query, Vehicles.class);
        while (vehiclesQueryCursor.hasNext()){
            System.out.println(vehiclesQueryCursor.next());
        }


    }

    @Test
    public void deleteQueryEntity(){

        DeleteResult delete = mars.delete(new Query(new Criteria("_id").is(1)),Vehicles.class);
        Assert.assertEquals(delete.getDeletedCount(),1);
    }

    @Test
    public void deleteQueryCollection(){

        DeleteResult delete = mars.delete(new Query(new Criteria("_id").is("01002")),"zip");

        Assert.assertEquals(delete.getDeletedCount(),1);
    }

    @Test
    public void testForDeleteQueryCollection(){


        DeleteResult delete = mars.delete(new Query(new Criteria("_id").is(6)),Vehicles.class,"vehicle");

        Assert.assertEquals(delete.getDeletedCount(),1);
    }

    @Test
    public void testForDeleteObject(){
        Vehicles vehicles = new Vehicles();
        vehicles.setId(2);

        DeleteResult delete = mars.delete(vehicles);
        Assert.assertEquals(delete.getDeletedCount(),1);
    }

    @Test
    public void testForDeleteCollection(){
        Num num = new Num();
        num.setId("01005");

        DeleteResult delete = mars.delete(num,"zip");

        Assert.assertEquals(delete.getDeletedCount(),1);
    }

    @Test
    public void testForDeleteMultiWithOutClass(){

        DeleteResult deleteResult = mars.deleteMulti(Query.query(Criteria.where("state").is("MA")), "zip");

        Assert.assertEquals(deleteResult.getDeletedCount(),6);
    }

    @Test
    public void testForDeleteMultiWithClass(){

        DeleteResult deleteResult = mars.deleteMulti(Query.query(Criteria.where("_id").is(6)), Vehicles.class);

        Assert.assertEquals(deleteResult.getDeletedCount(),1);
    }

    @Test
    public void testForDeleteMultiWithClassAndName(){

        DeleteResult deleteResult = mars.deleteMulti(Query.query(Criteria.where("_id").is(6)), Vehicles.class,"vehicle");

        Assert.assertEquals(deleteResult.getDeletedCount(),1);
    }

    @Test
    public void testForUpdateMultiWithClass(){

        Update update = new Update();
        update.set("type","b");

        mars.updateMulti(Query.query(new Criteria("_id").is(6)), update, Vehicles.class);
        Vehicles vehicles = mars.findOne(Query.query(new Criteria("_id").is(6)), Vehicles.class).orElse(null);

        Assert.assertEquals(vehicles.getType(),"b");
    }

    @Test
    public void testForUpdateMultiWithCollection(){

        Update update = new Update();
        update.set("type","c");

        mars.updateMulti(Query.query(new Criteria("_id").is(6)), update, "vehicle");
        Vehicles vehicles = mars.findOne(Query.query(new Criteria("_id").is(6)), Vehicles.class,"vehicle").orElse(null);

        Assert.assertEquals(vehicles.getType(),"c");
    }

    @Test
    public void testForUpdateMultiWithClassAndCollection(){

        Update update = new Update();
        update.set("type","b");

        mars.updateMulti(Query.query(new Criteria("_id").is(6)), update, Vehicles.class,"vehicle");
        Vehicles vehicles = mars.findOne(Query.query(new Criteria("_id").is(6)), Vehicles.class,"vehicle").orElse(null);

        Assert.assertEquals(vehicles.getType(),"b");
    }

    @Test
    public void testForUpdateEntity(){

        Vehicles vehicles = new Vehicles();
        vehicles.setId(6);
        vehicles.setType("999");

        mars.updateEntity(Query.query(new Criteria("_id").is(6)), vehicles,new UpdateOptions(),"vehicle");
        Vehicles result = mars.findOne(Query.query(new Criteria("_id").is(6)), Vehicles.class,"vehicle").orElse(null);

        Assert.assertEquals(result.getType(),"999");
    }

    @Test
    public void testForUpdateFirstWithClass(){

        Update update = new Update();
        update.set("type","b");

        mars.updateFirst(Query.query(new Criteria("_id").is(6)), update, Vehicles.class);
        Vehicles vehicles = mars.findOne(Query.query(new Criteria("_id").is(6)), Vehicles.class).orElse(null);

        Assert.assertEquals(vehicles.getType(),"b");
    }

    @Test
    public void testForUpdateFirstWithCollection(){

        Update update = new Update();
        update.set("type","c");

        mars.updateFirst(Query.query(new Criteria("_id").is(6)), update, "vehicle");
        Vehicles vehicles = mars.findOne(Query.query(new Criteria("_id").is(6)), Vehicles.class,"vehicle").orElse(null);

        Assert.assertEquals(vehicles.getType(),"c");
    }

    @Test
    public void testForUpdateFirstWithClassAndCollection(){

        Update update = new Update();
        update.set("type","b");

        mars.updateFirst(Query.query(new Criteria("_id").is(6)), update, Vehicles.class,"vehicle");
        Vehicles vehicles = mars.findOne(Query.query(new Criteria("_id").is(6)), Vehicles.class,"vehicle").orElse(null);

        Assert.assertEquals(vehicles.getType(),"b");
    }


}


