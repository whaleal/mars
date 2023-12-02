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
package com.whaleal.mars.codecs.reader;

import com.mongodb.DBRef;
import org.bson.BsonBinary;
import org.bson.BsonType;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

abstract class ReaderState {
    private final DocumentReader reader;
    private ReaderState nextState;

    ReaderState(DocumentReader reader) {
        this.reader = reader;
    }


    public ReaderState nextState() {
        return nextState;
    }

    public DocumentReader reader() {
        return reader;
    }

    @Override
    public String toString() {
        return getStateName();
    }

    protected ReaderState valueState(Object value) {
        Object unwind = unwind(value);
        if (unwind instanceof Document) {
            return new DocumentState(reader, (Document) unwind);
        } else if (unwind instanceof List) {
            return new ArrayState(reader, (List<?>) unwind);
        }
        return new ValueState(reader, unwind);
    }

    void advance() {
        reader.state(nextState);
    }

    void endArray() {
        throw new IllegalStateException("invalidReaderState" + "endArray" + ArrayState.NAME + getStateName());
    }

    void endDocument() {
        throw new IllegalStateException("invalidReaderState" + "endDocument" + DocumentState.NAME + getStateName());
    }

    abstract BsonType getCurrentBsonType();

    abstract String getStateName();

    String name() {
        throw new IllegalStateException("invalidReaderState" + "readName" + NameState.NAME + getStateName());
    }

    void next(ReaderState next) {
        ReaderState old = nextState;
        nextState = next;
        if (nextState != null) {
            nextState.nextState = old;
        }
    }

    void skipName() {
        throw new IllegalStateException("invalidReaderState" + "skipName" + NameState.NAME + getStateName());
    }

    void skipValue() {
        throw new IllegalStateException("invalidReaderState" + "skipValue" + ValueState.NAME + getStateName());
    }

    void startArray() {
        throw new IllegalStateException(("startArray" + ArrayState.NAME + getStateName()));
    }

    void startDocument() {
        throw new IllegalStateException("invalidReaderState" + "startDocument" + DocumentState.NAME + getStateName());
    }

    private Object unwind(Object value) {
        Object unwind = value;
        if (value instanceof DBRef) {
            DBRef dbRef = (DBRef) value;
            Document document = new Document("$ref", dbRef.getCollectionName())
                    .append("$id", dbRef.getId());
            if (dbRef.getDatabaseName() != null) {
                document.append("$db", dbRef.getDatabaseName());
            }
            unwind = document;
        } else if (value instanceof UUID) {
            unwind = new BsonBinary((UUID) value);
        }
        return unwind;
    }

    <T> T value() {
        throw new IllegalStateException("invalidReaderState" + "read value" + ValueState.NAME + getStateName());
    }
}
