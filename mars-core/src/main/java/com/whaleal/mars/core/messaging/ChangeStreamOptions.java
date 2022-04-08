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
package com.whaleal.mars.core.messaging;

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.changestream.FullDocument;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ObjectUtil;

import com.whaleal.mars.core.aggregation.AggregationPipeline;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.Document;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static com.whaleal.icefrog.core.util.ClassUtil.isAssignable;

/**
 * 改变流选项
 *
 * @author cs
 * @date 2021/04/13
 */
public class ChangeStreamOptions {

    private
    Object filter;
    private
    BsonValue resumeToken;
    private
    FullDocument fullDocumentLookup;
    private
    Collation collation;
    private
    Object resumeTimestamp;
    private Resume resume = Resume.UNDEFINED;

    /**
     * 无参构造
     */
    protected ChangeStreamOptions() {
    }


    /**
     * 获取过滤器
     *
     * @return {@link Optional<Object>}
     */
    public Optional<Object> getFilter() {
        //TODO
        return Optional.ofNullable(filter);
    }


    /**
     * 得到令牌
     *
     * @return {@link Optional<BsonValue>}
     */
    public Optional<BsonValue> getResumeToken() {
        return Optional.ofNullable(resumeToken);
    }


    /**
     * 得到完整的文档
     *
     * @return {@link Optional<FullDocument>}
     */
    public Optional<FullDocument> getFullDocumentLookup() {
        return Optional.ofNullable(fullDocumentLookup);
    }


    /**
     * 得到排序
     *
     * @return {@link Optional<Collation>}
     */
    public Optional<Collation> getCollation() {
        return Optional.ofNullable(collation);
    }


    /**
     * 得到恢复的时间戳
     *
     * @return {@link Optional<Instant>}
     */
    public Optional<Instant> getResumeTimestamp() {
        return Optional.ofNullable(resumeTimestamp).map(timestamp -> asTimestampOfType(timestamp, Instant.class));
    }

    /**
     * 得到bson类型的恢复时间戳
     *
     * @return {@link Optional<BsonTimestamp>}
     */
    public Optional<BsonTimestamp> getResumeBsonTimestamp() {
        return Optional.ofNullable(resumeTimestamp).map(timestamp -> asTimestampOfType(timestamp, BsonTimestamp.class));
    }

    /**
     * 是否后开始
     *
     * @return boolean
     */
    public boolean isStartAfter() {
        return Resume.START_AFTER.equals(resume);
    }

    /**
     * 是否后恢复
     *
     * @return boolean
     */
    public boolean isResumeAfter() {
        return Resume.RESUME_AFTER.equals(resume);
    }

    /**
     * 空
     *
     * @return empty {@link ChangeStreamOptions}.
     */
    public static ChangeStreamOptions empty() {
        return ChangeStreamOptions.builder().build();
    }

    /**
     * 构建器
     * 构建操作流选项的构建器
     *
     * @return new instance of {@link ChangeStreamOptionsBuilder}.
     */
    public static ChangeStreamOptionsBuilder builder() {
        return new ChangeStreamOptionsBuilder();
    }

    /**
     * 时间戳的类型
     *
     * @param timestamp  时间戳
     * @param targetType 目标类型
     * @return {@link T}
     */
    private static <T> T asTimestampOfType(Object timestamp, Class<T> targetType) {
        return targetType.cast(doGetTimestamp(timestamp, targetType));
    }

