package com.whaleal.mars.codecs.pojo;

import com.mongodb.client.MongoClients;
import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.codecs.Codec;
import org.junit.Test;
import com.whaleal.mars.bean.IdEntity;

import java.lang.reflect.Field;

import static com.whaleal.mars.Constant.connectingStr;

public class IdEntityTest {


    @Test
    public void testFieldName(){

        Field[] declaredFields = IdEntity.class.getDeclaredFields();

        for(Field field :declaredFields){
            System.out.println(field.getName());
        }
    }


    @Test
    public void testGetterSetter(){
        PropertyReflectionUtils.PropertyMethods propertyMethods = PropertyReflectionUtils.getPropertyMethods(IdEntity.class);


        System.out.println(propertyMethods);

    }


    @Test
    public void testGeneratEntityModelBuilder(){

        EntityModelBuilder<IdEntity> idEntityEntityModelBuilder = new EntityModelBuilder<>(IdEntity.class);
        System.out.println(idEntityEntityModelBuilder);


        EntityModel<IdEntity> build = idEntityEntityModelBuilder.build();

        System.out.println(build);

    }



    @Test
    public void testMarsCodec(){

        MongoMappingContext context = new MongoMappingContext(MongoClients.create(connectingStr).getDatabase("mars"));

        Codec<IdEntity> idEntityCodec = context.getCodecRegistry().get(IdEntity.class);


        System.out.println(idEntityCodec);


    }
}
