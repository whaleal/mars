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
package com.whaleal.mars.core.messaging;

import com.mongodb.CursorType;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
//import com.mongodb.client.model.Collation;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.internal.ErrorHandler;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

class TailableCursorTask extends CursorReadingTask<Document, Object> {


    @SuppressWarnings({"unchecked", "rawtypes"})
    public TailableCursorTask(Mars mars, TailableCursorRequest<?> request, Class<?> targetType,
                              ErrorHandler errorHandler) {
        super(mars, (TailableCursorRequest) request, (Class) targetType, errorHandler);
    }


    @Override
    protected MongoCursor<Document> initCursor(Mars mars, SubscriptionRequest.RequestOptions options, Class<?> targetType) {

        Document filter = new Document();
        com.mongodb.client.model.Collation collation = null;

        if (options instanceof TailableCursorRequest.TailableCursorRequestOptions) {

            TailableCursorRequest.TailableCursorRequestOptions requestOptions = (TailableCursorRequest.TailableCursorRequestOptions) options;
            if (requestOptions.getQuery().isPresent()) {

                Query query = requestOptions.getQuery().get();

                filter.putAll(query.getQueryObject());

                collation = query.getCollation().get().toMongoCollation();
            }
        }

        FindIterable<Document> iterable = mars.getDatabase().getCollection(options.getCollectionName()).find(filter)
                .cursorType(CursorType.TailableAwait).noCursorTimeout(true);

        if (collation != null) {
            iterable = iterable.collation(collation);
        }

        if (!options.maxAwaitTime().isZero()) {
            iterable = iterable.maxAwaitTime(options.maxAwaitTime().toMillis(), TimeUnit.MILLISECONDS);
        }

        return iterable.iterator();
    }
}