    private static <T> Object doGetTimestamp(Object timestamp, Class<T> targetType) {

        if (targetType != null ? isAssignable(targetType, timestamp.getClass()) : !targetType.isPrimitive()) {
            return timestamp;
        }

        if (timestamp instanceof Instant) {
            return new BsonTimestamp((int) ((Instant) timestamp).getEpochSecond(), 0);
        }

        if (timestamp instanceof BsonTimestamp) {
            return Instant.ofEpochSecond(((BsonTimestamp) timestamp).getTime());
        }

        throw new IllegalArgumentException(
                "o_O that should actually not happen. The timestamp should be an Instant or a BsonTimestamp but was "
                        + ObjectUtil.nullSafeClassName(timestamp));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChangeStreamOptions that = (ChangeStreamOptions) o;

        if (!ObjectUtil.nullSafeEquals(this.filter, that.filter)) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(this.resumeToken, that.resumeToken)) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(this.fullDocumentLookup, that.fullDocumentLookup)) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(this.collation, that.collation)) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(this.resumeTimestamp, that.resumeTimestamp)) {
            return false;
        }
        return resume == that.resume;
    }

    @Override
    public int hashCode() {
        int result = ObjectUtil.nullSafeHashCode(filter);
        result = 31 * result + ObjectUtil.nullSafeHashCode(resumeToken);
        result = 31 * result + ObjectUtil.nullSafeHashCode(fullDocumentLookup);
        result = 31 * result + ObjectUtil.nullSafeHashCode(collation);
        result = 31 * result + ObjectUtil.nullSafeHashCode(resumeTimestamp);
        result = 31 * result + ObjectUtil.nullSafeHashCode(resume);
        return result;
    }


    /**
     * 枚举
     *
     * @author cs
     * @date 2021/04/13
     */
    enum Resume {

        UNDEFINED,


        START_AFTER,


        RESUME_AFTER
    }

    /**
     * 改变流选项构建器
     */
    public static class ChangeStreamOptionsBuilder {

        private
        Object filter;
        private
        BsonValue resumeToken;
        private
        FullDocument fullDocumentLookup;
        private
        Collation collation;
        private
        Object resumeTimestamp;
        private Resume resume = Resume.UNDEFINED;

        private ChangeStreamOptionsBuilder() {
        }

        /**
         * 排序
         *
         * @param collation must not be {@literal null} nor {@literal empty}.
         * @return this.
         */
        public ChangeStreamOptionsBuilder collation(Collation collation) {

            Precondition.notNull(collation, "Collation must not be null nor empty!");

            this.collation = collation;
            return this;
        }

        /**
         * 过滤器
         * todo
         * Set the filter to apply.
         *
         * @return this.
         */
        public ChangeStreamOptionsBuilder filter( AggregationPipeline filter) {

            Precondition.notNull(filter, "Filter must not be null!");

            this.filter = filter;
            return this;
        }

        /**
         * 过滤器
         * Set the plain filter chain to apply.
         *
         * @return this.
         */
        public ChangeStreamOptionsBuilder filter(Document... filter) {

            Precondition.noNullElements(filter, "Filter must not contain null values");

            this.filter = Arrays.asList(filter);
            return this;
        }

        /**
         * Set the resume token (typically a {@link org.bson.BsonDocument} containing a {@link org.bson.BsonBinary binary
         * token}) after which to start with listening.
         *
         * @param resumeToken must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamOptionsBuilder resumeToken(BsonValue resumeToken) {

            Precondition.notNull(resumeToken, "ResumeToken must not be null!");

            this.resumeToken = resumeToken;

            if (this.resume == Resume.UNDEFINED) {
                this.resume = Resume.RESUME_AFTER;
            }

            return this;
        }

        /**
         * Set the {@link FullDocument} lookup to {@link FullDocument#UPDATE_LOOKUP}.
         *
         * @return this.
         * @see #fullDocumentLookup(FullDocument)
         */
        public ChangeStreamOptionsBuilder returnFullDocumentOnUpdate() {
            return fullDocumentLookup(FullDocument.UPDATE_LOOKUP);
        }

        /**
         * Set the {@link FullDocument} lookup to use.
         *
         * @param lookup must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamOptionsBuilder fullDocumentLookup(FullDocument lookup) {

            Precondition.notNull(lookup, "Lookup must not be null!");

            this.fullDocumentLookup = lookup;
            return this;
        }

        /**
         * Set the cluster time to resume from.
         *
         * @param resumeTimestamp must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamOptionsBuilder resumeAt(Instant resumeTimestamp) {

            Precondition.notNull(resumeTimestamp, "ResumeTimestamp must not be null!");

            this.resumeTimestamp = resumeTimestamp;
            return this;
        }

        /**
         * Set the cluster time to resume from.
         *
         * @param resumeTimestamp must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamOptionsBuilder resumeAt(BsonTimestamp resumeTimestamp) {

            Precondition.notNull(resumeTimestamp, "ResumeTimestamp must not be null!");

            this.resumeTimestamp = resumeTimestamp;
            return this;
        }

        /**
         * Set the resume token after which to continue emitting notifications.
         *
         * @param resumeToken must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamOptionsBuilder resumeAfter(BsonValue resumeToken) {

            resumeToken(resumeToken);
            this.resume = Resume.RESUME_AFTER;

            return this;
        }

        /**
         * Set the resume token after which to start emitting notifications.
         *
         * @param resumeToken must not be {@literal null}.
         * @return this.
         */
        public ChangeStreamOptionsBuilder startAfter(BsonValue resumeToken) {

            resumeToken(resumeToken);
            this.resume = Resume.START_AFTER;

            return this;
        }

        /**
         * @return the built {@link ChangeStreamOptions}
         */
        public ChangeStreamOptions build() {

            ChangeStreamOptions options = new ChangeStreamOptions();

            options.filter = this.filter;
            options.resumeToken = this.resumeToken;
            options.fullDocumentLookup = this.fullDocumentLookup;
            options.collation = this.collation;
            options.resumeTimestamp = this.resumeTimestamp;
            options.resume = this.resume;

            return options;
        }
    }
}