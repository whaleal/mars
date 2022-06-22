package com.whaleal.mars.core.aggregation.stages;


import com.whaleal.mars.core.query.filters.Filter;

/**
 * Limits the number of documents passed to the next stage in the pipeline.
 *
 * @aggregation.expression $match
 */
public class Match extends Stage {
    private final Filter[] filters;

    protected Match(Filter... filters) {
        super("$match");
        this.filters = filters;
    }

    /**
     * Creates the new stage using the filters for matching
     *
     * @param filters the filters to apply
     * @return this
     */
    public static Match match( Filter... filters) {
        return new Match(filters);
    }

    /**
     * Creates the new stage using the filters for matching
     *
     * @param filters the filters to apply
     * @return this
     * @deprecated use {@link #match(Filter...)}
     */
    @Deprecated()
    public static Match on(Filter... filters) {
        return new Match(filters);
    }

    /**
     * @return the filters
     */
    public Filter[] getFilters() {
        return filters;
    }
}
