package com.whaleal.mars.core.aggregation.stages;

import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.domain.Direction;
import com.whaleal.mars.core.domain.ISort;

import com.whaleal.mars.core.domain.SortType;
import org.bson.BsonWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Sorts all input documents and returns them to the pipeline in sorted order.
 *
 * @aggregation.expression $sort
 */
public class Sort extends Stage implements ISort {

    public static final Direction DEFAULT_DIRECTION = Direction.ASC;


    public static final String NATURAL = "$natural";


    private static final Sort UNSORTED  = Sort.by(new SortType[0]);

    private final List< SortType > sorts ;

    protected Sort() {
        super("$sort");
        this.sorts = new ArrayList<>();
    }

    protected Sort( List<SortType> sorts) {
        super("$sort");
        this.sorts = sorts;
    }



    /**
     * Creates a sort stage.
     *
     * @return the new stage
     */
    public static Sort sort() {
        return new Sort();
    }

    /**
     * Creates a new {@link Sort} for the given {@link SortType}s.
     *
     * @param orders must not be {@literal null}.
     * @return
     */
    public static Sort by( SortType... orders) {

        Precondition.notNull(orders, "Orders must not be null!");

        return new Sort(Arrays.asList(orders));
    }

    /**
     * Creates a new {@link Sort} for the given {@link SortType}s.
     *
     * @param direction must not be {@literal null}.
     * @param properties must not be {@literal null}.
     * @return
     */
    public static Sort by( Direction direction, String... properties) {

        Precondition.notNull(direction, "Direction must not be null!");
        Precondition.notNull(properties, "Properties must not be null!");
        Precondition.isTrue(properties.length > 0, "At least one property must be given!");

        return Sort.by(Arrays.stream(properties)//
                .map(it -> new SortType(it ,direction))//
                .collect(Collectors.toList()));
    }

    /**
     * Creates a new {@link com.whaleal.mars.core.domain.Sort} instance.
     *
     * @param direction defaults to {@link com.whaleal.mars.core.domain.Sort#DEFAULT_DIRECTION} (for {@literal null} cases, too)
     * @param properties must not be {@literal null} or contain {@literal null} or empty strings.
     */
    private Sort( Direction direction, List<String> properties) {
        super("$sort");

        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }

        this.sorts = properties.stream() //
                .map(it -> new SortType( it,direction)) //
                .collect(Collectors.toList());
    }


    /**
     * Creates a new {@link Sort} for the given properties.
     *
     * @param properties must not be {@literal null}.
     * @return
     */
    public static Sort by( String... properties) {

        Precondition.notNull(properties, "Properties must not be null!");

        return properties.length == 0 //
                ? Sort.unsorted() //
                : new Sort(DEFAULT_DIRECTION, Arrays.asList(properties));
    }

    /**
     * Creates a new {@link Sort} for the given {@link SortType}s.
     *
     * @param orders must not be {@literal null}.
     * @return
     */
    public static Sort by( List< SortType > orders) {

        Precondition.notNull(orders, "Orders must not be null!");

        return orders.isEmpty() ? Sort.unsorted() : new Sort(orders);
    }



    /**
     * Returns a {@link Sort} instances representing no sorting setup at all.
     *
     * @return
     */
    public static Sort unsorted() {
        return UNSORTED;
    }


    /**
     * Creates a sort stage.
     *
     * @return the new stage
     * @deprecated use {@link #sort()}
     */
    @Deprecated()
    public static Sort on() {
        return new Sort();
    }


    public boolean isSorted() {
        return !isEmpty();
    }


    public boolean isEmpty() {
        return sorts.isEmpty();
    }

    public boolean isUnsorted() {
        return !isSorted();
    }


    /**
     * Adds an ascending sort definition on the field.
     *
     * @param field      the sort field
     * @param additional any additional fields to sort on
     * @return this
     */
    public Sort ascending(String field, String... additional) {
        sorts.add(new SortType(field, Direction.ASC));
        for (String another : additional) {
            sorts.add(new SortType(another, Direction.ASC));
        }
        return this;
    }

    /**
     * Adds an descending sort definition on the field.
     *
     * @param field      the sort field
     * @param additional any additional fields to sort on
     * @return this
     */
    public Sort descending(String field, String... additional) {
        sorts.add(new SortType(field, Direction.DESC));
        for (String another : additional) {
            sorts.add(new SortType(another, Direction.DESC));
        }
        return this;
    }

    /**
     * @return the sorts
     */
    public List<SortType> getSorts() {
        return sorts;
    }

    /**
     * Adds a sort by the computed textScore metadata in descending order.
     *
     * @param field the sort field
     * @return this
     */
    public Sort meta(String field) {
        sorts.add(new SortType(field, Direction.META));
        return this;
    }

    @Override
    public Sort and( ISort sort ) {
        Precondition.notNull(sort, "Sort must not be null!");

        ArrayList<SortType> these = new ArrayList<>(this.getSorts());

        for (SortType order : sort.getSorts()) {
            these.add(order);
        }

        return Sort.by(these);
    }

    @Override
    public Sort natural( Direction direction ) {

        sorts.add(new SortType(NATURAL,direction));
        return this ;
    }

    @Override
    public void encode( BsonWriter writer ) {

        writer.writeStartDocument();
        if(this.isSorted()){
            for (SortType sort : this.getSorts()) {
                writer.writeName(sort.getField());
                sort.getDirection().encode(writer);
            }
        }
        writer.writeEndDocument();
    }

}
