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



import com.whaleal.mars.codecs.Convention;
import org.bson.codecs.configuration.CodecConfigurationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;

/**
 * 实体关系模型
 * 1。有属性
 * 2。属性：private 必须提供getter setter；
 *          public 可以不提供
 * 3。没有属性 以getter setter为准
 *
 *
 * @param <T>
 */
public final class EntityModel<T> {
    private final String name;
    private final Class<T> type;
    private final boolean hasTypeParameters;
    private final InstanceCreatorFactory<T> instanceCreatorFactory;
    //document与实体类的映射关系，主要用于补充说明
    private final boolean discriminatorEnabled;
    private final String discriminatorKey;
    //package+className
    private final String discriminator;

    //id holder
    private final IdPropertyModelHolder<?> idPropertyModelHolder;
    //  todo 后期是否修改为 LinkMap
    //无论属性有没有值 都会遍历一遍
    private final List<PropertyModel<?>> propertyModels;

    private final Map<String, TypeParameterMap> propertyNameToTypeParameterMap;

    private PropertyModel<?> IdProperty;


    private String idPropertyName;

    public String getCollectionName() {
        return collectionName;
    }

    private String collectionName;

    //  classModel  是否应该配置一个codec

    private List<Annotation> annotations;

    //子类的保存
    private final List<EntityModel> subtypes = new ArrayList<>();


    EntityModel(EntityModelBuilder<T> entityModelBuilder) {


        List<PropertyModel<?>> propertyModels = new ArrayList<PropertyModel<?>>();

        this.idPropertyName = entityModelBuilder.getIdPropertyName();


        MarsBuilderHelper.stateNotNull("type", entityModelBuilder.getType());
        for (Convention convention : entityModelBuilder.getConventions()) {
            convention.apply(entityModelBuilder);

        }

        MarsBuilderHelper.stateNotNull("instanceCreatorFactory", entityModelBuilder.getInstanceCreatorFactory());
        if (entityModelBuilder.useDiscriminator()) {
            MarsBuilderHelper.stateNotNull("discriminatorKey", entityModelBuilder.getDiscriminatorKey());
            MarsBuilderHelper.stateNotNull("discriminator", entityModelBuilder.getDiscriminator());
        }

        for (PropertyModelBuilder<?> propertyModelBuilder : entityModelBuilder.getPropertyModelBuilders()) {
            boolean isIdProperty = propertyModelBuilder.getName().equals(entityModelBuilder.getIdPropertyName());
            if (isIdProperty) {
                propertyModelBuilder.readName(EntityModelBuilder.ID_PROPERTY_NAME).writeName(EntityModelBuilder.ID_PROPERTY_NAME);
            }

            PropertyModel<?> model = propertyModelBuilder.build();

            propertyModels.add(model);
            if (isIdProperty) {
                IdProperty = model;
            }
        }

        if(IdProperty == null){
            //  重新处理 所有的 Models
            propertyModels = new ArrayList<>();
            for (PropertyModelBuilder<?> propertyModelBuilder : entityModelBuilder.getPropertyModelBuilders()) {
                boolean isIdProperty = propertyModelBuilder.getWriteName().equals(entityModelBuilder.getIdPropertyName()) && propertyModelBuilder.getReadName().equals(entityModelBuilder.getIdPropertyName());
                if (isIdProperty) {
                    propertyModelBuilder.readName(EntityModelBuilder.ID_PROPERTY_NAME).writeName(EntityModelBuilder.ID_PROPERTY_NAME);
                }

                PropertyModel<?> model = propertyModelBuilder.build();


                propertyModels.add(model);
                if (isIdProperty) {
                    IdProperty = model;
                }
            }
        }



        //  在这个阶段过后  仍然会存在以下情况
        //  实体对象异常   例如没有 @Id 注解 没有这个字段
        //  需要针对该Id  字段进行额外处理



        validatePropertyModels(entityModelBuilder.getType().getSimpleName(), propertyModels);


        this.name = entityModelBuilder.getType().getSimpleName();
        this.type = entityModelBuilder.getType();
        this.hasTypeParameters = entityModelBuilder.getType().getTypeParameters().length > 0;
        this.propertyNameToTypeParameterMap = Collections.unmodifiableMap(
                new HashMap<String, TypeParameterMap>(entityModelBuilder.getPropertyNameToTypeParameterMap()));
        this.instanceCreatorFactory = entityModelBuilder.getInstanceCreatorFactory();
        this.discriminatorEnabled = entityModelBuilder.useDiscriminator();
        this.discriminatorKey = entityModelBuilder.getDiscriminatorKey();
        this.discriminator = entityModelBuilder.getDiscriminator();

        this.propertyModels = unmodifiableList(new ArrayList<PropertyModel<?>>(unmodifiableList(unmodifiableList(propertyModels))));

        this.collectionName = entityModelBuilder.getCollectionName();
        this.annotations = entityModelBuilder.getAnnotations();
        //基于对象生成的要放在后面
        this.idPropertyModelHolder = IdPropertyModelHolder.create(entityModelBuilder.getType(), IdProperty, entityModelBuilder.getIdGenerator());


    }




