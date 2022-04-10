/**
 * Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 * <p>
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.whaleal.com/licensing/server-side-public-license>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the Server Side Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.whaleal.mars.core;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.session.DatastoreImpl;

/**
 * @author wh
 * @Date 2021-01-27
 * <p>
 * 对外开放的 操作 类
 * 可以自动在 容器中获取
 * 或者通过 uri  参数 来创建该对象
 *
 * 操作相关实现全部详见  DataStoreImpl
 * @see com.whaleal.mars.session.DatastoreImpl
 */
public class Mars extends DatastoreImpl {


    /**
     * 这个是连接级别的读写关注
     * 即为库级别的读写关注
     * 如果需要MongoClient 级别的读写关注
     * 请在生成Mongoclient 对象时授予相关权限设置,如连接字符串等：
     *
     */
    private WriteConcern writeConcern;
    private ReadPreference readPreference;
    private ReadConcern readConcern;


    public Mars( MongoClient mongoClient, MongoMappingContext mapper ) {
        super(mongoClient, mapper);

    }

    public Mars( String connectionString ) {
        this(new ConnectionString(connectionString));
    }

    public Mars( MongoClient mongoClient, String databaseName ) {
        super(mongoClient, databaseName == null ? "test" : databaseName);
    }

    public Mars( ConnectionString connectionString ) {
        this(MongoClients.create(connectionString), connectionString.getDatabase());
    }

    public Mars( MongoClientSettings clientSettings, String databaseName ) {
        this(MongoClients.create(clientSettings), databaseName);
    }

    public WriteConcern getWriteConcern() {
        return writeConcern;
    }

    @Override
    public void setWriteConcern( WriteConcern writeConcern ) {
        super.setWriteConcern(writeConcern);
        this.writeConcern = writeConcern;
    }

    public ReadPreference getReadPreference() {
        return readPreference;
    }

    @Override
    public void setReadPreference( ReadPreference readPerference ) {
        super.setReadPreference(readPerference);
        this.readPreference = readPerference;
    }

    public ReadConcern getReadConcern() {
        return readConcern;
    }

    @Override
    public void setReadConcern( ReadConcern readConcern ) {
        super.setReadConcern(readConcern);
        this.readConcern = readConcern;
    }
}
