package com.whaleal.mars.core.aggregation.stages;

/**
 * Randomly selects the specified number of documents from its input.
 *
 * @aggregation.expression $sample
 */
public class Sample extends Stage {
    private final long size;

    protected Sample(long size) {
        super("$sample");
        this.size = size;
    }

    /**
     * Creates a new stage with the given sample size.
     *
     * @param size the sample size
     * @return the new stage
     * @deprecated use {@link #sample(long)}
     */
    @Deprecated()
    public static Sample of(long size) {
        return new Sample(size);
    }

    /**
     * Creates a new stage with the given sample size.
     *
     * @param size the sample size
     * @return the new stage
     */
    public static Sample sample(long size) {
        return new Sample(size);
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }
}
