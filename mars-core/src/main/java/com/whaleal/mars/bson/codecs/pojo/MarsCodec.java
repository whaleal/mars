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
package com.whaleal.mars.bson.codecs.pojo;

import com.whaleal.mars.bson.codecs.Conversions;
import com.whaleal.mars.bson.codecs.MarsOrmException;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MarsCodec<T> implements CollectibleCodec<T> {

    private final PropertyModel idProperty;
    private final MongoMappingContext mapper;
    private final EntityModel entityModel;
    private final CodecRegistry registry;
    private final PropertyCodecRegistry propertyCodecRegistry;
    private final DiscriminatorLookup discriminatorLookup;
    private EntityEncoder encoder;
    private EntityDecoder decoder;
    private List<PropertyCodecProvider> propertyCodecProviders;


    public MarsCodec(MongoMappingContext mapper, EntityModel model,
                     List<PropertyCodecProvider> propertyCodecProviders,
                     DiscriminatorLookup discriminatorLookup, CodecRegistry registry) {
        this.mapper = mapper;
        this.discriminatorLookup = discriminatorLookup;

        this.propertyCodecProviders = propertyCodecProviders;
        this.entityModel = model;
        this.registry = fromRegistries(fromCodecs(this), registry);
        this.propertyCodecRegistry = new PropertyCodecRegistryImpl(this, registry, propertyCodecProviders);
        idProperty = model.getIdProperty();
        specializePropertyCodecs();
        encoder = new EntityEncoder(this);
        decoder = new EntityDecoder(this);
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        return (T) getDecoder().decode(reader, decoderContext);
    }

    @Override
    public boolean documentHasId(Object entity) {
        PropertyModel idField = entityModel.getIdProperty();
        if (idField == null) {
            throw new MarsOrmException("idRequired(" + entity.getClass().getName() + ")");
        }
        return idField.getPropertyAccessor().get(entity) != null;
    }


    public EntityModel getEntityModel() {
        return entityModel;
    }

    public EntityEncoder getEncoder() {
        return encoder;
    }


    public MarsCodec<T> setEncoder(EntityEncoder encoder) {
        this.encoder = encoder;
        return this;
    }


    protected EntityDecoder getDecoder() {
        return decoder;
    }


    public void setDecoder(EntityDecoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public void encode(BsonWriter writer, Object value, EncoderContext encoderContext) {
        encoder.encode(writer, value, encoderContext);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class getEncoderClass() {
        return getEntityModel().getType();
    }

    @Override
    public Object generateIdIfAbsentFromDocument(Object entity) {
        if (!documentHasId(entity)) {
            idProperty.getPropertyAccessor().set(entity, Conversions.convert(new ObjectId(), idProperty.getType()));
        }
        return entity;
    }

    @Override
    public BsonValue getDocumentId(Object document) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void specializePropertyCodecs() {
        EntityModel entityModel = getEntityModel();

        for (Object property : entityModel.getPropertyModels()) {
            PropertyModel<?> propertyModel = (PropertyModel<?>) property;
            Codec codec = getPropertyModelCodec(propertyModel);
            if (codec != null) {
                propertyModel.cachedCodec(codec);

            }
        }
    }

    private synchronized <V> Codec<V> getPropertyModelCodec(PropertyModel<V> propertyModel) {
        Codec<V> codec = propertyModel.getCodec();
        if (codec == null) {
            Codec<V> localCodec = this.getCodecFromPropertyRegistry(propertyModel);
            if (localCodec instanceof MarsCodec) {
                MarsCodec<V> marsCodec = (MarsCodec) localCodec;
                EntityModel<V> specialized = this.getSpecializedEntityModel(marsCodec.getEntityModel(), propertyModel);
                localCodec = new MarsCodec<V>(this.mapper, specialized, this.propertyCodecProviders, marsCodec.getDiscriminatorLookup(), this.registry);
            }

            codec = (Codec) localCodec;
        }

        return codec;
    }


    private <V> EntityModel<T> getSpecializedEntityModel(EntityModel<T> entityModel, PropertyModel<V> propertyModel) {
        boolean useDiscriminator = propertyModel.useDiscriminator() == null ? entityModel.useDiscriminator() : propertyModel.useDiscriminator();
        boolean validDiscriminator = entityModel.getDiscriminatorKey() != null && entityModel.getDiscriminator() != null;
        boolean changeTheDiscriminator = useDiscriminator != entityModel.useDiscriminator() && validDiscriminator;
        if (propertyModel.getTypeData().getTypeParameters().isEmpty() && !changeTheDiscriminator) {
            return entityModel;
        } else {
            ArrayList<PropertyModel<?>> concretePropertyModels = new ArrayList(entityModel.getPropertyModels());
            PropertyModel<?> concreteIdProperty = entityModel.getIdProperty();
            List<TypeData<?>> propertyTypeParameters = propertyModel.getTypeData().getTypeParameters();

            for (int i = 0; i < concretePropertyModels.size(); ++i) {
                PropertyModel<?> model = (PropertyModel) concretePropertyModels.get(i);
                String propertyName = model.getName();
                TypeParameterMap typeParameterMap = (TypeParameterMap) entityModel.getPropertyNameToTypeParameterMap().get(propertyName);
                if (typeParameterMap.hasTypeParameters()) {
                    PropertyModel<?> concretePropertyModel = this.getSpecializedPropertyModel(model, propertyTypeParameters, typeParameterMap);
                    concretePropertyModels.set(i, concretePropertyModel);
                    if (concreteIdProperty != null && concreteIdProperty.getName().equals(propertyName)) {
                        concreteIdProperty = concretePropertyModel;
                    }
                }
            }

            boolean discriminatorEnabled = changeTheDiscriminator ? propertyModel.useDiscriminator() : entityModel.useDiscriminator();
            return new EntityModel(entityModel.getType(), entityModel.getPropertyNameToTypeParameterMap(), entityModel.getInstanceCreatorFactory(), discriminatorEnabled, entityModel.getDiscriminatorKey(), entityModel.getDiscriminator(), IdPropertyModelHolder.create(entityModel, concreteIdProperty), concretePropertyModels);
        }
    }


    private <V> PropertyModel<V> getSpecializedPropertyModel(PropertyModel<V> propertyModel, List<TypeData<?>> propertyTypeParameters, TypeParameterMap typeParameterMap) {
        TypeData<V> specializedPropertyType = MarsSpecializationHelper.specializeTypeData(propertyModel.getTypeData(), propertyTypeParameters, typeParameterMap);
        return propertyModel.getTypeData().equals(specializedPropertyType) ? propertyModel : new PropertyModel(propertyModel.getName(), propertyModel.getReadName(), propertyModel.getWriteName(), specializedPropertyType, (Codec) null, propertyModel.getPropertySerialization(), propertyModel.useDiscriminator(), propertyModel.getPropertyAccessor(), propertyModel.getError(), propertyModel.getBsonRepresentation());
    }


    private <V> Codec<V> getCodecFromPropertyRegistry(PropertyModel<V> propertyModel) {
        Object localCodec;
        try {
            localCodec = this.propertyCodecRegistry.get(propertyModel.getTypeData());
        } catch (CodecConfigurationException var4) {
            return new LazyMissingCodec(propertyModel.getTypeData().getType(), var4);
        }

        if (localCodec == null) {
            localCodec = new LazyMissingCodec(propertyModel.getTypeData().getType(), new CodecConfigurationException("Unexpected missing codec for: " + propertyModel.getName()));
        }

        BsonType representation = propertyModel.getBsonRepresentation();
        if (representation != null) {
            if (localCodec instanceof RepresentationConfigurable) {
                return ((RepresentationConfigurable) localCodec).withRepresentation(representation);
            } else {
                throw new CodecConfigurationException("Codec must implement RepresentationConfigurable to support BsonRepresentation  in  Mars");
            }
        } else {
            return (Codec) localCodec;
        }
    }


    DiscriminatorLookup getDiscriminatorLookup() {
        return discriminatorLookup;
    }

    CodecRegistry getRegistry() {
        return registry;
    }


    public MongoMappingContext getMapper() {
        return mapper;
    }
}
