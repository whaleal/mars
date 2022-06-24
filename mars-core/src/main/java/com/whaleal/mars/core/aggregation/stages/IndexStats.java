package com.whaleal.mars.core.aggregation.stages;

/**
 * Returns statistics regarding the use of each index for the collection.
 *
 * @aggregation.expression $indexStats
 */
public class IndexStats extends Stage {
    protected IndexStats() {
        super("$indexStats");
    }

    /**
     * Creates the new stage.  There is nothing to configure as the collection is determined as part of the pipeline execution.
     *
     * @return the new stage
     */
    public static IndexStats indexStats() {
        return new IndexStats();
    }

    /**
     * Creates the new stage.  There is nothing to configure as the collection is determined as part of the pipeline execution.
     *
     * @return the new stage
     * @deprecated use {@link #indexStats()}
     */
    @Deprecated()
    public static IndexStats of() {
        return new IndexStats();
    }
}
