package com.whaleal.mars.core.concern;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import org.junit.jupiter.api.Test;

public class ReadPreference {


//    private Mars mars = new Mars(Constant.connectionStrReplication);
    private Mars mars = new Mars(Constant.connectionStr);


    @Test
    void test(){
        System.out.println(111);
    }

    @Test
    void readQueryPreferencePrimary(){

        String id = "6397eda3f1845f0bed7c4e59";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadPreference(com.mongodb.ReadPreference.primary());

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadPreference());
        System.out.println(a);
    }


    @Test
    void readQueryPreferenceSecondary(){

        String id = "6397eda3f1845f0bed7c4e59";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadPreference(com.mongodb.ReadPreference.secondary());

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadPreference());
        System.out.println(a);
    }

    @Test
    void readQueryPreferenceNearest(){

        String id = "6397eda3f1845f0bed7c4e59";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadPreference(com.mongodb.ReadPreference.nearest());

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadPreference());
        System.out.println(a);
    }

    @Test
    void readQueryPreferencePrimaryPreferred(){

        String id = "6397eda3f1845f0bed7c4e59";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadPreference(com.mongodb.ReadPreference.primaryPreferred());

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadPreference());
        System.out.println(a);
    }

    @Test
    void readQueryPreferenceSecondaryPreferred(){

        String id = "6397eda3f1845f0bed7c4e59";
        Query query = new Query(new Criteria("_id").is(id));
        query.setReadPreference(com.mongodb.ReadPreference.secondaryPreferred());

        Animal a = mars.findOne(query,Animal.class).orElse(null);
        System.out.println(query.getReadPreference());
        System.out.println(a);
    }






    @Test
    void readPreferencePrimary(){

        String id = "6397eda3f1845f0bed7c4e59";


        mars.setReadPreference(com.mongodb.ReadPreference.primary());
        System.out.println(mars.getReadPreference());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }


    @Test
    void readPreferenceSecondary(){

        String id = "6397edee48d001034dc687eb";


        mars.setReadPreference(com.mongodb.ReadPreference.secondary());
        System.out.println(mars.getReadPreference());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readPreferenceNearest(){

        String id = "6397edee48d001034dc687eb";


        mars.setReadPreference(com.mongodb.ReadPreference.nearest());
        System.out.println(mars.getReadPreference());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readPreferencePrimaryPreferred(){

        String id = "6397edee48d001034dc687eb";


        mars.setReadPreference(com.mongodb.ReadPreference.primaryPreferred());
        System.out.println(mars.getReadPreference());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }

    @Test
    void readPreferenceSecondaryPreferred(){

        String id = "6397edee48d001034dc687eb";


        mars.setReadPreference(com.mongodb.ReadPreference.secondaryPreferred());
        System.out.println(mars.getReadPreference());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }


}
