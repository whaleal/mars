package com.whaleal.mars.geojson.codecs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.Constant;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.bson.codecs.writer.DocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance3d.AxisPlaneCoordinateSequence;

public class MultiLineStringCodecTest {

    MongoMappingContext context ;

    GeometryDemo  geometryDemo ;

    @Before
    public  void  init(){
        MongoClient client = MongoClients.create(Constant.server101);


        context = new MongoMappingContext(client.getDatabase("mars"));


        geometryDemo = new GeometryDemo() ;
    }



    @Test
    public void test02(){



        DocumentWriter writer = new DocumentWriter();
        ((Codec) context.getCodecRegistry().get(Point.class))
                .encode(writer, geometryDemo.createMLineByWKT(), EncoderContext.builder().build());

        Document document = writer.getDocument();

        System.out.println(geometryDemo.createPolygonByWKT());

        System.out.println(document);

    }
}
