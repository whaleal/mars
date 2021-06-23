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
package com.whaleal.mars.core.aggregation.expressions;

import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

/**
 * Defines helper fields for referencing system variables
 */
public final class SystemVariables {
    /**
     * A variable that returns the current timestamp value.
     * CLUSTER_TIME is only available on replica sets and sharded clusters.
     * <p>
     * CLUSTER_TIME returns the same value for all members of the deployment and remains the same throughout all stages of
     * the pipeline.
     */
    public static final Expression CLUSTER_TIME = Expressions.value("$$CLUSTER_TIME");
    /**
     * References the start of the field path being processed in the aggregation pipeline stage. Unless documented otherwise, all
     * stages start with CURRENT the same as ROOT.
     * <p>
     * CURRENT is modifiable. However, since $<field> is equivalent to $$CURRENT.<field>, rebinding CURRENT changes the
     * meaning of $ accesses.
     */
    public static final Expression CURRENT = Expressions.value("$$CURRENT");
    /**
     * One of the allowed results of a $redact expression.
     */
    public static final Expression DESCEND = Expressions.value("$$DESCEND");
    /**
     * One of the allowed results of a $redact expression.
     */
    public static final Expression KEEP = Expressions.value("$$KEEP");
    /**
     * A variable that returns the current datetime value. NOW returns the same value for all members of the deployment and remains
     * the same throughout all stages of the aggregation pipeline.
     */
    public static final Expression NOW = Expressions.value("$$NOW");
    /**
     * One of the allowed results of a $redact expression.
     */
    public static final Expression PRUNE = Expressions.value("$$PRUNE");
    /**
     * A variable which evaluates to the missing value. Allows for the conditional exclusion of fields. In a $projection, a field
     * set to the variable REMOVE is excluded from the output.
     * <p>
     * For an example of its usage, see Conditionally Exclude Fields.
     */
    public static final Expression REMOVE = Expressions.value("$$REMOVE");
    /**
     * References the root document, i.e. the top-level document, currently being processed in the aggregation pipeline stage.
     */
    public static final Expression ROOT = Expressions.value("$$ROOT");

    private SystemVariables() {
    }
}
