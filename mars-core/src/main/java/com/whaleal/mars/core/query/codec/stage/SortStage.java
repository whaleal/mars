package com.whaleal.mars.core.query.codec.stage;

import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.aggregation.stages.Stage;
import com.whaleal.mars.core.query.Sort;
import org.bson.Document;


/**
 * @user Lyz
 * @description
 * @date 2022/3/8 10:12
 */
public class SortStage extends Stage {

    private final Document sort;

//    /**
//     * @param direction must not be {@literal null}.
//     */
//    public SortStage(Sort.Direction direction) {
//        super("");
//        Precondition.notNull(direction, "Direction must not be null!");
//        this.sort = direction.isAscending() ? 1 : -1;
//    }


    public SortStage(Sort sort) {
        super("$sort");
        Precondition.notNull(sort, "Sort must not be null!");
        Document append = new Document();
        for (Sort.SortType order : sort) {


            int i = Sort.Direction.ASCENDING.equals(order.getDirection()) ? 1 : -1;
            append = new Document().append(order.getField(),i);

        }

        this.sort = append;

    }


    public String getKey() {
        return "$sort";
    }


    public Document getValue() {
        return this.sort;
    }
}
