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

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.whaleal.icefrog.core.lang.Precondition;

import com.whaleal.mars.core.aggregation.AggregationPipeline;
import org.bson.BsonValue;
import org.bson.Document;

import java.time.Duration;
import java.time.Instant;

public class ChangeStreamRequest<T>
        implements SubscriptionRequest<ChangeStreamDocument<Document>, T, ChangeStreamRequest.ChangeStreamRequestOptions> {

    private final MessageListener<ChangeStreamDocument<Document>, ? super T> messageListener;
    private final ChangeStreamRequestOptions options;

    /**
     * Create a new {@link ChangeStreamRequest} with options, passing {@link Message messages} to the given
     * {@link MessageListener}.
     *
     * @param messageListener must not be {@literal null}.
     * @param options         must not be {@literal null}.
     */
    public ChangeStreamRequest(MessageListener<ChangeStreamDocument<Document>, ? super T> messageListener,
                               RequestOptions options) {

        Precondition.notNull(messageListener, "MessageListener must not be null!");
        Precondition.notNull(options, "Options must not be null!");

        this.options = options instanceof ChangeStreamRequestOptions ? (ChangeStreamRequestOptions) options
                : ChangeStreamRequestOptions.of(options);

        this.messageListener = messageListener;
    }

    @Override
    public MessageListener<ChangeStreamDocument<Document>, ? super T> getMessageListener() {
        return messageListener;
    }


    @Override
    public ChangeStreamRequestOptions getRequestOptions() {
        return options;
    }

    /**
     * Obtain a shiny new {@link ChangeStreamRequestBuilder} and start defining your {@link ChangeStreamRequest} in this
     * fancy fluent way. Just don't forget to call {@link ChangeStreamRequestBuilder#build() build()} when your're done.
     *
     * @return new instance of {@link ChangeStreamRequest}.
     */
    public static ChangeStreamRequestBuilder builder() {
        return new ChangeStreamRequestBuilder();
    }

    /**
     * Obtain a shiny new {@link ChangeStreamRequestBuilder} and start defining your {@link ChangeStreamRequest} in this
     * fancy fluent way. Just don't forget to call {@link ChangeStreamRequestBuilder#build() build()} when your're done.
     *
     * @return new instance of {@link ChangeStreamRequest}.
     */
    public static <T> ChangeStreamRequestBuilder<T> builder(
            MessageListener<ChangeStreamDocument<Document>, ? super T> listener) {

        ChangeStreamRequestBuilder<T> builder = new ChangeStreamRequestBuilder<>();
        return builder.publishTo(listener);
    }

    /**
     * {@link RequestOptions} implementation specific to a {@link ChangeStreamRequest}.
     */
    public static class ChangeStreamRequestOptions implements SubscriptionRequest.RequestOptions {

        private final
        String databaseName;
        private final
        String collectionName;
        private final
        Duration maxAwaitTime;
        private final ChangeStreamOptions options;

        /**
         * Create new {@link ChangeStreamRequestOptions}.
         *
         * @param databaseName   can be {@literal null}.
         * @param collectionName can be {@literal null}.
         * @param options        must not be {@literal null}.
         */
        public ChangeStreamRequestOptions( String databaseName, String collectionName,
                                           ChangeStreamOptions options ) {
            this(databaseName, collectionName, null, options);
        }

        /**
         * Create new {@link ChangeStreamRequestOptions}.
         *
         * @param databaseName   can be {@literal null}.
         * @param collectionName can be {@literal null}.
         * @param maxAwaitTime   can be {@literal null}.
         * @param options        must not be {@literal null}.
         */
        public ChangeStreamRequestOptions( String databaseName, String collectionName,
                                           Duration maxAwaitTime, ChangeStreamOptions options ) {

            Precondition.notNull(options, "Options must not be null!");

            this.collectionName = collectionName;
            this.databaseName = databaseName;
            this.maxAwaitTime = maxAwaitTime;
            this.options = options;
        }

        public static ChangeStreamRequestOptions of(RequestOptions options) {

            Precondition.notNull(options, "Options must not be null!");

            return new ChangeStreamRequestOptions(options.getDatabaseName(), options.getCollectionName(),
                    ChangeStreamOptions.builder().build());
        }

        /**
         * Get the {@link ChangeStreamOptions} defined.
         *
         * @return never {@literal null}.
         */
        public ChangeStreamOptions getChangeStreamOptions() {
            return options;
        }

        @Override
        public String getCollectionName() {
            return collectionName;
        }

        @Override
        public String getDatabaseName() {
            return databaseName;
        }


        @Override
        public Duration maxAwaitTime() {
            return maxAwaitTime != null ? maxAwaitTime : RequestOptions.super.maxAwaitTime();
        }
    }

    /**
     * Builder for creating {@link ChangeStreamRequest}.
     *
     * @see ChangeStreamOptions
     */
    public static class ChangeStreamRequestBuilder<T> {

        private
        String databaseName;
        private
        String collectionName;
        private
        Duration maxAwaitTime;
        private
        MessageListener<ChangeStreamDocument<Document>, ? super T> listener;
        private final ChangeStreamOptions.ChangeStreamOptionsBuilder delegate = ChangeStreamOptions.builder();

        private ChangeStreamRequestBuilder() {
        }

        /**
         * Set the name of the {@link com.mongodb.client.MongoDatabase} to listen to.
         *
         * @param databaseName must not be {@literal null} nor empty.
         * @return this.
         */
        public ChangeStreamRequestBuilder<T> database(String databaseName) {

            Precondition.hasText(databaseName, "DatabaseName must not be null!");

            this.databaseName = databaseName;
            return this;
        }

        /**
         * Set the name of the {@link com.mongodb.client.MongoCollection} to listen to.
         *
         * @param collectionName must not be {@literal null} nor empty.
         * @return this.
         */
        public ChangeStreamRequestBuilder<T> collection(String collectionName) {

            Precondition.hasText(collectionName, "CollectionName must not be null!");

            this.collectionName = collectionName;
            return this;
        }

        /**
         * Set the {@link MessageListener} event {@link Message messages} will be published to.
         *
         * @param messageListener must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamRequestBuilder<T> publishTo(
                MessageListener<ChangeStreamDocument<Document>, ? super T> messageListener) {

            Precondition.notNull(messageListener, "MessageListener must not be null!");

            this.listener = messageListener;
            return this;
        }

        /**
         * Set the filter to apply.
         * <p/>
         * Fields on aggregation expression root level are prefixed to map to fields contained in
         * {@link ChangeStreamDocument#getFullDocument() fullDocument}. However {@literal operationType}, {@literal ns},
         * {@literal documentKey} and {@literal fullDocument} are reserved words that will be omitted, and therefore taken
         * as given, during the mapping procedure. You may want to have a look at the
         * <a href="https://docs.mongodb.com/manual/reference/change-events/">structure of Change Events</a>.
         * <p/>
         *
         * @param aggregation the {@link AggregationPipeline Aggregation pipeline} to apply for filtering events. Must not be
         *                    {@literal null}.
         * @return this.
         * @see ChangeStreamOptions#getFilter()
         */
        public ChangeStreamRequestBuilder<T> filter( AggregationPipeline aggregation) {

            Precondition.notNull(aggregation, "Aggregation must not be null!");
            this.delegate.filter(aggregation);
            return this;
        }

        /**
         * Set the plain filter chain to apply.
         *
         * @param pipeline must not be {@literal null} nor contain {@literal null} values.
         * @return this.
         * @see ChangeStreamOptions#getFilter()
         */
        public ChangeStreamRequestBuilder<T> filter(Document... pipeline) {

            Precondition.notNull(pipeline, "Aggregation pipeline must not be null!");
            Precondition.noNullElements(pipeline, "Aggregation pipeline must not contain null elements!");

            this.delegate.filter(pipeline);
            return this;
        }

        /**
         * Set the collation to use.
         *
         * @param collation must not be {@literal null} nor {@literal empty}.
         * @return this.
         * @see ChangeStreamOptions#getCollation()
         */
        public ChangeStreamRequestBuilder<T> collation(Collation collation) {

            Precondition.notNull(collation, "Collation must not be null!");

            this.delegate.collation(collation);
            return this;
        }

        /**
         * Set the resume token (typically a {@link org.bson.BsonDocument} containing a {@link org.bson.BsonBinary binary
         * token}) after which to start with listening.
         *
         * @param resumeToken must not be {@literal null}.
         * @return this.
         * @see ChangeStreamOptions#getResumeToken()
         */
        public ChangeStreamRequestBuilder<T> resumeToken(BsonValue resumeToken) {

            Precondition.notNull(resumeToken, "Resume token not be null!");

            this.delegate.resumeToken(resumeToken);
            return this;
        }

        /**
         * Set the cluster time at which to resume listening.
         *
         * @param clusterTime must not be {@literal null}.
         * @return this.
         * @see ChangeStreamOptions#getResumeTimestamp()
         */
        public ChangeStreamRequestBuilder<T> resumeAt(Instant clusterTime) {

            Precondition.notNull(clusterTime, "ClusterTime must not be null!");

            this.delegate.resumeAt(clusterTime);
            return this;
        }

        /**
         * Set the resume token after which to continue emitting notifications.
         *
         * @param resumeToken must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamRequestBuilder<T> resumeAfter(BsonValue resumeToken) {

            Precondition.notNull(resumeToken, "ResumeToken must not be null!");
            this.delegate.resumeAfter(resumeToken);

            return this;
        }

        /**
         * Set the resume token after which to start emitting notifications.
         *
         * @param resumeToken must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamRequestBuilder<T> startAfter(BsonValue resumeToken) {

            Precondition.notNull(resumeToken, "ResumeToken must not be null!");
            this.delegate.startAfter(resumeToken);

            return this;
        }

        /**
         * Set the {@link FullDocument} lookup to {@link FullDocument#UPDATE_LOOKUP}.
         *
         * @return this.
         * @see #fullDocumentLookup(FullDocument)
         * @see ChangeStreamOptions#getFullDocumentLookup()
         */
        public ChangeStreamRequestBuilder<T> fullDocumentLookup(FullDocument lookup) {

            Precondition.notNull(lookup, "FullDocument not be null!");

            this.delegate.fullDocumentLookup(lookup);
            return this;
        }

        /**
         * Set the cursors maximum wait time on the server (for a new Entity to be emitted).
         *
         * @param timeout must not be {@literal null}.
         */
        public ChangeStreamRequestBuilder<T> maxAwaitTime(Duration timeout) {

            Precondition.notNull(timeout, "timeout not be null!");

            this.maxAwaitTime = timeout;
            return this;
        }

        /**
         * @return the build {@link ChangeStreamRequest}.
         */
        public ChangeStreamRequest<T> build() {

            Precondition.notNull(listener, "MessageListener must not be null!");

            return new ChangeStreamRequest<>(listener,
                    new ChangeStreamRequestOptions(databaseName, collectionName, maxAwaitTime, delegate.build()));
        }
    }
}
