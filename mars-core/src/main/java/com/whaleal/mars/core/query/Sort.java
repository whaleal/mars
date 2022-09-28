package com.whaleal.mars.core.query;


import com.whaleal.mars.core.domain.Direction;
import com.whaleal.mars.core.domain.SortType;

/**
 * Used for sorting query results or defining a sort stage in an aggregation pipeline
 *
 * @aggregation.expression $sort
 *
 */

public class Sort  extends com.whaleal.mars.core.domain.Sort {

    /**
     * Creates a sort on a field with a direction.
     * <ul>
     * <li>1 to specify ascending order.</li>
     * <li>-1 to specify descending order.</li>
     * </ul>
     *
     * @param field the field
     * @param order the order
     */
    protected Sort( String field, int order) {
        super();
        SortType sortType = new SortType(field, Direction.fromValue(order));
        super.getSorts().add(sortType);
    }


    /**
     * Creates an ascending sort on a field
     *
     * @param field the field
     * @return the Sort instance
     */
    public static Sort ascending(String field) {
        return new Sort(field, 1);
    }

    /**
     * Creates a descending sort on a field
     *
     * @param field the field
     * @return the Sort instance
     */
    public static Sort descending(String field) {
        return new Sort(field, -1);
    }



}
