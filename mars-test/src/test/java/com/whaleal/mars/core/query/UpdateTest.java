package com.whaleal.mars.core.query;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.codec.stage.EachStage;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:50
 */
public class UpdateTest {

    private MongoMappingContext context;

    private Update1 update1 = new Update1();

    @Before
    public void before(){
//        Mars mars = new Mars(Constant.connectionStr);
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());

    }

    @Test
    public void testForUpdateSet(){
//        Update1 update1 = new Update1();
        update1.set("size.uom","cm").set("status","P");
//        update1.currentTimestamp("lastModified");
        update1.currentDate("lastModified");

        Document document = context.toDocument(update1.getUpdateObject());
        Assert.assertEquals(document,Document.parse("   {\n" +
                "     $set: { \"size.uom\": \"cm\", status: \"P\" },\n" +
                "     $currentDate: { lastModified: true }\n" +
                "   }"));
//        mars.getDatabase("mars").getCollection("student").updateOne(new Document(),update1.getUpdateObject());

    }

    @Test
    public void testForUpdateNow(){
        Update1 update1 = new Update1();
        update1.set("test3",98);
//        update1.modifies("NOW");

        Document document = context.toDocument(update1);
        Assert.assertEquals(document,Document.parse("{ $set: { \"test3\": 98, modified: \"$$NOW\"} }"));
    }

    @Test
    public void testForPush(){
        update1.push("scores").each(90,92,85);

        Document document = context.toDocument(update1.getUpdateObject());
        Assert.assertEquals(document,Document.parse("{ $push: { scores: { $each: [ 90, 92, 85 ] } } }"));
    }

    @Test
    public void testForPushAll(){

        Document[] documents = new Document[]{new Document("wk",5).append("score",8),new Document("wk",6).append("score",7),new Document("wk",7).append("score",6)};

        update1.push("quizzes").each(documents);
        update1.push("quizzes").sort(Sort.on().descending("score"));
//        update1.push("quizzes").sort(new Sort());

        update1.push("quizzes").slice(3);
        //update1.push("quizzes").value("ccc");

        Document document1 = context.toDocument(update1.getUpdateObject());
        Assert.assertEquals(document1,Document.parse("{$push: {\n" +
                "       quizzes: {\n" +
                "          $each: [ { wk: 5, score: 8 }, { wk: 6, score: 7 }, { wk: 7, score: 6 } ],\n" +
                "          $sort: { score: -1 },\n" +
                "          $slice: 3\n" +
                "       }\n" +
                "     }}"));
    }



    @Test
    public void testForAddToSetEach(){
        Update1 update = new Update1();

        update.addToSet("phone").each("110","120","119");

        Document updateObject = update.getUpdateObject();

        Mars mars = new Mars(Constant.connectionStr);
        mars.getDatabase("mars").getCollection("student").updateOne(new Document(),updateObject);

    }

    @Test
    public void test2(){
        Update1 update1 = new Update1();
        Document name = update1.addToSet("name").each(1, 2, 3).getUpdateObject();
//        Document updateObject = new Update1().getUpdateObject();

        Mars mars = new Mars(Constant.connectionStr);

       // mars.update(new Query(),xx ,"cc",new UpdateOptions())

        mars.getDatabase().getCollection("cc").updateOne(new Document(),name);

    }

    @Test
    public void test3(){
        Mars mars = new Mars(Constant.connectionStr);

        Update1 update1 = new Update1();
        update1.push("addr").each("bj","hz");
        Document updateObject = update1.getUpdateObject();
        System.out.println(updateObject);

        MongoMappingContext context = new MongoMappingContext(mars.getDatabase());

        CodecRegistry codecRegistry = context.getCodecRegistry();
        mars.getDatabase("mars").withCodecRegistry(codecRegistry).getCollection("student").updateOne(new Document(), updateObject);

//        Document updateObject = update1.getUpdateObject();
//        System.out.println(updateObject);
//        MongoCollection<Document> student = mars.getDatabase().getCollection("student");
//        student.updateOne(new Document(),updateObject);





//        update1.push("name").each("lyz","yzl","zly");
//
//        update1.push("name").slice(2);
//
//        System.out.println(update1.getUpdateObject());
//        MongoCollection<Document> collection = mars.getDatabase("person").getCollection("student");
//        collection.updateOne(new Document(),update1.getUpdateObject());

    }

    @Test
    public void test4(){
        Mars mars = new Mars(Constant.connectionStr);

        Update1 update1 = new Update1();
        update1.push("addr").each("lyz","yzl","zly");

        update1.push("addr").atPosition(2);

        System.out.println(update1.getUpdateObject());

        MongoMappingContext context = new MongoMappingContext(mars.getDatabase());

        CodecRegistry codecRegistry = context.getCodecRegistry();
        mars.getDatabase().withCodecRegistry(codecRegistry).getCollection("student").updateOne(new Document(),update1.getUpdateObject());

    }

    @Test
    public void test(){
        MongoMappingContext context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());

        CodecRegistry codecRegistry = context.getCodecRegistry();
        Codec<EachStage> eachStageCodec = codecRegistry.get(EachStage.class);

        MongoClient mongoClient = MongoClients.create(Constant.connectionStr);
        mongoClient.getDatabase("cc").withCodecRegistry(codecRegistry).getCollection("cc").updateOne(new Document(),new Document("$push",new Document("key",new EachStage("1",2,"vv"))));


        System.out.println(eachStageCodec);
    }

    @Test
    public void testForSlice1(){
        Mars mars = new Mars(Constant.connectionStr);

        Update1 update1 = new Update1();
        update1.push("addr").each("zz","ly","kf");

        update1.push("addr").slice(2);

        System.out.println(update1.getUpdateObject());

//        MongoMappingContext context = new MongoMappingContext(mars.getDatabase());
//
//        CodecRegistry codecRegistry = context.getCodecRegistry();
//        mars.getDatabase().withCodecRegistry(codecRegistry).getCollection("student").updateOne(new Document(),update1.getUpdateObject());


       // mars.getDatabase("mars").getCollection("student").updateOne(new Document(),update1.getUpdateObject());
    }

    @Test
    public void testforSort(){
        Update1 update = new Update1();


        update.push("cc").each("22",33,44);

        update.push("cc").sort(Sort.on().ascending("score"));

        MongoMappingContext context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());

        Document document = context.toDocument(update.getUpdateObject());

        System.out.println(document);


    }

    @Test
    public void testforPush(){
        Update1 up = new Update1();
        up.push("cc").each("1",2,33,56);
        up.push("aa").each("12",13,15);
        up.push("cc").sort(Sort.on().ascending("name"));
        up.push("cc").slice(3);
        up.push("aa").slice(5);




        Document document = context.toDocument(up.getUpdateObject());
        System.out.println(document);
    }

}
