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
package com.whaleal.mars.core.index;

import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Index {


    private final Map<String, IndexDirection> key = new LinkedHashMap<String, IndexDirection>();

    private IndexOptions options;

    public Index() {
    }

    public Index(String field, IndexDirection direction, IndexOptions options) {

        key.put(field, direction);
        this.options = options;

    }

    public Index on(String field, IndexDirection direction) {
        key.put(field, direction);
        return this;
    }


    public Index setOptions(IndexOptions options) {
        this.options = options;
        return this;
    }


    public Document getIndexKeys() {

        Document document = new Document();

        for (Entry<String, IndexDirection> entry : key.entrySet()) {
            document.put(entry.getKey(), entry.getValue().toIndexValue());
        }

        return document;
    }


    public IndexOptions getIndexOptions() {

        return options;

    }

    @Override
    public String toString() {
        return String.format("Index: %s - Options: %s", getIndexKeys(), getIndexOptions());
    }
}
