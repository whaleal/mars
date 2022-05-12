package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Representation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.BsonType;

import java.util.Date;

/**
 * @author lyz
 * @desc
 * @date 2022-05-10 12:55
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id()
    @Representation(BsonType.STRING)
    private String id;

    private String name;

    private String size;

    private int price;

    private int quantity;

    private Date date;
}
