package com.whaleal.mars.core.aggregation.stages;

import com.mongodb.lang.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Performs a union of two collections; i.e. $unionWith combines pipeline results from two collections into a single result set. The
 * stage outputs the combined result set (including duplicates) to the next stage.
 *
 * @aggregation.expression $unionWith
 */
public class UnionWith extends Stage {
    private final List<Stage> stages;
    private Class<?> collectionType;
    private String collectionName;

    /**
     * Creates the new stage
     *
     * @param collection the collection to process
     * @param stages     the pipeline
     */
    public UnionWith(String collection, List<Stage> stages) {
        super("$unionWith");
        this.collectionName = collection;
        this.stages = Collections.unmodifiableList(stages);
    }

    /**
     * Creates the new stage
     *
     * @param type   the type to process
     * @param stages the pipeline
     */
    public UnionWith(Class<?> type, List<Stage> stages) {
        super("$unionWith");
        this.collectionType = type;
        this.stages = Collections.unmodifiableList(stages);
    }

    /**
     * @return the collection name
     */
    @Nullable
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * @return the collection type
     */
    public Class<?> getCollectionType() {
        return collectionType;
    }

    /**
     * @return the pipeline
     */
    public List<Stage> getStages() {
        return stages;
    }
}
