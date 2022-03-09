
package com.whaleal.mars.core.query;


import com.whaleal.mars.core.aggregation.stages.Stage;



import java.util.ArrayList;
import java.util.List;

/**
 * Defines an update operation
 *
 *
 */
public class PipelineUpdate {

    private final List< Stage > updates = new ArrayList<>();

    public PipelineUpdate( List<Stage> updates) {


        this.updates.addAll(updates);
    }




    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Stage> toDocument() {

      return this.updates ;
    }
}
