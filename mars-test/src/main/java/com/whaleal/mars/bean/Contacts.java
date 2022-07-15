package com.whaleal.mars.bean;

import com.whaleal.mars.codecs.pojo.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cjq
 * @ClassName Info
 * @Description
 * @date 2022/7/13 13:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contacts {

    @Id
    private int id;

    private String phone;

    private String email;

    private Status status;
}
