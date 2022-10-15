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
import com.mongodb.client.model.IndexOptionDefaults;
import com.mongodb.client.model.ValidationOptions;
import org.bson.conversions.Bson;

/**
 * @author cx
 * @Date 2020/12/15
 */
@Deprecated
public class CreateCollectionOptions implements WriteConfigurable<CreateCollectionOptions> {

    private WriteConcern writeConcern;

    private com.mongodb.client.model.CreateCollectionOptions originCreateCollectionOptions;

    public CreateCollectionOptions() {
        originCreateCollectionOptions = new com.mongodb.client.model.CreateCollectionOptions();
    }

    public CreateCollectionOptions(com.mongodb.client.model.CreateCollectionOptions originCreateCollectionOptions) {
        this.originCreateCollectionOptions = originCreateCollectionOptions;
    }


    public Collation getCollation() {
        return originCreateCollectionOptions.getCollation();
    }


    public long getMaxDocuments() {
        return originCreateCollectionOptions.getMaxDocuments();
    }


    public ValidationOptions getValidationOptions() {
        return originCreateCollectionOptions.getValidationOptions();
    }


    public IndexOptionDefaults getIndexOptionDefaults() {
        return originCreateCollectionOptions.getIndexOptionDefaults();
    }


    public long getSizeInBytes() {
        return originCreateCollectionOptions.getSizeInBytes();
    }


    public Bson getStorageEngineOptions() {
        return originCreateCollectionOptions.getStorageEngineOptions();
    }

    public CreateCollectionOptions maxDocuments(long maxDocuments) {
        originCreateCollectionOptions.maxDocuments(maxDocuments);
        return this;
    }

    public CreateCollectionOptions capped(boolean capped) {
        originCreateCollectionOptions.capped(capped);
        return this;
    }

    public CreateCollectionOptions sizeInBytes(long sizeInBytes) {
        originCreateCollectionOptions.sizeInBytes(sizeInBytes);
        return this;
    }

    public CreateCollectionOptions storageEngineOptions(Bson storageEngineOptions) {
        originCreateCollectionOptions.storageEngineOptions(storageEngineOptions);
        return this;
    }

    public CreateCollectionOptions indexOptionDefaults(IndexOptionDefaults indexOptionDefaults) {
        originCreateCollectionOptions.indexOptionDefaults(indexOptionDefaults);
        return this;
    }

    public CreateCollectionOptions validationOptions(ValidationOptions validationOptions) {
        originCreateCollectionOptions.validationOptions(validationOptions);
        return this;
    }

    public CreateCollectionOptions collation(Collation collation) {
        originCreateCollectionOptions.collation(collation);
        return this;
    }

    @Override
    public CreateCollectionOptions writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Override
    public WriteConcern writeConcern() {
        return writeConcern;
    }

    @Override
    public com.mongodb.client.model.CreateCollectionOptions getOriginOptions() {
        if (originCreateCollectionOptions == null) {
            originCreateCollectionOptions = new com.mongodb.client.model.CreateCollectionOptions();
        }
        return originCreateCollectionOptions;
    }
}
