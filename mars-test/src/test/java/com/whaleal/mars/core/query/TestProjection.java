package com.whaleal.mars.core.query;

import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @user Lyz
 * @description
 * @date 2022/3/9 15:06
 */

public class TestProjection {

    private Query query;

    private MongoMappingContext context;

    @BeforeMethod
    public void before() {
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());

        query = new Query();

    }

    @Test
    public void testForQueryReturn() {
        Criteria criteria = new Criteria("status").is("A");

        query.addCriteria(criteria);
        query.withProjection(new Projection().include("status").include("item"));

        Document queryObject = query.getQueryObject();
        System.out.println(query.getFieldsObject());
        System.out.println(queryObject);

        Assert.assertEquals(queryObject, Document.parse(" { status: \"A\" }, { item: 1, status: 1}"));

        Criteria criteria1 = new Criteria("item").is("1");

        query.addCriteria(criteria1);
        query.withProjection(new Projection().include("status").include("item").exclude("_id"));

        Document queryObject1 = query.getQueryObject();
        System.out.println(query.getFieldsObject());
        System.out.println(queryObject1);

        Assert.assertEquals(queryObject1, Document.parse(" { status: \"A\" }, { item: 1, status: 1 ,_id : 0}"));

    }

    @Test
    public void testForQueryProjection() {

        Projection include = new Projection().include("item", "status", "size.uom");
        System.out.println(include.getFieldsObject());

        System.out.println(new Projection().include("item", "status", "instock.qty").getFieldsObject());

        Document fieldsObject = new Projection().include("item", "status").slice("instock", -1).getFieldsObject();
        System.out.println(fieldsObject);
    }

    @Test
    public void testForMetaProject() {

        Projection projection = new Projection();


    }
}
