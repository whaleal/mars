package com.whaleal.mars.issues;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.Constant;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wh
 */
public class TestInsertList {

    public static void main( String[] args ) {


    }




    @Test
    public void  testList(){

        Document document = new Document();

        List<Object> data = new ArrayList<Object>();

        data.add("testStr");
        data.add(18L);
        data.add(new Document("key",null));
        data.add(19.00D);
        data.add(null);

        document.put("data",data);




        MongoClient mongoClient = MongoClients.create(Constant.connectionStr);

        mongoClient.getDatabase("wh").getCollection("testList").insertOne(document);

    }



    @Test
    public void testArray(){

        Document document = new Document();

        Object[]  data = new Object[8];
        data[0] = "testStr" ;
        data[1] = 18L ;
        data[2] = new Document("key",null);
        data[3] = 19.00D ;
        data[4] = null ;


        document.put("data",data);




        MongoClient mongoClient = MongoClients.create(Constant.connectionStr);

        mongoClient.getDatabase("wh").getCollection("testList").insertOne(document);

    }
}
