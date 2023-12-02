package com.whaleal.mars.core.transactions;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.MarsSessionImpl;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wh
 */
public class TestOringnal {



    ClientSession session ;
    MongoClient mongoClient ;
    Mars mars ;
    MarsSessionImpl marssession ;
    @Before
    public void init() {
        mars = new Mars(Constant.connectionStr);
        mongoClient = mars.getMongoClient();
        marssession = mars.startSession();
        session = mongoClient.startSession();

        try {
            mars.getDatabase().drop();
        }catch (Exception e){

        }




    }


    /**
     * 必须使用session
     * 当没有 session  或者 session  不一致的时候没有因果一致
     * 且在方法内必须用session
     *
     * 本方法 基于 Transaction  事务体来实现
     *
     *
     */

    @Test
    public void test1(){


        marssession.withTransaction((session1) -> {

            session1.startTransaction();
            session1.insert(new Document("a",100),"w1");

            session1.insert(new Document("_id",100),"w2");
            session1.insert(new Document("_id",100),"w2");

            if(true){
                session1.abortTransaction();
                throw  new IllegalArgumentException("xxx");

            }
            session1.commitTransaction();

            return new Object() ;

       });

        long w1 = mars.count(new Query(), "w1");
        long w2 = mars.count(new Query(), "w2");


        Assert.assertEquals(w1,0);
        Assert.assertEquals(w2,0);


    }


    @Test
    public void test2(){

        MarsSessionImpl session = mars.startSession();


        try {
            session.startTransaction();


            session.insert(new Document("a",100),"w1");

            session.insert(new Document("_id",100),"w2");
            session.insert(new Document("_id",100),"w2");

        }catch (Exception  e){
            System.out.println("这里有异常 ");
            session.abortTransaction();


        }finally {
           //
        }

        try {
            session.commitTransaction();
        }catch (Exception e){
            //
        }

        long w1 = mars.count(new Query(), "w1");
        long w2 = mars.count(new Query(), "w2");


        Assert.assertEquals(w1,0);
        Assert.assertEquals(w2,0);




    }


    /**
     * 基于原生的 同一个session  去 操作
     */
    @Test
    public void test3(){
        MarsSessionImpl session = mars.startSession();

        MongoDatabase database = mars.getDatabase();


        try {
            session.startTransaction();

            database.getCollection("w1").insertOne(session ,new Document("a",100));
            database.getCollection("w2").insertOne(session , new Document("_id",100));
            database.getCollection("w2").insertOne(session ,new Document("_id",100));



        }catch (Exception  e){
            System.out.println("这里有异常 ");
            session.abortTransaction();

        }finally {
            //
        }

        try {
            session.commitTransaction();
        }catch (Exception e){
            //
        }


        long w1 = mars.count(new Query(), "w1");
        long w2 = mars.count(new Query(), "w2");


        Assert.assertEquals(w1,0);
        Assert.assertEquals(w2,0);

    }


}
