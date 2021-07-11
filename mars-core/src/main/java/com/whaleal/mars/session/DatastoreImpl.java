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

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.lang.Nullable;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.bson.codecs.pojo.EntityModel;
import com.whaleal.mars.bson.codecs.pojo.PropertyModel;
import com.whaleal.mars.bson.codecs.pojo.annotations.CappedAt;
import com.whaleal.mars.bson.codecs.pojo.annotations.Concern;
import com.whaleal.mars.bson.codecs.pojo.annotations.Entity;
import com.whaleal.mars.bson.codecs.writer.DocumentWriter;
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
import com.whaleal.mars.util.Assert;
import com.whaleal.mars.util.BsonUtils;
import com.whaleal.mars.util.ObjectUtils;
import com.whaleal.mars.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.lang.annotation.Annotation;
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
@Slf4j
public class DatastoreImpl extends AggregationImpl implements Datastore,
        MongoOperations, GridFsOperations, Statistic {

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    private final MongoClient mongoClient;
    private final MongoMappingContext mapper;
    private MongoDatabase database;
    private final GridFSBucket defaultGridFSBucket;

    //缓存 collectionName
    private Map<String, String> collectionNameCache = new HashMap<String, String>();

    protected static Lock lock = new ReentrantLock();

    public DatastoreImpl(MongoClient mongoClient, String dbName) {
        this(mongoClient.getDatabase(dbName), mongoClient);
    }

    /**
     * Copy constructor for a datastore
     */
    public DatastoreImpl(MongoDatabase database, MongoClient mongoClient) {

        super(database);

        this.mongoClient = mongoClient;
        this.mapper = new MongoMappingContext(database);
        this.database = database.withCodecRegistry(mapper.getCodecRegistry());

        defaultGridFSBucket = GridFSBuckets.create(database);

    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    @Override
    public <T> DeleteResult delete(Query query, Class<T> entityClass, DeleteOptions options, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".delete()");

        MongoCollection collection = this.getCollection(entityClass, collectionName);

        collection = prepareConcern(collection, options);
        //根据query  去 删除相关数据
        ClientSession session = this.startSession();

        CrudExecutor executor = CrudExecutorFactory.create(CrudEnum.DELETE);

        DeleteResult result = executor.execute(session, collection, query, options, null);
        log.info("{} execute end", getClass() + ".delete()");
        return result;

    }


    @Override
    public <T> QueryCursor<T> findAll(Query query, Class<T> entityClass, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".findAll()");
        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entityClass, collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.FIND_ALL);

        MongoCursor<T> iterator = crudExecutor.execute(session, collection, query, null, null);

        log.info("{} execute end", getClass() + ".findAll()");
        return new QueryCursor<T>(iterator, entityClass);

    }


    @Override
    public <T> Optional<T> findOne(Query query, Class<T> entityClass, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".findOne()");
        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entityClass, collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.FIND_ONE);

        T result = crudExecutor.execute(session, collection, query, null, null);

        log.info("{} execute end", getClass() + ".delete()");
        if (result == null) {
            return Optional.empty();
        }

        log.info("{} execute end", getClass() + ".findOne()");
        return Optional.ofNullable(result);

    }


    @Override
    public <T> InsertOneResult insert(T entity, InsertOneOptions options, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".insert()");
        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entity.getClass(), collectionName);

        collection = prepareConcern(collection, options);
        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INSERT_ONE);
        InsertOneResult result = crudExecutor.execute(session, collection, null, options, entity);
        log.info("{} execute end", getClass() + ".insert()");
        return result;
    }

    @Override
    public <T> InsertManyResult insert(Collection<? extends T> entities, InsertManyOptions options, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".insert()");
        if (entities == null || entities.isEmpty()) {
            throw new IllegalArgumentException("entities in operation can't be null or empty ");
        }

        ClientSession session = this.startSession();

        Class<?> type = null;

        for (T entity : entities) {

            type = entity.getClass();

            break;
        }

        MongoCollection collection = this.getCollection(type, collectionName);

        collection = prepareConcern(collection, options);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INSERT_MANY);


        InsertManyResult result = crudExecutor.execute(session, collection, null, options, entities);
        log.info("{} execute end", getClass() + ".insert()");
        return result;

    }

    @Override
    public <T> UpdateResult update(Query query, T entity, UpdateOptions options, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".update()");
        Document entityDoc = this.toDocument(entity);
        if (entityDoc == null) {
            throw new IllegalArgumentException();
        }
        ClientSession session = this.startSession();
        MongoCollection<?> collection = this.getCollection(entity.getClass(), collectionName);

        collection = prepareConcern(collection, options);
        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.UPDATE);

        UpdateResult result = crudExecutor.execute(session, collection, query, options, entityDoc);
        log.info("{} execute end", getClass() + ".update()");
        return result;

    }


    /**
     * update的另一种形式
     */
    @Override
    public <T> UpdateResult update(Query query, UpdateDefinition update, Class<T> entityClass, UpdateOptions options, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".update()");
        ClientSession session = this.startSession();
        MongoCollection collection = this.getCollection(entityClass, collectionName);
        collection = prepareConcern(collection, options);
        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.UPDATE_DEFINITION);

        UpdateResult result = crudExecutor.execute(session, collection, query, options, update.getUpdateObject());
        log.info("{} execute end", getClass() + ".update()");
        return result;
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
    public <T> T save(T entity, InsertOneOptions options, @Nullable String collectionName) {
        log.info("{}execute", getClass() + ".save()");

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
        log.info("{} execute end", getClass() + ".save()");
        return entity;

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
    public MarsSession startSession(ClientSessionOptions options) {
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
    public <T> UpdateResult replace(Query query, T entity, ReplaceOptions options, String collectionName) {
        log.info("{}execute", getClass() + ".replace()");
        ClientSession session = this.startSession();

        MongoCollection collection = this.getCollection(entity.getClass(), collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.REPLACE);

        UpdateResult execute = crudExecutor.execute(session, collection, query, options, entity);
        log.info("{} execute end", getClass() + ".replace()");
        return execute;

    }

    //索引操作
    @Override
    public void createIndex(Index index, String collectionName) {
        log.info("{}execute", getClass() + ".createIndex()");
        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_CREATE_ONE);


        crudExecutor.execute(session, collection, null, index.getIndexOptions(), index);


        log.info("{} execute end", getClass() + ".createIndex()");
    }


    @Override
    public <T> void ensureIndexes(Class<T> entityClass, String collectionName) {
        log.info("{}execute", getClass() + ".ensureIndexes()");
        final IndexHelper indexHelper = new IndexHelper();
        String collName = this.mapper.determineCollectionName(entityClass, collectionName);
        indexHelper.createIndex(this.database.getCollection(collName), this.mapper.getEntityModel(entityClass));
        log.info("{} execute end", getClass() + ".ensureIndexes()");
    }

    @Override
    public void dropIndex(Index index, String collectionName) {
        log.info("{}execute", getClass() + ".dropIndex()");
        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_DROP_ONE);

        crudExecutor.execute(session, collection, null, null, index);
        log.info("{} execute end", getClass() + ".dropIndex()");
    }

    @Override
    public void dropIndexes(String collectionName) {
        log.info("{}execute", getClass() + ".dropIndexes()");
        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_DROP_MANY);

        crudExecutor.execute(session, collection, null, null, null);
        log.info("{} execute end", getClass() + ".dropIndexes()");
    }

    @Override
    public List<Index> getIndexes(String collectionName) {
        log.info("{}execute", getClass() + ".getIndexes()");
        ClientSession session = this.startSession();

        MongoCollection collection = database.getCollection(collectionName);

        CrudExecutor crudExecutor = CrudExecutorFactory.create(CrudEnum.INDEX_FIND);

        List<Index> execute = crudExecutor.execute(session, collection, null, null, null);
        log.info("{} execute end", getClass() + ".getIndexes()");
        return execute;

    }


    /**
     * 将Collections<T> 转成 List<Entity>
     */
    public <T> List<Document> toDocuments(Collection<? extends T> entities) {

        if (entities.isEmpty()) {
            return new ArrayList<Document>();
        }

        return entities.stream().map(x -> mapper.toDocument(x)).collect(Collectors.toList());

    }


    private <T> MongoCollection<T> prepareConcern(MongoCollection<T> collection, Option option) {
        return option.prepare(collection);

    }

    public void setWriteConcern(WriteConcern writeConcern) {
        this.database = database.withWriteConcern(writeConcern);

    }

    public void setReadConcern(ReadConcern readConcern) {
        this.database = database.withReadConcern(readConcern);
    }

    public void setReadPerference(ReadPreference readPerference) {
        this.database = database.withReadPreference(readPerference);
    }

    @Override
    public ObjectId storeGridFs(InputStream content, @Nullable String filename, @Nullable String contentType,
                                @Nullable Object metadata, @Nullable String bucketName) {
        return storeGridFs(content, filename, contentType, metadata, bucketName);
    }

    @Override
    public <T> T storeGridFs(GridFsObject<T, InputStream> upload, String bucketName) {
        log.info("{}execute", getClass() + ".storeGridFs()");
        GridFSUploadOptions uploadOptions = computeUploadOptionsFor(upload.getOptions().getContentType(),
                upload.getOptions().getMetadata());

        if (upload.getOptions().getChunkSize() > 0) {
            uploadOptions.chunkSizeBytes(upload.getOptions().getChunkSize());
        }

        if (upload.getFileId() == null) {
            return (T) getGridFsBucket(bucketName).uploadFromStream(upload.getFilename(), upload.getContent(), uploadOptions);
        }

        getGridFsBucket(bucketName).uploadFromStream(BsonUtils.simpleToBsonValue(upload.getFileId()), upload.getFilename(),
                upload.getContent(), uploadOptions);
        log.info("{} execute end", getClass() + ".storeGridFs()");
        return upload.getFileId();
    }

    @Override
    public GridFSFindIterable findGridFs(Query query, String bucketName) {
        log.info("{}execute", getClass() + ".findGridFs()");
        Assert.notNull(query, "Query must not be null!");

        Document queryObject = query.getQueryObject();
        Document sortObject = query.getSortObject();

        GridFSFindIterable iterable = getGridFsBucket(bucketName).find(queryObject).sort(sortObject);
        if (query.getSkip() > 0) {
            iterable = iterable.skip(Math.toIntExact(query.getSkip()));
        }

        if (query.getLimit() > 0) {
            iterable = iterable.limit(query.getLimit());
        }

        MongoCursor<GridFSFile> iterator = iterable.iterator();
        Assert.notNull(iterator.tryNext(), "No file found with the query");
        log.info("{} execute end", getClass() + ".findGridFs()");
        return iterable;
    }

    @Override
    public GridFSFile findOneGridFs(Query query, String bucketName) {
        return findGridFs(query, bucketName).first();
    }

    @Override
    public void rename(ObjectId id, String newFilename, String bucketName) {
        log.info("{}execute", getClass() + ".rename()");
        getGridFsBucket(bucketName).rename(id, newFilename);
        log.info("{} execute end", getClass() + ".rename()");
    }


    @Override
    public void deleteGridFs(Query query, String bucketName) {
        log.info("{}execute", getClass() + ".deleteGridFs()");
        GridFSFindIterable iterable = findGridFs(query);
        MongoCursor<GridFSFile> iterator = iterable.iterator();
        Assert.notNull(iterator.tryNext(), "no file found to delete");
        while (iterator.hasNext()) {
            getGridFsBucket(bucketName).delete(iterator.next().getObjectId());
        }
        log.info("{} execute end", getClass() + ".deleteGridFs()");
    }


    @Override
    public void deleteGridFs(ObjectId id, String bucketName) {
        log.info("{}execute", getClass() + ".deleteGridFs()");
        getGridFsBucket(bucketName).delete(id);
        log.info("{} execute end", getClass() + ".deleteGridFs()");
    }


    @Override
    public ClassLoader getClassLoader() {
        return database.getClass().getClassLoader();
    }

    @Override
    public GridFsResource getResource(GridFSFile file, String bucketName) {

        Assert.notNull(file, "GridFSFile must not be null!");

        return new GridFsResource(file, getGridFsBucket(bucketName).openDownloadStream(file.getId()));
    }


    private GridFSBucket getGridFsBucket(String bucketName) {
        return bucketName == null ? defaultGridFSBucket : GridFSBuckets.create(database, bucketName);
    }

    private GridFSUploadOptions computeUploadOptionsFor(@Nullable String contentType, @Nullable Document metadata) {

        Document targetMetadata = new Document();

        if (StringUtils.hasText(contentType)) {
            targetMetadata.put(GridFsResource.CONTENT_TYPE_FIELD, contentType);
        }

        if (metadata != null) {
            targetMetadata.putAll(metadata);
        }

        GridFSUploadOptions options = new GridFSUploadOptions();
        options.metadata(targetMetadata);

        return options;
    }


    public Document toDocument(Object entity) {
        final EntityModel entityModel = this.mapper.getEntityModel(entity.getClass());

        DocumentWriter writer = new DocumentWriter();
        ((Codec) this.mapper.getCodecRegistry().get(entityModel.getType()))
                .encode(writer, entity, EncoderContext.builder().build());

        return writer.getDocument();
    }

    /**
     * @param type the type look up
     * @param <T>  the class type
     * @return the collection mapped for this class
     */
    public <T> MongoCollection<T> getCollection(Class<T> type) {
        EntityModel entityModel = mapper.getEntityModel(type);
        String collectionName = entityModel.getCollectionName();

        MongoCollection<T> collection = this.database.getCollection(collectionName, type);

        Concern annotation = (Concern) entityModel.getAnnotation(Concern.class);
        if (annotation != null ) {






        }
        return collection;
    }


    /**
     * @param type the type look up
     * @param <T>  the class type
     * @return the collection mapped for this class
     */
    public <T> MongoCollection<T> getCollection(Class<T> type, String collectionName) {

        EntityModel entityModel = this.mapper.getEntityModel(type);
        String collName = this.mapper.determineCollectionName(entityModel, collectionName);
        MongoCollection<T> collection = this.database.getCollection(collName, type);


        return  this.withConcern(collection , type);




    }


    @Override
    public <T> long count(Class<T> clazz, CountOptions countOptions) {
        log.info("{}execute", getClass() + ".count()");

        com.mongodb.client.model.EstimatedDocumentCountOptions escountOptions = new com.mongodb.client.model.EstimatedDocumentCountOptions();

        escountOptions.maxTime(countOptions.getMaxTime(TimeUnit.SECONDS), TimeUnit.SECONDS);

        String collectionName = this.mapper.determineCollectionName(clazz, null);
        return this.database.getCollection(collectionName).estimatedDocumentCount(escountOptions);

    }

    @Override
    public <T> long countById(Query query, Class<T> clazz, CountOptions countOptions) {
        log.info("{} execute", getClass() + ".countById()");
        String collectionName = this.mapper.determineCollectionName(clazz, null);
        return this.database.getCollection(collectionName).countDocuments(query.getQueryObject(), countOptions.getOriginOptions());
    }

    @Override
    public <T> long count(String collName, CountOptions countOptions) {
        log.info("{} execute", getClass() + ".count()");
        com.mongodb.client.model.EstimatedDocumentCountOptions escountOptions = new com.mongodb.client.model.EstimatedDocumentCountOptions();

        escountOptions.maxTime(countOptions.getMaxTime(TimeUnit.SECONDS), TimeUnit.SECONDS);

        return this.database.getCollection(collName).estimatedDocumentCount(escountOptions);
    }

    @Override
    public <T> long countById(Query query, String collName, CountOptions countOptions) {
        log.info("{} execute", getClass() + ".count()");
        return this.database.getCollection(collName).countDocuments(query.getQueryObject(), countOptions.getOriginOptions());
    }


    @Override
    public <T> MongoCollection<Document> createCollection(Class<T> entityClass) {
        return createCollection(entityClass, CollectionOptions.empty());
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
    public <T> MongoCollection<Document> createCollection(Class<T> entityClass,
                                                          @Nullable CollectionOptions collectionOptions) {

        Assert.notNull(entityClass, "EntityClass must not be null!");

        CollectionOptions options = collectionOptions != null ? collectionOptions : CollectionOptions.empty();
        Document document = convertToDocument(options);

        //TODO it may contains some bug，so please use it carefully.
        CappedAt capped = (CappedAt) this.mapper.getEntityModel(entityClass).getAnnotation(CappedAt.class);

         if(!ObjectUtils.isEmpty(capped)) {
            document.put("capped", true);
            document.put("size", capped.value());
            document.put("max", capped.count());
        }

        /*
        if (!ObjectUtils.isEmpty(capped)) {
            document.put("collation", collation.value());
        }
        document.put("concern", annotation.concern());*/


        return doCreateCollection(this.mapper.getEntityModel(entityClass).getCollectionName(), document);
    }

    @Override
    public MongoCollection<Document> createCollection(String collectionName) {
        Assert.notNull(collectionName, "CollectionName must not be null!");

        return doCreateCollection(collectionName, new Document());
    }

    @Override
    public MongoCollection<Document> createCollection(String collectionName, CollectionOptions collectionOptions) {

        Assert.notNull(collectionName, "CollectionName must not be null!");
        return doCreateCollection(collectionName, convertToDocument(collectionOptions));
    }

    @Override
    public <T> void dropCollection(Class<T> entityClass) {
        dropCollection(this.mapper.getEntityModel(entityClass).getCollectionName());
    }

    @Override
    public void dropCollection(String collectionName) {
        Assert.notNull(collectionName, "CollectionName must not be null!");
        lock.lock();
        try {
            this.database.getCollection(collectionName).drop();
        } finally {
            lock.unlock();
        }

    }

    protected Document convertToDocument(@Nullable CollectionOptions collectionOptions) {
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

    @Nullable
    protected static com.mongodb.client.model.Collation fromDocument(@Nullable Document source) {

        if (source == null) {
            return null;
        }

        return Collation.from(source).toMongoCollation();
    }

    protected MongoCollection<Document> doCreateCollection(String collectionName, Document collectionOptions) {
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

            MongoCollection<Document> coll = database.getCollection(collectionName, Document.class);
            return coll;
        } finally {
            lock.unlock();
        }
    }


    public <T> MongoCollection<T>  withConcern(MongoCollection<T> collection ,Class<T>  clazz){

        EntityModel entityModel = this.mapper.getEntityModel(clazz);

        Concern annotation = (Concern)entityModel.getAnnotation(Concern.class);

        if(annotation!=null){

            if( WriteConcern.valueOf(annotation.writeConcern()) != null){
                collection = collection.withWriteConcern(WriteConcern.valueOf(annotation.writeConcern()));
            }

            if(ReadPreference.valueOf(annotation.readPreference())!=null){
                collection = collection.withWriteConcern(WriteConcern.valueOf(annotation.writeConcern()));

            }

            //todo  ReadConcern

        }
        
        return collection ;
        
    }

}
