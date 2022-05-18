package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author lyz
 * @desc
 * @date 2022-05-12 14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users {

    @Id
    private String id;

    private Date joined;

    private String[] liked;
}
