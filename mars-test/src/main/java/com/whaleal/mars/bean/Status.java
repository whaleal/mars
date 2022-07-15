package com.whaleal.mars.bean;

import lombok.AllArgsConstructor;

/**
 * @author cjq
 * @ClassName Status
 * @Description
 * @date 2022/7/13 14:09
 */
@AllArgsConstructor
public enum Status {


    Unknown("Unknown"),
    Incomplete("Incomplete");

    private String status;


    public String getStatus() {
        return status;
    }
}
