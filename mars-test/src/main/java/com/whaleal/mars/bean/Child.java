package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;

/**
 * @author wh
 */
@Entity("chi")
public class Child  extends Parent{

    public Double getWeight() {
        return weight;
    }

    public void setWeight( Double weight ) {
        this.weight = weight;
    }

    private Double weight ;


}
