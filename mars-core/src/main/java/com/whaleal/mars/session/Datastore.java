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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.lang.Nullable;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ClassUtil;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.core.query.*;
import com.whaleal.mars.session.option.*;
import com.whaleal.mars.session.transactions.MarsTransaction;
import org.bson.Document;
import com.mongodb.client.model.ReplaceOptions;
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


    /**
     * 根据条件进行替换
     * @param query
     * @param replacement
     * @param <T>
     * @return
     */
    default < T > UpdateResult replace( Query query, T replacement ) {

        Precondition.notNull(query, "Query must not be null");
        Precondition.notNull(replacement,"Replacement must not be null!");
        return replace(query, replacement, new ReplaceOptions().upsert(false),getCollectionName(replacement.getClass()));
    }


    /**
     * 根据条件进行替换
     * @param <T>
     * @param query
     * @param replacement
     * @param options
     * @return
     */
    default < T > UpdateResult replace( Query query, T replacement, ReplaceOptions options ) {
        Precondition.notNull(query, "Query must not be null");
        Precondition.notNull(replacement,"Replacement must not be null!");
        Precondition.notNull(options, "Options must not be null Use ReplaceOptions#new() instead");
        return replace(query, replacement, options, getCollectionName(replacement.getClass()));
    }


    /**
     * 根据条件进行替换
     * @param <T>
     * @param query
     * @param replacement
     * @param collectionName
     * @return
     */
    default < T > UpdateResult replace( Query query, T replacement, String collectionName ) {
        Precondition.notNull(query, "Query must not be null");
        Precondition.notNull(collectionName, "CollectionName must not be null");
        Precondition.notNull(replacement,"Replacement must not be null!");
        return replace(query, replacement, new ReplaceOptions().upsert(false), collectionName);
    }


    /**
     * 根据条件进行替换
     * @param <T>
     * @param query
     * @param replacement
     * @param options
     * @param collectionName
     * @return
     */
    < T > UpdateResult replace( Query query, T replacement, ReplaceOptions options, String collectionName );



    /**
     * Remove the given object from the collection by {@literal id} and (if applicable) its
     *
     * Use {@link DeleteResult#getDeletedCount()} for insight whether an {@link DeleteResult#wasAcknowledged()
     * acknowledged} remove operation was successful or not.
     *
     * @param object must not be {@literal null}.
     * @return the {@link DeleteResult} which lets you access the results of the previous delete.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the target collection name cannot be
     *           {@link #getCollectionName(Class) derived} from the given object type.
     */
    default DeleteResult delete(Object object){
        notNull(object, "Object must not be null");
        return delete(object ,getCollectionName(object.getClass()));
    }



    /**
     * Removes the given object from the given collection by {@literal id} and (if applicable) its
     *
     * Use {@link DeleteResult#getDeletedCount()} for insight whether an {@link DeleteResult#wasAcknowledged()
     * acknowledged} remove operation was successful or not.
     *
     * 根据 对象的 id  字段删除
     *
     * @param object must not be {@literal null}.
     * @param collectionName name of the collection where the objects will removed, must not be {@literal null} or empty.
     * @return the {@link DeleteResult} which lets you access the results of the previous delete.
     */
    DeleteResult delete( Object object, String collectionName );


    /**
     * Remove one documents that match the provided query document criteria from the collection used to store the
     * entityClass. The Class parameter is also used to help convert the Id of the object if it is present in the query.
     *
     * @param query the query document that specifies the criteria used to remove a record.
     * @param entityClass class that determines the collection to use.
     * @return the {@link DeleteResult} which lets you access the results of the previous delete.
     * @throws IllegalArgumentException when {@literal query} or {@literal entityClass} is {@literal null}.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the target collection name cannot be
     *           {@link #getCollectionName(Class) derived} from the given type.
     */
    default < T > DeleteResult delete( Query query, Class< T > entityClass ) {
        return delete(query, entityClass,getCollectionName(entityClass));
    }

    /**
     * Remove one documents from the specified collection that match the provided query document criteria. There is no
     * conversion/mapping done for any criteria using the id field. <br />
     * <strong>NOTE:</strong> Any additional support for field mapping is not available due to the lack of domain type
     * information. Use {@link #delete(Query, Class, String)} to get full type specific support.
     *
     * @param query the query document that specifies the criteria used to remove a record.
     * @param collectionName name of the collection where the objects will removed, must not be {@literal null} or empty.
     * @return the {@link DeleteResult} which lets you access the results of the previous delete.
     * @throws IllegalArgumentException when {@literal query} or {@literal collectionName} is {@literal null}.
     */
    default DeleteResult delete( Query query, String collectionName ) {
        return delete(query, Object.class, collectionName);
    }


    /**
     * Remove one documents that match the provided query document criteria from the collection used to store the
     * entityClass. The Class parameter is also used to help convert the Id of the object if it is present in the query.
     *
     * @param query the query document that specifies the criteria used to remove a record.
     * @param entityClass class of the pojo to be operated on. Can be {@literal null}.
     * @param collectionName name of the collection where the objects will removed, must not be {@literal null} or empty.
     * @return the {@link DeleteResult} which lets you access the results of the previous delete.
     * @throws IllegalArgumentException when {@literal query}, {@literal entityClass} or {@literal collectionName} is
     *           {@literal null}.
     */
    DeleteResult delete( Query query, @Nullable Class< ? > entityClass, String collectionName ) ;



    /**
     * Remove Multi documents from the specified collection that match the provided query document criteria. There is no
     * conversion/mapping done for any criteria using the id field. <br />
     * <strong>NOTE:</strong> Any additional support for field mapping is not available due to the lack of domain type
     * information. Use {@link #delete(Query, Class, String)} to get full type specific support.
     *
     * @param query the query document that specifies the criteria used to remove a record.
     * @param collectionName name of the collection where the objects will removed, must not be {@literal null} or empty.
     * @return the {@link DeleteResult} which lets you access the results of the previous delete.
     * @throws IllegalArgumentException when {@literal query} or {@literal collectionName} is {@literal null}.
     */
    default DeleteResult deleteMulti( Query query, String collectionName ) {
        Precondition.notNull(query, "Query must not be null");
        Precondition.hasText(collectionName, "Collection name must not be null or empty");
        return deleteMulti(query, Document.class, collectionName);
    }

    default DeleteResult deleteMulti( Query query, Class<?> entityClass ) {
        Precondition.notNull(query, "Query must not be null");
        return deleteMulti(query, entityClass,this.getCollectionName(entityClass));
    }

    /**
     * Remove Multi documents that match the provided query document criteria from the collection used to store the
     * entityClass. The Class parameter is also used to help convert the Id of the object if it is present in the query.
     *
     * @param query the query document that specifies the criteria used to remove a record.
     * @param entityClass class of the pojo to be operated on. Can be {@literal null}.
     * @param collectionName name of the collection where the objects will removed, must not be {@literal null} or empty.
     * @return the {@link DeleteResult} which lets you access the results of the previous delete.
     * @throws IllegalArgumentException when {@literal query}, {@literal entityClass} or {@literal collectionName} is
     *           {@literal null}.
     */
    DeleteResult deleteMulti( Query query, @Nullable Class< ? > entityClass, String collectionName ) ;



    /**
     * Query for a list of objects of type T from the collection used by the entity class. <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used. <br />
     * If your collection does not contain a homogeneous collection of types, this operation will not be an efficient way
     * to map objects since the test for class type is done in the client and not on the server.
     *
     * @param entityClass the parametrized type of the returned list.
     * @return the QueryCursor.
     */
    default < T > QueryCursor< T > findAll(  Class< T > entityClass ) {
        return find(new Query(), entityClass, getCollectionName(entityClass));
    }

    /**
     * Query for a list of objects of type T from the specified collection. <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used. <br />
     * If your collection does not contain a homogeneous collection of types, this operation will not be an efficient way
     * to map objects since the test for class type is done in the client and not on the server.
     *
     * @param entityClass the parametrized type of the returned list.
     * @param collectionName name of the collection to retrieve the objects from.
     * @return the QueryCursor.
     */
    default < T > QueryCursor< T > findAll(  @Nullable Class< T > entityClass ,String collectionName ) {
        return find(new Query(), entityClass, collectionName);
    }



    @Deprecated
    default < T > QueryCursor< T > findAll( Query query, Class< T > entityClass ) {
        Precondition.notNull(query, "Query must not be null");
        return find(query, entityClass, getCollectionName(entityClass));
    }

    @Deprecated
    default < T > QueryCursor< T > findAll( Query query, Class< T > entityClass,String collectionName) {
        Precondition.notNull(query, "Query must not be null");
        return find(query, entityClass, collectionName);
    }

    /**
     * Map the results of an ad-hoc query on the collection for the entity class to a List of the specified type. <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used. <br />
     * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
     * feature rich {@link Query}.
     *
     * @param query the query class that specifies the criteria used to find a record and also an optional fields
     *          specification. Must not be {@literal null}.
     * @param entityClass the parametrized type of the returned list. Must not be {@literal null}.
     * @return the QueryCursor of converted objects.
     */
    default <T> QueryCursor<T> find(Query query, Class<T> entityClass){
        Precondition.notNull(query, "Query must not be null");
        Precondition.notNull(entityClass,"Class must not be null!");
        return find(query ,entityClass ,getCollectionName(entityClass));
    }

    /**
     * Map the results of an ad-hoc query on the specified collection to a List of the specified type. <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used. <br />
     * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
     * feature rich {@link Query}.
     *
     * @param query the query class that specifies the criteria used to find a record and also an optional fields
     *          specification. Must not be {@literal null}.
     * @param entityClass the parametrized type of the returned list. Must not be {@literal null}.
     * @param collectionName name of the collection to retrieve the objects from. Must not be {@literal null}.
     * @return the QueryCursor of converted objects.
     */
    <T> QueryCursor<T> find(Query query, @Nullable Class<T> entityClass, String collectionName);



    /**
     * Map the results of an ad-hoc query on the collection for the entity class to a single instance of an object of the
     * specified type. <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used. <br />
     * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
     * feature rich {@link Query}.
     *
     * @param query the query class that specifies the criteria used to find a record and also an optional fields
     *          specification.
     * @param entityClass the parametrized type of the returned list.
     * @return the converted object.
     */
    default < T > Optional< T > findOne( Query query, Class< T > entityClass ) {
        Precondition.notNull(query, "Query must not be null");
        Precondition.notNull(entityClass, "Class must not be null");
        return findOne(query, entityClass, getCollectionName(entityClass));
    }


    /**
     * Map the results of an ad-hoc query on the specified collection to a single instance of an object of the specified
     * type. <br />
     * The object is converted from the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used. <br />
     * The query is specified as a {@link Query} which can be created either using the {@link BasicQuery} or the more
     * feature rich {@link Query}.
     *
     * @param query the query class that specifies the criteria used to find a record and also an optional fields
     *          specification.
     * @param entityClass the parametrized type of the returned list.
     * @param collectionName name of the collection to retrieve the objects from.
     * @return the converted object.
     */
    < T > Optional< T > findOne( Query query, @Nullable Class< T > entityClass, String collectionName );


    /**
     * Returns a document with the given id mapped onto the given class. The collection the query is ran against will be
     * derived from the given target class as well.
     *
     * @param id the id of the document to return. Must not be {@literal null}.
     * @param entityClass the type the document shall be converted into. Must not be {@literal null}.
     * @return the document with the given id mapped onto the given target class.
     */
    default <T> Optional< T > findById(Object id, Class< T > entityClass){
        Precondition.notNull(id, "Query must not be null");
        Precondition.notNull(entityClass, "Class must not be null");
        return findById(id,entityClass,getCollectionName(entityClass));
    }

    /**
     * Returns the document with the given id from the given collection mapped onto the given target class.
     *
     * @param id the id of the document to return.
     * @param entityClass the type to convert the document to.
     * @param collectionName the collection to query for the document.
     * @return he converted object or {@literal null} if document does not exist.
     */
    <T> Optional< T > findById(Object id,@Nullable Class< T > entityClass,String collectionName);


    /**
     * Finds the distinct values for a specified {@literal field} across a single {@link MongoCollection} or view and
     * returns the results in a {@link List}.
     *
     * @param field the name of the field to inspect for distinct values. Must not be {@literal null}.
     * @param entityClass the domain type used for determining the actual {@link MongoCollection}. Must not be
     *          {@literal null}.
     * @param resultClass the result type. Must not be {@literal null}.
     * @return never {@literal null}.
     *
     */
    default <T> QueryCursor<T> findDistinct(String field, @Nullable Class<?> entityClass, Class<T> resultClass) {
        Precondition.notNull(field, "field must not be null");
        Precondition.notNull(resultClass, "Result class must not be null");
        return this.findDistinct(new Query(), field, entityClass, resultClass);
    }


    /**
     * Finds the distinct values for a specified {@literal field} across a single {@link MongoCollection} or view and
     * returns the results in a {@link List}.
     *
     * @param query filter {@link Query} to restrict search. Must not be {@literal null}.
     * @param field the name of the field to inspect for distinct values. Must not be {@literal null}.
     * @param entityClass the domain type used for determining the actual {@link MongoCollection} and mapping the
     *          {@link Query} to the domain type fields. Must not be {@literal null}.
     * @param resultClass the result type. Must not be {@literal null}.
     * @return never {@literal null}.
     *
     */
    default <T> QueryCursor<T> findDistinct(Query query, String field, @Nullable Class<?> entityClass, Class<T> resultClass){
        Precondition.notNull(query, "Query must not be null");
        Precondition.notNull(field, "field must not be null");
        Precondition.notNull(resultClass, "Result class must not be null");
        return findDistinct(query, field, this.getCollectionName(entityClass), entityClass, resultClass);
    }


    /**
     * Finds the distinct values for a specified {@literal field} across a single {@link MongoCollection} or view and
     * returns the results in a {@link List}.
     *
     * @param query filter {@link Query} to restrict search. Must not be {@literal null}.
     * @param field the name of the field to inspect for distinct values. Must not be {@literal null}.
     * @param collectionName the explicit name of the actual {@link MongoCollection}. Must not be {@literal null}.
     * @param entityClass the domain type used for mapping the {@link Query} to the domain type fields.
     * @param resultClass the result type. Must not be {@literal null}.
     * @return never {@literal null}.
     *
     */
    <T> QueryCursor<T> findDistinct(Query query, String field, String collectionName, @Nullable Class<?> entityClass, Class<T> resultClass);


    /**
     * Finds the distinct values for a specified {@literal field} across a single {@link MongoCollection} or view and
     * returns the results in a {@link List}.
     *
     * @param query filter {@link Query} to restrict search. Must not be {@literal null}.
     * @param field the name of the field to inspect for distinct values. Must not be {@literal null}.
     * @param collectionName the explicit name of the actual {@link MongoCollection}. Must not be {@literal null}.
     * @param resultClass the result type. Must not be {@literal null}.
     * @return never {@literal null}.
     *
     */
    default <T> QueryCursor<T> findDistinct(Query query, String field, String collectionName, Class<T> resultClass) {
        return this.findDistinct(query, field, collectionName, Object.class, resultClass);
    }

    /**
     * Insert the object into the collection for the entity type of the object to save. <br />
     * The object is converted to the MongoDB native representation using an instance of {@see MongoConverter}. <br />
     * If your object has an "Id' property, it will be set with the generated Id from MongoDB. If your Id property is a
     * String then MongoDB ObjectId will be used to populate that string. Otherwise, the conversion from ObjectId to your
     * property type will be handled by Spring's BeanWrapper class that leverages Type Conversion API. See
     * <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation" > Spring's
     * Type Conversion"</a> for more details. <br />
     * Insert is used to initially store the object into the database. To update an existing object use the save method.
     * <br />
     * The {@code objectToSave} must not be collection-like.
     *
     * @param objectToSave the object to store in the collection. Must not be {@literal null}.
     * @return the inserted object.
     * @throws IllegalArgumentException in case the {@code objectToSave} is collection-like.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the target collection name cannot be
     *           {@link #getCollectionName(Class) derived} from the given object type.
     */
    default < T > T insert( T objectToSave ) {
        Precondition.notNull(objectToSave, "ObjectToSave must not be null");
        return insert(objectToSave, getCollectionName(objectToSave.getClass()));
    }

    /**
     * Insert the object into the specified collection. <br />
     * The object is converted to the MongoDB native representation using an instance of {@see MongoConverter}. Unless
     * configured otherwise, an instance of {@link MongoMappingContext} will be used. <br />
     * Insert is used to initially store the object into the database. To update an existing object use the save method.
     * <br />
     * The {@code objectToSave} must not be collection-like.
     *
     * @param objectToSave the object to store in the collection. Must not be {@literal null}.
     * @param collectionName name of the collection to store the object in. Must not be {@literal null}.
     * @return the inserted object.
     * @throws IllegalArgumentException in case the {@code objectToSave} is collection-like.
     */
     < T > T insert( T objectToSave, String collectionName );



    /**
     * Insert a Collection of objects into a collection in a single batch write to the database.
     *
     * @param batchToSave the batch of objects to save. Must not be {@literal null}.
     * @param entityClass class that determines the collection to use. Must not be {@literal null}.
     * @return the inserted objects that.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the target collection name cannot be
     *           {@link #getCollectionName(Class) derived} from the given type.
     */
     < T > Collection<T> insert( Collection< ? extends T > batchToSave, Class< ? > entityClass ) ;

    /**
     * Insert a batch of objects into the specified collection in a single batch write to the database.
     *
     * @param batchToSave the list of objects to save. Must not be {@literal null}.
     * @param collectionName name of the collection to store the object in. Must not be {@literal null}.
     * @return the inserted objects that.
     */
     < T > Collection<T>  insert( Collection< ? extends T > batchToSave, String collectionName ) ;

    /**
     * Insert a mixed Collection of objects into a database collection determining the collection name to use based on the
     * class.
     *
     * @param objectsToSave the list of objects to save. Must not be {@literal null}.
     * @return the inserted objects.
     * @throws com.whaleal.mars.codecs.MarsOrmException if the target collection name cannot be
     *           {@link #getCollectionName(Class) derived} for the given objects.
     */
    <T> Collection<T> insertAll(Collection<? extends T> objectsToSave);


    /**
     * Inserts entities in to the mapped collection.
     */
    @Deprecated
    < T > Collection<T> insert( Collection< ? extends T > entities, Class< ? > entityClass, InsertManyOptions options );

    /**
     * Inserts entities in to the mapped collection.
     */

    @Deprecated
    < T > Collection<T> insert( Collection< ? extends T > entities, String collectionName, InsertManyOptions options );



    @Deprecated
    default < T > UpdateResult updateEntity( Query query, T entity ) {
        return updateEntity(query, entity, new UpdateOptions(), null);
    }

    @Deprecated
    default < T > UpdateResult updateEntity( Query query, T entity, UpdateOptions options ) {
        return updateEntity(query, entity, options, null);
    }

    @Deprecated
    default < T > UpdateResult updateEntity( Query query, T entity, String collectionName ) {
        return updateEntity(query, entity, new UpdateOptions(), collectionName);
    }


    @Deprecated
    < T > UpdateResult updateEntity( Query query, T entity, UpdateOptions options, String collectionName );

    @Deprecated
    default < T > UpdateResult upertEntity( Query query, T entity){

        return  updateEntity(query, entity, new UpdateOptions().upsert(true), null);
    }

    @Deprecated
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
    //todo 单独实现
    < T > List< T > save( Collection< ? extends T > entities );

    /**
     * Saves the entities (Objects) and updates the @Id field
     */
    < T > List< T > save( Collection< ? extends T > entities, String collectionName );


    /**
     * Saves an entity (Object) and updates the @Id field
     */
    default < T > T save( T entity ) {
        return save(entity,  getCollectionName(entity.getClass()));
    }

    /**
     * Saves an entity (Object) and updates the @Id field
     */
     < T > T save( T entity, String collectionName ) ;


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


    /**
     * Determine result of given {@link Query} contains at least one element. <br />
     * <strong>NOTE:</strong> Any additional support for query/field mapping, etc. is not available due to the lack of
     * domain type information. Use {@link #exists(Query, Class, String)} to get full type specific support.
     *
     * @param query the {@link Query} class that specifies the criteria used to find a record.
     * @param collectionName name of the collection to check for objects.
     * @return {@literal true} if the query yields a result.
     */
    default boolean exists(Query query, String collectionName){
        return exists(query,null,collectionName);
    }

    /**
     * Determine result of given {@link Query} contains at least one element.
     *
     * @param query the {@link Query} class that specifies the criteria used to find a record.
     * @param entityClass the parametrized type.
     * @return {@literal true} if the query yields a result.
     */
    default boolean exists(Query query, Class<?> entityClass){
        return exists(query,entityClass,null);
    }

    /**
     * Determine result of given {@link Query} contains at least one element.
     *
     * @param query the {@link Query} class that specifies the criteria used to find a record.
     * @param entityClass the parametrized type. Can be {@literal null}.
     * @param collectionName name of the collection to check for objects.
     * @return {@literal true} if the query yields a result.
     */
    boolean exists(Query query, @Nullable Class<?> entityClass, String collectionName);

}