    private void validatePropertyModels(final String declaringClass, final List<PropertyModel<?>> propertyModels) {

        Map<String, Integer> propertyNameMap = new HashMap<String, Integer>();
        Map<String, Integer> propertyReadNameMap = new HashMap<String, Integer>();
        Map<String, Integer> propertyWriteNameMap = new HashMap<String, Integer>();

        for (PropertyModel<?> propertyModel : propertyModels) {
            if (propertyModel.hasError()) {
                throw new CodecConfigurationException(propertyModel.getError());
            }
            checkForDuplicates("property", propertyModel.getName(), propertyNameMap, declaringClass);
            if (propertyModel.isReadable()) {
                checkForDuplicates("read property", propertyModel.getReadName(), propertyReadNameMap, declaringClass);
            }
            if (propertyModel.isWritable()) {
                checkForDuplicates("write property", propertyModel.getWriteName(), propertyWriteNameMap, declaringClass);
            }
        }

        if (idPropertyName != null && !propertyNameMap.containsKey(idPropertyName)) {
            throw new CodecConfigurationException(format("Invalid id property, property named '%s' can not be found.", idPropertyName));
        }
    }


    private void checkForDuplicates(final String propertyType, final String propertyName, final Map<String, Integer> propertyNameMap,
                                    final String declaringClass) {
        if (propertyNameMap.containsKey(propertyName)) {
            throw new CodecConfigurationException(format("Duplicate %s named '%s' found in %s.", propertyType, propertyName,
                    declaringClass));
        }
        propertyNameMap.put(propertyName, 1);
    }


    EntityModel(final Class<T> clazz, final Map<String, TypeParameterMap> propertyNameToTypeParameterMap,
                final InstanceCreatorFactory<T> instanceCreatorFactory, final Boolean discriminatorEnabled, final String discriminatorKey,
                final String discriminator, final IdPropertyModelHolder<?> idPropertyModelHolder,
                final List<PropertyModel<?>> propertyModels) {
        this.name = clazz.getSimpleName();
        this.type = clazz;
        this.hasTypeParameters = clazz.getTypeParameters().length > 0;
        this.propertyNameToTypeParameterMap = Collections.unmodifiableMap(
                new HashMap<String, TypeParameterMap>(propertyNameToTypeParameterMap));
        this.instanceCreatorFactory = instanceCreatorFactory;
        this.discriminatorEnabled = discriminatorEnabled;
        this.discriminatorKey = discriminatorKey;
        this.discriminator = discriminator;
        this.idPropertyModelHolder = idPropertyModelHolder;
        this.propertyModels = unmodifiableList(new ArrayList<PropertyModel<?>>(propertyModels));
    }


    public static <S> EntityModelBuilder<S> builder(final Class<S> type) {
        return new EntityModelBuilder<S>(type);
    }


