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
 * only overwrite the  set  Method
 * the get fuction  always  use it's parent fuction
 * @mongodb.driver.manual reference/command/createIndexes Index options
 * see  https://www.mongodb.com/docs/v6.0/reference/method/db.collection.createIndex/#std-label-ensureIndex-options
 * 索引明细  主要分为以下几类
 * 通用类型
 *
 *    storageEngine
 *    background
 *    unique
 *    name
 *    partialFilterExpression
 *    sparse
 *    expireAfterSeconds
 *    hidden
 *
 *
 * Collation
 *    locale: <string>,
 *    caseLevel: <boolean>,
 *    caseFirst: <string>,
 *    strength: <int>,
 *    numericOrdering: <boolean>,
 *    alternate: <string>,
 *    maxVariable: <string>,
 *    backwards: <boolean>
 *    normalization: <boolean>
 * Text
 *  weights
 *  default_language
 *  language_override
 *  textIndexVersion
 * 2dsphere
 *  2dsphereIndexVersion
 * 2d
 *  bit
 *  min
 *  max
 * geoHaystack
 *  bucketSize
 * wildcard
 *  wildcardProjection
 *
 */
public class IndexOptions extends com.mongodb.client.model.IndexOptions implements WriteConfigurable<IndexOptions> {

    private WriteConcern writeConcern;

    public IndexOptions() {
        super();

    }

    public IndexOptions background(boolean background) {
        super.background(background);
        return this;
    }

    public IndexOptions unique(boolean unique) {
        super.unique(unique);
        return this;
    }

    public IndexOptions name(String name) {
        super.name(name);
        return this;
    }

    public IndexOptions sparse(boolean sparse) {
        super.sparse(sparse);
        return this;
    }

    public IndexOptions expireAfter(Long expireAfter, TimeUnit timeUnit) {
        super.expireAfter(expireAfter, timeUnit);
        return this;
    }

    public IndexOptions version(Integer version) {
        super.version(version);
        return this;
    }

    public IndexOptions weights(Bson weights) {
        super.weights(weights);
        return this;
    }

    public IndexOptions defaultLanguage(String defaultLanguage) {
        super.defaultLanguage(defaultLanguage);
        return this;
    }

    public IndexOptions languageOverride(String languageOverride) {
        super.languageOverride(languageOverride);
        return this;
    }

    public IndexOptions textVersion(Integer textVersion) {
        super.textVersion(textVersion);
        return this;
    }

    public IndexOptions sphereVersion(Integer sphereVersion) {
        super.sphereVersion(sphereVersion);
        return this;
    }

    public IndexOptions bits(Integer bits) {
        super.bits(bits);
        return this;
    }

    public IndexOptions min(Double min) {
        super.min(min);
        return this;
    }

    public IndexOptions max(Double max) {
        super.max(max);
        return this;
    }

    public IndexOptions bucketSize(Double bucketSize) {
        super.bucketSize(bucketSize);
        return this;
    }

    public IndexOptions storageEngine(Bson storageEngine) {
        super.storageEngine(storageEngine);
        return this;
    }

    public IndexOptions partialFilterExpression(Bson partialFilterExpression) {
        super.partialFilterExpression(partialFilterExpression);
        return this;
    }

    public IndexOptions collation(Collation collation) {
        super.collation(collation);
        return this;
    }

    public IndexOptions wildcardProjection(Bson wildcardProjection) {
        super.wildcardProjection(wildcardProjection);
        return this;
    }

    public IndexOptions hidden(boolean hidden) {
        super.hidden(hidden);
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
       return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
