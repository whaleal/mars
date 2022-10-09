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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateViewOptions;
import com.mongodb.lang.Nullable;
import com.whaleal.mars.core.aggregation.AggregationPipeline;
import com.whaleal.mars.core.aggregation.stages.Stage;
import com.whaleal.mars.core.gridfs.GridFsOperations;
import com.whaleal.mars.core.query.Query;
import com.mongodb.client.model.CreateCollectionOptions ;
import com.whaleal.mars.session.option.CountOptions;
import org.bson.Document;

import java.util.Arrays;
import java.util.Set;

interface MongoOperations extends GridFsOperations {

    /**
     * 传递一个类对象，根据对象创建集合
     *
     * @param entityClass 实体类
     * @return {@link MongoCollection<Document>}
     */
    < T > MongoCollection< Document > createCollection( Class< T > entityClass );

    /**
     * 传递对象和集合的选项进行创建集合
     *
     * @param entityClass       实体类
     * @param collectionOptions 收集选项
     * @return {@link MongoCollection<Document>}
     */
    < T > MongoCollection< Document > createCollection( Class< T > entityClass, CreateCollectionOptions collectionOptions );

    /**
     * 根据名字创建集合，使用一些默认的参数
     *
     * @param collectionName 集合名称
     * @return {@link MongoCollection<Document>}
     */
    MongoCollection< Document > createCollection( String collectionName );

    /**
     * 根据名字和选项创建集合
     *
     * @param collectionName    集合名称
     * @param collectionOptions 收集选项
     * @return {@link MongoCollection<Document>}
     */
    MongoCollection< Document > createCollection( String collectionName, CreateCollectionOptions collectionOptions );


    /**
     * Create a view with the provided name. The view content is defined by the {@link Stage pipeline
     * stages} on another collection or view identified by the given {@link #getCollectionName(Class) source type}.
     *
     * @param name the name of the view to create.
     * @param source the type defining the views source collection.
     * @param stages the {@link Stage aggregation pipeline stages} defining the view content.
     *
     */
    default MongoCollection< Document > createView( String name, Class< ? > source, Stage... stages ) {
        return createView(name, source, AggregationPipeline.create(Document.class, Arrays.asList(stages)));
    }

    /**
     * Create a view with the provided name. The view content is defined by the {@link AggregationPipeline pipeline} on
     * another collection or view identified by the given {@link #getCollectionName(Class) source type}.
     *
     * @param name the name of the view to create.
     * @param source the type defining the views source collection.
     * @param pipeline the {@link AggregationPipeline} defining the view content.
     *
     */
    default MongoCollection< Document > createView( String name, Class< ? > source, AggregationPipeline pipeline ) {
        return createView(name, source, pipeline, null);
    }

    /**
     * Create a view with the provided name. The view content is defined by the {@link AggregationPipeline pipeline} on
     * another collection or view identified by the given {@link #getCollectionName(Class) source type}.
     *
     * @param name the name of the view to create.
     * @param source the type defining the views source collection.
     * @param pipeline the {@link AggregationPipeline} defining the view content.
     * @param options additional settings to apply when creating the view. Can be {@literal null}.
     *
     */
    MongoCollection< Document > createView( String name, Class< ? > source, AggregationPipeline pipeline, @Nullable CreateViewOptions options );

    /**
     * Create a view with the provided name. The view content is defined by the {@link AggregationPipeline pipeline} on
     * another collection or view identified by the given source.
     *
     * @param name the name of the view to create.
     * @param source the name of the collection or view defining the to be created views source.
     * @param pipeline the {@link AggregationPipeline} defining the view content.
     * @param options additional settings to apply when creating the view. Can be {@literal null}.
     *
     */
    MongoCollection< Document > createView( String name, String source, AggregationPipeline pipeline, @Nullable CreateViewOptions options );


    /**
     * A set of collection names.
     *
     * @return list of collection names.
     */
    Set< String > getCollectionNames();

    /**
     * The collection name used for the specified class by this template.
     *
     * @param entityClass must not be {@literal null}.
     * @return never {@literal null}.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the collection name cannot be derived from the type.
     */
    String getCollectionName( Class< ? > entityClass );

    /**
     * Get a {@link MongoCollection} by its name. The returned collection may not exists yet (except in local memory) and
     * is created on first interaction with the server. Collections can be explicitly created via
     * {@link #createCollection(Class)}. Please make sure to check if the collection {@link #collectionExists(Class)
     * exists} first. <br />
     * Translate any exceptions as necessary.
     *
     * @param collectionName name of the collection. Must not be {@literal null}.
     * @return an existing collection or one created on first server interaction.
     */
    MongoCollection< Document > getCollection( String collectionName );


    /**
     * Check to see if a collection with a name indicated by the entity class exists. <br />
     * Translate any exceptions as necessary.
     *
     * @param entityClass class that determines the name of the collection. Must not be {@literal null}.
     * @return true if a collection with the given name is found, false otherwise.
     */
    < T > boolean collectionExists( Class< T > entityClass );

