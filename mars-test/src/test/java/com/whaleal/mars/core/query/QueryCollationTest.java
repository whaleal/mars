package com.whaleal.mars.core.query;

import com.mongodb.client.model.CollationStrength;
import com.whaleal.mars.Constant;
import com.whaleal.mars.base.CreateDataUtil;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

/**
 * @author lyz
 * @description
 * @date 2022-06-27 18:01
 **/
public class QueryCollationTest {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void tesFor() {
        List<Document> list = CreateDataUtil.parseString("{ \"_id\" : 1, \"x\" : \"a\" }\n" +
                "{ \"_id\" : 2, \"x\" : \"A\" }\n" +
                "{ \"_id\" : 3, \"x\" : \"á\" }");

        mars.insert(list, "foo");
    }

    @After
    public void dropCollection(){
        mars.dropCollection("foo");
    }

    @Test
    public void testFor(){
        Query query = new Query();


        /**
         * Document{{_id=1, x=a}}
         * Document{{_id=2, x=A}}
         * Document{{_id=3, x=á}}
         */
        query.collation(Collation.of(Locale.CHINA));

        QueryCursor<Document> foo = mars.findAll(query, Document.class, "foo");
        while (foo.hasNext()){
            System.out.println(foo.next());
        }

        query.collation(Collation.of(Locale.FRENCH));

        QueryCursor<Document> foo1 = mars.findAll(query, Document.class, "foo");
        while (foo.hasNext()){
            System.out.println(foo1.next());
        }


    }

    /**
     * Document{{_id=62ba5de1b4786d754c25e395, n=-10}}
     * Document{{_id=62ba5de1b4786d754c25e391, n=-2.1}}
     * Document{{_id=62ba5de1b4786d754c25e38e, n=1}}
     * Document{{_id=62ba5de1b4786d754c25e396, n=10}}
     * Document{{_id=62ba5de1b4786d754c25e38f, n=2}}
     * Document{{_id=62ba5de1b4786d754c25e390, n=2.1}}
     * Document{{_id=62ba5de1b4786d754c25e393, n=2.10}}
     * Document{{_id=62ba5de1b4786d754c25e392, n=2.2}}
     * Document{{_id=62ba5de1b4786d754c25e394, n=2.20}}
     * Document{{_id=62ba5de1b4786d754c25e397, n=20}}
     * Document{{_id=62ba5de1b4786d754c25e398, n=20.1}}
     */
    @Test
    public void testForNumber(){
        Query query = new Query();

        query.with(Sort.ascending("n"));
        query.collation(Collation.of(Locale.ENGLISH).numericOrdering(true));

        QueryCursor<Document> c = mars.findAll(query, Document.class, "c");
        while (c.hasNext()){
            System.out.println(c.next());
        }
    }
}
