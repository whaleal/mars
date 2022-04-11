package com.whaleal.mars.core.query;

import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @user Lyz
 * @description
 * @date 2022/3/9 10:00
 */
public class TestQuery {
    private MongoMappingContext context;

    private Query query;

    @BeforeMethod
    public void before() {
//        Mars mars = new Mars(Constant.connectionStr);
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());
        query = new Query();
    }

    @Test
    public void testForSimpleQuery() {
        query.addCriteria(new Criteria("status").is("D"));

        Document queryObject = query.getQueryObject();
        Assert.assertEquals(queryObject, Document.parse("{ status: \"D\" } "));

        query.addCriteria(new Criteria("status").in("A", "D"));

        Document queryObject1 = query.getQueryObject();
        System.out.println(queryObject1);
        Assert.assertEquals(queryObject1, Document.parse("{ status: { $in: [ \"A\", \"D\" ] } }"));

        query.addCriteria(new Criteria("status").is("A").and("qty").lt(30));

        Document queryObject2 = query.getQueryObject();
        System.out.println(queryObject2);
        Assert.assertEquals(queryObject2, Document.parse("{ status: \"A\", qty: { $lt: 30 } }"));
    }

    @Test
    public void testForQueryOrLt() {
//        query.addCriteria(new Criteria("status").is("A").orOperator(new Criteria("qty").lt(30)));

        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria("status").is("A");
        Criteria criteria2 = new Criteria("qty").lt(30);
        criteria.orOperator(criteria1, criteria2);
        query.addCriteria(criteria);


        Document queryObject = query.getQueryObject();
        System.out.println(queryObject);
        Assert.assertEquals(queryObject, Document.parse(" { $or: [ { status: \"A\" }, { qty: { $lt: 30 } } ] }"));
    }

    //todo Criteria criteria1 = new Criteria("item").regex("/^p/")存在问题
    @Test
    public void testForQueryOrAndRegex() {
//        query.addCriteria(new Criteria("status").is("A").orOperator(new Criteria("qty").lt(30)));

        Criteria criteria = new Criteria("status").is("A");
        Criteria criteria2 = new Criteria("qty").lt(30);
//        Criteria criteria1 = new Criteria("item").regex("^p");
        Criteria criteria1 = new Criteria("item").regex(new BsonRegularExpression("^p"));

        criteria.orOperator(criteria2, criteria1);
        query.addCriteria(criteria);

        Document queryObject = query.getQueryObject();
        System.out.println(queryObject);
        Assert.assertEquals(Document.parse(" {\n" +
                "     status: \"A\",\n" +
                "     $or: [ { qty: { $lt: 30 } }, { item: /^p/ } ]\n" +
                "} "), queryObject);
    }

    //
    @Test
    public void testForQueryEmbedded() {

        Criteria criteria = new Criteria("size").is(new Document("h", 14).append("w", 21).append("uom", "cm"));
//        Criteria criteria = new Criteria("size").elemMatch(new Criteria("h").is(14).and("w").is(21).and("uom").is("cm"));

        query.addCriteria(criteria);

        Document queryObject = query.getQueryObject();
        System.out.println(queryObject);
        Assert.assertEquals(Document.parse("{ size: { h: 14, w: 21, uom: \"cm\" } }"), queryObject);
    }

    @Test
    public void testForQueryEmbeddedNest() {

        Criteria criteria = new Criteria("size.h").lt(15);

        query.addCriteria(criteria);

        Document queryObject = query.getQueryObject();
        System.out.println(queryObject);
        Assert.assertEquals(Document.parse(" { \"size.h\": { $lt: 15 } } "), queryObject);
    }

    @Test
    public void testForQueryEmbeddedNestAnd() {

        Criteria criteria = new Criteria("size.h").lt(15).and("size.uom").is("in").and("status").is("D");

        query.addCriteria(criteria);

        Document queryObject = query.getQueryObject();
        System.out.println(queryObject);
        Assert.assertEquals(Document.parse("{ \"size.h\": { $lt: 15 }, \"size.uom\": \"in\", status: \"D\" }"), queryObject);
    }

    @Test
    public void testForQueryForArray() {

        Criteria criteria = new Criteria("tags").is(new String[]{"red", "blank"});

        query.addCriteria(criteria);

        Document queryObject = query.getQueryObject();
        Document document = context.toDocument(queryObject);


        Assert.assertEquals(document, Document.parse("{ tags: [\"red\", \"blank\"] }"));


        Criteria criteria1 = new Criteria("tags").all(new String[]{"red", "blank"});

        Document criteriaObject = criteria1.getCriteriaObject();
        Assert.assertEquals(criteriaObject, Document.parse("{ tags: { $all: [\"red\", \"blank\"] } } "));


        Criteria criteria2 = new Criteria("dim_cm").gt(25);

        Assert.assertEquals(criteria2.getCriteriaObject(), Document.parse("{ dim_cm: { $gt: 25 } } "));
        //Assert.assertEquals(Document.parse("{ \"size.h\": { $lt: 15 }, \"size.uom\": \"in\", status: \"D\" }"),queryObject);


        Criteria criteria3 = new Criteria("dim_cm").elemMatch(new Criteria().gt(22).lt(30));

        Assert.assertEquals(criteria3.getCriteriaObject(), Document.parse(" { dim_cm: { $elemMatch: { $gt: 22, $lt: 30 } } }"));

        Criteria criteria4 = new Criteria("dim_cm.1").gt(25);
        Assert.assertEquals(criteria4.getCriteriaObject(), Document.parse("{ \"dim_cm.1\": { $gt: 25 } }"));

        Criteria criteria5 = new Criteria("tags").size(3);
        Assert.assertEquals(criteria5.getCriteriaObject(), Document.parse(" { \"tags\": { $size: 3 } } "));
    }

    @Test
    public void testForQueryEmbeddedArray() {
        Criteria criteria = new Criteria("instock").is(new Document("warehouse", "A").append("qty", 5));

        Document document = context.toDocument(criteria.getCriteriaObject());
        System.out.println(document);
//        Document criteriaObject = criteria.getCriteriaObject();
        Assert.assertEquals(document, Document.parse(" { \"instock\": { warehouse: \"A\", qty: 5 } }"));

        Criteria criteria1 = new Criteria("instock.0.qty").lte(20);

        System.out.println(criteria1.getCriteriaObject());
        Assert.assertEquals(criteria1.getCriteriaObject(), Document.parse("{ 'instock.0.qty': { $lte: 20 } } "));

        Criteria criteria2 = new Criteria("instock").elemMatch(new Criteria("qty").is(5).and("warehouse").is("A"));

        System.out.println(criteria2.getCriteriaObject());
        Assert.assertEquals(criteria2.getCriteriaObject(), Document.parse(" { \"instock\": { $elemMatch: { qty: 5, warehouse: \"A\" } } }"));

        Criteria criteria3 = new Criteria("instock").elemMatch(new Criteria("qty").gt(10).lte(20));

        System.out.println(criteria3.getCriteriaObject());
        Assert.assertEquals(criteria3.getCriteriaObject(), Document.parse(" { \"instock\": { $elemMatch: { qty: { $gt: 10, $lte: 20 } } } }"));

        Criteria criteria4 = new Criteria("instock.qty").gt(10).lte(20);

        System.out.println(criteria4.getCriteriaObject());
        Assert.assertEquals(criteria4.getCriteriaObject(), Document.parse(" { \"instock.qty\": { $gt: 10,  $lte: 20 } } "));

//        new Document("instock.qty",5).append("instock.warehouse","A");
        Criteria criteria5 = new Criteria("instock.qty").is(5).and("instock.warehouse").is("A");

        System.out.println(criteria5.getCriteriaObject());
        Assert.assertEquals(criteria5.getCriteriaObject(), Document.parse("{ \"instock.qty\": 5, \"instock.warehouse\": \"A\" }"));

    }

    @Test
    public void testForQueryNull() {
        Criteria criteria = new Criteria("item").is(null);

        System.out.println(criteria.getCriteriaObject());

        Criteria item = new Criteria("item").type(10);
        System.out.println(item.getCriteriaObject());

        Criteria item1 = new Criteria("item").exists(false);
        System.out.println(item1.getCriteriaObject());
    }
}
