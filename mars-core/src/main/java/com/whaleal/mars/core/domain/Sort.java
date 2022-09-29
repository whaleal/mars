package com.whaleal.mars.core.domain;

import com.mongodb.lang.Nullable;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.StrUtil;
import org.bson.BsonWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wh
 */
public class Sort implements ISort{

    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private final   List<SortType> sorts ;
    public static final String NATURAL = "$natural";


    private static final Sort UNSORTED  = Sort.by(new SortType[0]);

    protected Sort() {
        this.sorts = new ArrayList<>();
    }

    protected Sort( List<SortType> sorts) {
        this.sorts = sorts;
    }

    /**
     * Creates a new {@link Sort} instance.
     *
     * @param direction defaults to {@link Sort#DEFAULT_DIRECTION} (for {@literal null} cases, too)
     * @param properties must not be {@literal null} or contain {@literal null} or empty strings.
     */
    private Sort( Direction direction, List<String> properties) {

        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }

        this.sorts = properties.stream() //
                .map(it -> new SortType( it,direction)) //
                .collect(Collectors.toList());
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
    public static Sort by(SortType... orders) {

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
    public static Sort by(Direction direction, String... properties) {

        Precondition.notNull(direction, "Direction must not be null!");
        Precondition.notNull(properties, "Properties must not be null!");
        Precondition.isTrue(properties.length > 0, "At least one property must be given!");

        return Sort.by(Arrays.stream(properties)//
                .map(it -> new SortType(it ,direction))//
                .collect(Collectors.toList()));
    }
    /**
     * Creates a new {@link Sort} for the given properties.
     *
     * @param properties must not be {@literal null}.
     * @return
     */
    public static Sort by(String... properties) {

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
    public static Sort by(List<SortType> orders) {

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


    public boolean isSorted() {
        return !isEmpty();
    }


    public boolean isEmpty() {
        return sorts.isEmpty();
    }

    public boolean isUnsorted() {
        return !isSorted();
    }


    public Sort ascending( String field, String... additional ) {
        sorts.add(new SortType(field, Direction.ASC));
        for (String another : additional) {
            sorts.add(new SortType(another, Direction.ASC));
        }
        return this;
    }


    public Sort descending( String field, String... additional ) {
        sorts.add(new SortType(field, Direction.DESC));
        for (String another : additional) {
            sorts.add(new SortType(another, Direction.DESC));
        }
        return this;
    }


    public Sort meta( String field ) {
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


    public Sort natural( Direction direction ) {

        sorts.add(new SortType(NATURAL,direction));
        return this ;
    }


    public List< SortType > getSorts() {
        return sorts;
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


    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Sort)) {
            return false;
        }

        Sort that = (Sort) obj;

        return this.getSorts().equals(that.getSorts());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;
        result = 31 * result + sorts.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return isEmpty() ? "UNSORTED" : StrUtil.collectionToCommaDelimitedString(sorts);
    }



}
