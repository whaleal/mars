package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cjq
 * @ClassName Articles
 * @Description
 * @date 2022/7/14 16:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Articles {

    @Id
    private int id;

    private String subject;

    private String author;

    private int views;
}
