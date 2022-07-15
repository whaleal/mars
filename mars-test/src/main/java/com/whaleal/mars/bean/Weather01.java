package com.whaleal.mars.bean;

import com.mongodb.client.model.TimeSeriesGranularity;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Property;
import com.whaleal.mars.codecs.pojo.annotations.TimeSeries;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author cjq
 * @ClassName Weather01
 * @Description
 * @date 2022/7/14 10:21
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@TimeSeries(timeField = "timestamp",granularity = TimeSeriesGranularity.HOURS,enableExpire = true,expireAfterSeconds = 60 * 60)
public class Weather01 {

    @Id
    private int sensorId;

    private Date timestamp;

    private String type;

    //    @PropIgnore
//    此注解当前存在问题，bug先搁置
    private String temp;

    @Property("property")
    private String prop;

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}

