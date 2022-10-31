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

import com.mongodb.client.MongoClient;
import org.bson.Document;

import java.util.Date;

/**
 * JMX Metrics for Background Flushing
 *
 * 此项指标不支持v3.6及之后的版本
 * backgroundFlushing information only appears for instances that use the MMAPv1 storage engine.
 * @link{https://www.mongodb.com/docs/v3.6/reference/command/serverStatus/}
 */
public class BackgroundFlushingMetrics extends AbstractMonitor {

    /**
     * @param mongoClient must not be {@literal null}.
     */
    public BackgroundFlushingMetrics(MongoClient mongoClient) {
        super(mongoClient);
    }

    public Integer getFlushes() {
        return getFlushingData("flushes", Integer.class);
    }

    public Integer getTotalMs() {
        return getFlushingData("total_ms", Integer.class);
    }

    public Double getAverageMs() {
        return getFlushingData("average_ms", Double.class);
    }

    public Integer getLastMs() {
        return getFlushingData("last_ms", Integer.class);
    }

    public Date getLastFinished() {
        return getLast();
    }

    @SuppressWarnings("unchecked")
    private <T> T getFlushingData(String key, Class<T> targetClass) {
        Document mem = (Document) serverStatus.get("backgroundFlushing");
        return (T) mem.get(key);
    }

    private Date getLast() {
        Document bgFlush = (Document) serverStatus.get("backgroundFlushing");
        Date lastFinished = (Date) bgFlush.get("last_finished");
        return lastFinished;
    }

}
