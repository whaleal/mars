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
import org.bson.Document;

import static com.whaleal.mars.codecs.writer.PendingValue.SLUG;
import static java.lang.String.format;

class NameState extends WriteState {
    private final String name;
    private ValueState value = SLUG;

    NameState(DocumentWriter writer, String name, WriteState previous) {
        super(writer, previous);
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return format("%s: %s", name, value);
    }

    @Nullable
    public Object value() {
        return value.value();
    }

    @Override
    protected String state() {
        return "name";
    }

    @Override
    WriteState array() {
        value = new ArrayState(getWriter(), this);
        return value;
    }

    @Override
    WriteState document() {
        value = new DocumentState(getWriter(), this);
        return value;
    }

    @Override
    void done() {
        end();
    }

    @Override
    void value(Object value) {
        this.value = new SingleValue(getWriter(), value, this);
        this.value.end();
    }
}
