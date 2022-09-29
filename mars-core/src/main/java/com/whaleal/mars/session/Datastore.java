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

import com.mongodb.ClientSessionOptions;
import com.mongodb.ReadPreference;
import com.mongodb.lang.Nullable;
import com.whaleal.icefrog.core.util.ClassUtil;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.query.*;
import com.whaleal.mars.session.option.*;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import com.whaleal.mars.session.result.UpdateResult;
import com.whaleal.mars.session.transactions.MarsTransaction;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.whaleal.icefrog.core.lang.Precondition.notNull;

/**
 * 数据操作的顶级接口
 * <p>
 * 删除
 * <p>
 * 查找
 * <p>
 * 创建
 * <p>
 * 更新
 * <p>
 * Save (replace )
 * <p>
 * 内部设计 merge
 *
 * @desc
 * @Date 2020-12-07
 * 设计 相关数据交互接口
 * 主要为  crud  index   replace
 * @Date 2020-12-08
 * <p>
 * 将 索引 部分的接口单独拿出来
 */

interface Datastore extends IndexOperations, MongoOperations {


    default < T > UpdateResult replace( Query query, T entity ) {
        return replace(query, entity, new ReplaceOptions());
    }

    @Deprecated
    default < T > UpdateResult replace( Query query, T entity, ReplaceOptions options ) {
        return replace(query, entity, options, null);
    }

    @Deprecated
    < T > UpdateResult replace( Query query, T entity, ReplaceOptions options, String collectionName );

    default < T > UpdateResult replacert( Query query, T entity ){
        return replace(query ,entity ,new ReplaceOptions().upsert(true));
    }

    default < T > UpdateResult replacert( Query query, T entity, String collectionName ){
        return replace(query ,entity ,new ReplaceOptions().upsert(true),collectionName);
    }


    default < T > DeleteResult delete( Query query, Class< T > entityClass ) {
        return delete(query, entityClass, new DeleteOptions());
    }

    @Deprecated
    default < T > DeleteResult delete( Query query, Class< T > entityClass, DeleteOptions deleteOptions ) {
        return delete(query, entityClass, deleteOptions, null);
    }


    default DeleteResult delete( Query query, String collectionName ) {
        return delete(query, org.bson.Document.class, new DeleteOptions(), collectionName);
    }


    @Deprecated
    default DeleteResult delete( Query query, String collectionName, DeleteOptions options ) {
        return delete(query, org.bson.Document.class, options, collectionName);
    }


    default DeleteResult delete( Query query, Class< ? > entityClass, String collectionName ) {

        notNull(entityClass, "EntityClass must not be null!");
        return delete(query, entityClass, new DeleteOptions(), collectionName);
    }


    @Deprecated
    < T > DeleteResult delete( Query query, Class< T > entityClass, DeleteOptions options, String collectionName );

    default < T > QueryCursor< T > findAll( Query query, Class< T > entityClass ) {
        return findAll(query, entityClass, null);
    }

    < T > QueryCursor< T > findAll( Query query, Class< T > entityClass, String collectionName );

    default < T > Optional< T > findOne( Query query, Class< T > entityClass ) {
        return findOne(query, entityClass, null);
    }

    /**
     * 根据id查询记录
     * @param id
     * @param entityClass
     * @param <T>
     * @return
     */
    default <T> Optional< T > findById(Object id, Class< T > entityClass){
        return this.findById(id,entityClass,null);
    }

    <T> Optional< T > findById(Object id,Class< T > entityClass,String collectionName);


    < T > Optional< T > findOne( Query query, Class< T > entityClass, String collectionName );

    /**
     * 查询去重
     * @param field
     * @param entityClass
     * @param resultClass
     * @param <T>
     * @return
     */
    default <T> QueryCursor<T> findDistinct(String field, Class<?> entityClass, Class<T> resultClass) {
        return this.findDistinct(new Query(), field, entityClass, resultClass);
    }


    <T> QueryCursor<T> findDistinct(Query query, String field, Class<?> entityClass, Class<T> resultClass);

    <T> QueryCursor<T> findDistinct(Query query, String field, String collectionName, Class<?> entityClass, Class<T> resultClass);

    default <T> QueryCursor<T> findDistinct(Query query, String field, String collection, Class<T> resultClass) {
        return this.findDistinct(query, field, collection, Object.class, resultClass);
    }

