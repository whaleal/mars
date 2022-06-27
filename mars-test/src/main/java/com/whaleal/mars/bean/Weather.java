package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.TimeSeries;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.index.annotation.*;
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
@Indexes(@Index(fields = @Field(value = "sensorId",type = IndexDirection.ASC),options = @IndexOptions(collation = @Collation(locale = "zh"))))
public class Weather {

    private int sensorId;

    private Date timestamp;

    private String type;

    private String temp;
}
