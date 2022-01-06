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
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ObjectUtil;
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
import com.whaleal.mars.core.index.IndexHelper;
import com.whaleal.mars.core.query.Collation;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.mars.core.query.UpdateDefinition;
import com.whaleal.mars.gridfs.GridFsObject;
import com.whaleal.mars.gridfs.GridFsOperations;
import com.whaleal.mars.gridfs.GridFsResource;
import com.whaleal.mars.session.executor.CrudExecutor;
import com.whaleal.mars.session.executor.CrudExecutorFactory;
import com.whaleal.mars.session.option.*;
import com.whaleal.mars.session.result.DeleteResult;
import com.whaleal.mars.session.result.InsertManyResult;
import com.whaleal.mars.session.result.InsertOneResult;
import com.whaleal.mars.session.result.UpdateResult;
import com.whaleal.mars.util.BsonUtil;
import com.whaleal.mars.util.SerializationUtil;
import org.bson.Document;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


/**
 * 关于数据操作的数据操作的一些具体实现
 *
 * @Date 2020-12-03
 */

public class DatastoreImpl extends AggregationImpl implements Datastore{

    private static final Log log = LogFactory.get(DatastoreImpl.class);
    private final Lock lock = new ReentrantLock();
    private final MongoClient mongoClient;

    private final GridFSBucket defaultGridFSBucket;
    //缓存 collectionName
    private final Map< String, String > collectionNameCache = new HashMap< String, String >();


    public DatastoreImpl( MongoClient mongoClient, String databaseName ) {
        super(mongoClient.getDatabase(databaseName));
        this.mongoClient = mongoClient;
        defaultGridFSBucket = GridFSBuckets.create(this.database);
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
        return this.database;
    }

    @Override
    public < T > DeleteResult delete( Query query, Class< T > entityClass, DeleteOptions options, String collectionName ) {


        MongoCollection collection = this.getCollection(entityClass, collectionName);

        collection = prepareConcern(collection, options);
        //根据query  去 删除相关数据
        ClientSession session = this.startSession();

        CrudExecutor executor = CrudExecutorFactory.create(CrudEnum.DELETE);

        DeleteResult result = executor.execute(session, collection, query, options, null);

        return result;

    }

    @Override
    public < T > QueryCursor< T > findAll( Query query, Class< T > entityClass, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entityClass, collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.FIND_ALL);

        MongoCursor< T > iterator = crudExecutor.execute(session, collection, query, null, null);

