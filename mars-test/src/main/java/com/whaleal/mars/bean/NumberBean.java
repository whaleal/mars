package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.CappedAt;
import com.whaleal.mars.codecs.pojo.annotations.Collation;
import com.whaleal.mars.codecs.pojo.annotations.Entity;

/**
 * @author lyz
 * @description
 * @date 2022-07-13 16:21
 **/
@Entity
@Collation(locale = "zh")
@CappedAt(count = 1000,value = 1024 * 1024)
public class NumberBean {

}
