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
package com.whaleal.mars.core.query;

import com.mongodb.lang.Nullable;
import com.whaleal.mars.util.Assert;
import org.bson.Document;

import static com.whaleal.mars.util.ObjectUtils.nullSafeEquals;
import static com.whaleal.mars.util.ObjectUtils.nullSafeHashCode;

/**
 * Custom {@link Query} implementation to setup a basic query from some arbitrary JSON query string.
 */
public class BasicQuery extends Query {

    private final Document queryObject;
    private Document fieldsObject;
    private Document sortObject;

    /**
     * Create a new {@link BasicQuery} given a JSON {@code query}.
     *
     * @param query may be {@literal null}.
     */
    public BasicQuery(@Nullable String query) {
        this(query, null);
    }

    /**
     * Create a new {@link BasicQuery} given a query {@link Document}.
     *
     * @param queryObject must not be {@literal null}.
     */
    public BasicQuery(Document queryObject) {
        this(queryObject, new Document());
    }

    /**
     * Create a new {@link BasicQuery} given a JSON {@code query} and {@code fields}.
     *
     * @param query  may be {@literal null}.
     * @param fields may be {@literal null}.
     */
    public BasicQuery(@Nullable String query, @Nullable String fields) {

        this(query != null ? Document.parse(query) : new Document(),
                fields != null ? Document.parse(fields) : new Document());
    }

    /**
     * Create a new {@link BasicQuery} given a query {@link Document} and field specification {@link Document}.
     *
     * @param queryObject  must not be {@literal null}.
     * @param fieldsObject must not be {@literal null}.
     * @throws IllegalArgumentException when {@code sortObject} or {@code fieldsObject} is {@literal null}.
     */
    public BasicQuery(Document queryObject, Document fieldsObject) {

        Assert.notNull(queryObject, "Query document must not be null");
        Assert.notNull(fieldsObject, "Projection document must not be null");

        this.queryObject = queryObject;
        this.fieldsObject = fieldsObject;
        this.sortObject = new Document();
    }


    @Override
    public Query addCriteria(CriteriaDefinition criteria) {

        this.queryObject.putAll(criteria.getCriteriaObject());

        return this;
    }


    @Override
    public Document getQueryObject() {
        return this.queryObject;
    }


    @Override
    public Document getFieldsObject() {

        Document combinedFieldsObject = new Document();
        combinedFieldsObject.putAll(fieldsObject);
        combinedFieldsObject.putAll(super.getFieldsObject());
        return combinedFieldsObject;
    }

    /**
     * Set the fields (projection) {@link Document}.
     *
     * @param fieldsObject must not be {@literal null}.
     * @throws IllegalArgumentException when {@code fieldsObject} is {@literal null}.
     */
    protected void setFieldsObject(Document fieldsObject) {

        Assert.notNull(sortObject, "Projection document must not be null");

        this.fieldsObject = fieldsObject;
    }


    @Override
    public Document getSortObject() {

        Document result = new Document();
        result.putAll(sortObject);

        Document overrides = super.getSortObject();
        result.putAll(overrides);

        return result;
    }

    /**
     * Set the sort {@link Document}.
     *
     * @param sortObject must not be {@literal null}.
     * @throws IllegalArgumentException when {@code sortObject} is {@literal null}.
     */
    public void setSortObject(Document sortObject) {

        Assert.notNull(sortObject, "Sort document must not be null");

        this.sortObject = sortObject;
    }


    public void setSortObject(String sortObject) {

        Assert.notNull(sortObject, "Sort String must not be null");

        this.sortObject = Document.parse(sortObject);
    }


    @Override
    public boolean isSorted() {
        return super.isSorted() || !sortObject.isEmpty();
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof BasicQuery)) {
            return false;
        }

        BasicQuery that = (BasicQuery) o;

        return querySettingsEquals(that) && //
                nullSafeEquals(fieldsObject, that.fieldsObject) && //
                nullSafeEquals(queryObject, that.queryObject) && //
                nullSafeEquals(sortObject, that.sortObject);
    }


    @Override
    public int hashCode() {

        int result = super.hashCode();
        result = 31 * result + nullSafeHashCode(queryObject);
        result = 31 * result + nullSafeHashCode(fieldsObject);
        result = 31 * result + nullSafeHashCode(sortObject);

        return result;
    }
}
