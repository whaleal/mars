package com.whaleal.mars.codecs.pojo;

import com.mongodb.client.MongoClients;
import com.whaleal.mars.bean.IdEntity;
import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.codecs.Codec;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import static com.whaleal.mars.Constant.connectionStr;

public class IdEntityTest {


    @Test
    public void testFieldName() {

        Field[] declaredFields = IdEntity.class.getDeclaredFields();

        for (Field field : declaredFields) {
            System.out.println(field.getName());
        }
    }


    @Test
    public void testGetterSetter() {
        PropertyReflectionUtil.PropertyMethods propertyMethods = PropertyReflectionUtil.getPropertyMethods(IdEntity.class);

        Collection< Method > getterMethods = propertyMethods.getGetterMethods();

        Collection< Method > setterMethods = propertyMethods.getSetterMethods();

        System.out.println(getterMethods);
        System.out.println(setterMethods);

        for (Method method : getterMethods) {
            System.out.println(PropertyReflectionUtil.toPropertyName(method));
        }

    }


    @Test
    public void testGeneratEntityModelBuilder() {

        EntityModelBuilder< IdEntity > idEntityEntityModelBuilder = new EntityModelBuilder<>(IdEntity.class);
        System.out.println(idEntityEntityModelBuilder);


        EntityModel< IdEntity > build = idEntityEntityModelBuilder.build();

        System.out.println(build);

    }


    @Test
    public void testMarsCodec() {

        MongoMappingContext context = new MongoMappingContext(MongoClients.create(connectionStr).getDatabase("mars"));

        Codec< IdEntity > idEntityCodec = context.getCodecRegistry().get(IdEntity.class);


        System.out.println(idEntityCodec);


    }
}
