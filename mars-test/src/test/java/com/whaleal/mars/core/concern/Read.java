package com.whaleal.mars.core.concern;

import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public class Read {


    private Mars mars = new Mars(Constant.connectionStrReplication);

    @Test
    void test1(){

    }


    @Test
    void readConcernMAJORITY(){

        String id = "639480ca052dc0638c9855a9";

        mars.setReadConcern(ReadConcern.MAJORITY);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernDEFAULT(){

        String id = "6394814a0ee0005c5c340b0f";

        mars.setReadConcern(ReadConcern.DEFAULT);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernSNAPSHOT(){

        String id = "639481de07e2fb33fa5050d7";

        mars.setReadConcern(ReadConcern.SNAPSHOT);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernAVAILABLE(){

        String id = "6394824b15d77f52588cff16";

        mars.setReadConcern(ReadConcern.AVAILABLE);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }



    @Test
    void readConcernLINEARIZABLE(){

        String id = "639482c4bad73f176ebdf001";

        mars.setReadConcern(ReadConcern.LINEARIZABLE);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernLOCAL(){

        String id = "6394837ddbe6633a217087cd";

        mars.setReadConcern(ReadConcern.LOCAL);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

}
