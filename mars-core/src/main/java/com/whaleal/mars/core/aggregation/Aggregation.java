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
package com.whaleal.mars.core.aggregation;


import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.*;

import com.whaleal.mars.core.query.filters.Filter;
import com.whaleal.mars.session.option.AggregationOptions;

/**
 *
 * 聚合操作的上层接口
 * 目前 本接口只对包内可见
 * 对外展示 全部使用 其具体实现 AggregationPipeline
 *
 * 其他方面 需要考虑 updatePipeLine
 *
 *
 *
 * @author wh
 *
 * @param <T>  返回类型
 */
interface Aggregation<T>  {

    Aggregation<T> addFields(AddFields fields);


    Aggregation<T> autoBucket(AutoBucket bucket);


    Aggregation<T> bucket(Bucket bucket);


    Aggregation<T> collStats(CollectionStats stats);


    Aggregation<T> count(String name);


    Aggregation<T> currentOp(CurrentOp currentOp);



    Aggregation<T> facet(Facet facet);


    Aggregation<T> geoNear(GeoNear near);


    Aggregation<T> graphLookup(GraphLookup lookup);


    Aggregation<T> group(Group group);


    Aggregation<T> indexStats();


    Aggregation<T> limit(long limit);


    Aggregation<T> lookup(Lookup lookup);


    Aggregation<T> match( Filter... filters);

    <M> void merge(Merge<M> merge);

    <M> void merge(Merge<M> merge, AggregationOptions options);

    <O> void out(Out<O> out);


    <O> void out(Out<O> out, AggregationOptions options);


    Aggregation<T> planCacheStats();


    Aggregation<T> project(Projection projection);


    Aggregation<T> redact(Redact redact);


    Aggregation<T> replaceRoot(ReplaceRoot root);


    Aggregation<T> replaceWith(ReplaceWith with);


    Aggregation<T> sample(long sample);


    default Aggregation<T> set(AddFields fields) {
        return addFields(fields);
    }

    Aggregation<T> skip(long skip);


    Aggregation<T> sort(Sort sort);


    Aggregation<T> sortByCount(Expression sort);


    Aggregation<T> unionWith(Class<?> type, Stage first, Stage... others);


    Aggregation<T> unionWith(String collection, Stage first, Stage... others);


    Aggregation<T> unset(Unset unset);

    Aggregation<T> unwind(Unwind unwind);

    AggregationPipeline<T> setWindowFields(SetWindowFields fields);
    AggregationPipeline<T> set(Set set);
}
