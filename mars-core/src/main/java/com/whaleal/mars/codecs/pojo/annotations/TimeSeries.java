package com.whaleal.mars.codecs.pojo.annotations;

/**
 * @author wh
 */

import com.mongodb.client.model.TimeSeriesGranularity;

import java.lang.annotation.*;

import static com.mongodb.client.model.TimeSeriesGranularity.SECONDS;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
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