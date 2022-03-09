package com.whaleal.mars.core.query;

import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

/**
 * @user Lyz
 * @description
 * @date 2022/3/9 18:57
 */
public class TestUpdateOperators {
    private MongoMappingContext context;

    @Before
    public void before(){
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());
    }

    @Test
    public void testForUpdate(){
        Update1 set = new Update1().set("lastModified", "$$NOW").set("cancellation", new Document("date", "$$CLUSTER_TIME").append("reason", "request")).set("status", "D");
        System.out.println(set.getUpdateObject());

        System.out.println(new Update1().inc("quantity", -2).inc("metrics.orders", 1).getUpdateObject());

        System.out.println(new Update1().min("lowScore", 150).getUpdateObject());
        System.out.println(new Update1().max("highScore", 500).getUpdateObject());

        System.out.println(new Update1().multiply("price", 1.25).multiply("qty", 25).getUpdateObject());

        System.out.println(new Update1().rename("nickname", "alias").rename("cell", "mobile").getUpdateObject());

        Update1 update1 = new Update1().set("item", "apple").setOnInsert("defaultQty", 100);

        System.out.println(new Update1().unset("quantity").unset("instock").getUpdateObject());

        new Update1().pop("scores", Update1.Position.FIRST);
        new Update1().pop("scores", Update1.Position.LAST);

        Criteria in = new Criteria("fruits").in("apples", "oranges");
        System.out.println(new Update1().pull("vagetables", "carrots").getUpdateObject());

        System.out.println(new Update1().pullAll("scores", new Object[]{0, 5}).getUpdateObject());
    }
}
