package com.whaleal.mars.core.query.codec.stage;

import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.aggregation.stages.Stage;
import com.whaleal.mars.core.query.Sort;


/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:12
 */
public class SortStage extends Stage {

    private final Object sort;

    /**
     * @param direction must not be {@literal null}.
     */
    public SortStage(Sort.Direction direction) {
        super("");
        Precondition.notNull(direction, "Direction must not be null!");
        this.sort = direction.isAscending() ? 1 : -1;
    }


    public SortStage(Sort sort) {
        super("");
        Precondition.notNull(sort, "Sort must not be null!");

        for (Sort.Order order : sort) {

            if (order.isIgnoreCase()) {
                throw new IllegalArgumentException(String.format("Given sort contained an Order for %s with ignore case! "
                        + "MongoDB does not support sorting ignoring case currently!", order.getProperty()));
            }
        }

        this.sort = sort;
    }


    public String getKey() {
        return "$sort";
    }


    public Object getValue() {
        return this.sort;
    }
}
