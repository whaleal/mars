package com.whaleal.mars.core.query.codec.stage;

import com.whaleal.mars.core.aggregation.stages.Stage;


import java.util.Arrays;
import java.util.Collection;

/**
 * Implementation of {@link } representing {@code $each}.
 */
public class EachStage extends Stage {

    private Object[] values;

    public EachStage(Object... values) {
        super("$each");
        this.values = extractValues(values);
    }

    private Object[] extractValues(Object[] values) {

        if (values == null || values.length == 0) {
            return values;
        }

        if (values.length == 1 && values[0] instanceof Collection) {
            return ((Collection<?>) values[0]).toArray();
        }

        return Arrays.copyOf(values, values.length);
    }






    public Object[] getValue() {
        return this.values;
    }
}
