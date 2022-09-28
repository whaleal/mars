/**
 * Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 * <p>
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.whaleal.com/licensing/server-side-public-license>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the Server Side Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */

package com.whaleal.mars.codecs.pojo.annotations;

/**
 * @author wh
 */

import com.mongodb.client.model.TimeSeriesGranularity;

import java.lang.annotation.*;

import static com.mongodb.client.model.TimeSeriesGranularity.SECONDS;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface TimeSeries {

    /**
     * Name of the property which contains the date in each time series document. <br />
     * Translation of property names to  will be considered during the mapping
     * process.
     *
     * @return never {@literal null}.
     */
    String timeField();

    /**
     * The name of the field which contains metadata in each time series document. Should not be the {@literal id} nor
     * {@link #timeField()} nor point to an {@literal array} or {@link java.util.Collection}. <br />
     * Translation of property names to  will be considered during the mapping
     * process.
     *
     * @return empty {@link String} by default.
     */
    String metaField() default "";

    /**
     * Select the {@link TimeSeriesGranularity granularity} parameter to define how data in the time series collection is organized.
     *
     * @return {@link TimeSeriesGranularity server default} by default.
     */
    TimeSeriesGranularity granularity() default SECONDS;

    /**
     * Optional. Enable the automatic deletion of documents in a time series collection by specifying the number of seconds
     * after which documents expire. MongoDB deletes expired documents automatically. See Set up Automatic Removal for
     * Time Series Collections (TTL) for more information.
     * 此参数生效需要指定enableExpire有效
     * @return expireAfterSeconds
     */
    long expireAfterSeconds() default 0;

    /**
     * 是否开启 过期
     * 限制 expireAfterSeconds 的启用
     * @return false  default
     */
    boolean enableExpire() default false;


}