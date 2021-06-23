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
package com.whaleal.mars.bson.codecs.reader;

import org.bson.BsonType;

import java.util.List;

import static java.util.stream.Collectors.toList;

class ArrayState extends ReaderState {
    public static final String NAME = "ARRAY";
    private final List<?> values;
    private ArrayEndState endState;

    ArrayState(DocumentReader reader, List<?> values) {
        super(reader);
        this.values = values;
    }

    @Override
    void skipValue() {
        reader().state(endState != null ? endState.nextState() : nextState());
    }

    @Override
    BsonType getCurrentBsonType() {
        return BsonType.ARRAY;
    }

    @Override
    String getStateName() {
        return NAME;
    }

    @Override
    void startArray() {
        if (endState == null) {
            List<ReaderState> states = values.stream()
                    .map(this::valueState)
                    .collect(toList());
            ReaderState docState = null;
            for (ReaderState state : states) {
                if (docState != null) {
                    docState.next(state);
                } else {
                    next(state);
                }
                docState = state;
            }

            endState = new ArrayEndState(reader());
            if (docState != null) {
                docState.next(endState);
            } else {
                next(endState);
            }

        }
        advance();
    }

}
