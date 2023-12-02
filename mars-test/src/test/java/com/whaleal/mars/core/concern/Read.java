package com.whaleal.mars.core.concern;

import com.mongodb.ReadConcern;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import org.junit.jupiter.api.Test;

public class Read {


//    private Mars mars = new Mars(Constant.connectionStrReplication);
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    void test1(){

    }


    @Test
    void readQueryConcernMAJORITY(){

        String id = "6397ec51b36f393b3820fd47";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadConcern(ReadConcern.MAJORITY);

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadConcern());
        System.out.println(a);
    }

    @Test
    void readQueryConcernDEFAULT(){

        String id = "6397ec51b36f393b3820fd47";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadConcern(ReadConcern.DEFAULT);

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadConcern());
        System.out.println(a);
    }

    @Test
    void readQueryConcernSNAPSHOT(){

        String id = "6397ec51b36f393b3820fd47";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadConcern(ReadConcern.SNAPSHOT);

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadConcern());
        System.out.println(a);
    }

    @Test
    void readQueryConcernAVAILABLE(){

        String id = "6397ecd7f4fed14e29039f21";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadConcern(ReadConcern.AVAILABLE);

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadConcern());
        System.out.println(a);
    }

    @Test
    void readQueryConcernLINEARIZABLE(){

        String id = "6397ecd7f4fed14e29039f21";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadConcern(ReadConcern.LINEARIZABLE);

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadConcern());
        System.out.println(a);
    }

    @Test
    void readQueryConcernLOCAL(){

        String id = "6397ecd7f4fed14e29039f21";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadConcern(ReadConcern.LOCAL);

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadConcern());
        System.out.println(a);
    }





    @Test
    void readConcernMAJORITY(){

        String id = "6397ee20d1558e4034f60786";

        mars.setReadConcern(ReadConcern.MAJORITY);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernDEFAULT(){

        String id = "6397eee060d1f315ae56a483";

        mars.setReadConcern(ReadConcern.DEFAULT);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernSNAPSHOT(){

        String id = "6397eee060d1f315ae56a483";

        mars.setReadConcern(ReadConcern.SNAPSHOT);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernAVAILABLE(){

        String id = "6397ef498815db67a4ec5f17";

        mars.setReadConcern(ReadConcern.AVAILABLE);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernLINEARIZABLE(){

        String id = "6397ef498815db67a4ec5f17";

        mars.setReadConcern(ReadConcern.LINEARIZABLE);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readConcernLOCAL(){

        String id = "6397ef498815db67a4ec5f17";

        mars.setReadConcern(ReadConcern.LOCAL);
        System.out.println(mars.getReadConcern());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }



}
