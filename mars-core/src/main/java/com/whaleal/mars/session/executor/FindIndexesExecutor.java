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
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.session.option.IndexOptions;
import com.whaleal.mars.session.option.Options;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: cx
 * @date: 2021/1/8
 */
public class FindIndexesExecutor implements CrudExecutor {
    @Override
    public <T> T execute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        ListIndexesIterable indexIterable = null;

        if (session == null) {
            indexIterable = collection.listIndexes();

        } else {

            indexIterable = collection.listIndexes(session);
        }


        MongoCursor iterator = indexIterable.iterator();

        Index index = null;

        List indexes = new ArrayList();


        while (iterator.hasNext()) {

            Document indexDocument = (Document) iterator.next();

            IndexOptions indexOptions = new IndexOptions();

            if (indexDocument.get("name") != null) {
                indexOptions.name((String) indexDocument.get("name"));
            }
            if (indexDocument.get("partialFilterExpression") != null) {
                indexOptions.partialFilterExpression((Bson) indexDocument.get("partialFilterExpression"));
            }
            if (indexDocument.get("weights") != null) {
                indexOptions.weights((Bson) indexDocument.get("weights"));
            }
            if (indexDocument.get("storageEngine") != null) {//不常用到
                indexOptions.storageEngine((Bson) indexDocument.get("storageEngine"));
            }
            if (indexDocument.get("wildcardProjection") != null) {
                indexOptions.wildcardProjection((Bson) indexDocument.get("wildcardProjection"));
            }
            if (indexDocument.get("collation") != null) {
                Document document = (Document) indexDocument.get("collation");
                Collation collation = Collation.from(document);
                indexOptions.collation(collation.toMongoCollation());
            }
            /*if (indexDocument.get("version") != null) {         官方文档也没有这个Index偏好设置，只在IndexOptions类里面有
                indexOptions.version((Integer) indexDocument.get("version"));
            }*/
            if (indexDocument.get("unique") != null) {
                indexOptions.unique((Boolean) indexDocument.get("unique"));
            }
            if (indexDocument.get("bits") != null) {
                indexOptions.bits((Integer) indexDocument.get("bits"));
            }
            if (indexDocument.get("bucketSize") != null) {
                indexOptions.bucketSize((Double) indexDocument.get("bucketSize"));
            }
            if (indexDocument.get("default_language") != null) {
                indexOptions.defaultLanguage((String) indexDocument.get("default_language"));
            }
            if (indexDocument.get("expireAfterSeconds") != null) {
                Long expireAfter = (Long) indexDocument.get("expireAfterSeconds");//秒以下会丢失
                indexOptions.expireAfter(expireAfter, TimeUnit.SECONDS);
            }
            if (indexDocument.get("hidden") != null) {
                indexOptions.hidden((Boolean) indexDocument.get("hidden"));
            }
            if (indexDocument.get("language_override") != null) {
                indexOptions.languageOverride((String) indexDocument.get("language_override"));
            }
            if (indexDocument.get("max") != null) {
                indexOptions.max((Double) indexDocument.get("max"));
            }
            if (indexDocument.get("min") != null) {
                indexOptions.min((Double) indexDocument.get("min"));
            }
            if (indexDocument.get("sparse") != null) {
                indexOptions.sparse((Boolean) indexDocument.get("sparse"));
            }
            if (indexDocument.get("2dsphereIndexVersion") != null) {
                indexOptions.sphereVersion((Integer) indexDocument.get("2dsphereIndexVersion"));
            }
            if (indexDocument.get("background") != null) {
                indexOptions.background((Boolean) indexDocument.get("background"));
            }
            if (indexDocument.get("textIndexVersion") != null) {
                indexOptions.textVersion((Integer) indexDocument.get("textIndexVersion"));
            }

            Document key = (Document) indexDocument.get("key");

            index = new Index();

            Set<String> strings = key.keySet();

            for (String keyName : strings) {
                index.on(keyName, IndexDirection.fromValue(key.get(keyName)));

            }
            index.setOptions(indexOptions);

            indexes.add(index);

        }

        return (T) indexes;
    }
}
