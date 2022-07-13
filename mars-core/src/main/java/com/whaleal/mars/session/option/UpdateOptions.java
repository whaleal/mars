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
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @author wh
 * @Date 2020-12-10
 * 继承了原生的 Option  属性
 * 同时实现了自定义的 config  相关接口
 */
public class UpdateOptions implements WriteConfigurable<UpdateOptions> {

    private WriteConcern writeConcern;

    private com.mongodb.client.model.UpdateOptions originUpdateOptions;

    // 更新多个属性
    private boolean multi;

    public UpdateOptions() {
        originUpdateOptions = new com.mongodb.client.model.UpdateOptions();
    }

    public UpdateOptions(com.mongodb.client.model.UpdateOptions originUpdateOptions) {
        this.originUpdateOptions = originUpdateOptions;
    }

    public boolean isMulti() {
        return multi;
    }

    public UpdateOptions multi(boolean multi) {
        this.multi = multi;
        return this;
    }


    public List getArrayFilters() {
        return originUpdateOptions.getArrayFilters();
    }


    public Boolean getBypassDocumentValidation() {
        return originUpdateOptions.getBypassDocumentValidation();
    }


    public Collation getCollation() {
        return originUpdateOptions.getCollation();
    }

    public UpdateOptions upsert(boolean upsert) {
        originUpdateOptions.upsert(upsert);
        return this;
    }

    public UpdateOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
        originUpdateOptions.bypassDocumentValidation(bypassDocumentValidation);
        return this;
    }

    public UpdateOptions collation(Collation collation) {
        originUpdateOptions.collation(collation);
        return this;
    }

    public UpdateOptions arrayFilters(List<? extends Bson> arrayFilters) {
        originUpdateOptions.arrayFilters(arrayFilters);
        return this;
    }

    @Override
    public WriteConcern writeConcern() {
        return writeConcern;
    }


    @Override
    public UpdateOptions writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Override
    public com.mongodb.client.model.UpdateOptions getOriginOptions() {
        if (originUpdateOptions == null) {
            originUpdateOptions = new com.mongodb.client.model.UpdateOptions();
        }
        return originUpdateOptions;
    }
}
