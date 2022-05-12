package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @desc
 * @date 2022-05-11 16:43
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZipCodes {

    private String id;

    private String city;

    private Double[] loc;

    private int pop;

    private String state;

}
