package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.ConstructorModule;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.EntityModel;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * @author wh
 */
public class ConstructorModuleTest {


    private Mars mars ;
    @Before
    public void init(){

        mars = new Mars(Constant.connectionStr);

    }

    @Test
    public void test001(){

        ConstructorModule module  =new ConstructorModule("lyz",18);
        ConstructorModule module1  =new ConstructorModule("lyz",18);

        mars.insert(module ,"module");
        mars.insert(module1,"module");


//        QueryCursor< ConstructorModule > mm = mars.findAll(new Query(), ConstructorModule.class, "module");

//        while (mm.hasNext()){
//            System.out.println(mm.next().toString());
//        }


    }



    @Test
    public void test002(){

        MongoMappingContext mapper = mars.getMapper();

        EntityModel entityModel = mapper.getEntityModel(ConstructorModule.class);

        System.out.println(entityModel);

    }


    public static void main( String[] args ) {
        Constructor< ? >[] constructors = ConstructorModule.class.getConstructors();

        for (Constructor<?> constructor :constructors){
            Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            for(Annotation[] parameterAnnotation :parameterAnnotations){
                for (Annotation annotation : parameterAnnotation) {
                    System.out.println(annotation.annotationType());
                }
            }
        }
    }


}
