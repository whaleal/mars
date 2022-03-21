package com.whaleal.mars.core.query;

import com.mongodb.client.MongoCollection;
import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.QueryCursor;
import com.whaleal.mars.session.option.UpdateOptions;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.GreaterThan;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @user Lyz
 * @description
 * @date 2022/3/11 10:57
 */
public class TestUse {

    private Mars mars;

    private MongoMappingContext context;

    @Before
    public void before(){
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());
        mars = new Mars(Constant.connectionStr);
    }

    @Test
    public void test1(){

        ArrayList<Document> documents = new ArrayList<>();
        Document document = new Document("name", "lyz1");
        Document document1 = new Document("name", "zyl");
        Document document2 = new Document("name", "zly");
        documents.add(document);
        documents.add(document1);
        documents.add(document2);

//        InsertManyResult insert = mars.insert(documents,"student");

        Document append = new Document("h", 28).append("w", 35.5).append("uom", "cm");
        Object[] objects = {"cotton"};
        Document append1 = new Document("item", "canvas").append("qty", 100).append("tags", objects).append("size", append);

//        mars.insert(append1,"inventory");


        Document[] documents1 = new Document[3];
        Object[] objects1 = {"blank","red"};
        append = new Document("h", 14).append("w", 21).append("uom", "cm");
        documents1[0]  = new Document("item", "journal").append("qty", 25).append("tags", objects1).append("size", append);
        documents1[1]  = new Document("item", "mat").append("qty", 85).append("tags", objects1).append("size", append);
        documents1[2]  = new Document("item", "mousepad").append("qty", 25).append("tags", objects1).append("size", append);

        mars.insert(Arrays.asList(documents1),"inventory");
//        mars.getDatabase("mars").getCollection("inventory").insertMany(Arrays.asList(documents1));


    }

    @Test
    public void test2(){
        Criteria criteria = new Criteria("size").is(new Document("h", 14).append("w", 21).append("uom", "cm"));

        Query query = new Query().addCriteria(criteria);
        Optional<Inventory> inventory = mars.findOne(query, Inventory.class, "inventory");
        System.out.println(inventory.get());

        Criteria criteria1 = Criteria.where("size.uom").is("cm");
        Query query1 = new Query(criteria1);
        Optional<Inventory> one = mars.findOne(query1, Inventory.class);
        System.out.println(one.orElse(null));

        QueryCursor<Inventory> all = mars.findAll(query1, Inventory.class);
        while (all.hasNext()){
            System.out.println(all.next());
        }

        Criteria criteria2 = Criteria.where("size.h").lt(15).and("size.uom").is("cm");
        Query query2 = new Query(criteria2);
        System.out.println(query2.getQueryObject());
        QueryCursor<Inventory> all1 = mars.findAll(query2, Inventory.class);
        while (all1.hasNext()){
            System.out.println(all1.next());
        }






    }

    @Test
    public void testForArrayFilter(){
        Update update = new Update();
        update.inc("grades.$[elem].std",-1);
        update.filterArray(Criteria.where("elem.grade").gte(80).and("elem.std").gte(5));

        Query query = new Query();
        UpdateOptions updateOptions = new UpdateOptions();

//        Document $gte = new Document("elem.grad", new Document("$gte", 80));
//        Document $gte1 = new Document("elem.std", new Document("$gte", 5));
//        List<Document> docu = new ArrayList<>();
//        docu.add($gte);
//        docu.add($gte1);

//        updateOptions.arrayFilters(docu);
//        mars.update(query,update,updateOptions);
        mars.update(query,update,"student3");

//        for (UpdateDefinition.ArrayFilter arrayFilter : update.getArrayFilters()) {
//            System.out.println(context.toDocument(arrayFilter));
//        }

//        System.out.println(arrayFilters);

    }

    @Test
    public void test3(){
        Update update = new Update();
        update.inc("grades.$[elem].std",-1);
        Criteria gte = Criteria.where("elem.grade").gte(80).and("elem.std").gte(5);

        System.out.println(gte.getCriteriaObject());
        update.filterArray(gte);

        List<UpdateDefinition.ArrayFilter> arrayFilters = update.getArrayFilters();


        for (int i = 0; i < arrayFilters.size(); i++) {
            System.out.println(context.toDocument(arrayFilters.get(i).toData()));
        }

        Document updateObject = update.getUpdateObject();
        System.out.println(updateObject);
//        System.out.println(context.toDocument(arrayFilters));
    }
}