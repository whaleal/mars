package com.whaleal.mars.core.aggregation.stages;



/**
 * Base type for stages.
 */
public abstract class Stage {
    private final String stageName;


    protected Stage(String stageName) {
        this.stageName = stageName;
    }



    /**
     * The name of the stage.
     *
     * @return the name
     */
    public String stageName() {
        return stageName;
    }


}
