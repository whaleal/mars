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
package com.whaleal.mars.session.executor;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.option.InsertOneOptions;
import com.whaleal.mars.session.option.Options;
import com.whaleal.mars.session.result.InsertOneResult;

/**
 * @author cx
 * @Date 2020/12/31
 */
public class InsertOneExecutor implements CrudExecutor {
    @Override
    public <T> T execute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        InsertOneResult insertOneResult = new InsertOneResult();

        //Entity insertDocument = new Entity((Map) data);

        if (options == null) {

            if (session == null) {

                insertOneResult.setOriginInsertOneResult(collection.insertOne(data));

            } else {

                insertOneResult.setOriginInsertOneResult(collection.insertOne(session, data));

            }

            return (T) insertOneResult;

        }

        if (!(options instanceof InsertOneOptions)) {
            throw new ClassCastException();
        }


        InsertOneOptions insertOneOptions = (InsertOneOptions) options;

        if (session == null) {

            insertOneResult.setOriginInsertOneResult(collection.insertOne(data, insertOneOptions.getOriginOptions()));

        } else {

            insertOneResult.setOriginInsertOneResult(collection.insertOne(session, data, insertOneOptions.getOriginOptions()));

        }
        return (T) insertOneResult;
    }
}
