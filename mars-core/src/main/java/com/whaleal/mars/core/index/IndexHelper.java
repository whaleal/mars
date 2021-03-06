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
package com.whaleal.mars.core.index;

import com.mongodb.client.MongoCollection;
import com.whaleal.icefrog.log.Log;
import com.whaleal.icefrog.log.LogFactory;
import com.whaleal.mars.codecs.MarsOrmException;
import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.EntityModel;
import com.whaleal.mars.core.index.annotation.Index;
import com.whaleal.mars.core.index.annotation.*;
import com.whaleal.mars.core.internal.PathTarget;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A helper class for dealing with index definitions
 * ?????? ???????????? Index ????????????????????????
 *
 *
 * @author wh
 *
 */
public final class IndexHelper {
    private static final Log log = LogFactory.get(IndexHelper.class);

    private final MongoMappingContext mapper ;

    public IndexHelper(MongoMappingContext mapper ){
        this.mapper  = mapper ;
    }

    private void calculateWeights(Index index, com.whaleal.mars.session.option.IndexOptions indexOptions) {
        Document weights = new Document();
        for (Field field : index.fields()) {
            if (field.weight() != -1) {
                if (field.type() != IndexDirection.TEXT) {
                    throw new MarsOrmException("Weight values only apply to text indexes: " + Arrays.toString(index.fields()));
                }
                weights.put(field.value(), field.weight());
            }
        }
        if (!weights.isEmpty()) {
            indexOptions.weights(weights);
        }
    }


    /**
     * @param entityModel
     * @param parentEntityModels
     * @return
     * ???????????????  ???????????? ??? ??? ??????
     * <p>
     * ???????????????????????? parentEntityModels  ???
     * ?????????????????????
     * ?????? ????????????
     */
    private List<Index> collectIndexes(EntityModel entityModel, List<EntityModel> parentEntityModels) {

        List<Index> indexes = collectTopLevelIndexes(entityModel);

        return indexes;
    }

    /**
     * @param entityModel
     * @return ???????????????????????????  ????????? EntityModel  ?????????
     * ????????????????????????????????? ???????????? ??????????????? ???????????? List
     * ??????????????? ????????????????????? ?????????????????? ??? ??????
     */
    private List<Index> collectTopLevelIndexes(EntityModel entityModel) {
        List<Index> list = new ArrayList<>();
        if (entityModel != null) {
            final Indexes indexes = (Indexes) entityModel.getAnnotation(Indexes.class);
            if (indexes != null) {
                for (Index index : indexes.value()) {
                    // ????????????
                    List<Field> fields = new ArrayList<>();
                    for (Field field : index.fields()) {
                        fields.add(new FieldBuilder()
                                .value(field.value())
                                .type(field.type())
                                .weight(field.weight()));
                    }

                    list.add(replaceFields(index, fields));
                }
            }

        }

        return list;
    }

    private Index replaceFields(Index original, List<Field> list) {
        return new IndexBuilder(original)
                .fields(list);
    }

    Document calculateKeys(EntityModel entityModel, Index index) {
        Document keys = new Document();
        for (Field field : index.fields()) {
            String path;
            try {
                path = findField(entityModel, index.options(), field.value());
            } catch (Exception e) {
                path = field.value();
                if (!index.options().disableValidation()) {
                    throw new MarsOrmException("error  with : " + path + entityModel.getType().getName());
                }
                log.warn("error  with : " + path + entityModel.getType().getName());
            }
            keys.putAll(new Document(path, field.type().toIndexValue()));
        }
        return keys;
    }


    /**
     * @param options
     * @return ??????????????????  IndexOption
     * ????????? ??? ??? ????????????
     * ?????? Mongodb?????????  IndexOption
     * 
     */
    com.whaleal.mars.session.option.IndexOptions convert(IndexOptions options) {
        com.whaleal.mars.session.option.IndexOptions indexOptions = new com.whaleal.mars.session.option.IndexOptions()
                .background(options.background())
                .sparse(options.sparse())
                .unique(options.unique());

        if (!options.language().equals("")) {
            indexOptions.defaultLanguage(options.language());
        }
        if (!options.languageOverride().equals("")) {
            indexOptions.languageOverride(options.languageOverride());
        }
        if (!options.name().equals("")) {
            indexOptions.name(options.name());
        }
        if (options.expireAfterSeconds() != -1) {
            indexOptions.expireAfter((long) options.expireAfterSeconds(), TimeUnit.SECONDS);
        }
        if (!options.partialFilter().equals("")) {
            indexOptions.partialFilterExpression(Document.parse(options.partialFilter()));
        }
        if (!options.collation().locale().equals("")) {
            indexOptions.collation(convert(options.collation()));
        }

        return indexOptions;
    }

    /**
     * @param collation
     * @return ?????????????????? Collation   ?????? ??????
     */
    com.mongodb.client.model.Collation convert(Collation collation) {
        return com.mongodb.client.model.Collation.builder()
                .locale(collation.locale())
                .backwards(collation.backwards())
                .caseLevel(collation.caseLevel())
                .collationAlternate(collation.alternate())
                .collationCaseFirst(collation.caseFirst())
                .collationMaxVariable(collation.maxVariable())
                .collationStrength(collation.strength())
                .normalization(collation.normalization())
                .numericOrdering(collation.numericOrdering())
                .build();
    }


    /**
     * ???????????? ????????????????????????????????????
     * @param collection  ?????????
     * @param entityModel ??????????????? ?????????????????????
     *                    ?????? collection  ??? ????????????
     *                    ??????????????????????????? ??????
     */
    public void createIndex(MongoCollection<?> collection, EntityModel entityModel) {
        /** ?????? EntityModel ???????????? */
        if (!entityModel.isInterface() && !entityModel.isAbstract()) {
            for (Index index : collectIndexes(entityModel, Collections.emptyList())) {
                createIndex(collection, entityModel, index);
            }
        }
    }

    /**
     * ???????????????????????????
     * ????????????????????????
     *
     * @param collection
     * @param entityModel
     * @param index
     */
    void createIndex(MongoCollection<?> collection, EntityModel entityModel, Index index) {

        /**?????? key */
        Document keys = calculateKeys(entityModel, index);
        /** ??????Option */
        com.whaleal.mars.session.option.IndexOptions indexOptions = convert(index.options());

        /**???????????? ??? text ?????? ????????? option */
        calculateWeights(index, indexOptions);

        /** ???????????? ?????? ??????*/
        collection.createIndex(keys, indexOptions.getOriginOptions());
    }

    String findField(EntityModel entityModel, IndexOptions options, String path) {
        if (path.equals("$**")) {
            return path;
        }

        return new PathTarget(mapper, entityModel, path, !options.disableValidation()).translatedPath();

    }
}
