package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Author: cjq
 * Date: 2022/6/27 0027 16:50
 * FileName: TestTimeSeries
 * Description:
 */
public class TestTimeSeries {

    private Mars mars = new Mars(Constant.connectionStr);

    @Before
    public void createTimeSeriesCollection() {
        //创建时序集合
        mars.createCollection(Weather.class);
    }

    @After
    public void dropCollection() {
        mars.dropCollection("weather");
    }


    @Test
    public void watchTimeSeriesCollection() {
        //查看集合结构  Weather  为 时序集合
        Document document = mars.executeCommand("{listCollections:1}");
        //Document{{cursor=Document{{id=0, ns=wh.$cmd.listCollections, firstBatch=
        //[Document{{name=system.views, type=collection, options=Document{{}}, info=Document{{readOnly=false, uuid=org.bson.types.Binary@6212b82}}, idIndex=Document{{v=2, key=Document{{_id=1}}, name=_id_}}}},
        //Document{{name=animal, type=collection, options=Document{{}}, info=Document{{readOnly=false, uuid=org.bson.types.Binary@a6f55778}}, idIndex=Document{{v=2, key=Document{{_id=1}}, name=_id_}}}},
        //Document{{name=weather, type=timeseries, options=Document{{expireAfterSeconds=12960000, timeseries=Document{{timeField=timestamp, granularity=hours, bucketMaxSpanSeconds=2592000}}}}, info=Document{{readOnly=false}}}},
        //-------------
        //Document{{name=system.buckets.weather, type=collection, options=Document{{validator=Document{{$jsonSchema=Document{{bsonType=object, required=[_id, control, data], properties=Document{{_id=Document{{bsonType=objectId}}, control=Document{{bsonType=object, required=[version, min, max], properties=Document{{version=Document{{bsonType=number}}, min=Document{{bsonType=object, required=[timestamp], properties=Document{{timestamp=Document{{bsonType=date}}}}}}, max=Document{{bsonType=object, required=[timestamp], properties=Document{{timestamp=Document{{bsonType=date}}}}}}, closed=Document{{bsonType=bool}}, count=Document{{bsonType=number, minimum=1}}}}, additionalProperties=false}}, data=Document{{bsonType=object}}, meta=Document{{}}}}, additionalProperties=false}}}}, clusteredIndex=true, expireAfterSeconds=12960000, timeseries=Document{{timeField=timestamp, granularity=hours, bucketMaxSpanSeconds=2592000}}}}, info=Document{{readOnly=false, uuid=org.bson.types.Binary@5ff75895}}}}]}}, ok=1.0, $clusterTime=Document{{clusterTime=Timestamp{value=7307816605658906626, seconds=1701483644, inc=2}, signature=Document{{hash=org.bson.types.Binary@77c90f33, keyId=7307561561910935559}}}}, operationTime=Timestamp{value=7307816605658906626, seconds=1701483644, inc=2}}}


        List<Document> list = (List<Document>) document.get("cursor", Document.class).get("firstBatch");

        for (Document document1 : list) {
            if ("weather".equals(document1.get("name"))) {
                assertEquals("timeseries", document1.get("type"));
                Document document2 = document1.get("options", Document.class);

                Document parse = Document.parse("{\n" +
                        "\t\t\t\t\"expireAfterSeconds\" : NumberLong(3600),\n" +
                        "\t\t\t\t\"timeseries\" : {\n" +
                        "\t\t\t\t\t\"timeField\" : \"timestamp\",\n" +
                        "\t\t\t\t\t\"granularity\" : \"hours\",\n" +
                        "\t\t\t\t\t\"bucketMaxSpanSeconds\" : 2592000\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}");
                assertEquals(parse, document2);
            }
        }

    }
}
