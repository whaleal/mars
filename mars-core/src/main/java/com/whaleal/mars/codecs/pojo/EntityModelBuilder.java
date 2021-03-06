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
package com.whaleal.mars.codecs.pojo;


import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.mars.codecs.Convention;
import com.whaleal.mars.codecs.pojo.annotations.Entity;
import com.whaleal.mars.codecs.pojo.annotations.PropIgnore;
import org.bson.codecs.pojo.IdGenerator;

import java.lang.annotation.Annotation;
import java.util.*;

import static com.whaleal.icefrog.core.lang.Precondition.notNull;
import static java.lang.String.format;
import static java.util.Collections.*;


public class EntityModelBuilder<T> {
    //  _id ???????????????
    static final String ID_PROPERTY_NAME = "_id";
    //  ????????????
    private final List<PropertyModelBuilder<?>> propertyModelBuilders = new ArrayList<PropertyModelBuilder<?>>();
    //  id ????????? ????????????????????????
    // ???????????????
    private IdGenerator<?> idGenerator;
    private InstanceCreatorFactory<T> instanceCreatorFactory;
    //  type
    private Class<T> type;
    private Map<String, TypeParameterMap> propertyNameToTypeParameterMap = emptyMap();
    private List<Convention> conventions = Conventions.DEFAULT_CONVENTIONS;
    //  ?????????????????????
    private List<Annotation> annotations = emptyList();
    //discriminator  ????????????
    private boolean discriminatorEnabled;
    private String discriminator;
    private String discriminatorKey;

    //  id  ???????????????
    private String idPropertyName;

    //  ?????????????????????
    private String collectionName;


    public EntityModelBuilder(final Class<T> type) {
        MarsBuilderHelper.configureClassModelBuilder(this, notNull("type", type));
        initCollectionName();
    }


    public String getCollectionName() {
        return collectionName;
    }


    /**
     * ?????? ??????????????????Id ????????????
     * @param idGenerator
     * @return EntityModelBuilder  this
     */
    public EntityModelBuilder<T> idGenerator(final IdGenerator<?> idGenerator) {
        this.idGenerator = idGenerator;
        return this;
    }


    public IdGenerator<?> getIdGenerator() {
        return idGenerator;
    }


    public EntityModelBuilder<T> instanceCreatorFactory(final InstanceCreatorFactory<T> instanceCreatorFactory) {
        this.instanceCreatorFactory = notNull("instanceCreatorFactory", instanceCreatorFactory);
        return this;
    }


    public InstanceCreatorFactory<T> getInstanceCreatorFactory() {
        return instanceCreatorFactory;
    }


    /**
     * type  ??????
     * @param type
     * @return
     */
    public EntityModelBuilder<T> type(final Class<T> type) {
        this.type = notNull("type", type);
        return this;
    }


    public Class<T> getType() {
        return type;
    }


    public EntityModelBuilder<T> conventions(final List<Convention> conventions) {
        this.conventions = notNull("conventions", conventions);
        return this;
    }


    public List<Convention> getConventions() {
        return conventions;
    }


    public EntityModelBuilder<T> annotations(final List<Annotation> annotations) {
        this.annotations = notNull("annotations", annotations);
        return this;
    }


    public List<Annotation> getAnnotations() {
        return annotations;
    }


    public EntityModelBuilder<T> discriminator(final String discriminator) {
        this.discriminator = discriminator;
        return this;
    }


    public String getDiscriminator() {
        return discriminator;
    }


    public EntityModelBuilder<T> discriminatorKey(final String discriminatorKey) {
        this.discriminatorKey = discriminatorKey;
        return this;
    }

    public String getDiscriminatorKey() {
        return discriminatorKey;
    }

    public EntityModelBuilder<T> enableDiscriminator(final boolean discriminatorEnabled) {
        this.discriminatorEnabled = discriminatorEnabled;
        return this;
    }

    public Boolean useDiscriminator() {
        return discriminatorEnabled;
    }

    public EntityModelBuilder<T> idPropertyName(final String idPropertyName) {
        this.idPropertyName = idPropertyName;
        return this;
    }


    public String getIdPropertyName() {
        return idPropertyName;
    }


    public boolean removeProperty(final String propertyName) {
        return propertyModelBuilders.remove(getProperty(notNull("propertyName", propertyName)));
    }


    public PropertyModelBuilder<?> getProperty(final String propertyName) {
        notNull("propertyName", propertyName);
        for (PropertyModelBuilder<?> propertyModelBuilder : propertyModelBuilders) {
            if (propertyModelBuilder.getName().equals(propertyName)) {
                return propertyModelBuilder;
            }
        }
        return null;
    }


    public List<PropertyModelBuilder<?>> getPropertyModelBuilders() {
        return Collections.unmodifiableList(propertyModelBuilders);
    }


    public EntityModel<T> build() {

        return new EntityModel<T>(this);
    }

    @Override
    public String toString() {
        return format("EntityModelBuilder{type=%s}", type);
    }

    Map<String, TypeParameterMap> getPropertyNameToTypeParameterMap() {
        return propertyNameToTypeParameterMap;
    }

    EntityModelBuilder<T> propertyNameToTypeParameterMap(final Map<String, TypeParameterMap> propertyNameToTypeParameterMap) {
        this.propertyNameToTypeParameterMap = unmodifiableMap(new HashMap<String, TypeParameterMap>(propertyNameToTypeParameterMap));
        return this;
    }

    EntityModelBuilder<T> addProperty(final PropertyModelBuilder<?> propertyModelBuilder) {
        boolean flag = false;
        for (Annotation annotation : propertyModelBuilder.getWriteAnnotations()){
            if(PropIgnore.class.equals(annotation.annotationType())){
                flag = true;
            }
        }
        if(!flag){
            propertyModelBuilders.add(notNull("propertyModelBuilder", propertyModelBuilder));
        }
        return this;
    }


    private void initCollectionName() {

        Entity anno = null;

        for (Annotation annotation : annotations) {
            if (Entity.class.equals(annotation.annotationType())) {
                anno = Entity.class.cast(annotation);
                break;
            }
        }
        if (anno == null) {
            this.collectionName = StrUtil.uncapitalize(type.getSimpleName());
            return;
        } else if ("".equals(anno.value())) {
            this.collectionName = StrUtil.uncapitalize(type.getSimpleName());
            return;
        } else {
            this.collectionName = anno.value();
        }

    }


}
