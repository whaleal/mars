package com.whaleal.mars.core.query;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @user Lyz
 * @description
 * @date 2022/3/9 18:15
 */
public class TestQueryAndProjection {

    private MongoMappingContext context;

    @BeforeMethod
    public void before() {
        context = new MongoMappingContext(new Mars(Constant.connectionStr).getDatabase());
    }

    @Test
    public void testForSimple() {
        System.out.println(new Criteria("qty").mod(4, 0).getCriteriaObject());
        System.out.println(new Criteria("qty").nin(5, 10).getCriteriaObject());
        System.out.println(new Criteria("qty").ne(20).getCriteriaObject());
        System.out.println(new Criteria("price").not().gt(1.99).getCriteriaObject());

        Criteria criteria = new Criteria("price").is(1.99);
        Criteria criteria1 = new Criteria("sale").is(true);
        Criteria criteria2 = new Criteria().norOperator(criteria, criteria1);
        System.out.println(criteria2.getCriteriaObject());


        ArrayList< Integer > objects = new ArrayList<>();
        objects.add(1);
        objects.add(5);

        Criteria criteria3 = new Criteria("a").bits().allClear(objects);
        Document criteriaObject = criteria3.getCriteriaObject();
        System.out.println(criteriaObject);

        Criteria criteria4 = new Criteria("a").bits().allSet(50);
        Document criteriaObject1 = criteria4.getCriteriaObject();
        System.out.println(criteriaObject1);

        Criteria criteria5 = new Criteria("a").bits().anyClear(objects);
        Document criteriaObject2 = criteria5.getCriteriaObject();
        System.out.println(criteriaObject2);

        Criteria criteria6 = new Criteria("a").bits().allSet(50);
        Document criteriaObject3 = criteria6.getCriteriaObject();
        System.out.println(criteriaObject3);

        new Criteria("x").mod(2, 0);

        Query query = new Query().withProjection(new Projection().slice("comments", -5));
        System.out.println(context.toDocument(query.getFieldsObject()));

        List< Double > objects1 = new ArrayList<>();
        List< Object > inObjects1 = new ArrayList<>();

        objects1.add(-74.0);
        objects1.add(40.0);
        objects1.add(74.0);

        inObjects1.add(objects1);
        inObjects1.add(10);
        Document loc = new Criteria("loc").withinCenter(new Point(new Position(objects1)), 10.0).getCriteriaObject();
        System.out.println(context.toDocument(loc));

    }
}
