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
import com.whaleal.mars.util.ObjectUtils;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Projection projection.
 *
 * 其实就是一个projection
 */
public class Projection {

    private final Map<String, Integer> criteria = new HashMap<>();
    private final Map<String, Object> slices = new HashMap<>();
    private final Map<String, Criteria> elemMatchs = new HashMap<>();
    private @Nullable
    String positionKey;
    private int positionValue;

    /**
     * Include a single {@code field} to be returned by the query operation.
     *
     * @param field the document field name to be included.
     * @return {@code this} field projection instance.
     */
    public Projection include(String field) {

        Assert.notNull(field, "Key must not be null!");

        criteria.put(field, 1);

        return this;
    }

    /**
     * Include one or more {@code fields} to be returned by the query operation.
     *
     * @param fields the document field names to be included.
     * @return {@code this} field projection instance.
     */
    public Projection include(String... fields) {

        Assert.notNull(fields, "Keys must not be null!");

        for (String key : fields) {
            criteria.put(key, 1);
        }

        return this;
    }

    /**
     * Exclude a single {@code field} from being returned by the query operation.
     *
     * @param field the document field name to be included.
     * @return {@code this} field projection instance.
     */
    public Projection exclude(String field) {

        Assert.notNull(field, "Key must not be null!");

        criteria.put(field, 0);

        return this;
    }

    /**
     * Exclude one or more {@code fields} from being returned by the query operation.
     *
     * @param fields the document field names to be included.
     * @return {@code this} field projection instance.
     */
    public Projection exclude(String... fields) {

        Assert.notNull(fields, "Keys must not be null!");

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

        Assert.notNull(field, "Key must not be null!");

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

        Assert.hasText(field, "DocumentField must not be null or empty!");

        positionKey = field;
        positionValue = value;

        return this;
    }

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
        if (!ObjectUtils.nullSafeEquals(criteria, projection.criteria)) {
            return false;
        }
        if (!ObjectUtils.nullSafeEquals(slices, projection.slices)) {
            return false;
        }
        if (!ObjectUtils.nullSafeEquals(elemMatchs, projection.elemMatchs)) {
            return false;
        }
        return ObjectUtils.nullSafeEquals(positionKey, projection.positionKey);
    }

    @Override
    public int hashCode() {

        int result = ObjectUtils.nullSafeHashCode(criteria);
        result = 31 * result + ObjectUtils.nullSafeHashCode(slices);
        result = 31 * result + ObjectUtils.nullSafeHashCode(elemMatchs);
        result = 31 * result + ObjectUtils.nullSafeHashCode(positionKey);
        result = 31 * result + positionValue;
        return result;
    }
}
