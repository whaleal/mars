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
package com.whaleal.mars.session;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ServerAddress;
import com.mongodb.TransactionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.TransactionBody;
import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import com.mongodb.session.ServerSession;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

/**
 * 注意该类的继承类
 * 分别继承 session 会话相关的接口 及数据基本操作的基本实现
 * 主要是通用会话层的实现
 * <p>
 * 具体数据库 操作 还是要看 继承的 MongoMappingContextImpl
 * <p>
 * 子类为 为 MarsSessionImpl
 *
 * @Date 2020-12-03
 */
public class BaseMarsSesssion extends DatastoreImpl implements MarsSession {
    //  内部封装了一个 clientSession
    private final ClientSession session;

    BaseMarsSesssion(ClientSession session,
                     MongoClient mongoClient,
                     String databaseName) {
        super(mongoClient,databaseName);
        this.session = session;
    }
    @Override
    @Nullable
    public ServerAddress getPinnedServerAddress() {
        return session.getPinnedServerAddress();
    }

    @Override
    public Object getTransactionContext() {
        return this.session.getTransactionContext();
    }

    @Override
    public void setTransactionContext( ServerAddress serverAddress, Object o ) {

        this.session.setTransactionContext(serverAddress,o);
    }

    @Override
    public void clearTransactionContext() {
        this.session.clearTransactionContext();
    }


    @Override
    public boolean hasActiveTransaction() {
        return session.hasActiveTransaction();
    }

    @Override
    public boolean notifyMessageSent() {
        return session.notifyMessageSent();
    }

    @Override
    public void notifyOperationInitiated( Object o ) {

        this.session.notifyOperationInitiated(o);
    }

    @Override
    public TransactionOptions getTransactionOptions() {
        return session.getTransactionOptions();
    }

    @Override
    public void startTransaction() {
        session.startTransaction();
    }

    @Override
    public void startTransaction(TransactionOptions transactionOptions) {
        session.startTransaction(transactionOptions);
    }

    @Override
    public void commitTransaction() {
        session.commitTransaction();
    }

    @Override
    public void abortTransaction() {
        session.abortTransaction();
    }

    @Override
    public <T> T withTransaction(TransactionBody<T> transactionBody) {
        return session.withTransaction(transactionBody);
    }

    @Override
    public <T> T withTransaction(TransactionBody<T> transactionBody, TransactionOptions options) {
        return session.withTransaction(transactionBody, options);
    }

    @Override
    @Nullable
    public BsonDocument getRecoveryToken() {
        return session.getRecoveryToken();
    }

    @Override
    public void setRecoveryToken(BsonDocument recoveryToken) {
        session.setRecoveryToken(recoveryToken);
    }

    @Override
    public ClientSessionOptions getOptions() {
        return session.getOptions();
    }

    @Override
    public boolean isCausallyConsistent() {
        return session.isCausallyConsistent();
    }

    @Override
    public Object getOriginator() {
        return session.getOriginator();
    }

    @Override
    public ServerSession getServerSession() {
        return session.getServerSession();
    }

    @Override
    public BsonTimestamp getOperationTime() {
        return session.getOperationTime();
    }

    @Override
    public void advanceOperationTime(BsonTimestamp operationTime) {
        session.advanceOperationTime(operationTime);
    }

    @Override
    public void advanceClusterTime(BsonDocument clusterTime) {
        session.advanceClusterTime(clusterTime);
    }

    @Override
    public void setSnapshotTimestamp( BsonTimestamp bsonTimestamp ) {

        this.session.setSnapshotTimestamp(bsonTimestamp);
    }

    @Override
    public BsonTimestamp getSnapshotTimestamp() {
        return this.session.getSnapshotTimestamp();
    }

    @Override
    public BsonDocument getClusterTime() {
        return session.getClusterTime();
    }

    @Override
    public void close() {
        session.close();
    }

    /**
     * @return the session
     */
    @NonNull
    public ClientSession getSession() {
        return session;
    }

}
