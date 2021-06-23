package com.whaleal.mars;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.bson.codecs.pojo.annotations.Entity;
import org.junit.Test;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.bson.codecs.pojo.EntityModel;
import com.whaleal.mars.bson.codecs.pojo.annotations.CappedAt;

public class TestDocumentAn {


    @Test
    public void test(){

        MongoDatabase mars = MongoClients.create(Constant.server100).getDatabase("mars");
        MongoMappingContext context = new MongoMappingContext(mars);


        EntityModel entityModel = context.getEntityModel(Student.class);

        Entity annotation =(Entity) entityModel.getAnnotation(Entity.class);

        CappedAt cap = annotation.cap();

        long value = cap.value();

        System.out.println(value);

    }
}
