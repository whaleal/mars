package com.whaleal.mars.core.domain;

import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyz
 * @desc
 * @create: 2022-10-24 17:52
 **/
public abstract class Projection implements IProjection{

    protected final Map<String, Object> criteria = new HashMap<>();

    protected Projection(){

    }

    private boolean suppressId;


    /**
     * Include a single {@code field} to be returned by the query operation.
     *
     * @param field the document field name to be included.
     * @return {@code this} field projection instance.
     */
    @Override
    public Projection include(String field) {

        Precondition.notNull(field, "Key must not be null!");

        criteria.put(field, 1);

        return this;
    }

    @Override
    public IProjection suppressId() {
        this.suppressId = true;
        return this;
    }

    /**
     * Include one or more {@code fields} to be returned by the query operation.
     *
     * @param field the document field names to be included.
     * @return {@code this} field projection instance.
     */
    @Override
    public Projection include(String field, Expression expression) {

        Precondition.notNull(field, "Keys must not be null!");

        criteria.put(field, expression);

        return this;
    }

    /**
     * Exclude a single {@code field} from being returned by the query operation.
     *
     * @param field the document field name to be included.
     * @return {@code this} field projection instance.
     */
    @Override
    public Projection exclude(String field) {

        Precondition.notNull(field, "Key must not be null!");

        criteria.put(field, 0);

        return this;
    }

}
