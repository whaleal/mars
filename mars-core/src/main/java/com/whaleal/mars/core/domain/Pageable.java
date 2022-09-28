package com.whaleal.mars.core.domain;

import com.whaleal.icefrog.core.lang.Precondition;

import java.util.Optional;

/**
 * @author wh
 */
public interface Pageable {
    /**
     * Returns a {@link Pageable} instance representing no pagination setup.
     *
     * @return
     */
    static Pageable unpaged() {
        return Unpaged.INSTANCE;
    }

    /**
     * Creates a new {@link Pageable} for the first page (page number {@code 0}) given {@code pageSize} .
     *
     * @param pageSize the size of the page to be returned, must be greater than 0.
     * @return a new {@link Pageable}.
     * 
     */
    static Pageable ofSize(int pageSize) {
        return PageRequest.of(0, pageSize);
    }

    /**
     * Returns whether the current {@link Pageable} contains pagination information.
     *
     * @return
     */
    default boolean isPaged() {
        return true;
    }

    /**
     * Returns whether the current {@link Pageable} does not contain pagination information.
     *
     * @return
     */
    default boolean isUnpaged() {
        return !isPaged();
    }

    /**
     * Returns the page to be returned.
     *
     * @return the page to be returned.
     */
    int getPageNumber();

    /**
     * Returns the number of items to be returned.
     *
     * @return the number of items of that page
     */
    int getPageSize();

    /**
     * Returns the offset to be taken according to the underlying page and page size.
     *
     * @return the offset to be taken
     */
    long getOffset();

    /**
     * Returns the sorting parameters.
     *
     * @return
     */
    ISort getSort();

    /**
     * Returns the current {@link Sort} or the given one if the current one is unsorted.
     *
     * @param sort must not be {@literal null}.
     * @return
     */
    default ISort getSortOr(ISort sort) {

        Precondition.notNull(sort, "Fallback Sort must not be null!");

        return getSort().isSorted() ? getSort() : sort;
    }

    /**
     * Returns the {@link Pageable}.
     *
     * @return
     */
    Pageable next();

    /**
     * Returns the previous {@link Pageable} or the first {@link Pageable} if the current one already is the first one.
     *
     * @return
     */
    Pageable previousOrFirst();

    /**
     * Returns the {@link Pageable} requesting the first page.
     *
     * @return
     */
    Pageable first();

    /**
     * Creates a new {@link Pageable} with {@code pageNumber} applied.
     *
     * @param pageNumber
     * @return a new {@link PageRequest}.
     * 
     */
    Pageable withPage(int pageNumber);

    /**
     * Returns whether there's a previous {@link Pageable} we can access from the current one. Will return
     * {@literal false} in case the current {@link Pageable} already refers to the first page.
     *
     * @return
     */
    boolean hasPrevious();

    /**
     * Returns an {@link Optional} so that it can easily be mapped on.
     *
     * @return
     */
    default Optional<Pageable> toOptional() {
        return isUnpaged() ? Optional.empty() : Optional.of(this);
    }

}
