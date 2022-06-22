package com.whaleal.mars.core.aggregation.stages;

/**
 *
 */
public class Count extends Stage {
    private final String name;

    /**
     * @param name the field name
     */
    public Count(String name) {
        super("$count");
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
