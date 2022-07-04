package com.whaleal.mars.core;

import com.whaleal.mars.Constant;
import com.whaleal.mars.bean.Weather;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.QueryCursor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.sql.Timestamp;


/**
 * Author: cjq
 * Date: 2022/6/27 0027 16:50
 * FileName: TestTimeSeries
 * Description:
 */
public class TestTimeSeries {

    private Mars mars = new Mars(Constant.connectionStr);

    @Test
    public void createTimeSeriesCollection(){
        //创建时序集合
        mars.createCollection(Weather.class);
    }

    @Test
    public void watchTimeSeriesCollection(){
        //查看集合结构
        Document document = mars.executeCommand("{listCollections:1}");
        System.out.println(document);
    }
    @Test
    public void testForTTL(){

        Weather weather = new Weather();
        weather.setSensorId(1);
        weather.setTimestamp(new Timestamp(1111L));
        weather.setTemp("11");
        weather.setType("temperature");
        weather.setProp("test");
        //插入数据测试
        mars.insert(weather);
    }

    @Test
    public void findData(){
        //查weather有没有数据
        QueryCursor<Weather> weather = mars.findAll(new Query(), Weather.class, "weather");
        while(weather.hasNext()){
            System.out.println(weather.next());
        }
    }

    @Test
    public void testForDropCollection(){
        mars.dropCollection(Weather.class);
    }



}
