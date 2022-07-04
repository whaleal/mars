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
package com.whaleal.mars.codecs;

import com.mongodb.DBObjectCodecProvider;
import com.mongodb.DBRefCodecProvider;
import com.mongodb.DocumentToDBRefTransformer;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider;
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import com.whaleal.mars.codecs.internal.JsonObjectCodecProvider;

import com.whaleal.mars.codecs.pojo.*;
import com.whaleal.mars.codecs.pojo.EnumCodecProvider;
import com.whaleal.mars.codecs.pojo.MarsCodecProvider;
import com.whaleal.mars.codecs.pojo.PropertyModel;
import com.whaleal.mars.codecs.pojo.annotations.Concern;
import com.whaleal.mars.codecs.pojo.annotations.Discriminator;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.reader.DocumentReader;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.codecs.AggregationCodecProvider;

import com.whaleal.mars.core.internal.NotMappableException;
import org.bson.Document;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.Jsr310CodecProvider;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

/**
 * MongoDB  的 Mapping  工具
 * <p>
 * <p>
 * 需要将原来的   的功能转移到这里来
 * 自动注入 相关属性会注入到这个相关属性中
 */
public class MongoMappingContext {


    public static final String IGNORED_FIELDNAME = ".";

    private final Map<Class, EntityModel> mappedEntities = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<EntityModel>> mappedEntitiesByCollection = new ConcurrentHashMap<>();

    //EntityInterceptors; these are called after EntityListeners and lifecycle methods on an Entity, for all Entities
    //private final List<EntityInterceptor> interceptors = new LinkedList<>();
    //private final MapperOptions options;
    private final DiscriminatorLookup discriminatorLookup = new DiscriminatorLookup(Collections.emptyMap(), Collections.emptySet());
    private final MarsCodecProvider marsCodecProvider;
    //
    private final CodecRegistry codecRegistry;
    // 命名策略 保留状态  todo  转为实体直接保存 。并预先设置相关策略 。
    // 相关 name strategy 需要设计 ，并通过反射方式 生成该bean
    private Class< ? > strategyClass;

    public MongoDatabase getDatabase() {
        return database;
    }

    private final MongoDatabase database;
    private final DateStorage dateStorage = DateStorage.UTC;


    //所有扫描到的带有@Entity的类的集合
    private Set<? extends Class<?>> initialEntitySet;

    public void setInitialEntitySet(Set<? extends Class<?>> initialEntitySet) {
        this.initialEntitySet = initialEntitySet;
    }


    public Set<? extends Class<?>> getInitialEntitySet(){
        return initialEntitySet;
    }

    //是否开启自动创建注解
    private boolean autoIndexCreation = false;

    public MongoMappingContext( MongoDatabase database ) {
        this.database = database;

        CodecRegistry codecRegistry = fromProviders(
                new com.whaleal.mars.codecs.internal.ValueCodecProvider(),

                new BsonValueCodecProvider(),
                new DBRefCodecProvider(),
                new DBObjectCodecProvider(),
                new DocumentCodecProvider(new DocumentToDBRefTransformer()),
                new IterableCodecProvider(new DocumentToDBRefTransformer()),
                new com.whaleal.mars.codecs.internal.MapCodecProvider(),
                new GeoJsonCodecProvider(),
                new GridFSFileCodecProvider(),
                new Jsr310CodecProvider(),
                new JsonObjectCodecProvider(),
                new BsonCodecProvider()
        );


        marsCodecProvider = new MarsCodecProvider(this);
        this.codecRegistry = fromProviders(

                new MarsTypesCodecProvider(this),
                new PrimitiveCodecRegistry(codecRegistry),
                new EnumCodecProvider(),
                new AggregationCodecProvider(this),

                codecRegistry,
                marsCodecProvider


        );

//        this.autoIndexCreation = isAutoIndexCreation();

    }

