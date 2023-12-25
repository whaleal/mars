package com.whaleal.mars.core.codecs;

import com.mongodb.DBObject;
import com.whaleal.mars.Constant;
import com.whaleal.mars.core.Mars;
import org.bson.BasicBSONObject;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author wh
 */


public class TestDocument {

    @Test
    public  void test(  ) {
        Mars mars = new Mars(Constant.connectionStr);
        Document  doc1 = new Document();
        doc1.append("a12","asdfhahfoi");
        doc1.append("b12","dahsahf");
        doc1.append("_id",1234123123);

        BasicBSONObject  obj = new BasicBSONObject();
        obj.put("a12","asdfhahfoi");
        obj.put("b12","dahsahf");
        obj.put("_id",1234123123);

        Document document = mars.toDocument(doc1);
        Document document1 = mars.toDocument(obj);

        System.out.println(document);
        System.out.println(document1);

        Assert.assertEquals(doc1,document);

        Assert.assertEquals(document,document1);


    }
}
