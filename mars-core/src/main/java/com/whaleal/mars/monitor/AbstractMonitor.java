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

import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ServerDescription;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class to encapsulate common configuration settings when connecting to a database
 * 抽象基类，连接数据库执行db.runCommand({"serverStatus" : 1,"rangeDeleter" : 1,"repl" : 1})，子类继承这个类，解析命令返回结果，获取对应的监控指标
 */
public abstract class AbstractMonitor {

    private final MongoClient mongoClient;

    /**
     * @param mongoClient must not be {@literal null}.
     */
    protected AbstractMonitor(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * 执行db.serverStatus()命令
     * @return
     */
    public Document getServerStatus() {
        return getDb("admin").runCommand(new Document("serverStatus", 1).append("rangeDeleter", 1).append("repl", 1));
    }

    /**
     * 执行db.runCommand( { "connPoolStats" : 1 } )命令
     * @return
     */
    protected Document getConnPoolStats(){
        return getDb("admin").runCommand(new Document("connPoolStats",1));
    }

    public MongoDatabase getDb(String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }

    protected List<ServerAddress> hosts() {
        return mongoClient.getClusterDescription().getServerDescriptions().stream().map(ServerDescription::getAddress)
                .collect(Collectors.toList());
    }

}