    MarsInstanceCreator<T> getInstanceCreator() {
        return instanceCreatorFactory.create();
    }


    public Class<T> getType() {
        return type;
    }


    public boolean isInterface() {
        return getType().isInterface();
    }


    public boolean isAbstract() {
        return Modifier.isAbstract(getType().getModifiers());
    }

    public boolean hasTypeParameters() {
        return hasTypeParameters;
    }

    public boolean useDiscriminator() {
        return discriminatorEnabled;
    }

    public String getDiscriminatorKey() {
        return discriminatorKey;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public PropertyModel<?> getPropertyModel(final String propertyName) {
        for (PropertyModel<?> propertyModel : propertyModels) {
            if (propertyModel.getName().equals(propertyName)) {
                return propertyModel;
            }
        }
        return null;
    }

    public PropertyModel<?> getPropertyModelFromReadName(final String propertyName) {
        for (PropertyModel<?> propertyModel : propertyModels) {
            if (propertyModel.getReadName().equals(propertyName)) {
                return propertyModel;
            }
        }
        return null;
    }


    public PropertyModel<?> getPropertyModelFromWriteName(final String propertyName) {
        for (PropertyModel<?> propertyModel : propertyModels) {
            if (propertyModel.getWriteName().equals(propertyName)) {
                return propertyModel;
            }
        }
        return null;
    }


    public List<PropertyModel<?>> getPropertyModels() {
        return propertyModels;
    }


    public PropertyModel<?> getIdProperty() {
        return idPropertyModelHolder != null ? idPropertyModelHolder.getPropertyModel() : null;
    }

    IdPropertyModelHolder<?> getIdPropertyModelHolder() {
        return idPropertyModelHolder;
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EntityModel{"
                + "type=" + type
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntityModel<?> that = (EntityModel<?>) o;

        if (discriminatorEnabled != that.discriminatorEnabled) {
            return false;
        }
        if (!getType().equals(that.getType())) {
            return false;
        }
        if (!getInstanceCreatorFactory().equals(that.getInstanceCreatorFactory())) {
            return false;
        }
        if (getDiscriminatorKey() != null ? !getDiscriminatorKey().equals(that.getDiscriminatorKey())
                : that.getDiscriminatorKey() != null) {
            return false;
        }
        if (getDiscriminator() != null ? !getDiscriminator().equals(that.getDiscriminator()) : that.getDiscriminator() != null) {
            return false;
        }
        if (idPropertyModelHolder != null ? !idPropertyModelHolder.equals(that.idPropertyModelHolder)
                : that.idPropertyModelHolder != null) {
            return false;
        }
        if (!getPropertyModels().equals(that.getPropertyModels())) {
            return false;
        }
        if (!getPropertyNameToTypeParameterMap().equals(that.getPropertyNameToTypeParameterMap())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getInstanceCreatorFactory().hashCode();
        result = 31 * result + (discriminatorEnabled ? 1 : 0);
        result = 31 * result + (getDiscriminatorKey() != null ? getDiscriminatorKey().hashCode() : 0);
        result = 31 * result + (getDiscriminator() != null ? getDiscriminator().hashCode() : 0);
        result = 31 * result + (getIdPropertyModelHolder() != null ? getIdPropertyModelHolder().hashCode() : 0);
        result = 31 * result + getPropertyModels().hashCode();
        result = 31 * result + getPropertyNameToTypeParameterMap().hashCode();
        return result;
    }

    InstanceCreatorFactory<T> getInstanceCreatorFactory() {
        return instanceCreatorFactory;
    }

    Map<String, TypeParameterMap> getPropertyNameToTypeParameterMap() {
        return propertyNameToTypeParameterMap;
    }


    public <A extends Annotation> A getAnnotation(Class<A> type) {

        for (Annotation annotation : annotations) {
            if (type.equals(annotation.annotationType())) {
                return (A) type.cast(annotation);
            }
        }
        return null;

    }



}
