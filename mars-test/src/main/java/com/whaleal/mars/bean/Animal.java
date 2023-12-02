package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import com.whaleal.mars.codecs.pojo.annotations.PropIgnore;
import com.whaleal.mars.codecs.pojo.annotations.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lyz
 * @description
 * @date 2022-06-24 14:06
 *
 * 这个对象属性为 非基本类型
 * 默认值为null
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Indexes(value = @Index(fields = @Field(value = "age"),options = @IndexOptions(unique = true)))
public class Animal {

    @Id
    private String id;

    private Integer age;

    private Boolean isAlive;


    @Transient
    private Book book;


    @Transient
    private List<Num> num;


    @PropIgnore
    private Map<String,Person> art;



    @PropIgnore
    private  Status status;

    private Date birthday;
}
