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


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.query.Query;
import org.bson.Document;

import java.util.Optional;

public class TailableCursorRequest<T> implements SubscriptionRequest<Document, T, SubscriptionRequest.RequestOptions> {

    private final MessageListener<Document, ? super T> messageListener;
    private final TailableCursorRequestOptions options;

    /**
     * Create a new {@link TailableCursorRequest} with options, passing {@link Message messages} to the given
     * {@link MessageListener}.
     *
     * @param messageListener must not be {@literal null}.
     * @param options         must not be {@literal null}.
     */
    public TailableCursorRequest(MessageListener<Document, ? super T> messageListener, RequestOptions options) {

        Precondition.notNull(messageListener, "MessageListener must not be null!");
        Precondition.notNull(options, "Options must not be null!");

        this.messageListener = messageListener;
        this.options = options instanceof TailableCursorRequestOptions ? (TailableCursorRequestOptions) options
                : TailableCursorRequestOptions.of(options);
    }


    @Override
    public MessageListener<Document, ? super T> getMessageListener() {
        return messageListener;
    }


    @Override
    public TailableCursorRequestOptions getRequestOptions() {
        return options;
    }

    /**
     * Obtain a shiny new {@link TailableCursorRequestBuilder} and start defining options in this fancy fluent way. Just
     * don't forget to call {@link TailableCursorRequestBuilder#build() build()} when your're done.
     *
     * @return new instance of {@link TailableCursorRequestBuilder}.
     */
    public static TailableCursorRequestBuilder builder() {
        return new TailableCursorRequestBuilder();
    }

    /**
     * Obtain a shiny new {@link TailableCursorRequestBuilder} and start defining options in this fancy fluent way. Just
     * don't forget to call {@link TailableCursorRequestBuilder#build() build()} when your're done.
     *
     * @return new instance of {@link TailableCursorRequestBuilder}.
     */
    public static <T> TailableCursorRequestBuilder<T> builder(MessageListener<Document, ? super T> listener) {

        TailableCursorRequestBuilder<T> builder = new TailableCursorRequestBuilder<>();
        return builder.publishTo(listener);
    }

    /**
     * {@link RequestOptions} implementation specific to a {@link TailableCursorRequest}.
     */
    public static class TailableCursorRequestOptions implements RequestOptions {

        private
        String collectionName;
        private
        Query query;

        TailableCursorRequestOptions() {
        }

        public static TailableCursorRequestOptions of(RequestOptions options) {
            return builder().collection(options.getCollectionName()).build();
        }

        /**
         * Obtain a shiny new {@link TailableCursorRequestOptionsBuilder} and start defining options in this fancy fluent
         * way. Just don't forget to call {@link TailableCursorRequestOptionsBuilder#build() build()} when your're done.
         *
         * @return new instance of {@link TailableCursorRequestOptionsBuilder}.
         */
        public static TailableCursorRequestOptionsBuilder builder() {
            return new TailableCursorRequestOptionsBuilder();
        }

        @Override
        public String getCollectionName() {
            return collectionName;
        }

        public Optional<Query> getQuery() {
            return Optional.ofNullable(query);
        }

        /**
         * Builder for creating {@link TailableCursorRequestOptions}.
         */
        public static class TailableCursorRequestOptionsBuilder {

            private
            String collectionName;
            private
            Query query;

            private TailableCursorRequestOptionsBuilder() {
            }

            /**
             * Set the collection name to tail.
             *
             * @param collection must not be {@literal null} nor {@literal empty}.
             * @return this.
             */
            public TailableCursorRequestOptionsBuilder collection(String collection) {

                Precondition.hasText(collection, "Collection must not be null nor empty!");

                this.collectionName = collection;
                return this;
            }

            /**
             * Set the filter to apply.
             *
             * @param filter the {@link Query } to apply for filtering events. Must not be {@literal null}.
             * @return this.
             */
            public TailableCursorRequestOptionsBuilder filter(Query filter) {

                Precondition.notNull(filter, "Filter must not be null!");

                this.query = filter;
                return this;
            }

            /**
             * @return the built {@link TailableCursorRequestOptions}.
             */
            public TailableCursorRequestOptions build() {

                TailableCursorRequestOptions options = new TailableCursorRequestOptions();

                options.collectionName = collectionName;
                options.query = query;

                return options;
            }
        }
    }

    /**
     * Builder for creating {@link TailableCursorRequest}.
     *
     * @see TailableCursorRequestOptions
     */
    public static class TailableCursorRequestBuilder<T> {

        private
        MessageListener<Document, ? super T> listener;
        private TailableCursorRequestOptions.TailableCursorRequestOptionsBuilder delegate = TailableCursorRequestOptions.builder();

        private TailableCursorRequestBuilder() {
        }

        /**
         * Set the name of the {@link com.mongodb.client.MongoCollection} to listen to.
         *
         * @param collectionName must not be {@literal null} nor empty.
         * @return this.
         */
        public TailableCursorRequestBuilder<T> collection(String collectionName) {

            Precondition.hasText(collectionName, "CollectionName must not be null!");

            delegate.collection(collectionName);
            return this;
        }

        /**
         * Set the {@link MessageListener} event {@link Message messages} will be published to.
         *
         * @param messageListener must not be {@literal null}.
         * @return this.
         */
        public TailableCursorRequestBuilder<T> publishTo(MessageListener<Document, ? super T> messageListener) {

            Precondition.notNull(messageListener, "MessageListener must not be null!");

            this.listener = messageListener;
            return this;
        }

        /**
         * Set the filter to apply.
         *
         * @param filter the {@link Query } to apply for filtering events. Must not be {@literal null}.
         * @return this.
         */
        public TailableCursorRequestBuilder<T> filter(Query filter) {

            Precondition.notNull(filter, "Filter must not be null!");

            delegate.filter(filter);
            return this;
        }

        /**
         * @return the build {@link ChangeStreamRequest}.
         */
        public TailableCursorRequest<T> build() {

            Precondition.notNull(listener, "MessageListener must not be null!");

            return new TailableCursorRequest<>(listener, delegate.build());
        }
    }
}
