package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * @author lyz
 * @desc
 * @create: 2022-08-01 18:14
 **/
public class TestForPropIgnore {
    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void testFor(){
        Animal animal = new Animal();
        animal.setAge(24);
        animal.setHello("no");
        animal.setId(new ObjectId().toString());
        animal.setIsAlive(true);

        mars.insert(animal);
    }

    @Test
    public void testForQuery(){
        Query query = new Query(new Criteria());

        Animal animal = mars.findAll(query, Animal.class).tryNext();
        System.out.println(animal);
    }
}
