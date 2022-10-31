package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cjq
 * @ClassName Num
 * @Description
 * @date 2022/7/15 11:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Num {

    @Id
    private String id;

    private String name;

    private int num;
}
