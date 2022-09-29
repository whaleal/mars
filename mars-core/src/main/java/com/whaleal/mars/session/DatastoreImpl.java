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
import com.mongodb.client.model.*;
import com.mongodb.lang.Nullable;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.map.MapUtil;
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
import com.whaleal.mars.codecs.pojo.annotations.Collation;
import com.whaleal.mars.codecs.pojo.annotations.TimeSeries;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.index.Index;
import com.whaleal.mars.core.index.IndexDirection;
import com.whaleal.mars.core.index.IndexHelper;
import com.whaleal.mars.core.query.*;
import com.whaleal.mars.core.gridfs.GridFsObject;
import com.whaleal.mars.core.gridfs.GridFsResource;
import com.whaleal.mars.core.internal.MongoNamespace;

import com.whaleal.mars.session.option.*;
import com.whaleal.mars.session.option.CountOptions;
import com.whaleal.mars.session.option.DeleteOptions;
import com.whaleal.mars.session.option.FindOneAndDeleteOptions;
import com.whaleal.mars.session.option.FindOneAndReplaceOptions;
import com.whaleal.mars.session.option.FindOneAndUpdateOptions;
import com.whaleal.mars.session.option.IndexOptions;
import com.whaleal.mars.session.option.InsertManyOptions;
import com.whaleal.mars.session.option.InsertOneOptions;
import com.whaleal.mars.session.option.ReplaceOptions;
import com.whaleal.mars.session.option.TimeSeriesOptions;
import com.whaleal.mars.session.option.UpdateOptions;
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
public class DatastoreImpl extends AggregationImpl implements Datastore{


    private static final Log log = LogFactory.get(DatastoreImpl.class);

    private final Lock lock = new ReentrantLock();
    private final MongoClient mongoClient;

    private final GridFSBucket defaultGridFSBucket;
    //缓存 collectionName
    private final Map< Class<?> , String > collectionNameCache = new HashMap< Class<?>, String >();



    @Override
    public Document executeCommand( String jsonCommand ) {
        Precondition.notNull(jsonCommand);
        return this.database.runCommand(Document.parse(jsonCommand));
    }

    @Override
    public Document executeCommand( Document command ) {
        return this.database.runCommand(command);
    }

    @Override
    public Document executeCommand( Document command, ReadPreference readPreference ) {
        return this.database.runCommand(command,readPreference);
    }


    protected DatastoreImpl( MongoClient mongoClient, String databaseName ) {
        super(mongoClient.getDatabase(databaseName));
        this.mongoClient = mongoClient;
        defaultGridFSBucket = GridFSBuckets.create(super.database);
    }

    protected DatastoreImpl( MongoClient mongoClient , MongoMappingContext mapper){
        super(mapper.getDatabase(),mapper);
        this.mongoClient = mongoClient ;
        this.defaultGridFSBucket = GridFSBuckets.create(super.database);

        for (Class<?> entity : mapper.getInitialEntitySet()){
//            createCollection(entity);
            if (mapper.isAutoIndexCreation()){
                lock.lock();
                try {
                    ensureIndexes(entity);
                }finally {
                    lock.unlock();
                }
            }
        }


    }



    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public MongoDatabase getDatabase() {
        return super.database;
    }

    public MongoDatabase getDatabase(String databaseName){
        MongoNamespace.checkDatabaseNameValidity(databaseName);
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

        return new QueryCursor< T >(iterator);

    }

    @Override
    public < T > Optional< T > findOne( Query query, Class< T > entityClass, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entityClass, collectionName);

//        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.FIND_ONE);

        T result = findOneExecute(session, collection, query, null, null);
        if (log.isDebugEnabled() ) {
            log.debug("Executing query: {} sort: {} fields: {} in collection: {}", query.getQueryObject().toJson(),
                    query.getSortObject(), query.getFieldsObject(), collectionName);
        }

        if (result == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(result);

    }

