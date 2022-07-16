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
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author cx
 * @Date 2020/12/15
 */
public class FindOneAndUpdateOptions implements WriteConfigurable<FindOneAndUpdateOptions> {

    private WriteConcern writeConcern;

    private com.mongodb.client.model.FindOneAndUpdateOptions originFindOneAndUpdateOptions;

    public FindOneAndUpdateOptions() {
        originFindOneAndUpdateOptions = new com.mongodb.client.model.FindOneAndUpdateOptions();
    }

    public FindOneAndUpdateOptions(com.mongodb.client.model.FindOneAndUpdateOptions originFindOneAndUpdateOptions) {
        this.originFindOneAndUpdateOptions = originFindOneAndUpdateOptions;
    }


    public List getArrayFilters() {
        return originFindOneAndUpdateOptions.getArrayFilters();
    }


    public Boolean getBypassDocumentValidation() {
        return originFindOneAndUpdateOptions.getBypassDocumentValidation();
    }


    public Optional<Collation> getCollation() {
        return Optional.ofNullable(originFindOneAndUpdateOptions.getCollation());
    }


    public long getMaxTime(TimeUnit timeUnit) {
        return originFindOneAndUpdateOptions.getMaxTime(timeUnit);
    }


    public Bson getSort() {
        return originFindOneAndUpdateOptions.getSort();
    }


    public Bson getProjection() {
        return originFindOneAndUpdateOptions.getProjection();
    }


    public ReturnDocument getReturnDocument() {
        return originFindOneAndUpdateOptions.getReturnDocument();
    }

    public FindOneAndUpdateOptions projection(Bson projection) {
        originFindOneAndUpdateOptions.projection(projection);
        return this;
    }

    public FindOneAndUpdateOptions sort(Bson sort) {
        originFindOneAndUpdateOptions.sort(sort);
        return this;
    }

    public FindOneAndUpdateOptions upsert(boolean upsert) {
        originFindOneAndUpdateOptions.upsert(upsert);
        return this;
    }

    public FindOneAndUpdateOptions returnDocument(ReturnDocument returnDocument) {
        originFindOneAndUpdateOptions.returnDocument(returnDocument);
        return this;
    }

    public FindOneAndUpdateOptions maxTime(long maxTime, TimeUnit timeUnit) {
        originFindOneAndUpdateOptions.maxTime(maxTime, timeUnit);
        return this;
    }

    public FindOneAndUpdateOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
        originFindOneAndUpdateOptions.bypassDocumentValidation(bypassDocumentValidation);
        return this;
    }

    public FindOneAndUpdateOptions collation(Collation collation) {
        originFindOneAndUpdateOptions.collation(collation);
        return this;
    }

    public FindOneAndUpdateOptions arrayFilters(List<? extends Bson> arrayFilters) {
        originFindOneAndUpdateOptions.arrayFilters(arrayFilters);
        return this;
    }

    @Override
    public FindOneAndUpdateOptions writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Override
    public WriteConcern writeConcern() {
        return writeConcern;
    }

    @Override
    public com.mongodb.client.model.FindOneAndUpdateOptions getOriginOptions() {
        if (originFindOneAndUpdateOptions == null) {
            originFindOneAndUpdateOptions = new com.mongodb.client.model.FindOneAndUpdateOptions();
        }
        return originFindOneAndUpdateOptions;
    }
}