    /**
     * Returns whether auto-index creation is enabled or disabled. <br />
     * <strong>NOTE:</strong>Index creation should happen at a well-defined time that is ideally controlled by the
     * application itself.
     *
     * @return {@literal true} when auto-index creation is enabled;
     */
    public boolean isAutoIndexCreation() {
        return autoIndexCreation;
    }

    /**
     * Enables/disables auto-index creation. <br />
     * <strong>NOTE:</strong>Index creation should happen at a well-defined time that is ideally controlled by the
     * application itself.
     *
     * @param autoCreateIndexes set to {@literal true} to enable auto-index creation.
     *
     */
    public void setAutoIndexCreation(boolean autoCreateIndexes) {
        this.autoIndexCreation = autoCreateIndexes;
    }


    public <T> PropertyModel findIdProperty( Class<?> type) {
        EntityModel entityModel = getEntityModel(type);
        PropertyModel idField = entityModel.getIdProperty();

        if (idField == null) {
            throw new NotMappableException("idRequired(type.getName())");
        }
        return idField;
    }


    public List<EntityModel> map(Class... entityClasses) {
        return map(Arrays.asList(entityClasses));
    }


    public MongoCollection enforceWriteConcern(MongoCollection collection, Class type) {
        WriteConcern applied = getWriteConcern(type);
        return applied != null
                ? collection.withWriteConcern(applied)
                : collection;
    }


    public <T> T fromDocument(Class<T> type, Document document) {
        Class<T> aClass = type;


        CodecRegistry codecRegistry = getCodecRegistry();

        DocumentReader reader = new DocumentReader(document);

        return codecRegistry
                .get(aClass)
                .decode(reader, DecoderContext.builder().build());
    }


    @SuppressWarnings("unchecked")

    public <T> Class<T> getClass(Document document) {
        // see if there is a className value
        Class c = null;
        String discriminator = (String) document.get("_t");
        if (discriminator != null) {
            c = getClass(discriminator);
        }
        return c;
    }


    public Class getClass(String discriminator) {
        return discriminatorLookup.lookup(discriminator);
    }


    public <T> Class<T> getClassFromCollection(String collection) {
        final List<EntityModel> classes = getClassesMappedToCollection(collection);

        return (Class<T>) classes.get(0).getType();
    }


    public List<EntityModel> getClassesMappedToCollection(String collection) {
        final Set<EntityModel> entities = mappedEntitiesByCollection.get(collection);
        if (entities == null || entities.isEmpty()) {
            throw new NotMappableException("collectionNotMapped(collection)");
        }
        return new ArrayList<>(entities);
    }