    public <T> Optional<T> findById(Object id,Class<T> entityClass,String collectionName){
        ClientSession session = this.startSession();
        MongoCollection collection = this.getCollection(entityClass,collectionName);
        Optional<T> result = findByIdExecute(session, collection,id);
       return result;
    }

    @Override
    public < T > InsertOneResult insert( T entity, InsertOneOptions options, String collectionName ) {

        // 开启与数据库的连接
        ClientSession session = this.startSession();

        //根据传入的集合名和实体类获取对应的MongoCollection对象
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
    public UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass) {
        return doUpdate(getCollectionName(entityClass), query, update, entityClass, true, false);
    }

    @Override
    public UpdateResult upsert(Query query, UpdateDefinition update, String collectionName) {
        return doUpdate(collectionName, query, update, null, true, false);
    }

    @Override
    public UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {

        Precondition.notNull(entityClass, "EntityClass must not be null");

        return doUpdate(collectionName, query, update, entityClass, true, false);
    }

    @Override
    public UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass) {
        return doUpdate(getCollectionName(entityClass), query, update, entityClass, false, false);
    }

    @Override
    public UpdateResult updateFirst(Query query, UpdateDefinition update, String collectionName) {
        return doUpdate(collectionName, query, update, null, false, false);
    }

    @Override
    public UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {

        Precondition.notNull(entityClass, "EntityClass must not be null");

        return doUpdate(collectionName, query, update, entityClass, false, false);
    }

    @Override
    public UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass) {
        return doUpdate(getCollectionName(entityClass), query, update, entityClass, false, true);
    }

    @Override
    public UpdateResult updateMulti(Query query, UpdateDefinition update, String collectionName) {
        return doUpdate(collectionName, query, update, null, false, true);
    }

    @Override
    public UpdateResult updateMulti(Query query, UpdateDefinition update, Class<?> entityClass, String collectionName) {

        Precondition.notNull(entityClass, "EntityClass must not be null");

        return doUpdate(collectionName, query, update, entityClass, false, true);
    }

    @SuppressWarnings("ConstantConditions")
    protected <T> UpdateResult doUpdate( String collectionName, Query query, UpdateDefinition update,
                                     @Nullable Class<?> entityClass, boolean upsert, boolean multi) {

        Precondition.notNull(collectionName, "CollectionName must not be null");
        Precondition.notNull(query, "Query must not be null");
        Precondition.notNull(update, "Update must not be null");

        if (query.isSorted() && log.isWarnEnabled()) {

            log.warn(String.format("%s does not support sort ('%s'); Please use findAndModify() instead",
                    upsert ? "Upsert" : "UpdateFirst", MapUtil.toString(query.getSortObject())));
        }

        com.mongodb.client.model.UpdateOptions  opts = new com.mongodb.client.model.UpdateOptions();

        opts.upsert(upsert);
        if(query.getCollation().isPresent()){

            opts.collation(query.getCollation().get());

        }

        if(query.getHint() !=null){
            String hint = query.getHint() ;

            try {
                Document parse = Document.parse(hint);
                opts.hint(parse);
            }catch (Exception e){
                opts.hintString(hint);
            }
        }


        if(query.getMeta().getComment()!=null){
           opts.comment( query.getMeta().getComment());
        }

        if(update.hasArrayFilters()){
            opts.arrayFilters(update.getArrayFilters().stream().map(x ->x.toData()).collect(Collectors.toList()));
        }

        MongoCollection< ? > collection = entityClass == null ? getDatabase().getCollection(collectionName, Document.class) : withConcern(getDatabase().getCollection(collectionName, entityClass), entityClass);


        com.mongodb.client.result.UpdateResult result =  multi ?  collection.updateMany(query.getQueryObject(),update.getUpdateObject(),opts) : collection.updateOne(query.getQueryObject(),update.getUpdateObject(),opts);

        return new UpdateResult(result);

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
        OptionalUtil.ifAllPresent(query.getCollation(), options.getCollation(), (l, r) -> {
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

    @Override
    public void setWriteConcern( WriteConcern writeConcern ) {
        super.setWriteConcern(writeConcern);
        this.database = database.withWriteConcern(writeConcern);
    }

    @Override
    public void setReadConcern( ReadConcern readConcern ) {
        super.setReadConcern(readConcern);
        this.database = database.withReadConcern(readConcern);
    }

    @Override
    public void setReadPreference( ReadPreference readPerference ) {
        super.setReadPreference(readPerference);
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

        DocumentWriter writer = new DocumentWriter(mapper);
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

        //集合名不为空，则有优先使用集合名
        if (collectionName != null) {
            MongoCollection< T > collection = this.database.getCollection(collectionName, type);
            return this.withConcern(collection, type);
        } else {

            return getCollection(type);
        }

    }


    @Override
    public < T > long estimatedCount( Class< T > clazz) {

        com.mongodb.client.model.EstimatedDocumentCountOptions escountOptions = new com.mongodb.client.model.EstimatedDocumentCountOptions();


        String collectionName = this.mapper.determineCollectionName(clazz, null);
        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", "{}", collectionName);
        }
        return this.database.getCollection(collectionName).estimatedDocumentCount(escountOptions);

    }

    @Override
    public long count( Query query, Class< ? > entityClass, CountOptions countOptions, String collectionName ) {

        String s = this.mapper.determineCollectionName(entityClass, collectionName);
        return this.database.getCollection(s).countDocuments(query.getQueryObject(),countOptions.getOriginOptions());
    }
    @Override
    public long count( Query query, Class< ? > entityClass, String collectionName ) {

        String s = this.mapper.determineCollectionName(entityClass, collectionName);

        return count(query,s);
    }

    @Override
    public < T > long count( Query query, Class< T > clazz ) {

        com.mongodb.client.model.CountOptions countOptions = decorateCountOption(query);

        String collectionName = this.mapper.determineCollectionName(clazz, null);
        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", query.getQueryObject().toJson(), collectionName);
        }
        return this.database.getCollection(collectionName).countDocuments(query.getQueryObject(), countOptions);
    }

    @Override
    public < T > long estimatedCount( String collectionName) {


        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", "{}", collectionName);
        }

        return this.database.getCollection(collectionName).estimatedDocumentCount( new com.mongodb.client.model.EstimatedDocumentCountOptions());
    }

    @Override
    public < T > long count( Query query, String collectionName) {
        com.mongodb.client.model.CountOptions countOptions = decorateCountOption(query);
        if (log.isDebugEnabled()) {
            log.debug("Executing count: {} in collection: {}", query.getQueryObject().toJson(), collectionName);
        }
        return this.database.getCollection(collectionName).countDocuments(query.getQueryObject(), countOptions);
    }


    //只传入实体类的方式，根据实体类的注解获取CollectionOptions
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
            collectionOptions.getCollation().ifPresent(val -> document.append("collation", val.asDocument()));

            collectionOptions.getValidationOptions().ifPresent(it -> {
                it.getValidationLevel().ifPresent(val -> document.append("validationLevel", val.getValue()));
                it.getValidationAction().ifPresent(val -> document.append("validationAction", val.getValue()));
            });

            Optional<TimeSeriesOptions> timeSeries = collectionOptions.getTimeSeriesOptions();
            if(ObjectUtil.isNotEmpty(timeSeries)){
                TimeSeriesOptions timeSeriesOptions = timeSeries.get();
                Document document1 = new Document();
                if(ObjectUtil.isNotEmpty(timeSeriesOptions.getTimeField())){
                    document1.append("timeField",timeSeriesOptions.getTimeField());
                }

                if (ObjectUtil.isNotEmpty(timeSeriesOptions.getMetaField())){
                    document1.append("metaField",timeSeriesOptions.getMetaField());
                }

                if (ObjectUtil.isNotEmpty(timeSeriesOptions.getGranularity())){
                    document1.append("granularity",timeSeriesOptions.getGranularity());
                }

                if (ObjectUtil.isNotEmpty(timeSeriesOptions.getExpireAfterSeconds())){
                    document1.append("expireAfterSeconds",timeSeriesOptions.getExpireAfterSeconds());
                }
                document.append("timeseries",document1);
            }
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
                co.collation( com.whaleal.mars.core.query.Collation.from((Document.parse(collectionOptions.get("collation").toString()))).toMongoCollation());
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

            if(collectionOptions.containsKey("timeseries")){
                Document timeseries = collectionOptions.get("timeseries", Document.class);

                com.mongodb.client.model.TimeSeriesOptions timeSeriesOptions = null ;
                String timeField = timeseries.getString("timeField");
                String metaField = timeseries.getString("metaField");
                if(ObjectUtil.isNotEmpty(timeField)){
                    timeSeriesOptions = new com.mongodb.client.model.TimeSeriesOptions(timeField);
                }

                //不需要手动对这个参数进行合法性校验
                if(ObjectUtil.isNotEmpty(metaField)){
                    timeSeriesOptions.metaField(timeseries.getString("metaField"));
                }

                if(ObjectUtil.isNotEmpty(timeseries.get("granularity"))){
                    timeSeriesOptions.granularity(timeseries.get("granularity", TimeSeriesGranularity.class));
                }

                //需要先判断是否开启TTL，如果开启了并且传入了过期时间，则指定
                if(ObjectUtil.isNotEmpty(timeseries.getLong("expireAfterSeconds"))){
                    co.expireAfter(timeseries.getLong("expireAfterSeconds"), TimeUnit.SECONDS);
                }
                co.timeSeriesOptions(timeSeriesOptions);

            }

            this.database.createCollection(collectionName, co);

            MongoCollection< Document > coll = database.getCollection(collectionName, Document.class);
            //如果配置文件中开启了自动创建索引，则在表创建成功后创建索引
            if(this.mapper.isAutoIndexCreation()){
                Class<Object> entity = this.mapper.getClassFromCollection(collectionName);
                ensureIndexes(entity,coll.getNamespace().getCollectionName());
            }
            return coll;
        } finally {
            lock.unlock();
        }
    }


    public  <T> MongoCollection<T> withConcern( MongoCollection<T> collection, Class<?> clazz ) {

        EntityModel entityModel = this.mapper.getEntityModel(clazz);

        Concern annotation = (Concern) entityModel.getAnnotation(Concern.class);

        //判断实体类是否有@Concern注解
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


        Collation collation = (Collation) entityModel.getAnnotation(Collation.class);

        if (!ObjectUtil.isEmpty(collation)) {
            com.mongodb.client.model.Collation collationOption = com.mongodb.client.model.Collation.builder()
                    .locale(collation.locale())
                    .caseLevel(collation.caseLevel())
                    .collationCaseFirst(collation.caseFirst())
                    .collationStrength(collation.strength())
                    .numericOrdering(collation.numericOrdering())
                    .collationAlternate(collation.alternate())
                    .collationMaxVariable(collation.maxVariable())
                    .backwards(collation.backwards())
                    .normalization(collation.normalization())
                    .build();

            System.out.println(collationOption);
            options = options.collation(collationOption);

        }

        TimeSeries timeSeries = (TimeSeries) entityModel.getAnnotation(TimeSeries.class);

        if (!ObjectUtil.isEmpty(timeSeries)) {
            TimeSeriesOptions toptions = TimeSeriesOptions.timeSeries(timeSeries.timeField());
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

            if(ObjectUtil.equal(timeSeries.enableExpire(),true)){
                if (ObjectUtil.isNotEmpty(timeSeries.expireAfterSeconds())){
                    toptions = toptions.expireAfterSeconds(timeSeries.expireAfterSeconds());
                }
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

        if (query.getCollation().orElse(null) != null){
            findIterable = findIterable.collation(query.getCollation().get());
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



    /**
     * 根据id查询记录
     * @param session
     * @param collection
     * @param <T>
     * @return
     */
    private <T>Optional<T> findByIdExecute(ClientSession session,MongoCollection collection,Object id){

        FindIterable<T> findIterable;
        Document document = new Document().append("_id", id);
        if(session==null){
            findIterable = collection.find(document);
        }else {
            findIterable = collection.find(session,document);
        }

        if((T)findIterable.first()!=null){
            return (Optional<T>) findIterable.first();
        }else {
            return Optional.empty();
        }
    }

    /**
     * 查询去重
     * @param query
     * @param field
     * @param entityClass
     * @param resultClass
     * @param <T>
     * @return
     */
    public <T> QueryCursor<T> findDistinct(Query query, String field, Class<?> entityClass, Class<T> resultClass) {
        return this.findDistinct(query, field, this.getCollectionName(entityClass), entityClass, resultClass);
    }

    public <T> QueryCursor<T> findDistinct(Query query, String field, String collectionName, Class<?> entityClass, Class<T> resultClass) {
        ClientSession session = this.startSession();
        MongoCollection collection = this.getCollection(entityClass,collectionName);
        QueryCursor<T> result = this.findDistinctExecute(session, collection, query, field, resultClass);
        return result;
    }
    private <T> QueryCursor<T> findDistinctExecute(ClientSession session,MongoCollection collection,Query query,String field,Class<T> resultClass){

        DistinctIterable<T> distinctIterable;
        if(query!=null){
            distinctIterable = collection.distinct(session,field,query.getQueryObject(),resultClass);
            if (query.getCollation().orElse(null) != null){
                distinctIterable = distinctIterable.collation(query.getCollation().get());
            }
        }else {
            distinctIterable = collection.distinct(session,field,null,resultClass);
        }
        return new QueryCursor<T>(distinctIterable.iterator());
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



        List indexes = new ArrayList();


        while (iterator.hasNext()) {

            Document  o= (Document) iterator.next();

            IndexOptions indexOptions = new IndexOptions();
            if (o.get("background") != null) {

                //todo  具体值可能为多种类型  如 "true"  true  1 1.0  "1.0"  "5.0"  甚至为 任意字符"xxx"尤其是老版本 情况很多 这里并不能全部列举
                // C语言决策. C语言编程假定任何非零和非空值为真，并且如果它是零或null，那么它被假定为假值
                // 主要为 boolean  类型 String  类型  数值类型
                if(o.get("background") instanceof Boolean){
                    indexOptions.background((Boolean) o.get("background"));
                }else if(o.get("background") instanceof String){
                    indexOptions.background(true);
                }else if(o.get("background") instanceof Number){
                    // 非 0 为真
                    double v ;
                    try{
                       v = Double.valueOf(o.get("background").toString());
                       if(v>0){
                           indexOptions.background(true);
                       }else if(v <0){
                           indexOptions.background(true);
                       }else {
                           indexOptions.background(false);
                       }
                    }catch (Exception e){
                        log.warn("Index background Option parse error from  index name %s  with background value %s ", o.get("name") ,o.get("background") );
                        indexOptions.background(true);
                    }
                }

            }

            if (o.get("unique") != null) {
                indexOptions.unique((Boolean) o.get("unique"));
            }
            if (o.get("name") != null) {
                indexOptions.name((String) o.get("name"));
            }

            if (o.get("partialFilterExpression") != null) {
                indexOptions.partialFilterExpression((Bson) o.get("partialFilterExpression"));
            }
            if (o.get("sparse") != null) {
                indexOptions.sparse((Boolean) o.get("sparse"));
            }
            if (o.get("expireAfterSeconds") != null) {
                Long expireAfter = ((Double) Double.parseDouble(o.get("expireAfterSeconds").toString())).longValue();
                //秒以下会丢失
                indexOptions.expireAfter(expireAfter, TimeUnit.SECONDS);
            }

            if (o.get("hidden") != null) {
                indexOptions.hidden((Boolean) o.get("hidden"));
            }

            if (o.get("storageEngine") != null) {
                //不常用到
                indexOptions.storageEngine((Bson) o.get("storageEngine"));
            }

            //---------deal with Collation

            if (o.get("collation") != null) {
                com.mongodb.client.model.Collation.Builder collationBuilder = com.mongodb.client.model.Collation.builder();
                Document collation = (Document) o.get("collation");
                if (collation.get("locale") != null) {
                    collationBuilder.locale(collation.getString("locale"));
                }
                if (collation.get("caseLevel") != null) {
                    collationBuilder.caseLevel(collation.getBoolean("caseLevel"));
                }
                if (collation.get("caseFirst") != null) {
                    collationBuilder.collationCaseFirst(CollationCaseFirst.fromString(collation.getString("caseFirst")));
                }
                if (collation.get("strength") != null) {
                    collationBuilder.collationStrength(CollationStrength.fromInt(
                            ((Double) Double.parseDouble(collation.get("strength").toString())).intValue()
                    ));
                }
                if (collation.get("numericOrdering") != null) {
                    collationBuilder.numericOrdering(collation.getBoolean("numericOrdering"));
                }
                if (collation.get("alternate") != null) {
                    collationBuilder.collationAlternate(CollationAlternate.fromString(collation.getString("alternate")));
                }
                if (collation.get("maxVariable") != null) {
                    collationBuilder.collationMaxVariable(CollationMaxVariable.fromString(collation.getString("maxVariable")));
                }
                if (collation.get("normalization") != null) {
                    collationBuilder.normalization(collation.getBoolean("normalization"));
                }
                if (collation.get("backwards") != null) {
                    collationBuilder.backwards(collation.getBoolean("backwards"));
                }
                indexOptions.collation(collationBuilder.build());
            }

            //---------deal with Text


            if (o.get("weights") != null) {
                indexOptions.weights((Bson) o.get("weights"));
            }
            if (o.get("textIndexVersion") != null) {
                indexOptions.textVersion(((Double) Double.parseDouble(o.get("textIndexVersion").toString())).intValue());
            }
            if (o.get("default_language") != null) {
                indexOptions.defaultLanguage((String) o.get("default_language"));
            }
            if (o.get("language_override") != null) {
                indexOptions.languageOverride(o.get("language_override").toString());
            }

            //--------deal with wildcard

            if (o.get("wildcardProjection") != null) {
                indexOptions.wildcardProjection((Bson) o.get("wildcardProjection"));
            }

            //---------deal with geoHaystack
            if (o.get("bucketSize") != null) {
                indexOptions.bucketSize(Double.parseDouble(o.get("bucketSize").toString()));
            }
            //---------deal with  2d

            if (o.get("bits") != null) {
                indexOptions.bits(((Double) Double.parseDouble(o.get("bits").toString())).intValue());
            }
            if (o.get("max") != null) {
                indexOptions.max((Double.parseDouble(o.get("max").toString())));
            }
            if (o.get("min") != null) {
                indexOptions.min((Double.parseDouble(o.get("min").toString())));
            }

            //---------------deal with 2dsphere

            if (o.get("2dsphereIndexVersion") != null) {
                indexOptions.sphereVersion(((Double) Double.parseDouble((o.get("2dsphereIndexVersion").toString()))).intValue());
            }

            //------ let it be backgroud
            indexOptions.background(true);

            Document key = (Document) o.get("key");

            Index index = new Index();

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
        try (AutoCloseable closeable = marssession){
            ClientSession session = marssession.startSession();
            if (session == null) {
                throw new IllegalStateException("No session could be found for the transaction.");
            }
            return session.withTransaction(() -> body.execute(marssession));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null ;
        }
    }


private com.mongodb.client.model.CountOptions  decorateCountOption(Query query ){
        com.mongodb.client.model.CountOptions options = new com.mongodb.client.model.CountOptions();

        if (query.getLimit() > 0) {
            options.limit(query.getLimit());
        }
        if (query.getSkip() > 0) {
            options.skip((int) query.getSkip());
        }
        if (StrUtil.hasText(query.getHint())) {
            String hint = query.getHint();
            try {
                Document parse = Document.parse(hint);
                options.hint(parse);
            }catch (Exception e){
                options.hintString(hint);
            }

        }

        return options;
    }
}



