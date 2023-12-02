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

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Variable;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @author cx
 * @Date 2020/12/15
 */
@Deprecated
public class MergeOptions implements ReadConfigurable<MergeOptions> {

    private ReadConcern readConcern;

    private ReadPreference readPreference;

    private com.mongodb.client.model.MergeOptions originMergeOptions;

    public MergeOptions() {
        originMergeOptions = new com.mongodb.client.model.MergeOptions();
    }

    public MergeOptions(com.mongodb.client.model.MergeOptions originMergeOptions) {
        this.originMergeOptions = originMergeOptions;
    }


    public List<String> getUniqueIdentifier() {
        return originMergeOptions.getUniqueIdentifier();
    }


    public List<Variable<?>> getVariables() {
        return originMergeOptions.getVariables();
    }


    public com.mongodb.client.model.MergeOptions.WhenMatched getWhenMatched() {
        return originMergeOptions.getWhenMatched();
    }


    public List<Bson> getWhenMatchedPipeline() {
        return originMergeOptions.getWhenMatchedPipeline();
    }


    public com.mongodb.client.model.MergeOptions.WhenNotMatched getWhenNotMatched() {
        return originMergeOptions.getWhenNotMatched();
    }


    public MergeOptions uniqueIdentifier(String uniqueIdentifier) {
        originMergeOptions.uniqueIdentifier(uniqueIdentifier);
        return this;
    }

    public MergeOptions uniqueIdentifier(List<String> uniqueIdentifier) {
        originMergeOptions.uniqueIdentifier(uniqueIdentifier);
        return this;
    }

    public MergeOptions whenMatched(com.mongodb.client.model.MergeOptions.WhenMatched whenMatched) {
        originMergeOptions.whenMatched(whenMatched);
        return this;
    }

    public MergeOptions variables(List<Variable<?>> variables) {
        originMergeOptions.variables(variables);
        return this;
    }

    public MergeOptions whenMatchedPipeline(List<Bson> whenMatchedPipeline) {
        originMergeOptions.whenMatchedPipeline(whenMatchedPipeline);
        return this;
    }

    public MergeOptions whenNotMatched(com.mongodb.client.model.MergeOptions.WhenNotMatched whenNotMatched) {
        originMergeOptions.whenNotMatched(whenNotMatched);
        return this;
    }

    @Override
    public ReadConcern getReadConcern() {
        return readConcern;
    }

    @Override
    public ReadPreference getReadPreference() {
        return readPreference;
    }

    @Override
    public MergeOptions readConcern(ReadConcern readConcern) {
        this.readConcern = readConcern;
        return this;
    }

    @Override
    public MergeOptions readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    @Override
    public com.mongodb.client.model.MergeOptions getOriginOptions() {
        if (originMergeOptions == null) {
            originMergeOptions = new com.mongodb.client.model.MergeOptions();
        }
        return originMergeOptions;
    }
}
