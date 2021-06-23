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
import com.mongodb.lang.Nullable;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.UpdateDefinition;
import com.whaleal.mars.session.option.*;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import com.whaleal.mars.session.result.UpdateResult;
import com.whaleal.mars.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

public interface Datastore extends IndexOperations {

    default <T> UpdateResult replace(Query query, T entity) {
        return replace(query, entity, new ReplaceOptions());
    }

    default <T> UpdateResult replace(Query query, T entity, ReplaceOptions options) {
        return replace(query, entity, options, null);
    }

    <T> UpdateResult replace(Query query, T entity, ReplaceOptions options, @Nullable String collectionName);

    default <T> DeleteResult delete(Query query, Class<T> entityClass) {
        return delete(query, entityClass, new DeleteOptions());
    }

    default <T> DeleteResult delete(Query query, Class<T> entityClass, DeleteOptions deleteOptions) {
        return delete(query, entityClass, deleteOptions, null);
    }


    //todo  根据实体 Id  删除
    /*default  DeleteResult delete(Object entity) {

        Assert.notNull(entity, "Object must not be null!");

        return delete(entity, getCollectionName(entity.getClass()));
    }


    default  DeleteResult delete(Object entity, String collectionName) {

        Assert.notNull(object, "Object must not be null!");
        Assert.hasText(collectionName, "Collection name must not be null or empty!");

        Query query = operations.forEntity(object).getRemoveByQuery();

        return delete(collectionName, query, object.getClass());
    }*/


    default DeleteResult delete(Query query, String collectionName) {
        return delete(query, org.bson.Document.class, new DeleteOptions(), collectionName);
    }


    default DeleteResult delete(Query query, String collectionName, DeleteOptions options) {
        return delete(query, org.bson.Document.class, options, collectionName);
    }


    default DeleteResult delete(Query query, Class<?> entityClass, String collectionName) {

        Assert.notNull(entityClass, "EntityClass must not be null!");
        return delete(query, entityClass, new DeleteOptions(), collectionName);
    }


    <T> DeleteResult delete(Query query, Class<T> entityClass, DeleteOptions options, @Nullable String collectionName);

    default <T> QueryCursor<T> findAll(Query query, Class<T> entityClass) {
        return findAll(query, entityClass, null);
    }

    <T> QueryCursor<T> findAll(Query query, Class<T> entityClass, @Nullable String collectionName);

    default <T> Optional<T> findOne(Query query, Class<T> entityClass) {
        return findOne(query, entityClass, null);
    }

    <T> Optional<T> findOne(Query query, Class<T> entityClass, @Nullable String collectionName);

    /**
     * Inserts an entity in to the mapped collection.
     */
    default <T> InsertOneResult insert(T entity) {
        return insert(entity, new InsertOneOptions());
    }

    /**
     * Inserts an entity in to the mapped collection.
     */
    default <T> InsertOneResult insert(T entity, @Nullable String collectionName) {
        return insert(entity, new InsertOneOptions(), collectionName);
    }

    /**
     * Inserts an entity in to the mapped collection.
     */
    default <T> InsertOneResult insert(T entity, InsertOneOptions insertOneOptions) {
        return insert(entity, insertOneOptions, null);
    }


    /**
     * Inserts an entity in to the mapped collection.
     */
    <T> InsertOneResult insert(T entity, InsertOneOptions options, @Nullable String collectionName);


    /**
     * Inserts a List of entities in to the mapped collection.
     */
    default <T> InsertManyResult insert(Collection<? extends T> entities) {
        return insert(entities, new InsertManyOptions(), null);
    }

    /**
     * Inserts a List of entities in to the mapped collection.
     */
    default <T> InsertManyResult insert(Collection<? extends T> entities, @Nullable String collectionName) {
        return insert(entities, new InsertManyOptions(), collectionName);
    }


    /**
     * Inserts entities in to the mapped collection.
     */
    default <T> InsertManyResult insert(Collection<? extends T> entities, InsertManyOptions options) {
        return insert(entities, options, null);
    }

    /**
     * Inserts entities in to the mapped collection.
     */
    <T> InsertManyResult insert(Collection<? extends T> entities, InsertManyOptions options, @Nullable String collectionName);


    default <T> UpdateResult update(Query query, T entity) {
        return update(query, entity, new UpdateOptions(), null);
    }

    default <T> UpdateResult update(Query query, T entity, UpdateOptions options) {
        return update(query, entity, options, null);
    }

    default <T> UpdateResult update(Query query, T entity, @Nullable String collectionName) {
        return update(query, entity, new UpdateOptions(), collectionName);
    }


    <T> UpdateResult update(Query query, T entity, UpdateOptions options, @Nullable String collectionName);


    /**
     * todo
     * 修改更新的定义
     */

    default <T> UpdateResult update(Query query, UpdateDefinition update, Class<T> entityClass) {
        return update(query, update, entityClass, new UpdateOptions());
    }


    default <T> UpdateResult update(Query query, UpdateDefinition update, Class<T> entityClass, UpdateOptions options) {
        return update(query, update, entityClass, options, null);
    }

    // 2021-05-18 新增更新方法

    default <T> UpdateResult update(Query query, UpdateDefinition update, String collectionName) {
        return update(query, update, org.bson.Document.class, new UpdateOptions(), collectionName);
    }


    default <T> UpdateResult update(Query query, UpdateDefinition update, String collectionName, UpdateOptions options) {
        return update(query, update, org.bson.Document.class, options, collectionName);
    }

    /**
     * todo
     * 修改更新的定义
     */

    <T> UpdateResult update(Query query, UpdateDefinition update, Class<T> entityClass, UpdateOptions options, @Nullable String collectionName);


    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    default <T> List<T> save(Collection<? extends T> entities) {
        return save(entities, new InsertManyOptions(), null);
    }


    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    default <T> List<T> save(Collection<? extends T> entities, InsertManyOptions insertManyOptions) {
        return save(entities, insertManyOptions, null);
    }

    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    default <T> List<T> save(Collection<? extends T> entities, InsertManyOptions options, @Nullable String collectionName) {
        if (entities.isEmpty()) {
            return new ArrayList<T>();
        }
        InsertOneOptions insertOneOption = new InsertOneOptions()
                .bypassDocumentValidation(options.getBypassDocumentValidation())
                .writeConcern(options.writeConcern());
        return entities.stream().map(entity -> save(entity, insertOneOption, collectionName)).collect(Collectors.toList());
    }


    /**
     * Saves an entity (Object) and updates the @Id field
     */
    default <T> T save(T entity) {
        return save(entity, new InsertOneOptions(), null);
    }


    /**
     * Saves an entity (Object) and updates the @Id field
     */
    default <T> T save(T entity, InsertOneOptions options) {
        return save(entity, options, null);
    }

    /**
     * Saves an entity (Object) and updates the @Id field
     */
    <T> T save(T entity, InsertOneOptions options, @Nullable String collectionName);


    /**
     * Starts a new session on the server.
     * 提供出去的对外的接口
     */
    MarsSession startSession();

    /**
     * Starts a new session on the server.
     * 提供出去的对外的接口
     */
    MarsSession startSession(ClientSessionOptions options);

    MongoMappingContext getMapper();

}
