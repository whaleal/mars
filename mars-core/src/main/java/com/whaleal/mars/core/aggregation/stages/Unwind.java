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

import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

/**
 * Deconstructs an array field from the input documents to output a document for each element. Each output document is the input document
 * with the value of the array field replaced by the element.
 *
 * @aggregation.expression $unwind
 */
public class Unwind extends Stage {
    private Expression path;
    private String includeArrayIndex;
    private Boolean preserveNullAndEmptyArrays;

    protected Unwind() {
        super("$unwind");
    }

    /**
     * Creates a stage with the named array field
     *
     * @param name the array field
     * @return this
     */
    public static Unwind on(String name) {
        return new Unwind()
                .path(name);
    }

    /**
     * @return the value
     */
    public String getIncludeArrayIndex() {
        return includeArrayIndex;
    }

    /**
     * @return the value
     */
    public Expression getPath() {
        return path;
    }

    /**
     * @return the value
     */
    public Boolean getPreserveNullAndEmptyArrays() {
        return preserveNullAndEmptyArrays;
    }

    /**
     * Optional. The name of a new field to hold the array index of the element. The name cannot start with a dollar sign $.
     *
     * @param name the new name
     * @return this
     */
    public Unwind includeArrayIndex(String name) {
        this.includeArrayIndex = name;
        return this;
    }

    /**
     * @return the value
     */
    public boolean optionsPresent() {
        return includeArrayIndex != null
                || preserveNullAndEmptyArrays != null;
    }

    /**
     * Optional.
     *
     * <li>If true, if the path is null, missing, or an empty array, $unwind outputs the document.
     * <li>If false, if path is null, missing, or an empty array, $unwind does not output a document.
     *
     * @param preserveNullAndEmptyArrays true to preserve
     * @return this
     */
    public Unwind preserveNullAndEmptyArrays(Boolean preserveNullAndEmptyArrays) {
        this.preserveNullAndEmptyArrays = preserveNullAndEmptyArrays;
        return this;
    }

    private Unwind path(String path) {
        this.path = Expressions.field(path);
        return this;
    }


}
