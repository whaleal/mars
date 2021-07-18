/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package com.whaleal.mars.core.aggregation.stages;

import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.filters.Filter;


public class GraphLookup extends Stage {
    private String from;
    private Expression startWith;
    private String connectFromField;
    private String connectToField;
    private String as;
    private Integer maxDepth;
    private String depthField;
    private Filter[] restriction;
    private Class fromType;


    public GraphLookup(String from) {
        this();
        this.from = from;
    }

    protected GraphLookup() {
        super("$graphLookup");
    }


    public GraphLookup(Class from) {
        this();
        this.fromType = from;
    }

    /**
     * Target collection for the $graphLookup operation to search, recursively matching the connectFromField to the connectToField.
     *
     * @param from the target collection name
     * @return this
     */
    public static GraphLookup from(String from) {
        return new GraphLookup(from);
    }

    /**
     * Target collection for the $graphLookup operation to search, recursively matching the connectFromField to the connectToField.
     *
     * @param from the target collection name
     * @return this
     */
    public static GraphLookup from(Class from) {
        return new GraphLookup(from);
    }


    /**
     * Name of the array field added to each output document. Contains the documents traversed in the $graphLookup stage to reach the
     * document.
     *
     * @param as the name
     * @return this
     */
    public GraphLookup as(String as) {
        this.as = as;
        return this;
    }

    /**
     * Projection name whose value $graphLookup uses to recursively match against the connectToField of other documents in the collection. If
     * the value is an array, each element is individually followed through the traversal process.
     *
     * @param connectFromField the field name
     * @return this
     */
    public GraphLookup connectFromField(String connectFromField) {
        this.connectFromField = connectFromField;
        return this;
    }

    /**
     * Projection name in other documents against which to match the value of the field specified by the connectFromField parameter.
     *
     * @param connectToField the field name
     * @return this
     */
    public GraphLookup connectToField(String connectToField) {
        this.connectToField = connectToField;
        return this;
    }

    /**
     * Optional. Name of the field to add to each traversed document in the search path. The value of this field is the recursion depth
     * for the document, represented as a NumberLong. Recursion depth value starts at zero, so the first lookup corresponds to zero depth.
     *
     * @param depthField the field name
     * @return this
     */
    public GraphLookup depthField(String depthField) {
        this.depthField = depthField;
        return this;
    }

    /**
     * @return the value
     */
    public String getAs() {
        return as;
    }

    /**
     * @return the value
     */
    public String getConnectFromField() {
        return connectFromField;
    }

    /**
     * @return the value
     */
    public String getConnectToField() {
        return connectToField;
    }

    /**
     * @return the value
     */
    public String getDepthField() {
        return depthField;
    }

    /**
     * @return the value
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the value
     */
    public Class getFromType() {
        return fromType;
    }

    /**
     * @return the value
     */
    public Integer getMaxDepth() {
        return maxDepth;
    }

    /**
     * @return the value
     */
    public Filter[] getRestriction() {
        return restriction;
    }

    /**
     * @return the value
     */
    public Expression getStartWith() {
        return startWith;
    }

    /**
     * Optional. Non-negative integral number specifying the maximum recursion depth.
     *
     * @param maxDepth the max depth
     * @return this
     */
    public GraphLookup maxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    /**
     * Optional. A query specifying additional conditions for the recursive search
     *
     * @param filters the filters to restrict the matching
     * @return this
     */
    public GraphLookup restrict(Filter... filters) {
        this.restriction = filters;
        return this;
    }

    /**
     * Expression that specifies the value of the connectFromField with which to start the recursive search.
     *
     * @param startWith the expression defining the starting point
     * @return this
     */
    public GraphLookup startWith(Expression startWith) {
        this.startWith = startWith;
        return this;
    }
}
