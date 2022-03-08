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
import org.junit.Test;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:50
 */
public class UpdateTest {

    @Test
    public void testForSlice(){
        Update1 update = new Update1();

        update.addToSet("key").each("1",2,3);
//        update.push("key").slice(3);
//
//        update.push("key").atPosition(1);

        Document updateObject = update.getUpdateObject();

        Mars mars = new Mars(Constant.connectionStr);

        // mars.update(new Query(),xx ,"cc",new UpdateOptions())

        mars.getDatabase().getCollection("cc").updateOne(new Document(),updateObject);
//        System.out.println(update.getUpdateObject());

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
        update1.push("name").each("lyz","yzl","zly");

        update1.push("name").atPosition(2);

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

}
