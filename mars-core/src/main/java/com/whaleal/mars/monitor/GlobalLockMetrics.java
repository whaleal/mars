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

import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import org.bson.Document;

/**
 * JMX Metrics for Global Locks
 */
public class GlobalLockMetrics extends AbstractMonitor {


    public GlobalLockMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public double getTotalTime() {
        return getGlobalLockData("totalTime", Double.class);
    }

    public double getLockTime() {
        return getGlobalLockData("lockTime", Double.class);
    }

    public double getLockTimeRatio() {
        return getGlobalLockData("ratio", Double.class);
    }

    public int getCurrentQueueTotal() {
        return getCurrentQueue("total");
    }

    public int getCurrentQueueReaders() {
        return getCurrentQueue("readers");
    }

    public int getCurrentQueueWriters() {
        return getCurrentQueue("writers");
    }

    @SuppressWarnings("unchecked")
    private <T> T getGlobalLockData(String key, Class<T> targetClass) {
        DBObject globalLock = (DBObject) getServerStatus().get("globalLock");
        return (T) globalLock.get(key);
    }

    private int getCurrentQueue(String key) {
        Document globalLock = (Document) getServerStatus().get("globalLock");
        Document currentQueue = (Document) globalLock.get("currentQueue");
        return (Integer) currentQueue.get(key);
    }
}
