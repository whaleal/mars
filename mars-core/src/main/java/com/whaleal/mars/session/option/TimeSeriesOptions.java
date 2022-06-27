package com.whaleal.mars.session.option;

/**
 * @author lyz
 * @description
 * @date 2022-06-19 18:50
 **/

import com.mongodb.client.model.TimeSeriesGranularity;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.StrUtil;

import javax.annotation.Nullable;

/**
 * Options applicable to Time Series collections.
 *
 *
 *
 * @see <a href=
 *      "https://docs.mongodb.com/manual/core/timeseries-collections">https://docs.mongodb.com/manual/core/timeseries-collections</a>
 */
public class TimeSeriesOptions {

    private final String timeField;

    private @Nullable final String metaField;

    private final TimeSeriesGranularity granularity;

    private final boolean enableExpire ;

    private final long expireAfterSeconds;

    private TimeSeriesOptions(String timeField, @Nullable String metaField, TimeSeriesGranularity granularity,boolean enableExpire,long expireAfterSeconds) {

        Precondition.hasText(timeField, "Time field must not be empty or null!");

        this.timeField = timeField;
        this.metaField = metaField;
        this.granularity = granularity;
        this.enableExpire = enableExpire;
        this.expireAfterSeconds = expireAfterSeconds;
    }

    /**
     * Create a new instance of {@link TimeSeriesOptions} using the given field as its {@literal timeField}. The one,
     * that contains the date in each time series document. <br />
     *  will be considered during the mapping process.
     *
     * @param timeField must not be {@literal null}.
     * @return new instance of {@link TimeSeriesOptions}.
     */
    public static TimeSeriesOptions timeSeries(String timeField) {
        return new TimeSeriesOptions(timeField, null, null,false,0);
    }

    /**
     * Set the name of the field which contains metadata in each time series document. Should not be the {@literal id}
     * nor {@link TimeSeriesOptions#timeSeries(String)} timeField} nor point to an {@literal array} or
     * {@link java.util.Collection}. <br />
     *  will be considered during the mapping process.
     *
     * @param metaField must not be {@literal null}.
     * @return new instance of {@link TimeSeriesOptions}.
     */
    public TimeSeriesOptions metaField(String metaField) {
        return new TimeSeriesOptions(timeField, metaField, granularity,enableExpire,expireAfterSeconds);
    }

    /**
     * Select the {@link TimeSeriesGranularity} parameter to define how data in the time series collection is organized.
     * Select one that is closest to the time span between incoming measurements.
     *
     * @return new instance of {@link TimeSeriesOptions}.
     * @see TimeSeriesGranularity
     */
    public TimeSeriesOptions granularity(TimeSeriesGranularity granularity) {
        return new TimeSeriesOptions(timeField, metaField, granularity,enableExpire,expireAfterSeconds);
    }

    public TimeSeriesOptions enableExpire(Boolean enableExpire) {
        return new TimeSeriesOptions(timeField, metaField, granularity,enableExpire,expireAfterSeconds);
    }

    public TimeSeriesOptions expireAfterSeconds(long expireAfterSeconds) {
        return new TimeSeriesOptions(timeField, metaField, granularity,enableExpire,expireAfterSeconds);
    }


    /**
     * @return never {@literal null}.
     */
    public String getTimeField() {
        return timeField;
    }

    /**
     * @return can be {@literal null}. Might be an {@literal empty} {@link String} as well, so maybe check via
     *         {@link StrUtil#hasText(String)}.
     */
    @Nullable
    public String getMetaField() {
        return metaField;
    }

    /**
     * @return never {@literal null}.
     */
    public TimeSeriesGranularity getGranularity() {
        return granularity;
    }

    public boolean getEnableExpire(){
        return enableExpire;
    }

    public long getExpireAfterSeconds(){
        return expireAfterSeconds;
    }
}

