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

import java.sql.Connection;

/**
 * Level of isolation.
 */
public enum IsolationLevel {

    /**
     * Dirty reads, non-repeatable reads and phantom reads are allowed.
     */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED, Constants.LOCK_MODE_OFF),

    /**
     * Dirty reads aren't allowed; non-repeatable reads and phantom reads are
     * allowed.
     */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED, Constants.LOCK_MODE_READ_COMMITTED),

    /**
     * Dirty reads and non-repeatable reads aren't allowed; phantom reads are
     * allowed.
     */
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ, Constants.LOCK_MODE_TABLE),

    /**
     * Dirty reads, non-repeatable reads and phantom reads are'n allowed.
     */
    SNAPSHOT(Constants.TRANSACTION_SNAPSHOT, Constants.LOCK_MODE_TABLE),

    /**
     * Dirty reads, non-repeatable reads and phantom reads are'n allowed.
     * Concurrent and serial execution of transactions with this isolation level
     * should have the same effect.
     */
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE, Constants.LOCK_MODE_TABLE);

    private final String sql;
    private final int jdbc, lockMode;

    private IsolationLevel(int jdbc, int lockMode) {
        sql = name().replace('_', ' ').intern();
        this.jdbc = jdbc;
        this.lockMode = lockMode;
    }

    /**
     * Returns the isolation level from LOCK_MODE equivalent for PageStore and
     * old versions of H2.
     *
     * @param level the LOCK_MODE value
     * @return the isolation level
     */
    public static IsolationLevel fromJdbc(int level) {
        switch (level) {
            case Connection.TRANSACTION_READ_UNCOMMITTED:
                return IsolationLevel.READ_UNCOMMITTED;
            case Connection.TRANSACTION_READ_COMMITTED:
                return IsolationLevel.READ_COMMITTED;
            case Connection.TRANSACTION_REPEATABLE_READ:
                return IsolationLevel.REPEATABLE_READ;
            case Constants.TRANSACTION_SNAPSHOT:
                return IsolationLevel.SNAPSHOT;
            case Connection.TRANSACTION_SERIALIZABLE:
                return IsolationLevel.SERIALIZABLE;
            default:
                throw new Error("");
        }
    }

    /**
     * Returns the isolation level from LOCK_MODE equivalent for PageStore and
     * old versions of H2.
     *
     * @param lockMode the LOCK_MODE value
     * @return the isolation level
     */
    public static IsolationLevel fromLockMode(int lockMode) {
        switch (lockMode) {
            case Constants.LOCK_MODE_OFF:
                return IsolationLevel.READ_UNCOMMITTED;
            case Constants.LOCK_MODE_READ_COMMITTED:
            default:
                return IsolationLevel.READ_COMMITTED;
            case Constants.LOCK_MODE_TABLE:
            case Constants.LOCK_MODE_TABLE_GC:
                return IsolationLevel.SERIALIZABLE;
        }
    }

    /**
     * Returns the isolation level from its SQL name.
     *
     * @param sql the SQL name
     * @return the isolation level from its SQL name
     */
    public static IsolationLevel fromSql(String sql) {
        switch (sql) {
            case "READ UNCOMMITTED":
                return READ_UNCOMMITTED;
            case "READ COMMITTED":
                return READ_COMMITTED;
            case "REPEATABLE READ":
                return REPEATABLE_READ;
            case "SNAPSHOT":
                return SNAPSHOT;
            case "SERIALIZABLE":
                return SERIALIZABLE;
            default:
                throw new Error("isolation level" + sql);
        }
    }

    /**
     * Returns the SQL representation of this isolation level.
     *
     * @return SQL representation of this isolation level
     */
    public String getSQL() {
        return sql;
    }

    /**
     * Returns the JDBC constant for this isolation level.
     *
     * @return the JDBC constant for this isolation level
     */
    public int getJdbc() {
        return jdbc;
    }

    /**
     * Returns the LOCK_MODE equivalent for PageStore and old versions of H2.
     *
     * @return the LOCK_MODE equivalent
     */
    public int getLockMode() {
        return lockMode;
    }

    /**
     * Returns whether a non-repeatable read phenomena is allowed.
     *
     * @return whether a non-repeatable read phenomena is allowed
     */
    public boolean allowNonRepeatableRead() {
        return ordinal() < REPEATABLE_READ.ordinal();
    }

}
