package com.whaleal.mars.core.query.codec.stage;

import com.whaleal.mars.core.aggregation.stages.Stage;

/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:09
 */
public class PositionStage extends Stage {

    private final int position;

    public PositionStage(int position) {
        super("$position");
        this.position = position;
    }

    public String getKey() {
        return "$position";
    }

    public Integer getValue() {
        return position;
    }
}
