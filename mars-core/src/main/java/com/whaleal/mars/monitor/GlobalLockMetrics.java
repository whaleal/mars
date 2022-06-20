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
package com.whaleal.mars.monitor;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import org.bson.Document;

import java.util.Date;

/**
 * JMX Metrics for Global Locks
 * 解析db.serverStatus的globalLock参数,对应OPS中的queue
 */
public class GlobalLockMetrics extends AbstractMonitor {


    public GlobalLockMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Long getTotalTime() {
        return getGlobalLockData("totalTime", Long.class);
    }

    public Integer getCurrentQueueTotal() {
        return getCurrentQueue("total");
    }

    public Integer getCurrentQueueReaders() {
        return getCurrentQueue("readers");
    }

    public Integer getCurrentQueueWriters() {
        return getCurrentQueue("writers");
    }

    public Integer getActiveClientsTotal() {
        return getaActiveClients("total");
    }

    public Integer getActiveClientsReaders() {
        return getaActiveClients("readers");
    }

    public Integer getActiveClientsWriters() {
        return getaActiveClients("writers");
    }
    @SuppressWarnings("unchecked")
    private <T> T getGlobalLockData(String key, Class<T> targetClass) {

        BasicDBObject globalLock1 = BasicDBObject.parse(getServerStatus().get("globalLock",Document.class).toJson());
        return (T) globalLock1.get(key);
    }

    private Integer getCurrentQueue(String key) {
        Document globalLock = (Document) getServerStatus().get("globalLock");
        Document currentQueue = (Document) globalLock.get("currentQueue");
        return (Integer) currentQueue.get(key);
    }

    private Integer getaActiveClients(String key) {
        Document globalLock = (Document) getServerStatus().get("globalLock");
        Document currentQueue = (Document) globalLock.get("activeClients");
        return (Integer) currentQueue.get(key);
    }
}
