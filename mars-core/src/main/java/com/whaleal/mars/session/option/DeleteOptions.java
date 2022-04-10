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


/**
 * @author cx
 * @Date 2020-12-15
 * 继承了原生的 Option 属性
 * 同时实现了自定义的 config 相关接口
 */
public class DeleteOptions
        implements WriteConfigurable<DeleteOptions> {


    private WriteConcern writeConcern;
    /** 删除多条数据 */
    private boolean multi;

    private com.mongodb.client.model.DeleteOptions originDeleteOptions;

    public DeleteOptions() {
        originDeleteOptions = new com.mongodb.client.model.DeleteOptions();
    }

    public DeleteOptions(com.mongodb.client.model.DeleteOptions originDeleteOptions) {
        this.originDeleteOptions = originDeleteOptions;
    }

    public boolean isMulti() {
        return multi;
    }

    public DeleteOptions multi(boolean multi) {
        this.multi = multi;
        return this;
    }


    public Collation getCollation() {
        return originDeleteOptions.getCollation();
    }

    public DeleteOptions collation(Collation collation) {
        originDeleteOptions.collation(collation);
        return this;
    }

    @Override
    public DeleteOptions writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Override
    public WriteConcern writeConcern() {
        return writeConcern;
    }

    @Override
    public com.mongodb.client.model.DeleteOptions getOriginOptions() {
        if (originDeleteOptions == null) {
            originDeleteOptions = new com.mongodb.client.model.DeleteOptions();
        }
        return originDeleteOptions;
    }
}
