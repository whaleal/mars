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
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Projection projection.
 *
 * 对于包含数组的字段，MongoDB 提供了以下用于操作数组的投影运算符：$elemMatch、$slice 和 $。
 * Name       ----------     Description
 * $
 * Projects the first element in an array that matches the query condition.
 * $elemMatch
 * Projects the first element in an array that matches the specified $elemMatch condition.
 * $meta
 * Projects the available per-document metadata.
 * $slice
 * Limits the number of elements projected from an array. Supports skip and limit slices.
 *
 * 其实就是一个projection
 * 官方文档链接
 * https://docs.mongodb.com/manual/reference/operator/projection/
 */
public class Projection extends com.whaleal.mars.core.domain.Projection {

//    private final Map<String, Object> criteria = new HashMap<>();
    //以下示例使用 $slice 投影运算符返回 instock 数组中的最后一个元素：
    // { item: 1, status: 1, instock: { $slice: -1 } }
    private final Map<String, Object> slices = new HashMap<>();

    private final Map<String, Criteria> elemMatchs = new HashMap<>();
    private String positionKey;
    private int positionValue;

//    public Projection(){
//        super();
//    }
//
//    public Projection(Map<String, Object> criteria){
//        super(criteria);
//    }

    public static Projection projection(){
        return new Projection();
    }

    /**
     * Include one or more {@code fields} to be returned by the query operation.
     *
     * @param fields the document field names to be included.
     * @return {@code this} field projection instance.
     */

    public Projection include(String... fields) {

        Precondition.notNull(fields, "Keys must not be null!");

        for (String key : fields) {
            criteria.put(key, 1);
        }

        return this;
    }

    /**
     * Exclude one or more {@code fields} from being returned by the query operation.
     *
     * @param fields the document field names to be included.
     * @return {@code this} field projection instance.
     */
    public Projection exclude(String... fields) {

        Precondition.notNull(fields, "Keys must not be null!");

        for (String key : fields) {
            criteria.put(key, 0);
        }

        return this;
    }


    /**
     * Project a {@code $slice} of the array {@code field} using the first {@code size} elements.
     *
     * @param field the document field name to project, must be an array field.
     * @param size  the number of elements to include.
     * @return {@code this} field projection instance.
     */
    public Projection slice(String field, int size) {

        Precondition.notNull(field, "Key must not be null!");

        slices.put(field, size);

        return this;
    }

    /**
     * Project a {@code $slice} of the array {@code field} using the first {@code size} elements starting at
     * {@code offset}.
     *
     * @param field  the document field name to project, must be an array field.
     * @param offset the offset to start at.
     * @param size   the number of elements to include.
     * @return {@code this} field projection instance.
     */
    public Projection slice(String field, int offset, int size) {

        slices.put(field, new Integer[]{offset, size});
        return this;
    }

    public Projection elemMatch(String field, Criteria elemMatchCriteria) {

        elemMatchs.put(field, elemMatchCriteria);
        return this;
    }

    /**
     * The array field must appear in the query. Only one positional {@code $} operator can appear in the projection and
     * only one array field can appear in the query.
     *
     * @param field query array field, must not be {@literal null} or empty.
     * @param value
     * @return {@code this} field projection instance.
     */
    public Projection position(String field, int value) {

        Precondition.hasText(field, "DocumentField must not be null or empty!");

        positionKey = field;
        positionValue = value;

        return this;
    }

    @Override
    public Document getFieldsObject() {

        @SuppressWarnings({"unchecked", "rawtypes"})
        Document document = new Document((Map) criteria);

        for (Entry<String, Object> entry : slices.entrySet()) {
            document.put(entry.getKey(), new Document("$slice", entry.getValue()));
        }

        for (Entry<String, Criteria> entry : elemMatchs.entrySet()) {
            document.put(entry.getKey(), new Document("$elemMatch", entry.getValue().getCriteriaObject()));
        }

        if (positionKey != null) {
            document.put(positionKey + ".$", positionValue);
        }

        return document;
    }
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Projection projection = (Projection) o;

        if (positionValue != projection.positionValue) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(criteria, projection.criteria)) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(slices, projection.slices)) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(elemMatchs, projection.elemMatchs)) {
            return false;
        }
        return ObjectUtil.nullSafeEquals(positionKey, projection.positionKey);
    }

    @Override
    public int hashCode() {

        int result = ObjectUtil.nullSafeHashCode(criteria);
        result = 31 * result + ObjectUtil.nullSafeHashCode(slices);
        result = 31 * result + ObjectUtil.nullSafeHashCode(elemMatchs);
        result = 31 * result + ObjectUtil.nullSafeHashCode(positionKey);
        result = 31 * result + positionValue;
        return result;
    }
}
