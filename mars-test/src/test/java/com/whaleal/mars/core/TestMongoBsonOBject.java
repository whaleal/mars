package com.whaleal.mars.core;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.junit.Precondition;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

/**
 * 用于测试MongoDB  默认的 的基本类型
 * BasicDBObject
 * BasicDBList
 * <p>
 * 并测试类型的 通用 父类型
 */
public class TestMongoBsonOBject {

    @Test
    public void testMongoObject() {
        BasicDBObject db = new BasicDBObject();
        String name = db.getClass().getName();
        System.out.println(name);

    }

    @Test
    public void testMongoList() {
        BasicDBList list = new BasicDBList();
        String name = list.getClass().getName();
        System.out.println(name);

    }


    @Test
    public void testMongoObjectAssign() {
        BasicDBObject db = new BasicDBObject();


        Precondition.PreconditionTrue(Map.class.isAssignableFrom(db.getClass()));
        Precondition.PreconditionFalse(Collection.class.isAssignableFrom(db.getClass()));

    }

    @Test
    public void testMongoListAssign() {
        BasicDBList list = new BasicDBList();


        Precondition.PreconditionFalse(Map.class.isAssignableFrom(list.getClass()));
        Precondition.PreconditionTrue(Collection.class.isAssignableFrom(list.getClass()));


    }


}
