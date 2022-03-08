package com.whaleal.mars.core.query.codec.stage;

import com.whaleal.mars.core.aggregation.stages.Stage;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:11
 */
public class SliceStage extends Stage {
    private int count;

    public  SliceStage(int count) {
        super("$slice");
        this.count = count;
    }


    public String getKey() {
        return "$slice";
    }


    public Integer getValue() {
        return this.count;
    }
}
