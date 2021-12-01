package com.whaleal.mars.geojson.codecs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.Constant;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class PolygonCodecTest {
    MongoMappingContext context ;

    GeometryDemo  geometryDemo ;

    @Before
    public  void  init(){
        MongoClient client = MongoClients.create(Constant.server101);


        context = new MongoMappingContext(client.getDatabase("mars"));


        geometryDemo = new GeometryDemo() ;
    }


    @Test
    public void test01(){

        Codec<Polygon> pointCodec = context.getCodecRegistry().get(Polygon.class);


        Assert.assertNotNull(pointCodec);


    }


    @Test
    public void test02(){



        DocumentWriter writer = new DocumentWriter();
        ((Codec) context.getCodecRegistry().get(Point.class))
                .encode(writer, geometryDemo.createPolygonByWKT(), EncoderContext.builder().build());

        Document document = writer.getDocument();

        System.out.println(document);

    }
}
