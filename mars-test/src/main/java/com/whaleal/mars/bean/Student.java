package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.Property;
import com.whaleal.mars.codecs.pojo.annotations.Representation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.BsonType;

/**
 * @author wh
 */
@Entity("student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id()
    @Representation(BsonType.STRING)
    private String stuNo;

    private String classNo;

    private String stuName;

    private Integer stuAge;

    @Property(value = "height")
    @Representation(BsonType.STRING)
    private Double stuHeight;

    @Property(value = "sex")
    private Gender stuSex;

    @Property(value = "cscore")
    private Double chineseScore;

    @Property(value = "mscore")
    private Double mathScore;

    @Property(value = "escore")
    private Double englishScore;

}