    public CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }

    public DiscriminatorLookup getDiscriminatorLookup() {
        return discriminatorLookup;
    }

    public List<EntityModel> map(List<Class> classes) {
        for (Class type : classes) {
            if (!isMappable(type)) {
                throw new NotMappableException("entityOrEmbedded(" + type.getName() + ")");
            }
        }
        return classes.stream()
                .map(this::getEntityModel)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    public EntityModel getEntityModel(Class<?> type) {

        EntityModel model = mappedEntities.get(type);

        if (model == null) {
            /*if (!isMappable(type)) {
                throw new NotMappableException(type);
            }*/
            model = register(createEntityModel(type));
            if (model == null) {
                throw new NotMappableException(type);
            }
        }

        return model;
    }


    public Object getId( Object entity ) {
        if (entity == null) {
            return null;
        }
        try {
            final EntityModel model = getEntityModel(entity.getClass());
            final PropertyModel idField = model.getIdProperty();
            if (idField != null) {

                return idField.getPropertyAccessor().get(entity);
            }
        } catch (NotMappableException ignored) {
        }

        return null;
    }


    public List<EntityModel> getMappedEntities() {
        return new ArrayList<>(mappedEntities.values());
    }


    public WriteConcern getWriteConcern(Class clazz) {
        WriteConcern wc = null;
        EntityModel entityModel = getEntityModel(clazz);
        if (entityModel != null) {
            final Concern entityAnn = (Concern) entityModel.getAnnotation(Concern.class);
            if (entityAnn != null && !entityAnn.writeConcern().isEmpty()) {
                try {
                    wc = WriteConcern.valueOf(entityAnn.writeConcern());
                } catch (Exception e) {

                }

            }
        }

        return wc;
    }


    public <T> boolean isMappable(Class<T> type) {
        final Class actual = type;
        return hasAnnotation(actual, Entity.class);
    }

    public boolean isMapped(Class c) {
        return mappedEntities.containsKey(c);
    }


    public <A extends Annotation> EntityModel mapExternal( A annotation, Class type ) {
        final Class actual = type;
        EntityModel model = mappedEntities.get(actual);

        if (model == null) {
            if (annotation == null) {

                throw new IllegalArgumentException("annotation  must with Entity.class");
            }
            model = register(createEntityModel(type, annotation));
        }


        return model;
    }


    private <T> EntityModel createEntityModel(Class<T> clazz) {
        return new EntityModelBuilder(clazz)
                .build();
    }

    private <T, A extends Annotation> EntityModel createEntityModel(Class<T> clazz, A annotation) {
        return new EntityModelBuilder(clazz).annotations(Arrays.asList(annotation))
                .build();
    }


    public Document toDocument(Object entity) {
        final EntityModel entityModel = getEntityModel(entity.getClass());

        DocumentWriter writer = new DocumentWriter();
        ((Codec) getCodecRegistry().get(entityModel.getType()))
                .encode(writer, entity, EncoderContext.builder().build());

        return writer.getDocument();
    }


    public void updateQueryWithDiscriminators(EntityModel model, Document query) {
        Discriminator annotation = (Discriminator) model.getAnnotation(Discriminator.class);
        if (annotation != null && annotation.useDiscriminator()
                && !query.containsKey("_id")
                && !query.containsKey(model.getDiscriminatorKey())) {
            List<String> values = new ArrayList<>();
            values.add(model.getDiscriminator());

            query.put(model.getDiscriminatorKey(),
                    new Document("$in", values));
        }
    }


    private String discriminatorKey(Class<?> type) {
        return mappedEntities.get(type)
                .getDiscriminatorKey();
    }

    private <T> boolean hasAnnotation(Class<T> clazz, Class<? extends Annotation> annotation) {
        {
            if (clazz.getAnnotation(annotation) != null) {
                return true;
            }
        }

        return clazz.getSuperclass() != null && hasAnnotation(clazz.getSuperclass(), annotation)
                || Arrays.stream(clazz.getInterfaces())
                .map(i -> hasAnnotation(i, annotation))
                .reduce(false, (l, r) -> l || r);
    }

    private EntityModel register(EntityModel entityModel) {
        discriminatorLookup.addModel(entityModel);
        mappedEntities.put(entityModel.getType(), entityModel);
        if (entityModel.getCollectionName() != null) {
            mappedEntitiesByCollection.computeIfAbsent(entityModel.getCollectionName(), s -> new CopyOnWriteArraySet<>())
                    .add(entityModel);
        }

        if (!entityModel.isInterface()) {

            //todo some check
        }

        return entityModel;
    }


    public String determineCollectionName( EntityModel model, String collectionName ) {

        if (collectionName != null) {
            return collectionName;
        } else {
            return model.getCollectionName();
        }

    }

    public String determineCollectionName(Class<?> clazz, String collectionName) {
        if (collectionName != null) {
            return collectionName;
        } else {
            return getEntityModel(clazz).getCollectionName();
        }
    }


    public void setNamingStrategy( Class<?> strategyClass ) {
        this.strategyClass = strategyClass ;
    }

    public DateStorage getDateStorage() {
        return dateStorage;
    }

    public MongoMappingContext getMapper() {
        return this;
    }
}
