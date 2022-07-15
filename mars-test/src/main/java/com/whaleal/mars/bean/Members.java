package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cjq
 * @ClassName Member
 * @Description
 * @date 2022/7/14 14:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Members {

    @Id
    private int id;

    private String member;

    private String status;

    private int points;

    private String misc1;

    private String misc2;
}
