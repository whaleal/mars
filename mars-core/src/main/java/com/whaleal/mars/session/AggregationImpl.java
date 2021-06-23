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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.bson.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Stage;
import com.whaleal.mars.session.option.AggregationOptions;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.stream.Collectors;

public class AggregationImpl {

    private final MongoMappingContext mapper;
    private final MongoDatabase database;


    public AggregationImpl(MongoDatabase database) {

        this.mapper = new MongoMappingContext(database);
        this.database = database.withCodecRegistry(mapper.getCodecRegistry());

    }


    /**
     * 如有类型 需要定义需要在这里传参
     * 这个  outputType  主要为 返回类型
     *
     * @param outputType
     * @param <T>
     * @return
     */
    public <T> QueryCursor<T> aggregate(AggregationPipeline pipeline, Class<T> outputType) {


        return this.aggregate(pipeline, outputType, null, null);
    }

    public <T> QueryCursor<T> aggregate(AggregationPipeline pipeline, Class<T> outputType, AggregationOptions options) {


        return this.aggregate(pipeline, outputType, null, options);
    }


    /**
     *
     */
    public <T> QueryCursor<T> aggregate(AggregationPipeline pipeline, Class<T> outputType, String collectionName, AggregationOptions options) {


        return this.execute(pipeline, outputType, collectionName, options);
    }


    /**
     * 默认情况下 直接进行转换
     *
     * @return
     */
    public QueryCursor<Document> aggregate(AggregationPipeline pipeline, String collectName) {
        return this.aggregate(pipeline, Document.class, collectName, null);
    }


    public MarsCursor<Document> aggregate(AggregationPipeline pipeline, String collectName, AggregationOptions options) {
        return this.aggregate(pipeline, Document.class, collectName, options);
    }


    public <R> QueryCursor<R> execute(AggregationPipeline pipeline, Class<R> resultType, String collectionName, AggregationOptions options) {


        MongoCollection<R> collection;
        if (resultType.isAssignableFrom(Document.class)) {

            if (collectionName == null) {

                throw new IllegalArgumentException("collectionName  can't  be nul  when resultType is Entity");
            }

            collection = this.database.getCollection(collectionName).withDocumentClass(resultType);
        } else {

            String collName = this.mapper.determineCollectionName(resultType, collectionName);

            collection = this.database.getCollection(collName).withDocumentClass(resultType);
        }

        if (options != null) {
            collection = options.prepare(collection);
        }

        MongoCursor<R> cursor = collection.aggregate(getDocuments(pipeline.getInnerStage()), resultType).iterator();

        return new QueryCursor<>(cursor);


    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Document> getDocuments(List<Stage> stages) {
        return stages.stream()
                .map(s -> {
                    Codec codec = mapper.getCodecRegistry().get(s.getClass());
                    DocumentWriter writer = new DocumentWriter();
                    codec.encode(writer, s, EncoderContext.builder().build());
                    return writer.getDocument();
                })
                .collect(Collectors.toList());
    }


}
