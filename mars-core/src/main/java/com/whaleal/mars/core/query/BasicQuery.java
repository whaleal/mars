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


import com.whaleal.icefrog.core.lang.Precondition;
import org.bson.Document;

import static com.whaleal.icefrog.core.util.ObjectUtil.nullSafeEquals;
import static com.whaleal.icefrog.core.util.ObjectUtil.nullSafeHashCode;


/**
 * json字符串转为基本查询语句的自定义实现
 */
public class BasicQuery extends Query {

    private final Document queryObject;
    private Document fieldsObject;
    private Document sortObject;

    /**
     * json字符串生成查询语句
     * @param query 可能为空
     */
    public BasicQuery( String query ) {
        this(query, null);
    }

    /**
     * 根据给定的Document对象生成查询语句
     * @param queryObject 不能为空
     */
    public BasicQuery(Document queryObject) {
        this(queryObject, new Document());
    }

    /**
     * 根据查询字符串和字段生成查询语句
     * @param query  可以为空.
     * @param fields 可以为空.
     */
    public BasicQuery( String query, String fields ) {

        this(query != null ? Document.parse(query) : new Document(),
                fields != null ? Document.parse(fields) : new Document());
    }

    /**
     * 根据查询文档和字段规范文档生成查询对象
     * @param queryObject  不能为空。
     * @param fieldsObject 不能为空。
     * @throws IllegalArgumentException queryObject或fieldsObject为空抛出IllegalArgumentException.
     */
    public BasicQuery(Document queryObject, Document fieldsObject) {

        Precondition.notNull(queryObject, "Query document must not be null");
        Precondition.notNull(fieldsObject, "Projection document must not be null");

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
     * 设置字段（投影）
     *
     * @param fieldsObject must not be {@literal null}.
     * @throws IllegalArgumentException when {@code fieldsObject} is {@literal null}.
     */
    protected void setFieldsObject(Document fieldsObject) {

        Precondition.notNull(sortObject, "Projection document must not be null");

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
     * 根据document设置sort
     * @param sortObject 不能为空.
     * @throws IllegalArgumentException sortObject为空抛出IllegalArgumentException.
     */
    public void setSortObject(Document sortObject) {

        Precondition.notNull(sortObject, "Sort document must not be null");

        this.sortObject = sortObject;
    }


    public void setSortObject(String sortObject) {

        Precondition.notNull(sortObject, "Sort String must not be null");

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
