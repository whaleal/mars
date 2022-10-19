package com.whaleal.mars.core.crud;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Animal;
import com.whaleal.mars.bean.Scores;
import com.whaleal.mars.bean.Spces;
import com.whaleal.mars.bean.Vehicles;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class FindTest {

    Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createData() {
        mars.insert(new Scores(1, "Maya", new int[]{10, 5, 10}, new int[]{10, 8}, 0));
        mars.insert(new Scores(2, "Ryan", new int[]{5, 6, 5}, new int[]{8, 8}, 8));


        mars.insert(new Vehicles(1, "car", new Spces(4, 4)));
        mars.insert(new Vehicles(2, "motorcycle", new Spces(0, 2)));
        Vehicles vehicles = new Vehicles();
        vehicles.setId(3);
        vehicles.setType("jet ski");
        mars.insert(vehicles, "vehicles");

        mars.insert(new Document("_id", "1").append("dogs", 10).append("cats", 15), "animals");

        mars.insert(new Document("_id", "1").append("item", "tangerine").append("type", "citrus"), "fruit");
        mars.insert(new Document("_id", "2").append("item", "lemon").append("type", "citrus"), "fruit");
        mars.insert(new Document("_id", "3").append("item", "grapefruit").append("type", "citrus"), "fruit");


    }

    @After
    public void dropCollection(){
        mars.dropCollection("vehicles");
        mars.dropCollection("scores");
        mars.dropCollection("animals");
        mars.dropCollection("fruit");
    }


    @Test
    public void findOne() {
        Query query = new Query(new Criteria("_id").is(2));
        Vehicles vehicles = mars.findOne(query, Vehicles.class).orElse(null);

        Assert.assertEquals(vehicles.getType(), "motorcycle");
    }


    @Test
    public void find() {
        Query query = new Query(new Criteria("_id").is(2));
        List<Vehicles> vehicles = mars.find(query, Vehicles.class, "vehicles").toList();

        Assert.assertEquals(vehicles.get(0).getType(), "motorcycle");
    }

    @Test
    public void findAll() {

        QueryCursor<Vehicles> all = mars.findAll(Vehicles.class);

        Assert.assertEquals(all.toList().get(1).getType(), "motorcycle");
    }

}
