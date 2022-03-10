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

import com.whaleal.mars.core.query.updates.UpdateOperator;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;




public class UpdatePipeline implements UpdateDefinition {

    public static final String UpdatePipeline = "_updatepipeline";

    private final List< UpdateOperator > updates = new ArrayList<>();


    public UpdatePipeline(
            List< UpdateOperator > updates ) {

        this.updates.addAll(updates);

    }


    /**
     * Adds a new operator to this update operation.
     */
    public void add( UpdateOperator operator ) {
        updates.add(operator);
    }


    protected List< UpdateOperator > getUpdates() {
        return updates;
    }

    @Override
    public Boolean isIsolated() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Document getUpdateObject() {
        Document docs = new Document();
        if (updates.isEmpty()) {
            return docs;
        } else if (updates.size() == 1) {
            docs = updates.get(0).toDocument();
            return docs;
        } else {
            List< Document > documentList = new ArrayList<>();
            for (UpdateOperator updateOperator : updates) {
                documentList.add(updateOperator.toDocument());
            }
            docs.put(UpdatePipeline, documentList);
            return docs;
        }
    }

    @Override
    public boolean modifies( String key ) {
        return false;
    }

    @Override
    public void inc( String key ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List< ArrayFilter > getArrayFilters() {
        throw new UnsupportedOperationException();
    }
}
