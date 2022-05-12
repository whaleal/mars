package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Representation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.BsonType;

/**
 * @author lyz
 * @desc
 * @date 2022-05-12 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scores {

    @Id
    @Representation(BsonType.INT64)
    private int id;

    private String student;

    private int[] homework;

    private int[] quiz;

    private int extraCredit;
}
