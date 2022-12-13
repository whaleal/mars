package com.whaleal.mars.core.concern;

import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class Write {

    private Mars mars = new Mars(Constant.connectionStrReplication);




    @Test
    void dropCollection(){

        mars.dropCollection("chen");


    }


    @Test
    void writeConcernW1(){

        Animal animal = new Animal();
        animal.setId(new ObjectId().toString());
        animal.setAge(111);

        System.out.println("----------------------------------------------");
        System.out.println(animal.getId());
        System.out.println("----------------------------------------------");

        mars.setWriteConcern(WriteConcern.W1);
        System.out.println(mars.getWriteConcern());


        Animal insert = mars.insert(animal);
        System.out.println(insert);
    }

    @Test
    void writeConcernW2(){

        Animal animal = new Animal();
        animal.setId(new ObjectId().toString());
        animal.setAge(222);

        System.out.println("----------------------------------------------");
        System.out.println(animal.getId());
        System.out.println("----------------------------------------------");

        mars.setWriteConcern(WriteConcern.W2);
        System.out.println(mars.getWriteConcern());


        Animal insert = mars.insert(animal);
        System.out.println(insert);
    }

    @Test
    void writeConcernW3(){

        Animal animal = new Animal();
        animal.setId(new ObjectId().toString());
        animal.setAge(333);


        System.out.println("----------------------------------------------");
        System.out.println(animal.getId());
        System.out.println("----------------------------------------------");

        mars.setWriteConcern(WriteConcern.W3);
        System.out.println(mars.getWriteConcern());


        Animal insert = mars.insert(animal);
        System.out.println(insert);
    }

    @Test
    void writeConcernUNACKNOWLEDGED(){

        Animal animal = new Animal();
        animal.setId(new ObjectId().toString());
        animal.setAge(444);


        System.out.println("----------------------------------------------");
        System.out.println(animal.getId());
        System.out.println("----------------------------------------------");

        mars.setWriteConcern(WriteConcern.UNACKNOWLEDGED);
        System.out.println(mars.getWriteConcern());


        Animal insert = mars.insert(animal,"animal");
        System.out.println(insert);
    }


    @Test
    void writeConcernACKNOWLEDGED(){

        Animal animal = new Animal();
        animal.setId(new ObjectId().toString());
        animal.setAge(555);


        System.out.println("----------------------------------------------");
        System.out.println(animal.getId());
        System.out.println("----------------------------------------------");

        mars.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        System.out.println(mars.getWriteConcern());

        Animal insert = mars.insert(animal,"animal");
        System.out.println(insert);
    }


    @Test
    void writeConcernJOURNALED(){

        Animal animal = new Animal();
        animal.setId(new ObjectId().toString());
        animal.setAge(666);

        System.out.println("----------------------------------------------");
        System.out.println(animal.getId());
        System.out.println("----------------------------------------------");

        mars.setWriteConcern(WriteConcern.JOURNALED);
        System.out.println(mars.getWriteConcern());

        Animal insert = mars.insert(animal,"animal");
        System.out.println(insert);
    }

    @Test
    void writeConcernMAJORITY(){

        Animal animal = new Animal();
        animal.setId(new ObjectId().toString());
        animal.setAge(777);

        System.out.println("----------------------------------------------");
        System.out.println(animal.getId());
        System.out.println("----------------------------------------------");

        mars.setWriteConcern(WriteConcern.MAJORITY);
        System.out.println(mars.getWriteConcern());

        Animal insert = mars.insert(animal,"animal");
        System.out.println(insert);
    }

}
