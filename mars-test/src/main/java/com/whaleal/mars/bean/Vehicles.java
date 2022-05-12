package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lyz
 * @desc
 * @date 2022-05-12 17:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehicles {

    @Id
    private int id;

    private String type;

    private Spces specs;

}

