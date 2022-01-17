package com.whaleal.mars.core;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import com.whaleal.mars.Constant;
import org.bson.BasicBSONObject;
import org.bson.Document;

public class TestSession {

    public static void main(String[] args) {
        MongoClient client = MongoClients.create(Constant.connectingStr);

        client.getDatabase("wh").getCollection("wg").insertOne(new Document());

        MongoCollection<Document> collection = client.getDatabase("wh").getCollection("bs");

        BasicBSONObject bs = new BasicBSONObject("a", 10);
        collection.insertOne(new Document("outter", bs));

        new Document();

        //new DBObject();

        DBObject basicDBObject = new BasicDBObject();

        //collection.insertOne(basicDBObject);


        Class<? extends DBObject> aClass = basicDBObject.getClass();


    }
}