    /**
     * Inserts an entity in to the mapped collection.
     */
    default < T > InsertOneResult insert( T entity ) {
        return insert(entity, new InsertOneOptions());
    }

    /**
     * Inserts an entity in to the mapped collection.
     */
    default < T > InsertOneResult insert( T entity, String collectionName ) {
        return insert(entity, new InsertOneOptions(), collectionName);
    }

    /**
     * Inserts an entity in to the mapped collection.
     */

    @Deprecated
    default < T > InsertOneResult insert( T entity, InsertOneOptions insertOneOptions ) {
        return insert(entity, insertOneOptions, null);
    }


    /**
     * Inserts an entity in to the mapped collection.
     */
    < T > InsertOneResult insert( T entity, InsertOneOptions options, String collectionName );


    /**
     * Inserts a List of entities in to the mapped collection.
     */
    default < T > InsertManyResult insert( Collection< ? extends T > entities, Class< ? > entityClass ) {
        return insert(entities, entityClass, new InsertManyOptions());
    }

    /**
     * Inserts a List of entities in to the mapped collection.
     */
    default < T > InsertManyResult insert( Collection< ? extends T > entities, String collectionName ) {
        return insert(entities, collectionName, new InsertManyOptions());
    }


    /**
     * Inserts entities in to the mapped collection.
     */
    < T > InsertManyResult insert( Collection< ? extends T > entities, Class< ? > entityClass, InsertManyOptions options );

    /**
     * Inserts entities in to the mapped collection.
     */

    < T > InsertManyResult insert( Collection< ? extends T > entities, String collectionName, InsertManyOptions options );



    default < T > UpdateResult updateEntity( Query query, T entity ) {
        return updateEntity(query, entity, new UpdateOptions(), null);
    }

    @Deprecated
    default < T > UpdateResult updateEntity( Query query, T entity, UpdateOptions options ) {
        return updateEntity(query, entity, options, null);
    }

    default < T > UpdateResult updateEntity( Query query, T entity, String collectionName ) {
        return updateEntity(query, entity, new UpdateOptions(), collectionName);
    }


    @Deprecated
    < T > UpdateResult updateEntity( Query query, T entity, UpdateOptions options, String collectionName );

    default < T > UpdateResult upertEntity( Query query, T entity){

        return  updateEntity(query, entity, new UpdateOptions().upsert(true), null);
    }

    default < T > UpdateResult upertEntity( Query query, T entity , String collectionName){
        return  updateEntity(query, entity, new UpdateOptions().upsert(true), collectionName);
    }



    /**
     * todo
     * 修改更新的定义
     */

    @Deprecated
    default < T > UpdateResult update( Query query, UpdateDefinition update, Class< T > entityClass ) {
        return update(query, update, entityClass, new UpdateOptions());
    }


    @Deprecated
    default < T > UpdateResult update( Query query, UpdateDefinition update, Class< T > entityClass, UpdateOptions options ) {
        return update(query, update, entityClass, options, null);
    }

    // 2021-05-18 新增更新方法

    @Deprecated
    default < T > UpdateResult update( Query query, UpdateDefinition update, String collectionName ) {
        return update(query, update, org.bson.Document.class, new UpdateOptions(), collectionName);
    }


    @Deprecated
    default < T > UpdateResult update( Query query, UpdateDefinition update, String collectionName, UpdateOptions options ) {
        return update(query, update, org.bson.Document.class, options, collectionName);
    }

    /**
     *
     * 修改更新的定义
     */

    @Deprecated
    < T > UpdateResult update( Query query, UpdateDefinition update, Class< T > entityClass, UpdateOptions options, String collectionName );



