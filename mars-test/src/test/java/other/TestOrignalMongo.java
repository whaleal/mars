package other;

import com.whaleal.mars.bean.Car;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Before;
import org.junit.Test;
import com.whaleal.mars.Constant;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class TestOrignalMongo {


    public static void main(String[] args) {

    }



    MongoClient client ;

    CodecRegistry codecRegistry ;




    @Before
    public void  init(){

        client = MongoClients.create(Constant.local);

        codecRegistry = client.getDatabase("mars").getCodecRegistry();


    }

    @Test
    public void testInsertFromSecondary(){

        client.getDatabase("mars").getCollection("tt").insertOne(new Document("tt",10));
    }

    @Test
    public void getDefaultCodec(){

        try {
            Codec<Car> carCodec = codecRegistry.get(Car.class);
        }catch (CodecConfigurationException exception){
            System.out.println(exception);
        }

    }



    @Test
    public void setMarsCodec(){


        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry()
         );

         MongoDatabase database = client.getDatabase("mars").withCodecRegistry(pojoCodecRegistry);

        //CodecRegistry codecRegistry = database.getCodecRegistry();

        //System.out.println(codecRegistry.get(Car.class));


        Car car = new Car();

        car.setWeight(99d);

        //car.setOo("607073369a6429244b6705a7");


        car.setCc("60686a4608cfc425fb3cc320");

        List<String>  temp = new ArrayList<>();
        temp.add("11");
        temp.add("22");;

        car.setListofString(temp);







        MongoCollection<Car> car1 = database.getCollection("car", Car.class);
        car1.insertOne(car);
    }






    @Test
    public void demo(){
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders());

        //MongoClient  client = MongoClients.create("mongodb://localhost:27000/mars");


       /* MongoCollection<Personal> collection = client.getDatabase("mars").withCodecRegistry(pojoCodecRegistry).getCollection("person",Personal.class);


        Personal ada = new Personal("Ada Byron", 20, new Address("St James Square", "London", "W1"));

        collection.insertOne(ada);

        Personal p = collection.find().first();

        System.out.println(p);*/
    }


    @Test
    public void demo2(){
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        //MongoClient  client = MongoClients.create("mongodb://localhost:27000/mars");


        MongoCollection<Car> collection = client.getDatabase("mars").withCodecRegistry(pojoCodecRegistry).getCollection("car",Car.class);


        Car  ada = new Car();

        collection.insertOne(ada);

       /* Person p = collection.find().first();

        System.out.println(p);*/
    }




}
