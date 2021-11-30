package com.whaleal.mars.bson.codecs.pojo;

import com.whaleal.mars.bean.Car;
import com.whaleal.mars.bean.Person;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.bson.codecs.RepresentationConfigurable;
import org.bson.BsonType;
import org.bson.codecs.Codec;

import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Precondition;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bson.codecs.internal.ValueCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class EntityBuilderTest {

    MongoMappingContext context ;

    @Before
    public void init(){
        MongoClient  client = MongoClients.create(Constant.server100);
        context = new MongoMappingContext(client.getDatabase("mars"));
        Precondition.PreconditionNotNull(context);
    }


    @Test
    public void checkRegister(){
        CodecRegistry codecRegistry = context.getCodecRegistry();

        Codec<Car> marsCodec = codecRegistry.get(Car.class);

        Precondition.PreconditionNotNull(marsCodec);

        System.out.println(marsCodec);


        Codec<String> stringCodec = codecRegistry.get(String.class);

        System.out.println(stringCodec);

        if(stringCodec instanceof RepresentationConfigurable){
            ((RepresentationConfigurable) stringCodec).withRepresentation(BsonType.OBJECT_ID);

            System.out.println("isOk");
        }

        Codec<Student> corporationCodec = codecRegistry.get(Student.class);

        System.out.println(corporationCodec);
    }


    @Test
    public void checkPersonCodec(){
       EntityModel model = new EntityModelBuilder(Person.class).build() ;
        PropertyModel idProperty = model.getIdProperty();

        System.out.println(idProperty);

        PropertyModel firstname = model.getPropertyModel("firstName");

        System.out.println(firstname);

    }



    @Test
    public void checkString(){
        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();

        Codec<String> stringCodec = defaultCodecRegistry.get(String.class);

        Precondition.PreconditionNotNull(stringCodec);

        CodecRegistry  marCodec   = fromProviders(
               new ValueCodecProvider()
        );

        Codec<String> stringCodec1 = marCodec.get(String.class);

        Precondition.PreconditionNotNull(stringCodec1);

        System.out.println(stringCodec1);

    }



    @Test
    public void testDouble(){


        CodecRegistry  marCodec   = fromProviders(
                new ValueCodecProvider()
        );

        Codec<Double> doubleCodec = marCodec.get(Double.class);

        Precondition.PreconditionNotNull(doubleCodec);

        System.out.println(doubleCodec);


    }



    @Test
    public void testEntityBuilder(){

        EntityModel build = new EntityModelBuilder(Car.class).build();

        Precondition.PreconditionNotNull(build);

        System.out.println(build);
    }



    @Test
    public void checkCar(){
        CodecRegistry codecRegistry = context.getCodecRegistry();

        Codec<Car> marsCodec = codecRegistry.get(Car.class);

        Precondition.PreconditionNotNull(marsCodec);


        System.out.println(marsCodec);
    }


    @Test
    public void checkDefault(){
        CodecRegistry codecRegistry = context.getCodecRegistry();

        Codec<String> marsCodec = codecRegistry.get(String.class);

        Precondition.PreconditionNotNull(marsCodec);


        System.out.println(marsCodec);
    }


    @Test
    public void checkPersonEntity(){


        EntityModel<Person> build = new EntityModelBuilder<>(Person.class).build();

        PropertyModel<?> idProperty = build.getIdProperty();

        System.out.println(idProperty);


    }







}
