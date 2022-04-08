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
package com.whaleal.mars.session;

import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 该 方法 需要重新设计
 *
 * @param <T>
 *
 *            基于Document 的 进行转换
 */

public class QueryCursor< T > implements MarsCursor< T > {
    private final MongoCursor< T > wrapped;

    private final Class< ? super T > rawType;


    /**
     * @param cursor the Iterator to use
     */
    public QueryCursor( MongoCursor< T > cursor, Class< ? super T > entityClass ) {
        this.wrapped = cursor;
        this.rawType = entityClass;
        if (this.wrapped == null) {
            throw new IllegalArgumentException("The wrapped cursor can not be null");
        }
    }


    public QueryCursor( MongoCursor< T > cursor ) {
        this.wrapped = cursor;
        this.rawType = null;
        if (wrapped == null) {
            throw new IllegalArgumentException("The wrapped cursor can not be null");
        }

    }

    /**
     * Converts this cursor to a List.  Care should be taken on large datasets as OutOfMemoryErrors are a risk.
     *
     * @return the list of Entities
     */
    public List< T > toList() {
        final List< T > results = new ArrayList<>();
        try {
            while (this.wrapped.hasNext()) {
                results.add(next());
            }
        } finally {
            this.wrapped.close();
        }
        return results;
    }

    /**
     * Closes the underlying cursor.
     */
    @Override
    public void close() {
        if (this.wrapped != null) {
            this.wrapped.close();
        }
    }

    @Override
    public boolean hasNext() {
        if (this.wrapped == null) {
            return false;
        }
        return this.wrapped.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return this.wrapped.next();

    }


    public T tryNext() {
        if (hasNext()) {
            return next();
        } else {
            return null;
        }
    }


    public ServerCursor getServerCursor() {
        return this.wrapped.getServerCursor();
    }


    public ServerAddress getServerAddress() {
        return this.wrapped.getServerAddress();
    }

    @Override
    public void remove() {
        wrapped.remove();
    }
}
