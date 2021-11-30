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
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.distance3d.AxisPlaneCoordinateSequence;

public class PointCodecTest {

    MongoMappingContext  context ;

    GeometryDemo  geometryDemo ;

    @Before
    public  void  init(){
        MongoClient client = MongoClients.create(Constant.server101);


          context = new MongoMappingContext(client.getDatabase("mars"));


          geometryDemo = new GeometryDemo() ;
    }


    @Test
    public void test01Point(){


        //GeometryFactory


    }


    @Test
    public void test02Point(){

        Codec<AxisPlaneCoordinateSequence> pointCodec = context.getCodecRegistry().get(AxisPlaneCoordinateSequence.class);


        DocumentWriter writer = new DocumentWriter();
        ((Codec) context.getCodecRegistry().get(Point.class))
                .encode(writer, geometryDemo.createPoint(), EncoderContext.builder().build());

        Document document = writer.getDocument();

        System.out.println(document);

    }
}
