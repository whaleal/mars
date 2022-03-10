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
package com.whaleal.mars.core.query;


import org.bson.Document;

import java.util.Arrays;
import java.util.Collections;


public class BasicUpdate extends Update {

    protected Document updateObject;

    public BasicUpdate(String updateString) {
        super();
        this.updateObject = Document.parse(updateString);
    }

    public BasicUpdate(Document updateObject) {
        super();
        this.updateObject = updateObject;
    }

    @Override
    public Update set( String key, Object value ) {
        updateObject.put("$set", Collections.singletonMap(key, value));
        return this;
    }

    @Override
    public Update unset(String key) {
        updateObject.put("$unset", Collections.singletonMap(key, 1));
        return this;
    }

    @Override
    public Update inc(String key, Number inc) {
        updateObject.put("$inc", Collections.singletonMap(key, inc));
        return this;
    }

    @Override
    public Update push( String key, Object value ) {
        updateObject.put("$push", Collections.singletonMap(key, value));
        return this;
    }

    @Override
    @Deprecated
    public Update pushAll(String key, Object[] values) {
        Document keyValue = new Document();
        keyValue.put(key, values);
        updateObject.put("$pushAll", keyValue);
        return this;
    }

    @Override
    public Update addToSet( String key, Object value ) {
        updateObject.put("$addToSet", Collections.singletonMap(key, value));
        return this;
    }

    @Override
    public Update pop(String key, Position pos) {
        updateObject.put("$pop", Collections.singletonMap(key, (pos == Position.FIRST ? -1 : 1)));
        return this;
    }

    @Override
    public Update pull( String key, Object value ) {
        updateObject.put("$pull", Collections.singletonMap(key, value));
        return this;
    }

    @Override
    public Update pullAll(String key, Object[] values) {
        Document keyValue = new Document();
        keyValue.put(key, Arrays.copyOf(values, values.length));
        updateObject.put("$pullAll", keyValue);
        return this;
    }

    @Override
    public Update rename(String oldName, String newName) {
        updateObject.put("$rename", Collections.singletonMap(oldName, newName));
        return this;
    }

    @Override
    public Document getUpdateObject() {
        return updateObject;
    }

}
