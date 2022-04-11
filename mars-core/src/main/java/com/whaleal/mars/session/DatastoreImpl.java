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

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;

import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.icefrog.core.util.OptionalUtil;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.icefrog.log.Log;
import com.whaleal.icefrog.log.LogFactory;
import com.whaleal.mars.codecs.MarsOrmException;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.EntityModel;
import com.whaleal.mars.codecs.pojo.PropertyModel;
import com.whaleal.mars.codecs.pojo.annotations.CappedAt;
import com.whaleal.mars.codecs.pojo.annotations.Concern;
import com.whaleal.mars.codecs.pojo.annotations.Language;
import com.whaleal.mars.codecs.pojo.annotations.TimeSeries;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.index.IndexHelper;
import com.whaleal.mars.core.query.*;
import com.whaleal.mars.core.gridfs.GridFsObject;
import com.whaleal.mars.core.gridfs.GridFsResource;
import com.whaleal.mars.session.option.*;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import com.whaleal.mars.session.result.UpdateResult;
import com.whaleal.mars.core.query.BsonUtil;

import com.whaleal.mars.session.transactions.MarsTransaction;
import org.bson.Document;
import org.bson.codecs.EncoderContext;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.whaleal.icefrog.core.lang.Precondition.isTrue;
import static com.whaleal.icefrog.core.lang.Precondition.notNull;


/**
 * 关于数据操作的数据操作的一些具体实现
 *
 * @Date 2020-12-03
 *
 */
public abstract class DatastoreImpl extends AggregationImpl implements Datastore{

    private static final Log log = LogFactory.get(DatastoreImpl.class);

    private final Lock lock = new ReentrantLock();
    private final MongoClient mongoClient;

    private final GridFSBucket defaultGridFSBucket;
    //缓存 collectionName
    private final Map< Class<?> , String > collectionNameCache = new HashMap< Class<?>, String >();


    protected DatastoreImpl( MongoClient mongoClient, String databaseName ) {
        super(mongoClient.getDatabase(databaseName));
        this.mongoClient = mongoClient;
        defaultGridFSBucket = GridFSBuckets.create(super.database);
    }

    protected DatastoreImpl(MongoClient mongoClient ,MongoMappingContext mapper){
        super(mapper.getDatabase(),mapper);
        this.mongoClient = mongoClient ;
        this.defaultGridFSBucket = GridFSBuckets.create(super.database);
    }



    protected static com.mongodb.client.model.Collation fromDocument( Document source ) {

        if (source == null) {
            return null;
        }

        return Collation.from(source).toMongoCollation();
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoDatabase getDatabase() {
        return super.database;
    }

    public MongoDatabase getDatabase(String databaseName){
        if (StrUtil.isBlank(databaseName)) {
            throw new IllegalArgumentException("databaseName in getDatabase can't be null or empty ");
        }
        return this.mongoClient.getDatabase(databaseName).withCodecRegistry(super.mapper.getCodecRegistry());
    }

    @Override
    public < T > DeleteResult delete( Query query, Class< T > entityClass, DeleteOptions options, String collectionName ) {


        MongoCollection collection = this.getCollection(entityClass, collectionName);

        collection = prepareConcern(collection, options);
        //根据query  去 删除相关数据
        ClientSession session = this.startSession();
//        CrudExecutor executor = CrudExecutorFactory.create(CrudEnum.DELETE);

        DeleteResult result = deleteExecute(session, collection, query, options, null);

        return result;

    }

    @Override
    public < T > QueryCursor< T > findAll( Query query, Class< T > entityClass, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entityClass, collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.FIND_ALL);

        MongoCursor< T > iterator = findAllExecute(session, collection, query, null, null);

        return new QueryCursor< T >(iterator, entityClass);

    }

    @Override
    public < T > Optional< T > findOne( Query query, Class< T > entityClass, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entityClass, collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.FIND_ONE);

        T result = findOneExecute(session, collection, query, null, null);
        if (log.isDebugEnabled()) {
            log.debug("Executing query: {} sort: {} fields: {} in collection: {}", query.getQueryObject().toJson(),
                    query.getSortObject(), query.getFieldsObject(), collectionName);
        }

        if (result == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(result);

    }

    @Override
    public < T > InsertOneResult insert( T entity, InsertOneOptions options, String collectionName ) {

        ClientSession session = this.startSession();
        MongoCollection collection = this.getCollection(entity.getClass(), collectionName);

        collection = prepareConcern(collection, options);
//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INSERT_ONE);
        InsertOneResult result = insertOneExecute(session, collection, null, options, entity);

        return result;
    }

    @Override
    public < T > InsertManyResult insert( Collection< ? extends T > entities, Class< ? > entityClass, InsertManyOptions options ) {
        String collectionName = this.mapper.determineCollectionName(entityClass, null);
        return this.insert(entities,collectionName,options);
    }



    @Override
    public < T > InsertManyResult insert( Collection< ? extends T > entities, String collectionName ,InsertManyOptions options) {

        if (entities == null || entities.isEmpty()) {
            throw new IllegalArgumentException("entities in operation can't be null or empty ");
        }

        ClientSession session = this.startSession();

        Class< ? > type = null;

        for (T entity : entities) {

            type = entity.getClass();

            break;
        }

        MongoCollection collection = this.getCollection(type, collectionName);

        collection = prepareConcern(collection, options);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INSERT_MANY);

