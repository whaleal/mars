package com.whaleal.mars.base;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
        //查看集合结构
        Document document = mars.executeCommand("{listCollections:1}");
        List<Document> list = (List<Document>) document.get("cursor", Document.class).get("firstBatch");
        for (Document document1 : list) {
            if ("weather".equals(document1.get("name"))) {
                Assert.assertEquals("timeseries", document1.get("type"));
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
        System.out.println(document);
    }
}
