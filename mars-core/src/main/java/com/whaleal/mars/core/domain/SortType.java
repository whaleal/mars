package com.whaleal.mars.core.domain;

/**
 * @author wh
 * 排序类型
 * 主要构成为 一个字段名及一个排序方向
 */
public class SortType {
    private final String field;
    private final Direction direction;

    public SortType( String field, Direction direction ) {
        this.field = field;
        this.direction = direction;
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }
}
