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

import com.mongodb.client.model.MergeOptions.WhenMatched;
import com.mongodb.client.model.MergeOptions.WhenNotMatched;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;


public class Merge<M> extends Stage {
    private Class<M> type;
    private String database;
    private String collection;
    private List<String> on;
    private Map<String, Expression> variables;
    private WhenMatched whenMatched;
    private List<Stage> whenMatchedPipeline;
    private WhenNotMatched whenNotMatched;

    protected Merge(Class<M> type) {
        this();
        this.type = type;
    }

    protected Merge() {
        super("$merge");
    }

    protected Merge(String collection) {
        this();
        this.collection = collection;
    }

    protected Merge(String database, String collection) {
        this();
        this.database = database;
        this.collection = collection;
    }


    public static <M> Merge<M> into(Class<M> type) {
        return new Merge<>(type);
    }


    public static Merge<?> into(String collection) {
        return new Merge<>(collection);
    }


    public static Merge<?> into(String database, String collection) {
        return new Merge<>(database, collection);
    }


    public String getCollection() {
        return collection;
    }


    public String getDatabase() {
        return database;
    }


    public List<String> getOn() {
        return on;
    }

    public Class<M> getType() {
        return type;
    }

    public Map<String, Expression> getVariables() {
        return variables;
    }

    public WhenMatched getWhenMatched() {
        return whenMatched;
    }


    public List<Stage> getWhenMatchedPipeline() {
        return whenMatchedPipeline;
    }


    public WhenNotMatched getWhenNotMatched() {
        return whenNotMatched;
    }


    public Merge<M> let(String variable, Expression value) {
        if (variables == null) {
            variables = new LinkedHashMap<>();
        }
        variables.put(variable, value);
        return this;
    }


    public Merge<M> on(String field, String... fields) {
        List<String> list = new ArrayList<>();
        list.add(field);
        list.addAll(asList(fields));
        this.on = list;
        return this;
    }


    public Merge<M> whenMatched(WhenMatched whenMatched) {
        this.whenMatched = whenMatched;
        return this;
    }


    public Merge<M> whenMatched(List<Stage> pipeline) {
        this.whenMatchedPipeline = pipeline;
        return this;
    }


    public Merge<M> whenNotMatched(WhenNotMatched whenNotMatched) {
        this.whenNotMatched = whenNotMatched;
        return this;
    }
}
