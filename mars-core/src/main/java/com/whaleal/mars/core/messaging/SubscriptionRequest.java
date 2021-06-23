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

import com.mongodb.lang.Nullable;
import com.whaleal.mars.util.Assert;

import java.time.Duration;

public interface SubscriptionRequest<S, T, O extends SubscriptionRequest.RequestOptions> {

    /**
     * Obtain the {@link MessageListener} to publish {@link Message messages} to.
     *
     * @return never {@literal null}.
     */
    MessageListener<S, ? super T> getMessageListener();

    /**
     * Get the {@link RequestOptions} specifying the requests behaviour.
     *
     * @return never {@literal null}.
     */
    O getRequestOptions();


    /**
     * Options for specifying the behaviour of the {@link SubscriptionRequest}.
     */
    interface RequestOptions {

        /**
         * Get the database name of the db.
         *
         * @return the name of the database to subscribe to. Can be {@literal null} in which case the default
         */
        @Nullable
        default String getDatabaseName() {
            return null;
        }

        /**
         * Get the collection name.
         *
         * @return the name of the collection to subscribe to. Can be {@literal null}.
         */
        @Nullable
        String getCollectionName();

        /**
         * Get the maximum wait time (the time till the next Entity is emitted) to apply when reading from the collection.
         *
         * @return never {@literal null}. {@link Duration#ZERO} by default.
         */
        default Duration maxAwaitTime() {
            return Duration.ZERO;
        }

        /**
         * Create empty options.
         *
         * @return new instance of empty {@link RequestOptions}.
         */
        static RequestOptions none() {
            return () -> null;
        }

        /**
         * Create options with the provided database.
         *
         * @param database must not be {@literal null}.
         * @return new instance of empty {@link RequestOptions}.
         */
        static RequestOptions justDatabase(String database) {

            Assert.notNull(database, "Database must not be null!");

            return new RequestOptions() {

                @Override
                public String getCollectionName() {
                    return null;
                }

                @Override
                public String getDatabaseName() {
                    return database;
                }
            };
        }

        /**
         * Create options with the provided collection.
         *
         * @param collection must not be {@literal null}.
         * @return new instance of empty {@link RequestOptions}.
         */
        static RequestOptions justCollection(String collection) {

            Assert.notNull(collection, "Collection must not be null!");
            return () -> collection;
        }

        /**
         * Create options with the provided database and collection.
         *
         * @param database   must not be {@literal null}.
         * @param collection must not be {@literal null}.
         * @return new instance of empty {@link RequestOptions}.
         */
        static RequestOptions of(String database, String collection) {

            Assert.notNull(database, "Database must not be null!");
            Assert.notNull(collection, "Collection must not be null!");

            return new RequestOptions() {

                @Override
                public String getCollectionName() {
                    return collection;
                }

                @Override
                public String getDatabaseName() {
                    return database;
                }
            };
        }
    }
}