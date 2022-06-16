package com.whaleal.mars.core.query;

import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @user Lyz
 * @description
 * @date 2022/3/9 18:57
 */
public class TestUpdateOperators {
    private MongoMappingContext context;

    @BeforeMethod
    public void before() {
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());
    }

    @Test
    public void testForUpdate() {
        Update set = new Update().set("lastModified", "$$NOW").set("cancellation", new Document("date", "$$CLUSTER_TIME").append("reason", "request")).set("status", "D");
        System.out.println(set.getUpdateObject());

        System.out.println(new Update().inc("quantity", -2).inc("metrics.orders", 1).getUpdateObject());

        System.out.println(new Update().min("lowScore", 150).getUpdateObject());
        System.out.println(new Update().max("highScore", 500).getUpdateObject());

        System.out.println(new Update().multiply("price", 1.25).multiply("qty", 25).getUpdateObject());

        System.out.println(new Update().rename("nickname", "alias").rename("cell", "mobile").getUpdateObject());

        Update update = new Update().set("item", "apple").setOnInsert("defaultQty", 100);


        new Update().pop("scores", Update.Position.FIRST);
        new Update().pop("scores", Update.Position.LAST);

        Criteria in = new Criteria("fruits").in("apples", "oranges");
        System.out.println(new Update().pull("vagetables", "carrots").getUpdateObject());

        System.out.println(new Update().pullAll("scores", new Object[]{0, 5}).getUpdateObject());


        System.out.println(new Update().unset("quantity").unset("instock").getUpdateObject());

//        new Update().filterArray().
    }
}
