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
package com.whaleal.mars.session.option;

import com.mongodb.WriteConcern;
import com.mongodb.client.model.Collation;
import org.bson.conversions.Bson;

import java.util.concurrent.TimeUnit;

/**
 * @author cx
 * @Date 2020/12/15
 *
 * @see com.whaleal.mars.core.index.annotation.IndexOptions
 *
 */
public class IndexOptions extends com.mongodb.client.model.IndexOptions implements WriteConfigurable<IndexOptions> {

    private WriteConcern writeConcern;

    private com.mongodb.client.model.IndexOptions originIndexOptions;

    public IndexOptions() {
        this.originIndexOptions = new com.mongodb.client.model.IndexOptions();

    }

    public IndexOptions(com.mongodb.client.model.IndexOptions originIndexOptions) {
        this.originIndexOptions = originIndexOptions;
    }


    public Integer getBits() {
        return originIndexOptions.getBits();
    }


    public Collation getCollation() {
        return originIndexOptions.getCollation();
    }


    public String getName() {
        return originIndexOptions.getName();
    }


    public Integer getVersion() {
        return originIndexOptions.getVersion();
    }


    public Double getBucketSize() {
        return originIndexOptions.getBucketSize();
    }


    public String getDefaultLanguage() {
        return originIndexOptions.getDefaultLanguage();
    }


    public Long getExpireAfter(TimeUnit timeUnit) {
        return originIndexOptions.getExpireAfter(timeUnit);
    }


    public String getLanguageOverride() {
        return originIndexOptions.getLanguageOverride();
    }


    public Double getMax() {
        return originIndexOptions.getMax();
    }


    public Double getMin() {
        return originIndexOptions.getMin();
    }


    public Bson getPartialFilterExpression() {
        return originIndexOptions.getPartialFilterExpression();
    }


    public Integer getSphereVersion() {
        return originIndexOptions.getSphereVersion();
    }


    public Bson getStorageEngine() {
        return originIndexOptions.getStorageEngine();
    }


    public Integer getTextVersion() {
        return originIndexOptions.getTextVersion();
    }


    public Bson getWeights() {
        return originIndexOptions.getWeights();
    }


    public Bson getWildcardProjection() {
        return originIndexOptions.getWildcardProjection();
    }

    public IndexOptions background(boolean background) {
        originIndexOptions.background(background);
        return this;
    }

    public IndexOptions unique(boolean unique) {
        originIndexOptions.unique(unique);
        return this;
    }

    public IndexOptions name(String name) {
        originIndexOptions.name(name);
        return this;
    }

    public IndexOptions sparse(boolean sparse) {
        originIndexOptions.sparse(sparse);
        return this;
    }

    public IndexOptions expireAfter(Long expireAfter, TimeUnit timeUnit) {
        originIndexOptions.expireAfter(expireAfter, timeUnit);
        return this;
    }

    public IndexOptions version(Integer version) {
        originIndexOptions.version(version);
        return this;
    }

    public IndexOptions weights(Bson weights) {
        originIndexOptions.weights(weights);
        return this;
    }

    public IndexOptions defaultLanguage(String defaultLanguage) {
        originIndexOptions.defaultLanguage(defaultLanguage);
        return this;
    }

    public IndexOptions languageOverride(String languageOverride) {
        originIndexOptions.languageOverride(languageOverride);
        return this;
    }

    public IndexOptions textVersion(Integer textVersion) {
        originIndexOptions.textVersion(textVersion);
        return this;
    }

    public IndexOptions sphereVersion(Integer sphereVersion) {
        originIndexOptions.sphereVersion(sphereVersion);
        return this;
    }

    public IndexOptions bits(Integer bits) {
        originIndexOptions.bits(bits);
        return this;
    }

    public IndexOptions min(Double min) {
        originIndexOptions.min(min);
        return this;
    }

    public IndexOptions max(Double max) {
        originIndexOptions.max(max);
        return this;
    }

    public IndexOptions bucketSize(Double bucketSize) {
        originIndexOptions.bucketSize(bucketSize);
        return this;
    }

    public IndexOptions storageEngine(Bson storageEngine) {
        originIndexOptions.storageEngine(storageEngine);
        return this;
    }

    public IndexOptions partialFilterExpression(Bson partialFilterExpression) {
        originIndexOptions.partialFilterExpression(partialFilterExpression);
        return this;
    }

    public IndexOptions collation(Collation collation) {
        originIndexOptions.collation(collation);
        return this;
    }

    public IndexOptions wildcardProjection(Bson wildcardProjection) {
        originIndexOptions.wildcardProjection(wildcardProjection);
        return this;
    }

    public IndexOptions hidden(boolean hidden) {
        originIndexOptions.hidden(hidden);
        return this;
    }

    @Override
    public IndexOptions writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Override
    public WriteConcern writeConcern() {
        return writeConcern;
    }

    @Override
    public com.mongodb.client.model.IndexOptions getOriginOptions() {
        if (originIndexOptions == null) {
            originIndexOptions = new com.mongodb.client.model.IndexOptions();
        }
        return originIndexOptions;
    }

    @Override
    public String toString() {
        return "IndexOptions{" +
                "writeConcern=" + writeConcern +
                ", originIndexOptions=" + originIndexOptions +
                '}';
    }
}