    /**
     * Performs an upsert. If no document is found that matches the query, a new document is created and inserted by
     * combining the query document and the update document. <br />
     * <strong>NOTE:</strong> {@link Query#getSortObject() sorting} is not supported by {@code db.collection.updateOne}.
     * Use {@link #findAndModify(Query, UpdateDefinition, Class, String)} instead.
     *
     * @param query the query document that specifies the criteria used to select a record to be upserted. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing object. Must not be {@literal null}.
     * @param entityClass class that determines the collection to use. Must not be {@literal null}.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     */
    UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass);

    /**
     * Performs an upsert. If no document is found that matches the query, a new document is created and inserted by
     * combining the query document and the update document. <br />
     * <strong>NOTE:</strong> Any additional support for field mapping, versions, etc. is not available due to the lack of
     * domain type information. Use {@link #upsert(Query, UpdateDefinition, Class, String)} to get full type specific
     * support. <br />
     * <strong>NOTE:</strong> {@link Query#getSortObject() sorting} is not supported by {@code db.collection.updateOne}.
     * Use {@link #findAndModify(Query, UpdateDefinition, Class, String)} instead.
     *
     * @param query the query document that specifies the criteria used to select a record to be upserted. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing object. Must not be {@literal null}.
     * @param collectionName name of the collection to update the object in.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     * @see Update
     */
    UpdateResult upsert(Query query, UpdateDefinition update, String collectionName);

    /**
     * Performs an upsert. If no document is found that matches the query, a new document is created and inserted by
     * combining the query document and the update document.
     *
     * @param query the query document that specifies the criteria used to select a record to be upserted. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing object. Must not be {@literal null}.
     * @param entityClass class of the pojo to be operated on. Must not be {@literal null}.
     * @param collectionName name of the collection to update the object in. Must not be {@literal null}.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     * @see Update
     */
    UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName);

    /**
     * Updates the first object that is found in the collection of the entity class that matches the query document with
     * the provided update document.
     *
     * @param query the query document that specifies the criteria used to select a record to be updated. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing. Must not be {@literal null}.
     * @param entityClass class that determines the collection to use.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     * @see Update
     */
    UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass);

    /**
     * Updates the first object that is found in the specified collection that matches the query document criteria with
     * the provided updated document. <br />
     * <strong>NOTE:</strong> Any additional support for field mapping, versions, etc. is not available due to the lack of
     * domain type information. Use {@link #updateFirst(Query, UpdateDefinition, Class, String)} to get full type specific
     * support. <br />
     * <strong>NOTE:</strong> {@link Query#getSortObject() sorting} is not supported by {@code db.collection.updateOne}.
     * Use {@link #findAndModify(Query, UpdateDefinition, Class, String)} instead.
     *
     * @param query the query document that specifies the criteria used to select a record to be updated. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing. Must not be {@literal null}.
     * @param collectionName name of the collection to update the object in. Must not be {@literal null}.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     */
    UpdateResult updateFirst(Query query, UpdateDefinition update, String collectionName);

    /**
     * Updates the first object that is found in the specified collection that matches the query document criteria with
     * the provided updated document. <br />
     *
     * @param query the query document that specifies the criteria used to select a record to be updated. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing. Must not be {@literal null}.
     * @param entityClass class of the pojo to be operated on. Must not be {@literal null}.
     * @param collectionName name of the collection to update the object in. Must not be {@literal null}.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     */
    UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName);

    /**
     * Updates all objects that are found in the collection for the entity class that matches the query document criteria
     * with the provided updated document.
     *
     * @param query the query document that specifies the criteria used to select a record to be updated. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing. Must not be {@literal null}.
     * @param entityClass class of the pojo to be operated on. Must not be {@literal null}.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     */
    UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass);

    /**
     * Updates all objects that are found in the specified collection that matches the query document criteria with the
     * provided updated document. <br />
     * <strong>NOTE:</strong> Any additional support for field mapping, versions, etc. is not available due to the lack of
     * domain type information. Use {@link #updateMulti(Query, UpdateDefinition, Class, String)} to get full type specific
     * support.
     *
     * @param query the query document that specifies the criteria used to select a record to be updated. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing. Must not be {@literal null}.
     * @param collectionName name of the collection to update the object in. Must not be {@literal null}.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     */
    UpdateResult updateMulti(Query query, UpdateDefinition update, String collectionName);

    /**
     * Updates all objects that are found in the collection for the entity class that matches the query document criteria
     * with the provided updated document.
     *
     * @param query the query document that specifies the criteria used to select a record to be updated. Must not be
     *          {@literal null}.
     * @param update the {@link UpdateDefinition} that contains the updated object or {@code $} operators to manipulate
     *          the existing. Must not be {@literal null}.
     * @param entityClass class of the pojo to be operated on. Must not be {@literal null}.
     * @param collectionName name of the collection to update the object in. Must not be {@literal null}.
     * @return the {@link UpdateResult} which lets you access the results of the previous write.
     */
    UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName);

    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    default < T > List< T > save( Collection< ? extends T > entities ) {
        return save(entities, new InsertManyOptions(), null);
    }

    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    default < T > List< T > save( Collection< ? extends T > entities, String collectionName ) {
        return save(entities, new InsertManyOptions(), collectionName);
    }

    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    @Deprecated
    default < T > List< T > save( Collection< ? extends T > entities, InsertManyOptions insertManyOptions ) {
        return save(entities, insertManyOptions, null);
    }

    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    @Deprecated
    default < T > List< T > save( Collection< ? extends T > entities, InsertManyOptions options, String collectionName ) {
        if (entities.isEmpty()) {
            return new ArrayList< T >();
        }
        InsertOneOptions insertOneOption = new InsertOneOptions()
                .bypassDocumentValidation(options.getBypassDocumentValidation())
                .writeConcern(options.writeConcern());
        return entities.stream().map(entity -> save(entity, insertOneOption, collectionName)).collect(Collectors.toList());
    }


    /**
     * Saves an entity (Object) and updates the @Id field
     */
    default < T > T save( T entity ) {
        return save(entity, new InsertOneOptions(), null);
    }

    /**
     * Saves an entity (Object) and updates the @Id field
     */
    default < T > T save( T entity, String collectionName ) {
        return save(entity, new InsertOneOptions(), collectionName);
    }


    /**
     * Saves an entity (Object) and updates the @Id field
     */
    @Deprecated
    default < T > T save( T entity, InsertOneOptions options ) {
        return save(entity, options, null);
    }

    /**
     * Saves an entity (Object) and updates the @Id field
     */
    @Deprecated
    < T > T save( T entity, InsertOneOptions options, String collectionName );


    /**
     * Starts a new session on the server.
     * 提供出去的对外的接口
     */
    MarsSession startSession();

    /**
     * Starts a new session on the server.
     * 提供出去的对外的接口
     */
    MarsSession startSession( ClientSessionOptions options );

    MongoMappingContext getMapper();


    /**
     * Triggers <a href="https://docs.mongodb.org/manual/reference/method/db.collection.findAndModify/">findAndModify </a>
     * to apply provided {@link Update} on documents matching {@link Criteria} of given {@link Query}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param update the {@link UpdateDefinition} to apply on matching documents. Must not be {@literal null}.
     * @param entityClass the parametrized type. Must not be {@literal null}.
     * @return the converted object that was updated before it was updated or {@literal null}, if not found.
     *
     * @see Update
     *
     */

    default < T > T findAndModify( Query query, UpdateDefinition update, Class< T > entityClass ) {
        return findAndModify(query, update, new FindOneAndUpdateOptions(), entityClass);
    }

    /**
     * Triggers <a href="https://docs.mongodb.org/manual/reference/method/db.collection.findAndModify/">findAndModify </a>
     * to apply provided {@link Update} on documents matching {@link Criteria} of given {@link Query}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param update the {@link UpdateDefinition} to apply on matching documents. Must not be {@literal null}.
     * @param entityClass the parametrized type. Must not be {@literal null}.
     * @param collectionName the collection to query. Must not be {@literal null}.
     * @return the converted object that was updated before it was updated or {@literal null}, if not found.
     *
     * @see Update
     *
     */

    default < T > T findAndModify( Query query, UpdateDefinition update, Class< T > entityClass, String collectionName ) {
        return findAndModify(query, update, new FindOneAndUpdateOptions(), entityClass, collectionName);
    }

    /**
     * Triggers <a href="https://docs.mongodb.org/manual/reference/method/db.collection.findAndModify/">findAndModify </a>
     * to apply provided {@link Update} on documents matching {@link Criteria} of given {@link Query} taking
     * {@link FindOneAndUpdateOptions} into account.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification.
     * @param update the {@link UpdateDefinition} to apply on matching documents.
     * @param options the {@link FindOneAndUpdateOptions} holding additional information.
     * @param entityClass the parametrized type.
     * @return the converted object that was updated or {@literal null}, if not found. Depending on the value of
     *         {@link FindOneAndUpdateOptions#getReturnDocument()} this will either be the object as it was before the update or as
     *         it is after the update.
     *
     * @see Update
     *
     */

    default < T > T findAndModify( Query query, UpdateDefinition update, FindOneAndUpdateOptions options, Class< T > entityClass ) {
        return findAndModify(query, update, options, entityClass, getCollectionName(entityClass));
    }

    /**
     * Triggers <a href="https://docs.mongodb.org/manual/reference/method/db.collection.findAndModify/">findAndModify </a>
     * to apply provided {@link Update} on documents matching {@link Criteria} of given {@link Query} taking
     * {@link FindOneAndUpdateOptions} into account.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param update the {@link UpdateDefinition} to apply on matching documents. Must not be {@literal null}.
     * @param options the {@link FindOneAndUpdateOptions} holding additional information. Must not be {@literal null}.
     * @param entityClass the parametrized type. Must not be {@literal null}.
     * @param collectionName the collection to query. Must not be {@literal null}.
     * @return the converted object that was updated or {@literal null}, if not found. Depending on the value of
     *         {@link FindOneAndUpdateOptions#getReturnDocument()} this will either be the object as it was before the update or as
     *         it is after the update.
     *
     * @see Update
     *
     */

    < T > T findAndModify( Query query, UpdateDefinition update, FindOneAndUpdateOptions options, Class< T > entityClass,
                           String collectionName );


    /**
     * Triggers
     * <a href="https://docs.mongodb.com/manual/reference/method/db.collection.findOneAndReplace/">findOneAndReplace</a>
     * to replace a single document matching {@link Criteria} of given {@link Query} with the {@code replacement}
     * document. <br />
     * The collection name is derived from the {@literal replacement} type. <br />
     * Options are defaulted to {@link FindOneAndReplaceOptions}. <br />
     * <strong>NOTE:</strong> The replacement entity must not hold an {@literal id}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param replacement the replacement document. Must not be {@literal null}.
     * @return the converted object that was updated or {@literal null}, if not found.
     *
     */
    @Nullable
    default < T > T findAndReplace( Query query, T replacement ) {
        return findAndReplace(query, replacement, new FindOneAndReplaceOptions());
    }

    /**
     * Triggers
     * <a href="https://docs.mongodb.com/manual/reference/method/db.collection.findOneAndReplace/">findOneAndReplace</a>
     * to replace a single document matching {@link Criteria} of given {@link Query} with the {@code replacement}
     * document.<br />
     * Options are defaulted to {@link FindOneAndReplaceOptions}. <br />
     * <strong>NOTE:</strong> The replacement entity must not hold an {@literal id}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param replacement the replacement document. Must not be {@literal null}.
     * @param collectionName the collection to query. Must not be {@literal null}.
     * @return the converted object that was updated or {@literal null}, if not found.
     *
     */
    @Nullable
    default < T > T findAndReplace( Query query, T replacement, String collectionName ) {
        return findAndReplace(query, replacement, new FindOneAndReplaceOptions(), collectionName);
    }

    /**
     * Triggers
     * <a href="https://docs.mongodb.com/manual/reference/method/db.collection.findOneAndReplace/">findOneAndReplace</a>
     * to replace a single document matching {@link Criteria} of given {@link Query} with the {@code replacement} document
     * taking {@link FindOneAndReplaceOptions} into account.<br />
     * <strong>NOTE:</strong> The replacement entity must not hold an {@literal id}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param replacement the replacement document. Must not be {@literal null}.
     * @param options the {@link FindOneAndUpdateOptions} holding additional information. Must not be {@literal null}.
     * @return the converted object that was updated or {@literal null}, if not found. Depending on the value of
     *         {@link FindOneAndReplaceOptions#getReturnDocument()} this will either be the object as it was before the update or
     *         as it is after the update.
     *
     */
    @Nullable
    default < T > T findAndReplace( Query query, T replacement, FindOneAndReplaceOptions options ) {
        return findAndReplace(query, replacement, options, getCollectionName(ClassUtil.getClass(replacement)));
    }

    /**
     * Triggers
     * <a href="https://docs.mongodb.com/manual/reference/method/db.collection.findOneAndReplace/">findOneAndReplace</a>
     * to replace a single document matching {@link Criteria} of given {@link Query} with the {@code replacement} document
     * taking {@link FindOneAndReplaceOptions} into account.<br />
     * <strong>NOTE:</strong> The replacement entity must not hold an {@literal id}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param replacement the replacement document. Must not be {@literal null}.
     * @param options the {@link FindOneAndUpdateOptions} holding additional information. Must not be {@literal null}.
     * @return the converted object that was updated or {@literal null}, if not found. Depending on the value of
     *         {@link FindOneAndReplaceOptions#getReturnDocument()} this will either be the object as it was before the update or
     *         as it is after the update.
     *
     */
    @Nullable
    default < T > T findAndReplace( Query query, T replacement, FindOneAndReplaceOptions options, String collectionName ) {

        notNull(replacement, "Replacement must not be null!");
        return findAndReplace(query, replacement, options, (Class< T >) ClassUtil.getClass(replacement), collectionName);
    }

    /**
     * Triggers
     * <a href="https://docs.mongodb.com/manual/reference/method/db.collection.findOneAndReplace/">findOneAndReplace</a>
     * to replace a single document matching {@link Criteria} of given {@link Query} with the {@code replacement} document
     * taking {@link FindOneAndReplaceOptions} into account.<br />
     * <strong>NOTE:</strong> The replacement entity must not hold an {@literal id}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param replacement the replacement document. Must not be {@literal null}.
     * @param options the {@link FindOneAndUpdateOptions} holding additional information. Must not be {@literal null}.
     * @param entityType the parametrized type. Must not be {@literal null}.
     * @param collectionName the collection to query. Must not be {@literal null}.
     * @return the converted object that was updated or {@literal null}, if not found. Depending on the value of
     *         {@link FindOneAndReplaceOptions#getReturnDocument()} this will either be the object as it was before the update or
     *         as it is after the update.
     *
     */
    @Nullable
    default < T > T findAndReplace( Query query, T replacement, FindOneAndReplaceOptions options, Class< T > entityType,
                                    String collectionName ) {

        return findAndReplace(query, replacement, options, entityType, collectionName, entityType);
    }

    /**
     * Triggers
     * <a href="https://docs.mongodb.com/manual/reference/method/db.collection.findOneAndReplace/">findOneAndReplace</a>
     * to replace a single document matching {@link Criteria} of given {@link Query} with the {@code replacement} document
     * taking {@link FindOneAndReplaceOptions} into account.<br />
     * <strong>NOTE:</strong> The replacement entity must not hold an {@literal id}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param replacement the replacement document. Must not be {@literal null}.
     * @param options the {@link FindOneAndUpdateOptions} holding additional information. Must not be {@literal null}.
     * @param entityType the type used for mapping the {@link Query} to domain type fields and deriving the collection
     *          from. Must not be {@literal null}.
     * @param resultType the parametrized type projection return type. Must not be {@literal null}, use the domain type of
     *          {@code Object.class} instead.
     * @return the converted object that was updated or {@literal null}, if not found. Depending on the value of
     *         {@link FindOneAndReplaceOptions#getReturnDocument()} this will either be the object as it was before the update or
     *         as it is after the update.
     *
     */
    @Nullable
    default < S, T > T findAndReplace( Query query, S replacement, FindOneAndReplaceOptions options, Class< S > entityType,
                                       Class< T > resultType ) {

        return findAndReplace(query, replacement, options, entityType,
                getCollectionName(entityType), resultType);
    }

    /**
     * Triggers
     * <a href="https://docs.mongodb.com/manual/reference/method/db.collection.findOneAndReplace/">findOneAndReplace</a>
     * to replace a single document matching {@link Criteria} of given {@link Query} with the {@code replacement} document
     * taking {@link FindOneAndReplaceOptions} into account.<br />
     * <strong>NOTE:</strong> The replacement entity must not hold an {@literal id}.
     *
     * @param query the {@link Query} class that specifies the {@link Criteria} used to find a record and also an optional
     *          fields specification. Must not be {@literal null}.
     * @param replacement the replacement document. Must not be {@literal null}.
     * @param options the {@link FindOneAndUpdateOptions} holding additional information. Must not be {@literal null}.
     * @param entityType the type used for mapping the {@link Query} to domain type fields. Must not be {@literal null}.
     * @param collectionName the collection to query. Must not be {@literal null}.
     * @param resultType the parametrized type projection return type. Must not be {@literal null}, use the domain type of
     *          {@code Object.class} instead.
     * @return the converted object that was updated or {@literal null}, if not found. Depending on the value of
     *         {@link FindOneAndReplaceOptions#getReturnDocument()} this will either be the object as it was before the update or
     *         as it is after the update.
     *
     */
    @Nullable
    < S, T > T findAndReplace( Query query, S replacement, FindOneAndReplaceOptions options, Class< S > entityType,
                               String collectionName, Class< T > resultType );

    /**
     * Map the results of an ad-hoc query on the collection for the entity type to a single instance of an object of the
     * specified type. The first document that matches the query is returned and also removed from the collection in the
     * database.
     * <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}.
     * <br />
     * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
     * feature rich {@link Query}.
     *
     * @param query the query class that specifies the criteria used to find a record and also an optional fields
     *          specification.
     * @param entityClass the parametrized type of the returned list.
     * @return the converted object
     */

    default < T > T findAndDelete( Query query, Class< T > entityClass ) {
        return findAndDelete(query, entityClass, getCollectionName(entityClass));
    }

    /**
     * Map the results of an ad-hoc query on the specified collection to a single instance of an object of the specified
     * type. The first document that matches the query is returned and also removed from the collection in the database.
     * <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used.
     * <br />
     * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
     * feature rich {@link Query}.
     *
     * @param query the query class that specifies the criteria used to find a record and also an optional fields
     *          specification.
     * @param entityClass the parametrized type of the returned list.
     * @param collectionName name of the collection to retrieve the objects from.
     * @return the converted object.
     */

    default < T > T findAndDelete( Query query, Class< T > entityClass, String collectionName ) {
        return findAndDelete(query, entityClass, collectionName, new FindOneAndDeleteOptions());
    }


    /**
     * Map the results of an ad-hoc query on the specified collection to a single instance of an object of the specified
     * type. The first document that matches the query is returned and also removed from the collection in the database.
     * <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used.
     * <br />
     * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
     * feature rich {@link Query}.
     *
     * @param query the query class that specifies the criteria used to find a record and also an optional fields
     *          specification.
     * @param entityClass the parametrized type of the returned list.
     * @param collectionName name of the collection to retrieve the objects from.
     * @param options  FindOneAndDeleteOptions
     * @return the converted object.
     */
    < T > T findAndDelete( Query query, Class< T > entityClass, String collectionName, FindOneAndDeleteOptions options );

    /**
     * 根据类型 获取实体对应的表名
     *
     * @param entityClass must not be {@literal null}.
     * @return never {@literal null}.
     */
    String getCollectionName( Class< ? > entityClass );


    /**
     * @param transaction the transaction wrapper
     * @param <T>         the return type
     * @return the return value
     */
    <T> T withTransaction(MarsTransaction<T> transaction);

    /**
     * @param <T>         the return type
     * @param options     the session options to apply
     * @param transaction the transaction wrapper
     * @return the return value
     *
     */
    <T> T withTransaction( MarsTransaction<T> transaction , ClientSessionOptions options);


    /**
     * Execute the a MongoDB command expressed as a JSON string. Parsing is delegated to {@link Document#parse(String)} to
     * obtain the {@link Document} holding the actual command. Any errors that result from executing this command will be
     * converted into  exception hierarchy.
     *
     * @param jsonCommand a MongoDB command expressed as a JSON string. Must not be {@literal null}.
     * @return a result object returned by the action.
     */
    Document executeCommand(String jsonCommand);

    /**
     * Execute a MongoDB command. Any errors that result from executing this command will be converted into
     * exception hierarchy.
     *
     * @param command a MongoDB command.
     * @return a result object returned by the action.
     */
    Document executeCommand(Document command);

    /**
     * Execute a MongoDB command. Any errors that result from executing this command will be converted into
     * access exception hierarchy.
     *
     * @param command a MongoDB command, must not be {@literal null}.
     * @param readPreference read preferences to use, can be {@literal null}.
     * @return a result object returned by the action.
     *
     */
    Document executeCommand(Document command, @Nullable ReadPreference readPreference);

}
