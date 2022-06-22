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


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.stages.*;

import com.whaleal.mars.core.query.filters.Filter;
import com.whaleal.mars.session.option.AggregationOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AggregationPipeline<T> implements Aggregation<T> {

    private final List<Stage> stages = new ArrayList<>();

    public Class< T > getOutputType() {
        return outputType;
    }


    private final Class<T>  outputType ;

    private AggregationPipeline(Class<T> outputType ){
        this.outputType = outputType ;
    }

    private AggregationPipeline(Class<T> outputType ,List<Stage> stages ){
        this.outputType = outputType ;
        this.stages.addAll(stages);
    }

    public static <T> AggregationPipeline<T> create(Class<T> outputType,List<Stage> stages){
        Precondition.checkNotNull(outputType,"outputType can't be null in AggregationPipeline") ;
        return new AggregationPipeline<T>(outputType,stages);
    }

    public static <T> AggregationPipeline<T> create(Class<T> outputType){
        Precondition.checkNotNull(outputType,"outputType can't be null in AggregationPipeline") ;
        return new AggregationPipeline<T>(outputType);
    }

    public static AggregationPipeline<Document> create() {
        return new AggregationPipeline<Document>(Document.class);
    }

    @Override
    public AggregationPipeline<T> addFields(AddFields fields) {
        stages.add(fields);
        return this;
    }

    @Override
    public AggregationPipeline<T> autoBucket(AutoBucket bucket) {
        stages.add(bucket);
        return this;
    }

    @Override
    public AggregationPipeline<T> bucket(Bucket bucket) {
        stages.add(bucket);
        return this;
    }

    @Override
    public AggregationPipeline<T> collStats(CollectionStats stats) {
        stages.add(stats);
        return this;
    }

    @Override
    public AggregationPipeline<T> count(String name) {
        stages.add(new Count(name));
        return this;
    }

    @Override
    public AggregationPipeline<T> currentOp(CurrentOp currentOp) {
        stages.add(currentOp);
        return this;
    }


    @Override
    public AggregationPipeline<T> facet(Facet facet) {
        stages.add(facet);
        return this;
    }

    @Override
    public AggregationPipeline<T> geoNear(GeoNear near) {
        stages.add(near);
        return this;
    }

    @Override
    public AggregationPipeline<T> graphLookup(GraphLookup lookup) {
        stages.add(lookup);
        return this;
    }

    @Override
    public AggregationPipeline<T> group(Group group) {
        stages.add(group);
        return this;
    }

    @Override
    public AggregationPipeline<T> indexStats() {
        stages.add(IndexStats.of());
        return this;
    }

    @Override
    public AggregationPipeline<T> limit(long limit) {
        stages.add(Limit.of(limit));
        return this;
    }

    @Override
    public AggregationPipeline<T> lookup(Lookup lookup) {
        stages.add(lookup);
        return this;
    }

    @Override
    public AggregationPipeline<T> match( Filter... filters) {
        if (stages.isEmpty()) {
            Arrays.stream(filters)
                    .filter(f -> f.getName().equals("$eq"))
                    .forEach(f -> f.entityType(outputType));
        }
        stages.add(Match.match(filters));
        return this;
    }
    @Override
    public AggregationPipeline<T> set(Set set) {
        stages.add(set);
        return this;
    }



    @Override
    public AggregationPipeline<T> setWindowFields(SetWindowFields fields) {
        stages.add(fields);
        return this;
    }

    @Override
    public <M> void merge(Merge<M> merge) {
        stages.add(merge);

    }

    @Override
    public <M> void merge(Merge<M> merge, AggregationOptions options) {
        stages.add(merge);

    }

    @Override
    public <O> void out(Out<O> out) {
        stages.add(out);

    }

    @Override
    public <O> void out(Out<O> out, AggregationOptions options) {
        stages.add(out);

    }

    @Override
    public AggregationPipeline<T> planCacheStats() {
        stages.add(PlanCacheStats.of());
        return this;
    }

    @Override
    public AggregationPipeline<T> project(Projection projection) {
        stages.add(projection);
        return this;
    }

    @Override
    public AggregationPipeline<T> redact(Redact redact) {
        stages.add(redact);
        return this;
    }

    @Override
    public AggregationPipeline<T> replaceRoot(ReplaceRoot root) {
        stages.add(root);
        return this;
    }

    @Override
    public AggregationPipeline<T> replaceWith(ReplaceWith with) {
        stages.add(with);
        return this;
    }

    @Override
    public AggregationPipeline<T> sample(long sample) {
        stages.add(Sample.of(sample));
        return this;
    }

    @Override
    public AggregationPipeline<T> skip(long skip) {
        stages.add(Skip.of(skip));
        return this;
    }

    @Override
    public AggregationPipeline<T> sort(Sort sort) {
        stages.add(sort);
        return this;
    }

    @Override
    public AggregationPipeline<T> sortByCount(Expression sort) {
        stages.add(SortByCount.sortByCount(sort));
        return this;
    }

    @Override
    public AggregationPipeline<T> unionWith(Class<?> type, Stage first, Stage... others) {
        stages.add(new UnionWith(type, Expressions.toList(first, others)));
        return this;
    }

    @Override
    public AggregationPipeline<T> unionWith(String collection, Stage first, Stage... others) {
        stages.add(new UnionWith(collection, Expressions.toList(first, others)));
        return this;
    }

    @Override
    public AggregationPipeline<T> unset(Unset unset) {
        stages.add(unset);
        return this;
    }

    @Override
    public AggregationPipeline<T> unwind(Unwind unwind) {
        stages.add(unwind);
        return this;
    }


    public List<Stage> getInnerStage() {
        return stages;
    }


}