        InsertManyResult result = insertManyExecute(session, collection, null, options, entities);

        return result;

    }

    @Override
    public < T > UpdateResult updateEntity( Query query, T entity, UpdateOptions options, String collectionName ) {

        Document entityDoc = this.toDocument(entity);
        if (entityDoc == null) {
            throw new IllegalArgumentException();
        }
        ClientSession session = this.startSession();
        MongoCollection< ? > collection = this.getCollection(entity.getClass(), collectionName);

        collection = prepareConcern(collection, options);
//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.UPDATE);


        UpdateResult result = updateExecute(session, collection, query, options, entityDoc);

        return result;

    }

    /**
     * update的另一种形式
     */
    @Override
    public < T > UpdateResult update( Query query, UpdateDefinition update, Class< T > entityClass, UpdateOptions options, String collectionName ) {

        //update中的arrayList
        List<UpdateDefinition.ArrayFilter> arrayFilters = update.getArrayFilters();
        //option中的arrayList
        List<UpdateDefinition.ArrayFilter> arrayFilters1 = options.getArrayFilters();
        //最终的arraylist
        List<Document> arrayFilterList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(arrayFilters)){
            for (int i = 0; i < arrayFilters.size(); i++) {
                Document document = arrayFilters.get(i).toData();
                arrayFilterList.add(document);
            }
            if(ObjectUtil.isNotEmpty(arrayFilters1)){
                //保存arrayFilter
                for (int i = 0; i < arrayFilters1.size(); i++) {
                    Document document = arrayFilters1.get(i).toData();
                    arrayFilterList.add(document);
                }
            }


/*
            // todo
            // option 本身 是否包含相关  arrayFilters
            // 是否冲突
            //判断UpdateOption本身是否包含相关的arrayFilters
            List<Document> arrayFiltersOption = (List<Document>) options.getOriginOptions().getArrayFilters();
            //如果update的arrayFilter没有包含option的arrayFilter所有条件，就合并两个arrayFilter
            if(!arrayFilterList.containsAll(arrayFiltersOption)){
                arrayFilterList.addAll(arrayFiltersOption);
            }
*/
            options.arrayFilters(arrayFilterList);

        }

        ClientSession session = this.startSession();
        MongoCollection<T> collection = this.getCollection(entityClass, collectionName);
        collection = prepareConcern(collection, options);

        if(update instanceof UpdatePipeline){
            //todo
            // 针对所有的 UpdateDefinition
            //getUpdateObject () 方法 在这里都需要处理
            return null ;
        }else {
            UpdateResult result = updateDefinitionExecute(session, collection, query, options, update.getUpdateObject());

            return result;
        }

    }


    @Override
    public MarsSession startSession() {
        ClientSession clientSession = null;
        try {
            clientSession = this.mongoClient.startSession();
        } catch (MongoClientException exception) {

            return null;
        }
        return new MarsSessionImpl(clientSession, mongoClient, database.getName());
    }


    @Override
    public MarsSession startSession( ClientSessionOptions options ) {
        ClientSession clientSession = null;
        try {
            clientSession = this.mongoClient.startSession(options);
        } catch (MongoClientException exception) {

            return null;
        }

        return new MarsSessionImpl(clientSession, mongoClient, database.getName());
    }

    @Override
    public MongoMappingContext getMapper() {
        return mapper;
    }

    @Override
    public < T > T findAndModify( Query query, UpdateDefinition update, FindOneAndUpdateOptions options, Class< T > entityClass, String collectionName ) {
        notNull(query, "Query must not be null!");
        notNull(update, "Update must not be null!");
        notNull(options, "Options must not be null!");
        notNull(entityClass, "EntityClass must not be null!");
        notNull(collectionName, "CollectionName must not be null!");

        //  do with collation
        OptionalUtil.ifAllPresent(query.getCollation(), Optional.of(options.getCollation()), (l, r) -> {
            throw new IllegalArgumentException(
                    "Both Query and FindOneAndModifyOptions define a collation. Please provide the collation only via one of the two.");
        });


        return doFindAndModify(collectionName, query, entityClass, update, options);
    }

    @Override
    public < S, T > T findAndReplace( Query query, S replacement, FindOneAndReplaceOptions options, Class< S > entityType, String collectionName, Class< T > resultType ) {
        notNull(query, "Query must not be null!");
        notNull(replacement, "Replacement must not be null!");
        notNull(options, "Options must not be null! Use FindOneAndReplaceOptions#new() instead.");
        notNull(entityType, "EntityType must not be null!");
        notNull(collectionName, "CollectionName must not be null!");
        notNull(resultType, "ResultType must not be null! Use Object.class instead.");

        isTrue(query.getLimit() <= 1, "Query must not define a limit other than 1 ore none!");
        isTrue(query.getSkip() <= 0, "Query must not define skip.");


        MarsSession marsSession = this.startSession();

        if(entityType == resultType){
            S oneAndReplace = this.database.getCollection(collectionName, entityType).findOneAndReplace(marsSession, query.getQueryObject(), replacement, options.getOriginOptions());

            return (T) oneAndReplace;
        }

        Document document = this.toDocument(replacement);
        Document oneAndReplace = this.database.getCollection(collectionName).findOneAndReplace(marsSession, query.getQueryObject(), document, options.getOriginOptions());
        if(oneAndReplace == null){
            return null ;
        }

        T first = this.database.getCollection(collectionName, resultType).find(marsSession, new Document("_id", oneAndReplace.get("_id"))).limit(1).first();

        return first ;


    }

    @Override
    public < T > T findAndDelete( Query query, Class< T > entityClass, String collectionName, FindOneAndDeleteOptions options ) {
        notNull(query, "Query must not be null!");
        notNull(entityClass, "EntityClass must not be null!");
        notNull(collectionName, "CollectionName must not be null!");

        MarsSession marsSession = this.startSession();

        T oneAndDelete = this.database.getCollection(collectionName, entityClass).findOneAndDelete(marsSession,query.getQueryObject(), options.getOriginOptions());


        return oneAndDelete;
    }






    @Override
    public String getCollectionName( Class< ? > entityClass ) {
        
      return this.mapper.getEntityModel(entityClass).getCollectionName();
        
    }

    private < T > T doFindAndModify( String collectionName, Query query, Class<T> entityClass, UpdateDefinition update, FindOneAndUpdateOptions optionsToUse ) {
        MongoCollection< T > collection = getCollection(entityClass, collectionName);

        Document updateObject = update.getUpdateObject();
        MarsSession marsSession = this.startSession();

        if(update instanceof UpdatePipeline){
            //todo
            return null ;
        }else {

            T oneAndUpdate = collection.findOneAndUpdate(marsSession,query.getQueryObject(), updateObject, optionsToUse.getOriginOptions());
            return oneAndUpdate;

        }

    }


    @Override
    public < T > UpdateResult replace( Query query, T entity, ReplaceOptions options, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entity.getClass(), collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.REPLACE);

        UpdateResult execute = replaceExecute(session, collection, query, options, entity);

        return execute;

    }

    //索引操作
    @Override
    public void createIndex( Index index, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_CREATE_ONE);


        createIndexExecute(session, collection, null, index.getIndexOptions(), index);

    }


    @Override
    public < T > void ensureIndexes( Class< T > entityClass, String collectionName ) {

        final IndexHelper indexHelper = new IndexHelper(this.mapper);
        String collName = this.mapper.determineCollectionName(entityClass, collectionName);
        indexHelper.createIndex(this.database.getCollection(collName), this.mapper.getEntityModel(entityClass));

    }

    @Override
    public void dropIndex( Index index, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_DROP_ONE);

        dropOneIndexExecute(session, collection, null, null, index);

    }

    @Override
    public void dropIndexes( String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_DROP_MANY);

        dropManyIndexExecute(session, collection, null, null, null);

    }

    @Override
    public List< Index > getIndexes( String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_FIND);

        List< Index > execute = findManyIndexExecute(session, collection, null, null, null);

        return execute;

    }


    /**
     * 将Collections<T> 转成 List<Entity>
     */
    public < T > List< Document > toDocuments( Collection< ? extends T > entities ) {

        if (entities.isEmpty()) {
            return new ArrayList< Document >();
        }

        return entities.stream().map(x -> mapper.toDocument(x)).collect(Collectors.toList());

    }


    private < T > MongoCollection< T > prepareConcern( MongoCollection< T > collection, Options options ) {
        return options.prepare(collection);

    }

    public void setWriteConcern( WriteConcern writeConcern ) {
        this.database = database.withWriteConcern(writeConcern);
    }

    public void setReadConcern( ReadConcern readConcern ) {
        this.database = database.withReadConcern(readConcern);
    }

    public void setReadPreference( ReadPreference readPerference ) {
        this.database = database.withReadPreference(readPerference);
    }

    /**
     * save与insert是否有异
     * insert会抛异常
     * save若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作
     * 这里需要修改
     * function(obj, opts) {
     * if (obj == null)
     * throw Error("can't save a null");
     * <p>
     * if (typeof (obj) == "number" || typeof (obj) == "string")
     * throw Error("can't save a number or string");
     * <p>
     * if (typeof (obj._id) == "undefined") {
     * obj._id = new ObjectId();
     * return this.insert(obj, opts);
     * } else {
     * return this.update({_id: obj._id}, obj, Object.merge({upsert: true}, opts));
     * }
     * }
     */

    @Override
    public < T > T save( T entity, InsertOneOptions options, String collectionName ) {


        if (entity == null) {
            return null;
        }


        final EntityModel model = this.mapper.getEntityModel(entity.getClass());

        final PropertyModel idField = model.getIdProperty();
        if (idField != null && idField.getPropertyAccessor().get(entity) != null) {
            //  调用replace
            ReplaceOptions replaceOptiion = new ReplaceOptions()
                    .bypassDocumentValidation(options.getBypassDocumentValidation())
                    .upsert(true);

            Document document = this.toDocument(entity);


            replace(new Query(Criteria.where("_id").is(document.get("_id"))), entity, replaceOptiion, collectionName);
        } else {
            insert(entity, options, collectionName);
        }

        return entity;

    }

    @Override
    public < T > T storeGridFs( GridFsObject< T, InputStream > upload, String bucketName ) {

        GridFSUploadOptions uploadOptions = computeUploadOptionsFor(upload.getOptions().getContentType(),
                upload.getOptions().getMetadata());

        if (upload.getOptions().getChunkSize() > 0) {
            uploadOptions.chunkSizeBytes(upload.getOptions().getChunkSize());
        }

        if (upload.getFileId() == null) {
            return (T) getGridFsBucket(bucketName).uploadFromStream(upload.getFilename(), upload.getContent(), uploadOptions);
        }
        
        getGridFsBucket(bucketName).uploadFromStream(BsonUtil.simpleToBsonValue(upload.getFileId()), upload.getFilename(),
                upload.getContent(), uploadOptions);

        return upload.getFileId();
    }

    @Override
    public ObjectId storeGridFs( InputStream content, String filename, String contentType,
                                 Object metadata, String bucketName ) {
        return storeGridFs(content, filename, contentType, metadata, bucketName);
    }

    @Override
    public GridFSFile findOneGridFs( Query query, String bucketName ) {
        return findGridFs(query, bucketName).first();
    }

    @Override
    public void rename( ObjectId id, String newFilename, String bucketName ) {

        getGridFsBucket(bucketName).rename(id, newFilename);

    }

    @Override
    public GridFSFindIterable findGridFs( Query query, String bucketName ) {

        notNull(query, "Query must not be null!");

        Document queryObject = query.getQueryObject();
        Document sortObject = query.getSortObject();

        GridFSFindIterable iterable = getGridFsBucket(bucketName).find(queryObject).sort(sortObject);
        if (query.getSkip() > 0) {
            iterable = iterable.skip(Math.toIntExact(query.getSkip()));
        }

        if (query.getLimit() > 0) {
            iterable = iterable.limit(query.getLimit());
        }

        MongoCursor< GridFSFile > iterator = iterable.iterator();
        notNull(iterator.tryNext(), "No file found with the query");

        return iterable;
    }


    @Override
    public void deleteGridFs( ObjectId id, String bucketName ) {

        getGridFsBucket(bucketName).delete(id);

    }


    @Override
    public ClassLoader getClassLoader() {
        return database.getClass().getClassLoader();
    }

    @Override
    public void deleteGridFs( Query query, String bucketName ) {

        GridFSFindIterable iterable = findGridFs(query);
        MongoCursor< GridFSFile > iterator = iterable.iterator();
        notNull(iterator.tryNext(), "no file found to delete");
        while (iterator.hasNext()) {
            getGridFsBucket(bucketName).delete(iterator.next().getObjectId());
        }

    }


    private GridFSBucket getGridFsBucket( String bucketName ) {
        return bucketName == null ? defaultGridFSBucket : GridFSBuckets.create(database, bucketName);
    }

    @Override
    public GridFsResource getResource( GridFSFile file, String bucketName ) {

        notNull(file, "GridFSFile must not be null!");

        return new GridFsResource(file, getGridFsBucket(bucketName).openDownloadStream(file.getId()));
    }


    public Document toDocument( Object entity ) {
        final EntityModel entityModel = this.mapper.getEntityModel(entity.getClass());

        DocumentWriter writer = new DocumentWriter();
        this.mapper.getCodecRegistry().get(entityModel.getType())
                .encode(writer, entity, EncoderContext.builder().build());

        return writer.getDocument();
    }


    public < T > MongoCollection< T > getCollection( Class< T > type ) {

        String nameCache =null ;
        if( (nameCache = collectionNameCache.get(type) ) !=null){

            MongoCollection< T > collection = this.database.getCollection(nameCache, type);
            this.withConcern(collection,type);
            return collection ;
        }


        EntityModel entityModel = mapper.getEntityModel(type);
        String collectionName = entityModel.getCollectionName();

        collectionNameCache.put(type,collectionName);

        MongoCollection< T > collection = this.database.getCollection(collectionName, type);

        this.withConcern(collection,type);
        return collection;
    }



    public < T > MongoCollection< T > getCollection( Class< T > type, String collectionName ) {

        if (collectionName != null) {
            MongoCollection< T > collection = this.database.getCollection(collectionName, type);
            return this.withConcern(collection, type);
        } else {

            return getCollection(type);
        }

    }


    @Override
    public < T > long count( Class< T > clazz, CountOptions countOptions ) {

        com.mongodb.client.model.EstimatedDocumentCountOptions escountOptions = new com.mongodb.client.model.EstimatedDocumentCountOptions();

        escountOptions.maxTime(countOptions.getMaxTime(TimeUnit.SECONDS), TimeUnit.SECONDS);

        String collectionName = this.mapper.determineCollectionName(clazz, null);
        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", "{}", collectionName);
        }
        return this.database.getCollection(collectionName).estimatedDocumentCount(escountOptions);

    }

    @Override
    public < T > long countById( Query query, Class< T > clazz, CountOptions countOptions ) {

        String collectionName = this.mapper.determineCollectionName(clazz, null);
        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", query.getQueryObject().toJson(), collectionName);
        }
        return this.database.getCollection(collectionName).countDocuments(query.getQueryObject(), countOptions.getOriginOptions());
    }

    @Override
    public < T > long count( String collectionName, CountOptions countOptions ) {

        com.mongodb.client.model.EstimatedDocumentCountOptions escountOptions = new com.mongodb.client.model.EstimatedDocumentCountOptions();

        escountOptions.maxTime(countOptions.getMaxTime(TimeUnit.SECONDS), TimeUnit.SECONDS);

        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", "{}", collectionName);
        }

        return this.database.getCollection(collectionName).estimatedDocumentCount(escountOptions);
    }

    @Override
    public < T > long countById( Query query, String collectionName, CountOptions countOptions ) {
        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", query.getQueryObject().toJson(), collectionName);
        }
        return this.database.getCollection(collectionName).countDocuments(query.getQueryObject(), countOptions.getOriginOptions());
    }


    @Override
    public < T > MongoCollection< Document > createCollection( Class< T > entityClass ) {
        notNull(entityClass, "EntityClass must not be null!");
        return createCollection(entityClass, getCollectionOptions(entityClass));
    }

    private GridFSUploadOptions computeUploadOptionsFor( String contentType, Document metadata ) {

        Document targetMetadata = new Document();

        if (StrUtil.hasText(contentType)) {
            targetMetadata.put(GridFsResource.CONTENT_TYPE_FIELD, contentType);
        }

        if (metadata != null) {
            targetMetadata.putAll(metadata);
        }

        GridFSUploadOptions options = new GridFSUploadOptions();
        options.metadata(targetMetadata);

        return options;
    }

    /**
     * 创建集合
     * 根据类名和传递的集合参数名，找出最终的集合名称，并且将类上面的注解声明的内容放入 CollectionOptions 中
     *
     * @param entityClass       实体类
     * @param collectionOptions 收集选项
     * @return {@link MongoCollection<Document>}
     */
    @Override
    public < T > MongoCollection< Document > createCollection( Class< T > entityClass,
                                                               CollectionOptions collectionOptions ) {

        notNull(entityClass, "EntityClass must not be null!");

        CollectionOptions options = collectionOptions != null ? collectionOptions : CollectionOptions.empty();

        Document document = convertToDocument(options);

        return doCreateCollection(this.mapper.getEntityModel(entityClass).getCollectionName(), document);
    }

    @Override
    public MongoCollection< Document > createCollection( String collectionName ) {
        notNull(collectionName, "CollectionName must not be null!");

        return doCreateCollection(collectionName, new Document());
    }

    @Override
    public < T > void dropCollection( Class< T > entityClass ) {
        dropCollection(this.mapper.getEntityModel(entityClass).getCollectionName());
    }

    @Override
    public MongoCollection< Document > createCollection( String collectionName, CollectionOptions collectionOptions ) {

        notNull(collectionName, "CollectionName must not be null!");
        return doCreateCollection(collectionName, convertToDocument(collectionOptions));
    }

    @Override
    public void dropCollection( String collectionName ) {
        notNull(collectionName, "CollectionName must not be null!");
        lock.lock();
        try {
            MongoCollection< Document > collection = this.database.getCollection(collectionName);
            collection.drop();
            if (log.isDebugEnabled()) {
                log.debug("Dropped collection [{}]",
                        collection.getNamespace() != null ? collection.getNamespace().getCollectionName() : collectionName);
            }
        } finally {
            lock.unlock();
        }

    }

    protected Document convertToDocument( CollectionOptions collectionOptions ) {
        Document document = new Document();
        if (collectionOptions != null) {

            collectionOptions.getCapped().ifPresent(val -> document.put("capped", val));
            collectionOptions.getSize().ifPresent(val -> document.put("size", val));
            collectionOptions.getMaxDocuments().ifPresent(val -> document.put("max", val));
            collectionOptions.getCollation().ifPresent(val -> document.append("collation", val.toDocument()));

            collectionOptions.getValidationOptions().ifPresent(it -> {
                it.getValidationLevel().ifPresent(val -> document.append("validationLevel", val.getValue()));
                it.getValidationAction().ifPresent(val -> document.append("validationAction", val.getValue()));
            });
        }
        return document;
    }

    protected MongoCollection< Document > doCreateCollection( String collectionName, Document collectionOptions ) {
        lock.lock();
        try {
            com.mongodb.client.model.CreateCollectionOptions co = new com.mongodb.client.model.CreateCollectionOptions();

            if (collectionOptions.containsKey("capped")) {
                co.capped((Boolean) collectionOptions.get("capped"));
            }
            if (collectionOptions.containsKey("size")) {
                co.sizeInBytes(((Number) collectionOptions.get("size")).longValue());
            }
            if (collectionOptions.containsKey("max")) {
                co.maxDocuments(((Number) collectionOptions.get("max")).longValue());
            }

            //如果选项里面有关于collation方面的操作，
            if (collectionOptions.containsKey("collation")) {
                co.collation(fromDocument(collectionOptions.get("collation", Document.class)));
            }

            if (collectionOptions.containsKey("validator")) {

                com.mongodb.client.model.ValidationOptions options = new com.mongodb.client.model.ValidationOptions();

                if (collectionOptions.containsKey("validationLevel")) {
                    options.validationLevel(ValidationLevel.fromString(collectionOptions.getString("validationLevel")));
                }
                if (collectionOptions.containsKey("validationAction")) {
                    options.validationAction(ValidationAction.fromString(collectionOptions.getString("validationAction")));
                }

                options.validator(collectionOptions.get("validator", Document.class));
                co.validationOptions(options);
            }

            this.database.createCollection(collectionName, co);

            MongoCollection< Document > coll = database.getCollection(collectionName, Document.class);
            return coll;
        } finally {
            lock.unlock();
        }
    }


    public < T > MongoCollection< T > withConcern( MongoCollection< T > collection, Class< T > clazz ) {

        EntityModel entityModel = this.mapper.getEntityModel(clazz);

        Concern annotation = (Concern) entityModel.getAnnotation(Concern.class);

        if (annotation != null) {

            if (WriteConcern.valueOf(annotation.writeConcern()) != null) {
                collection = collection.withWriteConcern(WriteConcern.valueOf(annotation.writeConcern()));
            }

            if (ReadPreference.valueOf(annotation.readPreference()) != null) {
                collection = collection.withWriteConcern(WriteConcern.valueOf(annotation.writeConcern()));

            }

            ReadConcernLevel readConcernLevel = ReadConcernLevel.fromString(annotation.readConcern());

            if (readConcernLevel != null) {
                collection.withReadConcern(new ReadConcern(readConcernLevel));
            }
        }
        return collection;

    }

    private CollectionOptions getCollectionOptions( Class< ? > entity ) {

        EntityModel entityModel = this.mapper.getEntityModel(entity);
        notNull(entity, "EntityClass must not be null!");
        CollectionOptions options = CollectionOptions.empty();


        //TODO it may contains some bug，so please use it carefully.
        CappedAt capped = (CappedAt) entityModel.getAnnotation(CappedAt.class);

        if (!ObjectUtil.isEmpty(capped)) {

            options = options.capped();
            options = options.size(capped.value());
            options = options.maxDocuments(capped.count());
        }

        Language language = (Language) entityModel.getAnnotation(Language.class);

        if (!ObjectUtil.isEmpty(language)) {
            Collation locale = Collation.of(language.value());
            options = options.collation(locale);
        }


        TimeSeries timeSeries = (TimeSeries) entityModel.getAnnotation(TimeSeries.class);

        if (!ObjectUtil.isEmpty(timeSeries)) {
            CollectionOptions.TimeSeriesOptions toptions = CollectionOptions.TimeSeriesOptions.timeSeries(timeSeries.timeField());
            if (StrUtil.hasText(timeSeries.metaField())) {

                if (entityModel.getPropertyModel(timeSeries.metaField()) == null) {
                    throw new MarsOrmException(
                            String.format("Meta field '%s' does not exist in type %s", timeSeries.metaField(), entity.getName()));
                }

                toptions = toptions.metaField(timeSeries.metaField());
            }
            if (!ObjectUtil.isNull(timeSeries.granularity())) {
                toptions = toptions.granularity(timeSeries.granularity());

            }

            options = options.timeSeries(toptions);

        }


        return options;
    }

    private <T> T deleteExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        if (!(options instanceof DeleteOptions)) {
            throw new ClassCastException();
        }

        DeleteOptions option = (DeleteOptions) options;

        DeleteResult deleteResult = new DeleteResult();

        if (option.isMulti()) {

            if (session == null) {
                deleteResult.setOriginDeleteResult(collection.deleteMany(query.getQueryObject(), option.getOriginOptions()));
            } else {
                deleteResult.setOriginDeleteResult(collection.deleteMany(session, query.getQueryObject(), option.getOriginOptions()));
            }

            return (T) deleteResult;
        } else {

            if (session == null) {
                deleteResult.setOriginDeleteResult(collection.deleteOne(query.getQueryObject(), option.getOriginOptions()));
            } else {
                deleteResult.setOriginDeleteResult(collection.deleteOne(session, query.getQueryObject(), option.getOriginOptions()));
            }

            return (T) deleteResult;

        }

    }

    private <T> T findAllExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        FindIterable findIterable;

        //todo  query  解析缺少
        //  projection
        //  find  操作 没有 option   参数

        if (session == null) {
            findIterable = collection.find(query.getQueryObject());

        } else {

            findIterable = collection.find(session, query.getQueryObject());
        }

        if (!query.getFieldsObject().isEmpty()) {
            findIterable.projection(query.getFieldsObject());
        }


        if (query.getSortObject() != null) {
            findIterable = findIterable.sort(query.getSortObject());
        }

        if (query.getSkip() > 0) {
            findIterable = findIterable.skip((int) query.getSkip());
        }

        if (query.getLimit() > 0) {
            findIterable = findIterable.limit(query.getLimit());
        }


        return (T) findIterable.iterator();

    }

    /**
     *
     * find 操作本身没有相关 的Option 参数
     * @param session
     * @param collection
     * @param query
     * @param options
     * @param data
     * @param <T>
     * @return
     */
    private <T> T findOneExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        FindIterable findIterable;

        if (session == null) {



            findIterable = collection.find(query.getQueryObject());

        } else {

            findIterable = collection.find(session, query.getQueryObject());

        }


        if (query.getSkip() > 0) {
            findIterable = findIterable.skip((int) query.getSkip());
        }

        if (query.getSortObject() != null) {
            findIterable = findIterable.sort(query.getSortObject());
        }

        findIterable.limit(1);

        return (T) findIterable.first();


    }

    private <T> T insertOneExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        InsertOneResult insertOneResult = new InsertOneResult();

        //Entity insertDocument = new Entity((Map) data);

        if (options == null) {

            if (session == null) {

                insertOneResult.setOriginInsertOneResult(collection.insertOne(data));

            } else {

                insertOneResult.setOriginInsertOneResult(collection.insertOne(session, data));

            }

            return (T) insertOneResult;

        }

        if (!(options instanceof InsertOneOptions)) {
            throw new ClassCastException();
        }


        InsertOneOptions insertOneOptions = (InsertOneOptions) options;

        if (session == null) {

            insertOneResult.setOriginInsertOneResult(collection.insertOne(data, insertOneOptions.getOriginOptions()));

        } else {

            insertOneResult.setOriginInsertOneResult(collection.insertOne(session, data, insertOneOptions.getOriginOptions()));

        }
        return (T) insertOneResult;
    }

    private <T> T insertManyExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        InsertManyResult insertManyResult = new InsertManyResult();

        //options == null是另外一种情况
        if (options == null) {

            if (session == null) {

                insertManyResult.setOriginInsertManyResult(collection.insertMany((List) data));

            } else {

                insertManyResult.setOriginInsertManyResult(collection.insertMany(session, (List) data));

            }

            return (T) insertManyResult;

        }


        //options != null是一种情况
        if (!(options instanceof InsertManyOptions)) {
            throw new ClassCastException();
        }

        InsertManyOptions insertManyOptions = (InsertManyOptions) options;

        if (session == null) {


            insertManyResult.setOriginInsertManyResult(collection.insertMany((List) data, insertManyOptions.getOriginOptions()));

        } else {

            insertManyResult.setOriginInsertManyResult(collection.insertMany(session, (List) data, insertManyOptions.getOriginOptions()));

        }

        return (T) insertManyResult;
    }

    private <T> T updateExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        if (!(options instanceof UpdateOptions)) {
            throw new ClassCastException();
        }

        

        UpdateOptions option = (UpdateOptions) options;

        Document updateOperations = new Document("$set", new Document((Map) data));

        UpdateResult updateResult = new UpdateResult();

        if (option.isMulti()) {

            if (session == null) {
                updateResult.setOriginUpdateResult(collection.updateMany(query.getQueryObject(), updateOperations, option.getOriginOptions()));
            } else {
                updateResult.setOriginUpdateResult(collection.updateMany(session, query.getQueryObject(), updateOperations, option.getOriginOptions()));
            }

            return (T) updateResult;

        } else {

            if (session == null) {
                updateResult.setOriginUpdateResult(collection.updateOne(query.getQueryObject(), updateOperations, option.getOriginOptions()));
            } else {
                updateResult.setOriginUpdateResult(collection.updateOne(session, query.getQueryObject(), updateOperations, option.getOriginOptions()));
            }

            return (T) updateResult;

        }

    }

    private <T> T updateDefinitionExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {


        Document dd = (Document) data;


        dd.containsKey(UpdatePipeline.Updatepipeline);


        List< Document > list = dd.getList(UpdatePipeline.Updatepipeline, Document.class);




        if (!(options instanceof UpdateOptions)) {
            throw new ClassCastException();
        }

        UpdateOptions option = (UpdateOptions) options;

        UpdateResult updateResult = new UpdateResult();



        if (option.isMulti()) {

            if (session == null) {
                updateResult.setOriginUpdateResult(collection.updateMany(query.getQueryObject(), (Document) data, option.getOriginOptions()));
            } else {
                updateResult.setOriginUpdateResult(collection.updateMany(session, query.getQueryObject(), (Document) data, option.getOriginOptions()));
            }

            return (T) updateResult;

        } else {


            if (session == null) {
                updateResult.setOriginUpdateResult(collection.updateOne(query.getQueryObject(), (Document) data, option.getOriginOptions()));
            } else {
                updateResult.setOriginUpdateResult(collection.updateOne(session, query.getQueryObject(), (Document) data, option.getOriginOptions()));
            }

            return (T) updateResult;

        }


    }

    private <T> T replaceExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        UpdateResult updateResult = new UpdateResult();


        //options == null 是一种情况
        if (options == null) {
            if (session == null) {


                updateResult.setOriginUpdateResult(collection.replaceOne(query.getQueryObject(), data));

            } else {

                updateResult.setOriginUpdateResult(collection.replaceOne(session, query.getQueryObject(), data));

            }

            return (T) updateResult;
        }

        //options != null也是一种情况
        if (!(options instanceof ReplaceOptions)) {
            throw new ClassCastException();
        }

        ReplaceOptions replaceOptions = (ReplaceOptions) options;

        if (session == null) {

            updateResult.setOriginUpdateResult(collection.replaceOne(query.getQueryObject(), data, replaceOptions.getOriginOptions()));

        } else {

            updateResult.setOriginUpdateResult(collection.replaceOne(session, query.getQueryObject(), data, replaceOptions.getOriginOptions()));

        }


        return (T) updateResult;
    }

    private <T> T createIndexExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        Index index = (Index) data;

        IndexOptions indexOptions = index.getIndexOptions();

        if (indexOptions == null) {

            if (session == null) {

                collection.createIndex(index.getIndexKeys());

            } else {

                collection.createIndex(session, index.getIndexKeys());

            }


        } else {

            if (session == null) {
                collection.createIndex(index.getIndexKeys(), indexOptions.getOriginOptions());
            } else {
                collection.createIndex(session, index.getIndexKeys(), indexOptions.getOriginOptions());
            }

        }

        return null;
    }

    private <T> T dropOneIndexExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        Index index = (Index) data;

        if (session == null) {

            collection.dropIndex(index.getIndexKeys());

        } else {

            collection.dropIndex(session, index.getIndexKeys());

        }

        return null;
    }

    private <T> T dropManyIndexExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        if (session == null) {

            collection.dropIndexes();


        } else {

            collection.dropIndexes(session);


        }

        return null;
    }

    private <T> T findManyIndexExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        ListIndexesIterable indexIterable = null;

        if (session == null) {
            indexIterable = collection.listIndexes();

        } else {

            indexIterable = collection.listIndexes(session);
        }


        MongoCursor iterator = indexIterable.iterator();

        Index index = null;

        List indexes = new ArrayList();


        while (iterator.hasNext()) {

            Document indexDocument = (Document) iterator.next();

            IndexOptions indexOptions = new IndexOptions();

            if (indexDocument.get("name") != null) {
                indexOptions.name((String) indexDocument.get("name"));
            }
            if (indexDocument.get("partialFilterExpression") != null) {
                indexOptions.partialFilterExpression((Bson) indexDocument.get("partialFilterExpression"));
            }
            if (indexDocument.get("weights") != null) {
                indexOptions.weights((Bson) indexDocument.get("weights"));
            }
            if (indexDocument.get("storageEngine") != null) {//不常用到
                indexOptions.storageEngine((Bson) indexDocument.get("storageEngine"));
            }
            if (indexDocument.get("wildcardProjection") != null) {
                indexOptions.wildcardProjection((Bson) indexDocument.get("wildcardProjection"));
            }
            if (indexDocument.get("collation") != null) {
                Document document = (Document) indexDocument.get("collation");
                Collation collation = Collation.from(document);
                indexOptions.collation(collation.toMongoCollation());
            }
            /*if (indexDocument.get("version") != null) {         官方文档也没有这个Index偏好设置，只在IndexOptions类里面有
                indexOptions.version((Integer) indexDocument.get("version"));
            }*/
            if (indexDocument.get("unique") != null) {
                indexOptions.unique((Boolean) indexDocument.get("unique"));
            }
            if (indexDocument.get("bits") != null) {
                indexOptions.bits((Integer) indexDocument.get("bits"));
            }
            if (indexDocument.get("bucketSize") != null) {
                indexOptions.bucketSize((Double) indexDocument.get("bucketSize"));
            }
            if (indexDocument.get("default_language") != null) {
                indexOptions.defaultLanguage((String) indexDocument.get("default_language"));
            }
            if (indexDocument.get("expireAfterSeconds") != null) {
                Long expireAfter = (Long) indexDocument.get("expireAfterSeconds");//秒以下会丢失
                indexOptions.expireAfter(expireAfter, TimeUnit.SECONDS);
            }
            if (indexDocument.get("hidden") != null) {
                indexOptions.hidden((Boolean) indexDocument.get("hidden"));
            }
            if (indexDocument.get("language_override") != null) {
                indexOptions.languageOverride((String) indexDocument.get("language_override"));
            }
            if (indexDocument.get("max") != null) {
                indexOptions.max((Double) indexDocument.get("max"));
            }
            if (indexDocument.get("min") != null) {
                indexOptions.min((Double) indexDocument.get("min"));
            }
            if (indexDocument.get("sparse") != null) {
                indexOptions.sparse((Boolean) indexDocument.get("sparse"));
            }
            if (indexDocument.get("2dsphereIndexVersion") != null) {
                indexOptions.sphereVersion((Integer) indexDocument.get("2dsphereIndexVersion"));
            }
            if (indexDocument.get("background") != null) {
                indexOptions.background((Boolean) indexDocument.get("background"));
            }
            if (indexDocument.get("textIndexVersion") != null) {
                indexOptions.textVersion((Integer) indexDocument.get("textIndexVersion"));
            }

            Document key = (Document) indexDocument.get("key");

            index = new Index();

            Set<String> strings = key.keySet();

            for (String keyName : strings) {
                index.on(keyName, IndexDirection.fromValue(key.get(keyName)));

            }
            index.setOptions(indexOptions);

            indexes.add(index);

        }

        return (T) indexes;
    }


    private <T> T createManyIndexExecute( ClientSession session, MongoCollection collection, Query query, Options options, Object data) {

        List<Index> indexes = (List) data;

        if (session == null) {

            for (Index index : indexes) {

                if (index.getIndexOptions() == null) {
                    collection.createIndex(index.getIndexKeys());
                } else {
                    collection.createIndex(index.getIndexKeys(), index.getIndexOptions().getOriginOptions());
                }

            }

        } else {

            for (Index index : indexes) {

                if (index.getIndexOptions() == null) {
                    collection.createIndex(session, index.getIndexKeys());
                } else {
                    collection.createIndex(session, index.getIndexKeys(), index.getIndexOptions().getOriginOptions());
                }

            }

        }

        return null;
    }



    @Override
    public <T> T withTransaction(MarsTransaction<T> body) {
        return doTransaction(startSession(), body);
    }

    @Override
    public <T> T withTransaction(MarsTransaction<T> transaction ,ClientSessionOptions options ) {
        return doTransaction(startSession(options), transaction);
    }
    private <T> T doTransaction(MarsSession marssession, MarsTransaction<T> body) {
        try {
            ClientSession session = marssession.startSession();
            if (session == null) {
                throw new IllegalStateException("No session could be found for the transaction.");
            }
            return session.withTransaction(() -> body.execute(marssession));
        }catch (Exception e){
            log.error(e);
            throw e;
        }
    }


}
