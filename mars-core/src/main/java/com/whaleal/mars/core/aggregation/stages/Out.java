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

import org.bson.Document;


public class Out<O> extends Stage {
    private Class<?> type;
    private String collection;

    protected Out() {
        super("$out");
    }

    public static <O> Out<O> to(Class<O> type) {
        return new Out<O>()
                .type(type);
    }


    public static Out<Document> to(String collection) {
        return new Out<Document>()
                .collection(collection);
    }


    public String getCollection() {
        return collection;
    }


    public Class<?> getType() {
        return type;
    }

    private Out<O> collection(String collection) {
        this.collection = collection;
        return this;
    }

    private Out<O> type(Class<O> type) {
        this.type = type;
        return this;
    }
}
