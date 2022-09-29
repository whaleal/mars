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

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.whaleal.icefrog.core.util.ClassUtil;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.mars.core.Mars;

import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.internal.MongoNamespace;
import com.whaleal.mars.session.option.AggregationOptions;
import com.whaleal.mars.core.internal.ErrorHandler;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

class ChangeStreamTask extends CursorReadingTask<ChangeStreamDocument<Document>, Object> {

    private final Set<String> denylist = new HashSet<>(
            Arrays.asList("operationType", "fullDocument", "documentKey", "updateDescription", "ns"));

    private final CodecRegistry converter ;
    @SuppressWarnings({"unchecked", "rawtypes"})
    ChangeStreamTask(Mars mars, ChangeStreamRequest<?> request, Class<?> targetType,
                     ErrorHandler errorHandler) {
        super(mars, (ChangeStreamRequest) request, (Class) targetType, errorHandler);
        this.converter = mars.getMapper().getCodecRegistry();
    }


    @Override
    protected MongoCursor<ChangeStreamDocument<Document>> initCursor(Mars mars, SubscriptionRequest.RequestOptions options,
                                                                     Class<?> targetType) {

        List<Document> filter = Collections.emptyList();
        BsonDocument resumeToken = new BsonDocument();
        Collation collation = null;
        FullDocument fullDocument = ClassUtil.isAssignable(Document.class, targetType) ? FullDocument.DEFAULT
                : FullDocument.UPDATE_LOOKUP;
        BsonTimestamp startAt = null;
        boolean resumeAfter = true;

        if (options instanceof ChangeStreamRequest.ChangeStreamRequestOptions) {

            ChangeStreamOptions changeStreamOptions = ((ChangeStreamRequest.ChangeStreamRequestOptions) options).getChangeStreamOptions();
            filter = prepareFilter(mars, changeStreamOptions);

            if (changeStreamOptions.getFilter().isPresent()) {

                Object val = changeStreamOptions.getFilter().get();
                if (val instanceof AggregationPipeline) {
                    collation = new AggregationOptions().getCollation();
                }
            }

            if (changeStreamOptions.getResumeToken().isPresent()) {

                resumeToken = changeStreamOptions.getResumeToken().get().asDocument();
                resumeAfter = changeStreamOptions.isResumeAfter();
            }

            fullDocument = changeStreamOptions.getFullDocumentLookup()
                    .orElseGet(() -> ClassUtil.isAssignable(Document.class, targetType) ? FullDocument.DEFAULT
                            : FullDocument.UPDATE_LOOKUP);

            startAt = changeStreamOptions.getResumeBsonTimestamp().orElse(null);
        }

        MongoDatabase db = StrUtil.hasText(options.getDatabaseName())
                ? mars.getMongoClient().getDatabase(options.getDatabaseName())
                : mars.getDatabase();

        ChangeStreamIterable<Document> iterable;

        if (StrUtil.hasText(options.getCollectionName())) {
            iterable = filter.isEmpty() ? db.getCollection(options.getCollectionName()).watch(Document.class)
                    : db.getCollection(options.getCollectionName()).watch(filter, Document.class);
        } else {
            iterable = filter.isEmpty() ? db.watch(Document.class) : db.watch(filter, Document.class);
        }

        if (!options.maxAwaitTime().isZero()) {
            iterable = iterable.maxAwaitTime(options.maxAwaitTime().toMillis(), TimeUnit.MILLISECONDS);
        }

        if (!resumeToken.isEmpty()) {

            if (resumeAfter) {
                iterable = iterable.resumeAfter(resumeToken);
            } else {
                iterable = iterable.startAfter(resumeToken);
            }
        }

        if (startAt != null) {
            iterable.startAtOperationTime(startAt);
        }

        if (collation != null) {
            iterable = iterable.collation(collation);
        }

        iterable = iterable.fullDocument(fullDocument);

        return iterable.iterator();
    }

    @SuppressWarnings("unchecked")
    List<Document> prepareFilter(Mars mars, ChangeStreamOptions options) {
        if (!options.getFilter().isPresent()) {
            return Collections.emptyList();
        }

        Object filter = options.getFilter().orElse(null);

        //TODO
		/*if (filter instanceof Aggregation) {
			Aggregation agg = (Aggregation) filter;
			AggregationOperationContext context = agg instanceof TypedAggregation
					? new TypeBasedAggregationOperationContext(((TypedAggregation<?>) agg).getInputType(),
							template.getConverter().getMappingContext(), queryMapper)
					: Aggregation.DEFAULT_CONTEXT;

			return agg.toPipeline(new PrefixingDelegatingAggregationOperationContext(context, "fullDocument", denylist));
		}*/

        //这个返回操作管道之内返回的结果，也就是Document结果集  需要使用到Aggregation

        if (filter instanceof List) {
            return (List<Document>) filter;
        }

        throw new IllegalArgumentException(
                "ChangeStreamRequestOptions.filter mut be either an Aggregation or a plain list of Documents");
    }


    @Override
    protected Message<ChangeStreamDocument<Document>, Object> createMessage(ChangeStreamDocument<Document> source,
                                                                            Class<Object> targetType, SubscriptionRequest.RequestOptions options) {

        MongoNamespace namespace = source.getNamespace() != null ? MongoNamespace.convertFrom(source.getNamespace())
                : createNamespaceFromOptions(options);

        return new ChangeStreamEventMessage<>(new ChangeStreamEvent<>(source, targetType,converter), namespace);
    }

    MongoNamespace createNamespaceFromOptions(SubscriptionRequest.RequestOptions options) {

        String collectionName = StrUtil.hasText(options.getCollectionName()) ? options.getCollectionName() : "unknown";
        String databaseName = StrUtil.hasText(options.getDatabaseName()) ? options.getDatabaseName() : "unknown";

        return new MongoNamespace(databaseName, collectionName);
    }

    /**
     * {@link Message} implementation for ChangeStreams
     */
    static class ChangeStreamEventMessage<T> implements Message<ChangeStreamDocument<Document>, T> {

        private final ChangeStreamEvent<T> delegate;
        private final MongoNamespace ns;

        ChangeStreamEventMessage(ChangeStreamEvent<T> delegate, MongoNamespace ns) {
            this.delegate = delegate;
            this.ns = ns;
        }


        @Override
        public ChangeStreamDocument<Document> getRaw() {
            return delegate.getRaw();
        }


        @Override
        public T getBody() {
            return delegate.getBody();
        }


        @Override
        public MongoNamespace getMongoNamespace() {
            return this.ns;
        }

        /**
         * @return the resume token or {@literal null} if not set.
         * @see ChangeStreamEvent#getResumeToken()
         */

        BsonValue getResumeToken() {
            return delegate.getResumeToken();
        }

        /**
         * @return the cluster time of the event or {@literal null}.
         * @see ChangeStreamEvent#getTimestamp()
         */

        Instant getTimestamp() {
            return delegate.getTimestamp();
        }

        /**
         * Get the {@link ChangeStreamEvent} from the message.
         *
         * @return never {@literal null}.
         */
        ChangeStreamEvent<T> getChangeStreamEvent() {
            return delegate;
        }
    }
}