    /**
     * Check to see if a collection with a given name exists. <br />
     * Translate any exceptions as necessary.
     *
     * @param collectionName name of the collection. Must not be {@literal null}.
     * @return true if a collection with the given name is found, false otherwise.
     */
    boolean collectionExists( String collectionName );

    /**
     * 根据类对象删除对应的集合
     *
     * @param entityClass 实体类
     */
    < T > void dropCollection( Class< T > entityClass );

    /**
     * 根据类名称删除对应的集合
     *
     * @param collectionName 集合名称
     */
    void dropCollection( String collectionName );


    /**
     * Estimate the number of documents, in the collection {@link #getCollectionName(Class) identified by the given type},
     * based on collection statistics. <br />
     * Please make sure to read the MongoDB reference documentation about limitations on eg. sharded cluster or inside
     * transactions.
     *
     * @param entityClass must not be {@literal null}.
     * @return the estimated number of documents.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the collection name cannot be
     *           {@link #getCollectionName(Class) derived} from the given type.
     *
     */
    < T > long estimatedCount( Class< T > entityClass );

    /**
     * Estimate the number of documents in the given collection based on collection statistics. <br />
     * Please make sure to read the MongoDB reference documentation about limitations on eg. sharded cluster or inside
     * transactions.
     *
     * @param collectionName must not be {@literal null}.
     * @return the estimated number of documents.
     *
     */
    < T > long estimatedCount( String collectionName );

    /**
     * Returns the number of documents for the given {@link Query} by querying the collection of the given entity class.
     * <br />
     * <strong>NOTE:</strong> Query {@link Query#getSkip() offset} and {@link Query#getLimit() limit} can have direct
     * influence on the resulting number of documents found as those values are passed on to the server and potentially
     * limit the range and order within which the server performs the count operation. Use an {@literal unpaged} query to
     * count all matches. <br />
     * This method may choose to use {@link #estimatedCount(Class)} for empty queries instead of running an
     * {@link com.mongodb.client.MongoCollection#countDocuments(org.bson.conversions.Bson, com.mongodb.client.model.CountOptions)
     * aggregation execution} which may have an impact on performance.
     *
     * @param query the {@link Query} class that specifies the criteria used to find documents. Must not be
     *          {@literal null}.
     * @param entityClass class that determines the collection to use. Must not be {@literal null}.
     * @return the count of matching documents.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the collection name cannot be
     *      {@link #getCollectionName(Class) derived} from the given type.
     *
     * @see #estimatedCount(Class)
     */
    < T > long count( Query query, Class< T > entityClass );

    /**
     * Returns the number of documents for the given {@link Query} querying the given collection. The given {@link Query}
     * must solely consist of document field references as we lack type information to map potential property references
     * onto document fields. Use {@link #count(Query, Class, String)} to get full type specific support. <br />
     * <strong>NOTE:</strong> Query {@link Query#getSkip() offset} and {@link Query#getLimit() limit} can have direct
     * influence on the resulting number of documents found as those values are passed on to the server and potentially
     * limit the range and order within which the server performs the count operation. Use an {@literal unpaged} query to
     * count all matches. <br />
     * This method may choose to use {@link #estimatedCount(Class)} for empty queries instead of running an
     * {@link com.mongodb.client.MongoCollection#countDocuments(org.bson.conversions.Bson, com.mongodb.client.model.CountOptions)
     * aggregation execution} which may have an impact on performance.
     *
     * @param query the {@link Query} class that specifies the criteria used to find documents.
     * @param collectionName must not be {@literal null} or empty.
     * @return the count of matching documents.
     * @see #count(Query, Class, String)
     *
     * @see #estimatedCount(String)
     */
    < T > long count( Query query, String collectionName );

    /**
     * Returns the number of documents for the given {@link Query} by querying the given collection using the given entity
     * class to map the given {@link Query}. <br />
     * <strong>NOTE:</strong> Query {@link Query#getSkip() offset} and {@link Query#getLimit() limit} can have direct
     * influence on the resulting number of documents found as those values are passed on to the server and potentially
     * limit the range and order within which the server performs the count operation. Use an {@literal unpaged} query to
     * count all matches. <br />
     * This method may choose to use {@link #estimatedCount(Class)} for empty queries instead of running an
     * {@link com.mongodb.client.MongoCollection#countDocuments(org.bson.conversions.Bson, com.mongodb.client.model.CountOptions)
     * aggregation execution} which may have an impact on performance.
     *
     * @param query the {@link Query} class that specifies the criteria used to find documents. Must not be
     *          {@literal null}.
     * @param entityClass the parametrized type. Can be {@literal null}.
     * @param collectionName must not be {@literal null} or empty.
     * @return the count of matching documents.
     * @see #count(Query, Class, String)
     * @see #estimatedCount(String)
     */
    long count( Query query, Class< ? > entityClass, String collectionName );

    @Deprecated
    long count( Query query, Class< ? > entityClass, CountOptions countOptions, String collectionName );

}
