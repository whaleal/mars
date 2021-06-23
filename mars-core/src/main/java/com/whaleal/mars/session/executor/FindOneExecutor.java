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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.option.Option;

/**
 * @author cx
 * @Date 2021/1/3
 * <p>
 * 原生的findOne  不支持 skip  sort 等操作
 * <p>
 * 这里采用 find  操作
 * 强制 使用limit（1）
 */
public class FindOneExecutor implements CrudExecutor {
    @Override
    public <T> T execute(ClientSession session, MongoCollection collection, Query query, Option options, Object data) {

        FindIterable findIterable;

        if (session == null) {

            findIterable = collection.find(query.getQueryObject());

        } else {

            findIterable = collection.find(session, query.getQueryObject());

        }


        if (query.getSkip() > 0) {
            findIterable = findIterable.skip((int) query.getSkip());
        }

        if (query.getSortObject() != null) {
            findIterable = findIterable.sort(query.getSortObject());
        }

        findIterable.limit(1);

        return (T) findIterable.first();


    }

}
