package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Reference;
import lombok.Data;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-24 16:26
 **/
@Entity
@Data
public class Order {

    @Id
    private String id;

    @Reference(value = Book.class)
    private String foreignId;

    private int count;
}
