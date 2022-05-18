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
package com.whaleal.mars.codecs.writer;

import com.mongodb.lang.Nullable;
import com.whaleal.icefrog.core.collection.ListUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


class DocumentState extends ValueState<Map<String, Object>> {
    private final List<NameState> values = new ArrayList<>();
    private Document finished;

    DocumentState(DocumentWriter writer, WriteState previous) {
        super(writer, previous);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", finished != null ? "}" : "");
        values.forEach(v -> joiner.add(v.toString()));
        return joiner.toString();
    }

    @Override
    public Map<String, Object> value() {
        return finished;
    }

    @Override
    protected String state() {
        return "document";
    }

    @SuppressWarnings("unchecked")
    private Document andTogether(Document doc, String key, @Nullable Object additional) {
        if (additional != null) {
            Document newSubdoc = new Document(key, additional);
            Object extant = doc.remove(key);
            List<Document> and = (List<Document>) doc.get("$and");
            if (and != null) {
                and.add(newSubdoc);
            } else {
                and = new ArrayList<>();
                and.addAll(ListUtil.of(new Document(key, extant), newSubdoc));
                doc.put("$and", and);
                return newSubdoc;
            }
        }
        return doc;
    }

    @Override
    void end() {
        finished = new Document();
        values.forEach(v -> {
            Object doc = finished.get(v.name());
            if (doc == null) {
                finished.put(v.name(), v.value());
            } else {
                andTogether(finished, v.name(), v.value());
            }
        });
        super.end();
    }

    @Override
    NameState name(String name) {
        NameState state = new NameState(getWriter(), name, this);
        values.add(state);
        return state;
    }
}
