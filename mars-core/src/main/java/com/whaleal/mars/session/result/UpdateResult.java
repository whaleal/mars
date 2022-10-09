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
package com.whaleal.mars.session.result;


import org.bson.BsonValue;

/**
 * @author cx
 * @Date 2020/12/22
 */
@Deprecated
public class UpdateResult extends com.mongodb.client.result.UpdateResult {

    private com.mongodb.client.result.UpdateResult originUpdateResult;

    public UpdateResult(com.mongodb.client.result.UpdateResult originUpdateResult) {
        this.originUpdateResult = originUpdateResult;
    }

    public UpdateResult() {
    }

    @Override
    public boolean wasAcknowledged() {
        return originUpdateResult.wasAcknowledged();
    }

    @Override
    public long getMatchedCount() {
        return originUpdateResult.getMatchedCount();
    }

    @Override
    public long getModifiedCount() {
        return originUpdateResult.getModifiedCount();
    }

    @Override

    public BsonValue getUpsertedId() {
        return originUpdateResult.getUpsertedId();
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        UpdateResult that = (UpdateResult) obj;

        if (originUpdateResult != that.originUpdateResult) {
            return false;
        }

        return true;
    }


    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + (originUpdateResult != null ? originUpdateResult.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "UpdateResult{" +
                "originUpdateResult=" + originUpdateResult +
                '}';
    }


    public com.mongodb.client.result.UpdateResult getOriginUpdateResult() {
        return originUpdateResult;
    }

    public void setOriginUpdateResult(com.mongodb.client.result.UpdateResult originUpdateResult) {
        this.originUpdateResult = originUpdateResult;
    }

}
