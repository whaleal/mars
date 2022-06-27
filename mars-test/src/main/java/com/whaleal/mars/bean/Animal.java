package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.core.index.annotation.Field;
import com.whaleal.mars.core.index.annotation.Index;
import com.whaleal.mars.core.index.annotation.IndexOptions;
import com.whaleal.mars.core.index.annotation.Indexes;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lyz
 * @description
 * @date 2022-06-24 14:06
 **/
@Entity
@Data
@NoArgsConstructor
@Indexes(value = @Index(options = @IndexOptions(unique = true)))
public class Animal {

    private String id;

    private int age;

    private Boolean isAlive;
}
