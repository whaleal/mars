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
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

import java.util.concurrent.TimeUnit;

/**
 * @author cx
 * @Date 2020/12/15
 */
public class FindOneAndReplaceOptions implements WriteConfigurable<FindOneAndReplaceOptions> {

    private WriteConcern writeConcern;

    private com.mongodb.client.model.FindOneAndReplaceOptions originFindOneAndReplaceOptions;

    public FindOneAndReplaceOptions() {
        originFindOneAndReplaceOptions = new com.mongodb.client.model.FindOneAndReplaceOptions();
    }

    public FindOneAndReplaceOptions(com.mongodb.client.model.FindOneAndReplaceOptions originFindOneAndReplaceOptions) {
        this.originFindOneAndReplaceOptions = originFindOneAndReplaceOptions;
    }

    @Nullable
    public Boolean getBypassDocumentValidation() {
        return originFindOneAndReplaceOptions.getBypassDocumentValidation();
    }

    @Nullable
    public Collation getCollation() {
        return originFindOneAndReplaceOptions.getCollation();
    }

    @Nullable
    public long getMaxTime(TimeUnit timeUnit) {
        return originFindOneAndReplaceOptions.getMaxTime(timeUnit);
    }

    @Nullable
    public Bson getSort() {
        return originFindOneAndReplaceOptions.getSort();
    }

    @Nullable
    public Bson getProjection() {
        return originFindOneAndReplaceOptions.getProjection();
    }

    @Nullable
    public ReturnDocument getReturnDocument() {
        return originFindOneAndReplaceOptions.getReturnDocument();
    }

    public FindOneAndReplaceOptions projection(Bson projection) {
        originFindOneAndReplaceOptions.projection(projection);
        return this;
    }

    public FindOneAndReplaceOptions sort(Bson sort) {
        originFindOneAndReplaceOptions.sort(sort);
        return this;
    }

    public FindOneAndReplaceOptions upsert(boolean upsert) {
        originFindOneAndReplaceOptions.upsert(upsert);
        return this;
    }

    public FindOneAndReplaceOptions returnDocument(ReturnDocument returnDocument) {
        originFindOneAndReplaceOptions.returnDocument(returnDocument);
        return this;
    }

    public FindOneAndReplaceOptions maxTime(long maxTime, TimeUnit timeUnit) {
        originFindOneAndReplaceOptions.maxTime(maxTime, timeUnit);
        return this;
    }

    public FindOneAndReplaceOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
        originFindOneAndReplaceOptions.bypassDocumentValidation(bypassDocumentValidation);
        return this;
    }

    public FindOneAndReplaceOptions collation(Collation collation) {
        originFindOneAndReplaceOptions.collation(collation);
        return this;
    }

    @Override
    public FindOneAndReplaceOptions writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Override
    public WriteConcern writeConcern() {
        return writeConcern;
    }

    @Override
    public com.mongodb.client.model.FindOneAndReplaceOptions getOriginOptions() {
        if (originFindOneAndReplaceOptions == null) {
            originFindOneAndReplaceOptions = new com.mongodb.client.model.FindOneAndReplaceOptions();
        }
        return originFindOneAndReplaceOptions;
    }
}
