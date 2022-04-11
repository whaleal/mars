package com.whaleal.mars.codecs.pojo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Car;
import com.whaleal.mars.bean.Person;
import com.whaleal.mars.bean.Student;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.RepresentationConfigurable;
import com.whaleal.mars.codecs.internal.ValueCodecProvider;
import org.bson.BsonType;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class EntityBuilderTest {

    MongoMappingContext context;

    @BeforeMethod
    public void init() {
        MongoClient client = MongoClients.create(Constant.connectionStr);
        context = new MongoMappingContext(client.getDatabase("mars"));
        Assert.assertNotNull(context);
    }


    @Test
    public void test01() {
        EntityModelBuilder< Person > personEntityModelBuilder = new EntityModelBuilder<>(Person.class);

        String idPropertyName = personEntityModelBuilder.getIdPropertyName();
        System.out.println(idPropertyName);
        EntityModel< Person > build = personEntityModelBuilder.build();

        System.out.println(build.getIdProperty());

    }


    @Test
    public void checkRegister() {
        CodecRegistry codecRegistry = context.getCodecRegistry();

        Codec< Car > marsCodec = codecRegistry.get(Car.class);

        Assert.assertNotNull(marsCodec);

        System.out.println(marsCodec);


        Codec< String > stringCodec = codecRegistry.get(String.class);

        System.out.println(stringCodec);

        if (stringCodec instanceof RepresentationConfigurable) {
            ((RepresentationConfigurable) stringCodec).withRepresentation(BsonType.OBJECT_ID);

            System.out.println("isOk");
        }

        Codec< Student > corporationCodec = codecRegistry.get(Student.class);

        System.out.println(corporationCodec);
    }


    @Test
    public void checkPersonCodec() {
        EntityModel model = new EntityModelBuilder(Person.class).build();
        PropertyModel idProperty = model.getIdProperty();

        System.out.println(idProperty);

        PropertyModel firstname = model.getPropertyModel("firstName");

        System.out.println(firstname);

    }


    @Test
    public void checkString() {
        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();

        Codec< String > stringCodec = defaultCodecRegistry.get(String.class);

        Assert.assertNotNull(stringCodec);

        CodecRegistry marCodec = CodecRegistries.fromProviders(
                new ValueCodecProvider()
        );

        Codec< String > stringCodec1 = marCodec.get(String.class);

        Assert.assertNotNull(stringCodec1);

        System.out.println(stringCodec1);

    }


    @Test
    public void testDouble() {


        CodecRegistry marCodec = fromProviders(
                new ValueCodecProvider()
        );

        Codec< Double > doubleCodec = marCodec.get(Double.class);

        Assert.assertNotNull(doubleCodec);

        System.out.println(doubleCodec);


    }


    @Test
    public void testEntityBuilder() {

        EntityModel build = new EntityModelBuilder(Car.class).build();

        Assert.assertNotNull(build);

        System.out.println(build);
    }


    @Test
    public void checkCar() {
        CodecRegistry codecRegistry = context.getCodecRegistry();

        Codec< Car > marsCodec = codecRegistry.get(Car.class);

        Assert.assertNotNull(marsCodec);


        System.out.println(marsCodec);
    }


    @Test
    public void checkDefault() {
        CodecRegistry codecRegistry = context.getCodecRegistry();

        Codec< String > marsCodec = codecRegistry.get(String.class);

        Assert.assertNotNull(marsCodec);


        System.out.println(marsCodec);
    }


    @Test
    public void checkPersonEntity() {


        EntityModel< Person > build = new EntityModelBuilder<>(Person.class).build();

        PropertyModel< ? > idProperty = build.getIdProperty();

        System.out.println(idProperty);


    }


}
