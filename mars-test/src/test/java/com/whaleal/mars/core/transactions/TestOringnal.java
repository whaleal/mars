package com.whaleal.mars.core.transactions;

import com.mongodb.client.*;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.MarsSessionImpl;
import org.bson.Document;
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

    }


    /**
     * 必须使用session
     * 当没有 session  或者 session  不一致的时候没有因果一致
     * 且在方法内必须用session
     */

    @Test
    public void test(){


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

    }


    @Test
    public void test2(){
        MarsSessionImpl session = mars.startSession();

        MongoDatabase wh = mars.getMongoClient().getDatabase("wh");

        try {
            session.startTransaction();

            wh.getCollection("w1").insertOne(session ,new Document("a",100));
            wh.getCollection("w2").insertOne(session , new Document("_id",100));
            wh.getCollection("w2").insertOne(session ,new Document("_id",100));

            //session.insert(new Document("a",100),"w1");

            //session.insert(new Document("_id",100),"w2");
            //session.insert(new Document("_id",100),"w2");

        }catch (Exception  e){
            System.out.println("这里有异常 ");
            session.abortTransaction();
            return ;

        }finally {
           //
        }

        try {
            session.commitTransaction();
        }catch (Exception e){
            //
        }


        return ;

    }


}
