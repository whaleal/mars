package com.whaleal.mars.bean;

import com.mongodb.client.model.TimeSeriesGranularity;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.TimeSeries;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lyz
 * @description
 * @date 2022-06-17 15:09
 **/
@Data
@NoArgsConstructor
@Entity("test")
//@TimeSeries(timeField = "timestamp",expireAfterSeconds = 60)
public class Weather {

    private int SensorId;

    private Date timestamp;

    private String type;

    private String temp;
}
