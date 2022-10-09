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
import com.mongodb.client.model.Collation;

import java.util.concurrent.TimeUnit;

/**
 * @author cx
 * @Date 2020/12/15
 */
@Deprecated
public class CountOptions implements ReadConfigurable<CountOptions> {

    private ReadConcern readConcern;

    private ReadPreference readPreference;

    private com.mongodb.client.model.CountOptions originCountOptions;

    public CountOptions() {
        originCountOptions = new com.mongodb.client.model.CountOptions();
    }

    public CountOptions(com.mongodb.client.model.CountOptions originCountOptions) {
        this.originCountOptions = originCountOptions;
    }


    public Collation getCollation() {
        return originCountOptions.getCollation();
    }


    public int getLimit() {
        return originCountOptions.getLimit();
    }


    public long getMaxTime(TimeUnit timeUnit) {
        //这里获取最大Time，默认时间单位是MILLISECONDS
        return originCountOptions.getMaxTime(timeUnit);
    }


    public int getSkip() {
        return originCountOptions.getSkip();
    }

    public CountOptions limit(int limit) {
        originCountOptions.limit(limit);
        return this;
    }

    public CountOptions skip(int skip) {
        originCountOptions.skip(skip);
        return this;
    }

    public CountOptions maxTime(long maxTime, TimeUnit timeUnit) {
        originCountOptions.maxTime(maxTime, timeUnit);
        return this;
    }

    public CountOptions collation(Collation collation) {
        originCountOptions.collation(collation);
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
    public CountOptions readConcern(ReadConcern readConcern) {
        this.readConcern = readConcern;
        return this;
    }

    @Override
    public CountOptions readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    @Override
    public com.mongodb.client.model.CountOptions getOriginOptions() {
        if (originCountOptions == null) {
            originCountOptions = new com.mongodb.client.model.CountOptions();
        }
        return originCountOptions;
    }
}
