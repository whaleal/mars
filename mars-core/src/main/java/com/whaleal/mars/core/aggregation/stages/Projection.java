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

import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.codecs.MarsOrmException;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.expressions.impls.Fields;
import com.whaleal.mars.core.aggregation.expressions.impls.PipelineField;

import com.whaleal.mars.core.aggregation.stages.filters.Filter;

import org.bson.Document;

import java.util.*;


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
public class Projection extends Stage {
    private Fields<Projection> includes;
    private Fields<Projection> excludes;
    // 压制 _id
    private boolean suppressId;
    //{instock: { $slice: -1 }}
    private Map<String,ArraySlice> slices ;


    protected Projection() {
        super("$project");
    }


    public static Projection of() {
        return new Projection();
    }


    public Projection exclude(String name) {
        return exclude(name, Expressions.value(false));
    }


    public List<PipelineField> getFields() {
        List<PipelineField> fields = new ArrayList<>();

        if (includes != null) {
            fields.addAll(includes.getFields());
        }
        if (excludes != null) {
            fields.addAll(excludes.getFields());
        }

        if (suppressId) {
            fields.add(new PipelineField("_id", Expressions.value(false)));
        }
        if(slices != null){
            for (Map.Entry< String, ArraySlice > arraySliceEntry : slices.entrySet()) {
                fields.add(new PipelineField(arraySliceEntry.getKey(),Expressions.value(arraySliceEntry.getValue().toDocument())));
            }
        }
       

        return fields;
    }



    public Projection include(String name, Expression value) {
        if (includes == null) {
            includes = Fields.on(this);
        }
        includes.add(name, value);
        validateProjections();
        return this;
    }


    public Projection include(String name) {
        return include(name, Expressions.value(true));
    }


    /**
     * Project a {@code $slice} of the array {@code field} using the first {@code size} elements.
     *
     * @param field the document field name to project, must be an array field.
     * @param size  the number of elements to include.
     * @return {@code this} field projection instance.
     */
    public Projection slice( String field, int size) {
        if(slices ==null){
            slices = new HashMap<>();
        }
        Precondition.notNull(field, "Key must not be null!");

        slices.put(field, new ArraySlice(size));

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
    public Projection slice( String field, int offset, int size) {
        if(slices ==null){
            slices = new HashMap<>();
        }
        slices.put(field, new ArraySlice(offset,size));
        return this;
    }

    public Projection slice( String field, ArraySlice slice) {
        if(slices ==null){
            slices = new HashMap<>();
        }
        slices.put(field, slice);
        return this;
    }

    /**
     * 压制 _id 用
     * @return this
     */
    public Projection suppressId() {
        suppressId = true;
        return this;
    }

    private Projection exclude(String name, Expression value) {
        if (excludes == null) {
            excludes = Fields.on(this);
        }
        excludes.add(name, value);
        validateProjections();
        return this;
    }

    private void validateProjections() {
        // 当project 有数据时 就需要校验
        if (includes != null && excludes != null) {

             // _id 相关直接使用  suppressId 相关方法
            if (excludes.size() > 1 || !"_id".equals(excludes.getFields().get(0).getName())) {
                throw new MarsOrmException();
            }
        }
    }


    /**
     * Defines array slicing options for query projections.
     */
    public class ArraySlice {
        private final Integer limit;
        private Integer skip;

        /**
         * Specifies the number of array elements to return
         *
         * @param limit the number of array elements to return
         *
         */
        public ArraySlice(int limit) {
            this.limit = limit;
        }

        /**
         * Specifies the number of array elements to skip.
         *
         * @param skip  the number of array elements to skip
         * @param limit the number of array elements to return
         *
         */
        public ArraySlice(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }

        /**
         * @return the limit to apply to the projection
         */
        public Integer getLimit() {
            return limit;
        }

        /**
         * @return the skip value to apply to the projection
         */
        public Integer getSkip() {
            return skip;
        }

        Document toDocument() {
            return new Document("$slice", skip == null ? limit : Arrays.asList(skip, limit));

        }


    }

}
