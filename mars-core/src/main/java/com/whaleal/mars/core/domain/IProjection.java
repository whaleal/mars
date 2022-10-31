package com.whaleal.mars.core.domain;

import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import org.bson.Document;


/**
 * @author wh
 */
public interface IProjection {


    /**
     * Excludes a field.
     *
     * @param name the field name
     * @return this
     */
    IProjection exclude( String name);


    /**
     * Includes a field.
     *
     * @param name  the field name
     * @param value the value expression
     * @return this
     */
    IProjection include(String name, Expression value);
    /**
     * Includes a field.
     *
     * @param name the field name
     * @return this
     */
    IProjection include(String name) ;

    /**
     * Suppresses the _id field in the resulting document.
     *
     * @return this
     */
    IProjection suppressId();

    Document getFieldsObject();
}
