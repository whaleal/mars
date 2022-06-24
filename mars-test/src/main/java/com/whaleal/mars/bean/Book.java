package com.whaleal.mars.bean;

import com.mongodb.client.model.TimeSeriesGranularity;
import com.whaleal.mars.codecs.pojo.annotations.CappedAt;
import com.whaleal.mars.codecs.pojo.annotations.Concern;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.TimeSeries;

/**
 * @author lyz
 * @description
 * @date 2022-06-17 10:56
 **/
@Entity("book")
@TimeSeries(timeField = "create_time",granularity = TimeSeriesGranularity.HOURS,expireAfterSeconds = 60)
public class Book {

    private String name;

    private Double price;

    private int stocks;
}
