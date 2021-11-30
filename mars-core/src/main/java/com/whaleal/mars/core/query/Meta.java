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
package com.whaleal.mars.core.query;


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.icefrog.core.util.StrUtil;

import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Meta-data for {@link Query} instances.
 */
public class Meta {

    private final Map<String, Object> values = new LinkedHashMap<>(2);
    private final Set<CursorOption> flags = new LinkedHashSet<>();
    private Integer cursorBatchSize;
    private Boolean allowDiskUse;

    public Meta() {
    }

    /**
     * Copy a {@link Meta} object.
     *
     * @param source
     */
    Meta(Meta source) {
        this.values.putAll(source.values);
        this.flags.addAll(source.flags);
        this.cursorBatchSize = source.cursorBatchSize;
        this.allowDiskUse = source.allowDiskUse;
    }

    /**
     * @return {@literal null} if not set.
     */

    public Long getMaxTimeMsec() {
        return getValue(MetaKey.MAX_TIME_MS.key);
    }

    /**
     * Set the maximum time limit in milliseconds for processing operations.
     *
     * @param maxTimeMsec
     */
    public void setMaxTimeMsec(long maxTimeMsec) {
        setMaxTime(Duration.ofMillis(maxTimeMsec));
    }

    /**
     * Set the maximum time limit for processing operations.
     *
     * @param timeout
     * @param timeUnit
     * @deprecated . Use {@link #setMaxTime(Duration)} instead.
     */
    @Deprecated
    public void setMaxTime( long timeout, TimeUnit timeUnit ) {
        setValue(MetaKey.MAX_TIME_MS.key, (timeUnit != null ? timeUnit : TimeUnit.MILLISECONDS).toMillis(timeout));
    }

    /**
     * Set the maximum time limit for processing operations.
     *
     * @param timeout must not be {@literal null}.
     */
    public void setMaxTime(Duration timeout) {

        Precondition.notNull(timeout, "Timeout must not be null!");
        setValue(MetaKey.MAX_TIME_MS.key, timeout.toMillis());
    }

    /**
     * @return {@literal null} if not set.
     */

    public String getComment() {
        return getValue(MetaKey.COMMENT.key);
    }

    /**
     * Add a comment to the query that is propagated to the profile log.
     *
     * @param comment
     */
    public void setComment(String comment) {
        setValue(MetaKey.COMMENT.key, comment);
    }

    /**
     * @return {@literal null} if not set.
     */

    public Integer getCursorBatchSize() {
        return cursorBatchSize;
    }

    /**
     * Apply the batch size (number of documents to return in each response) for a query. <br />
     * Use {@literal 0 (zero)} for no limit. A <strong>negative limit</strong> closes the cursor after returning a single
     * batch indicating to the server that the client will not ask for a subsequent one.
     *
     * @param cursorBatchSize The number of documents to return per batch.
     */
    public void setCursorBatchSize(int cursorBatchSize) {
        this.cursorBatchSize = cursorBatchSize;
    }

    /**
     * Add {@link CursorOption} influencing behavior of the {@link com.mongodb.client.FindIterable}.
     *
     * @param option must not be {@literal null}.
     * @return
     */
    public boolean addFlag(CursorOption option) {

        Precondition.notNull(option, "CursorOption must not be null!");
        return this.flags.add(option);
    }

    /**
     * @return never {@literal null}.
     */
    public Set<CursorOption> getFlags() {
        return flags;
    }

    /**
     * When set to {@literal true}, aggregation stages can write data to disk.
     *
     * @return {@literal null} if not set.
     */

    public Boolean getAllowDiskUse() {
        return allowDiskUse;
    }

    /**
     * Set to {@literal true}, to allow aggregation stages to write data to disk.
     *
     * @param allowDiskUse use {@literal null} for server defaults.
     */
    public void setAllowDiskUse( Boolean allowDiskUse ) {
        this.allowDiskUse = allowDiskUse;
    }

    /**
     * @return
     */
    public boolean hasValues() {
        return !this.values.isEmpty() || !this.flags.isEmpty() || this.cursorBatchSize != null || this.allowDiskUse != null;
    }

    /**
     * Get {@link Iterable} of set meta values.
     *
     * @return
     */
    public Iterable<Entry<String, Object>> values() {
        return Collections.unmodifiableSet(this.values.entrySet());
    }

    /**
     * Sets or removes the value in case of {@literal null} or empty {@link String}.
     *
     * @param key   must not be {@literal null} or empty.
     * @param value
     */
    void setValue( String key, Object value ) {

        Precondition.hasText(key, "Meta key must not be 'null' or blank.");

        if (value == null || (value instanceof String && !StrUtil.hasText((String) value))) {
            this.values.remove(key);
        }
        this.values.put(key, value);
    }


    @SuppressWarnings("unchecked")
    private <T> T getValue(String key) {
        return (T) this.values.get(key);
    }

    private <T> T getValue(String key, T defaultValue) {

        T value = getValue(key);
        return value != null ? value : defaultValue;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int hash = ObjectUtil.nullSafeHashCode(this.values);
        hash += ObjectUtil.nullSafeHashCode(this.flags);
        return hash;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Meta)) {
            return false;
        }

        Meta other = (Meta) obj;
        if (!ObjectUtil.nullSafeEquals(this.values, other.values)) {
            return false;
        }
        return ObjectUtil.nullSafeEquals(this.flags, other.flags);
    }

    private enum MetaKey {
        MAX_TIME_MS("$maxTimeMS"), MAX_SCAN("$maxScan"), COMMENT("$comment"), SNAPSHOT("$snapshot");

        private String key;

        MetaKey(String key) {
            this.key = key;
        }
    }

    /**
     * {@link CursorOption} represents {@code OP_QUERY} wire protocol flags to change the behavior of queries.
     */
    public enum CursorOption {

        /**
         * Prevents the server from timing out idle cursors.
         */
        NO_TIMEOUT,

        /**
         * Sets the cursor to return all data returned by the query at once rather than splitting the results into batches.
         */
        EXHAUST,

        /**
         * Allows querying of a replica.
         *
         * @deprecated since 3.0.2, use {@link #SECONDARY_READS} instead.
         */
        @Deprecated
        SLAVE_OK,

        /**
         * Allows querying of a replica.
         * <p>
         * .2
         */
        SECONDARY_READS,

        /**
         * Sets the cursor to return partial data from a query against a sharded cluster in which some shards do not respond
         * rather than throwing an error.
         */
        PARTIAL
    }
}
