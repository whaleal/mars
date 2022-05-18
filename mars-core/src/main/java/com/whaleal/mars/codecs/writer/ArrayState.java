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

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

class ArrayState extends ValueState<List<Object>> {
    private final List<Object> list = new ArrayList<>();
    private boolean finished = false;
    private ValueState<?> substate;

    ArrayState(DocumentWriter writer, WriteState previous) {
        super(writer, previous);
    }

    public List<Object> getList() {
        return list;
    }

    @Override
    protected String state() {
        return "array";
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", finished ? "]" : "");
        list.forEach(v -> joiner.add(v instanceof Document ? ((Document) v).toJson() : String.valueOf(v)));
        if (substate != null) {
            joiner.add(substate.toString());
        }
        return joiner.toString();
    }

    @Override
    public List<Object> value() {
        return list;
    }

    @Override
    void value(Object value) {
        list.add(value);
    }

    @Override
    WriteState array() {
        substate = new ArrayState(getWriter(), this);
        return substate;
    }

    @Override
    WriteState document() {
        substate = new DocumentState(getWriter(), this);
        return substate;
    }

    @Override
    void end() {
        finished = true;
        if (substate != null) {
            substate.end();
        }
        super.end();
    }

    @Override
    void done() {
        if (substate != null) {
            list.add(substate.value());
            substate = null;
        }
        super.done();
    }
}