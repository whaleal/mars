package com.whaleal.mars.core.concern;

import com.mongodb.ReadConcern;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import org.junit.jupiter.api.Test;

public class ReadPreference {


    private Mars mars = new Mars(Constant.connectionStrReplication);

    @Test
    void test(){
        System.out.println(111);
    }

    @Test
    void readPreferencePrimary(){

        String id = "6394874af298787ac9aa4185";


        mars.setReadPreference(com.mongodb.ReadPreference.primary());
        System.out.println(mars.getReadPreference());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }


    @Test
    void readPreferenceSecondary(){

        String id = "639487e60e24a67b004afd14";


        mars.setReadPreference(com.mongodb.ReadPreference.secondary());
        System.out.println(mars.getReadPreference());

        Animal a = mars.findOne(Query.query(new Criteria("_id").is(id)),Animal.class).orElse(null);

        System.out.println(a);
    }


}