        return new QueryCursor< T >(iterator, entityClass);

    }

    @Override
    public < T > Optional< T > findOne( Query query, Class< T > entityClass, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entityClass, collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.FIND_ONE);

        T result = crudExecutor.execute(session, collection, query, null, null);
        if (log.isDebugEnabled()) {
            log.debug("Executing query: {} sort: {} fields: {} in collection: {}", SerializationUtil.serializeToJsonSafely(query.getQueryObject()),
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
        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INSERT_ONE);
        InsertOneResult result = crudExecutor.execute(session, collection, null, options, entity);

        return result;
    }

    @Override
    public < T > InsertManyResult insert( Collection< ? extends T > entities, InsertManyOptions options, String collectionName ) {

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

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INSERT_MANY);

        InsertManyResult result = crudExecutor.execute(session, collection, null, options, entities);

        return result;

    }

    @Override
    public < T > UpdateResult update( Query query, T entity, UpdateOptions options, String collectionName ) {

        Document entityDoc = this.toDocument(entity);
        if (entityDoc == null) {
            throw new IllegalArgumentException();
        }
        ClientSession session = this.startSession();
        MongoCollection< ? > collection = this.getCollection(entity.getClass(), collectionName);

        collection = prepareConcern(collection, options);
        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.UPDATE);

        UpdateResult result = crudExecutor.execute(session, collection, query, options, entityDoc);

        return result;

    }

    /**
     * update的另一种形式
     */
    @Override
    public < T > UpdateResult update( Query query, UpdateDefinition update, Class< T > entityClass, UpdateOptions options, String collectionName ) {

        ClientSession session = this.startSession();
        MongoCollection collection = this.getCollection(entityClass, collectionName);
        collection = prepareConcern(collection, options);
        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.UPDATE_DEFINITION);

        UpdateResult result = crudExecutor.execute(session, collection, query, options, update.getUpdateObject());

        return result;
    }


    @Override
    public MarsSession startSession() {
        ClientSession clientSession = null;
        try {
            clientSession = this.mongoClient.startSession();
        } catch (MongoClientException exception) {

            return null;
        }
        return new MarsSessionImpl(clientSession, mongoClient, database);
    }


    @Override
    public MarsSession startSession( ClientSessionOptions options ) {
        ClientSession clientSession = null;
        try {
            clientSession = this.mongoClient.startSession(options);
        } catch (MongoClientException exception) {

            return null;
        }

        return new MarsSessionImpl(clientSession, mongoClient, database);
    }

    @Override
    public MongoMappingContext getMapper() {
        return mapper;
    }


    @Override
    public < T > UpdateResult replace( Query query, T entity, ReplaceOptions options, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entity.getClass(), collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.REPLACE);

        UpdateResult execute = crudExecutor.execute(session, collection, query, options, entity);

        return execute;

    }

    //索引操作
    @Override
    public void createIndex( Index index, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_CREATE_ONE);


        crudExecutor.execute(session, collection, null, index.getIndexOptions(), index);

    }


    @Override
    public < T > void ensureIndexes( Class< T > entityClass, String collectionName ) {

        final IndexHelper indexHelper = new IndexHelper();
        String collName = this.mapper.determineCollectionName(entityClass, collectionName);
        indexHelper.createIndex(this.database.getCollection(collName), this.mapper.getEntityModel(entityClass));

    }

    @Override
    public void dropIndex( Index index, String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_DROP_ONE);

        crudExecutor.execute(session, collection, null, null, index);

    }

    @Override
    public void dropIndexes( String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_DROP_MANY);

        crudExecutor.execute(session, collection, null, null, null);

    }

    @Override
    public List< Index > getIndexes( String collectionName ) {

        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_FIND);

        List< Index > execute = crudExecutor.execute(session, collection, null, null, null);

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


    private < T > MongoCollection< T > prepareConcern( MongoCollection< T > collection, Option option ) {
        return option.prepare(collection);

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

        Precondition.notNull(query, "Query must not be null!");

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
        Precondition.notNull(iterator.tryNext(), "No file found with the query");

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
        Precondition.notNull(iterator.tryNext(), "no file found to delete");
        while (iterator.hasNext()) {
            getGridFsBucket(bucketName).delete(iterator.next().getObjectId());
        }

    }


    private GridFSBucket getGridFsBucket( String bucketName ) {
        return bucketName == null ? defaultGridFSBucket : GridFSBuckets.create(database, bucketName);
    }

    @Override
    public GridFsResource getResource( GridFSFile file, String bucketName ) {

        Precondition.notNull(file, "GridFSFile must not be null!");

        return new GridFsResource(file, getGridFsBucket(bucketName).openDownloadStream(file.getId()));
    }


    public Document toDocument( Object entity ) {
        final EntityModel entityModel = this.mapper.getEntityModel(entity.getClass());

        DocumentWriter writer = new DocumentWriter();
        this.mapper.getCodecRegistry().get(entityModel.getType())
                .encode(writer, entity, EncoderContext.builder().build());

        return writer.getDocument();
    }

    /**
     * @param type the type look up
     * @param <T>  the class type
     * @return the collection mapped for this class
     */
    public < T > MongoCollection< T > getCollection( Class< T > type ) {
        EntityModel entityModel = mapper.getEntityModel(type);
        String collectionName = entityModel.getCollectionName();

        MongoCollection< T > collection = this.database.getCollection(collectionName, type);

        Concern annotation = (Concern) entityModel.getAnnotation(Concern.class);
        if (annotation != null) {


        }
        return collection;
    }


    /**
     * @param type the type look up
     * @param <T>  the class type
     * @return the collection mapped for this class
     */
    public < T > MongoCollection< T > getCollection( Class< T > type, String collectionName ) {

        EntityModel entityModel = this.mapper.getEntityModel(type);
        String collName = this.mapper.determineCollectionName(entityModel, collectionName);
        MongoCollection< T > collection = this.database.getCollection(collName, type);

        return this.withConcern(collection, type);

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
            log.debug("Executing count: {} in collection: {}", SerializationUtil.serializeToJsonSafely(query.getQueryObject()), collectionName);
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
            log.debug("Executing count: {} in collection: {}", SerializationUtil.serializeToJsonSafely(query.getQueryObject()), collectionName);
        }
        return this.database.getCollection(collectionName).countDocuments(query.getQueryObject(), countOptions.getOriginOptions());
    }


    @Override
    public < T > MongoCollection< Document > createCollection( Class< T > entityClass ) {
        Precondition.notNull(entityClass, "EntityClass must not be null!");
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

        Precondition.notNull(entityClass, "EntityClass must not be null!");

        CollectionOptions options = collectionOptions != null ? collectionOptions : CollectionOptions.empty();

        Document document = convertToDocument(options);

        return doCreateCollection(this.mapper.getEntityModel(entityClass).getCollectionName(), document);
    }

    @Override
    public MongoCollection< Document > createCollection( String collectionName ) {
        Precondition.notNull(collectionName, "CollectionName must not be null!");

        return doCreateCollection(collectionName, new Document());
    }

    @Override
    public < T > void dropCollection( Class< T > entityClass ) {
        dropCollection(this.mapper.getEntityModel(entityClass).getCollectionName());
    }

    @Override
    public MongoCollection< Document > createCollection( String collectionName, CollectionOptions collectionOptions ) {

        Precondition.notNull(collectionName, "CollectionName must not be null!");
        return doCreateCollection(collectionName, convertToDocument(collectionOptions));
    }

    @Override
    public void dropCollection( String collectionName ) {
        Precondition.notNull(collectionName, "CollectionName must not be null!");
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
        Precondition.notNull(entity, "EntityClass must not be null!");
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
}
