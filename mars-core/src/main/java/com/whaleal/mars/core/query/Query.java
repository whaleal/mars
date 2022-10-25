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


import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.domain.IProjection;
import com.whaleal.mars.core.domain.ISort;
import com.whaleal.mars.core.domain.Pageable;
import com.whaleal.mars.core.domain.SortType;
import com.whaleal.mars.core.internal.InvalidMongoDbApiUsageException;


import org.bson.Document;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.whaleal.icefrog.core.util.ObjectUtil.nullSafeEquals;
import static com.whaleal.icefrog.core.util.ObjectUtil.nullSafeHashCode;


/**
 * MongoDB Query object representing criteria, projection, sorting and query hints.
 */
public class Query {

    // 主要查询 criteria 可以拼接多个,这里是有序存储 。
    private final Map<String, CriteriaDefinition> criteria = new LinkedHashMap<>();
    // projection
    private IProjection projectionSpec = null;
    // sorting
    //private List<Sort> sorts = new ArrayList<>();
    private ISort sorts = Sort.unsorted();
    private long skip;
    private int limit;

    //  hint 的 保存   主要有两种形式 ①indexName  ②索引结构的 document  形式
    private String hint;

    private WriteConcern writeConcern ;
    private ReadConcern readConcern ;

    private ReadPreference readPreference  ;

    private Meta meta = new Meta();
    // collation
    private Optional<com.mongodb.client.model.Collation> collation = Optional.empty();

    public ReadConcern getReadConcern() {
        return readConcern;
    }

    public Query setReadConcern( ReadConcern readConcern ) {
        this.readConcern = readConcern;
        return this ;
    }

    public ReadPreference getReadPreference() {
        return readPreference;
    }

    public Query setReadPreference( ReadPreference readPreference ) {
        this.readPreference = readPreference;
        return this ;
    }


    public Query setWriteConcern( WriteConcern writeConcern ) {

        this.writeConcern = writeConcern ;
        return this ;

    }

    public WriteConcern getWriteConcern() {

        return this.writeConcern ;
    }

    public Query() {
    }

    /**
     * Creates a new {@link Query} using the given {@link CriteriaDefinition}.
     *
     * @param criteriaDefinition must not be {@literal null}.
     */
    public Query(CriteriaDefinition criteriaDefinition) {
        addCriteria(criteriaDefinition);
    }

    /**
     * Static factory method to create a {@link Query} using the provided {@link CriteriaDefinition}.
     *
     * @param criteriaDefinition must not be {@literal null}.
     * @return new instance of {@link Query}.
     */
    public static Query query(CriteriaDefinition criteriaDefinition) {
        return new Query(criteriaDefinition);
    }

    /**
     * Create an independent copy of the given {@link Query}. <br />
     * The resulting {@link Query} will not be {@link Object#equals(Object) binary equal} to the given source but
     * semantically equal in terms of creating the same result when executed.
     *
     * @param source The source {@link Query} to use a reference. Must not be {@literal null}.
     * @return new {@link Query}.
     */
    public static Query of(Query source) {

        Precondition.notNull(source, "Source must not be null!");

        Document sourceFields = source.getFieldsObject();
        Document sourceSort = source.getSortObject();
        Document sourceQuery = source.getQueryObject();

        Query target = new Query() {

            @Override
            public Document getFieldsObject() {
                return BsonUtil.merge(sourceFields, super.getFieldsObject());
            }

            @Override
            public Document getSortObject() {
                return BsonUtil.merge(sourceSort, super.getSortObject());
            }

            @Override
            public Document getQueryObject() {
                return BsonUtil.merge(sourceQuery, super.getQueryObject());
            }

            @Override
            public boolean isSorted() {
                return source.isSorted() || super.isSorted();
            }
        };

        target.skip = source.getSkip();
        target.limit = source.getLimit();
        target.hint = source.getHint();
        target.collation = source.getCollation();


        if (source.getMeta().hasValues()) {
            target.setMeta(new Meta(source.getMeta()));
        }

        return target;
    }

    /**
     * Adds the given {@link CriteriaDefinition} to the current {@link Query}.
     *
     * @param criteriaDefinition must not be {@literal null}.
     * @return this.
     */
    public Query addCriteria(CriteriaDefinition criteriaDefinition) {

        Precondition.notNull(criteriaDefinition, "CriteriaDefinition must not be null!");

        CriteriaDefinition existing = this.criteria.get(criteriaDefinition.getKey());
        String key = criteriaDefinition.getKey();

        if (existing == null) {
            this.criteria.put(key, criteriaDefinition);
        } else {
            throw new InvalidMongoDbApiUsageException(
                    String.format("Due to limitations of the com.mongodb.BasicDocument, you can't add a second '%s' criteria. "
                            + "Query already contains '%s'", key, existing.getCriteriaObject() ==null ? "null": existing.getCriteriaObject().toJson()));
        }

        return this;
    }

    public IProjection fields() {

        if (this.projectionSpec == null) {
            this.projectionSpec = new Projection();
        }

        return this.projectionSpec;
    }

    public Query withProjection(IProjection projection) {

        Precondition.notNull(projection, "projection must not be empty or null!");
        this.projectionSpec = projection;
        return this;
    }

    /**
     * Set number of documents to skip before returning results.
     *
     * @param skip
     * @return this.
     */
    public Query skip(long skip) {
        this.skip = skip;
        return this;
    }

    /**
     * Limit the number of returned documents to {@code limit}.
     *
     * @param limit
     * @return this.
     */
    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }


    /**
     * Sets the given pagination information on the {@link Query} instance. Will transparently set {@code skip} and
     * {@code limit} as well as applying the {@link Sort} instance defined with the {@link Pageable}.
     *
     * @param pageable must not be {@literal null}.
     * @return this.
     */
    public Query with( Pageable pageable) {

        if (pageable.isUnpaged()) {
            return this;
        }

        this.limit = pageable.getPageSize();
        this.skip = pageable.getOffset();


        return with(pageable.getSort());

    }


    /**
     * Adds a {@link Sort} to the {@link Query} instance.
     *
     * @param sort must not be {@literal null}.
     * @return this.
     */
    public Query with(ISort sort) {

        Precondition.notNull(sort, "Sort must not be null!");

        if (sort.isUnsorted()) {
            return this;
        }

        this.sorts = this.sorts.and(sort);

        return this;
    }

    /**
     * Configures the query to use the given hint when being executed. The {@code hint} can either be an index name or a
     * json {@link Document} representation.
     *
     * @param hint must not be {@literal null} or empty.
     * @return this.
     */
    public Query withHint(String hint) {

        Precondition.hasText(hint, "Hint must not be empty or null!");
        this.hint = hint;
        return this;
    }

    /**
     * Configures the query to use the given {@link Document hint} when being executed.
     *
     * @param hint must not be {@literal null}.
     * @return this.
     */
    public Query withHint(Document hint) {

        Precondition.notNull(hint, "Hint must not be null!");
        this.hint = hint.toJson();
        return this;
    }


    /**
     * Adds a {@link Sort} to the {@link Query} instance.
     *
     * @param sort must not be {@literal null}.
     * @return this.
     */
    public Query with(ISort... sort) {

        Precondition.notNull(sort, "Sort must not be null!");

        if (sort.length == 0) {
            return this;
        }

        for(ISort isort :sort) {
            if (isort.isUnsorted()) {
               continue;
            }
            sorts.and(isort);
        }
        return this;
    }

    /**
     * @return the query {@link Document}.
     */
    public Document getQueryObject() {

        Document document = new Document();

        for (CriteriaDefinition definition : criteria.values()) {
            document.putAll(definition.getCriteriaObject());
        }

        return document;
    }

    /**
     * @return the field {@link Document}.
     */
    public Document getFieldsObject() {
        return this.projectionSpec == null ? new Document() : projectionSpec.getFieldsObject();
    }

    /**
     * @return the sort {@link Document}.
     */
    public Document getSortObject() {

        DocumentWriter writer = new DocumentWriter() ;
        if(isSorted()){
            ExpressionHelper.document(writer, () -> {
                for (SortType sortType : sorts.getSorts()) {
                    writer.writeName(sortType.getField());
                    sortType.getDirection().encode(writer);
                }
            });
            return writer.getDocument();
        }
        return null;
    }

    /**
     * Returns {@literal true} if the {@link Query} has a sort parameter.
     *
     * @return {@literal true} if sorted.
     */
    public boolean isSorted() {
        return ObjectUtil.isNotEmpty(sorts);
    }

    /**
     * Get the number of documents to skip.
     *
     * @return number of documents to skip
     */
    public long getSkip() {
        return this.skip;
    }

    /**
     * Get the maximum number of documents to be return.
     *
     * @return number of documents to return.
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * @return can be {@literal null}.
     */

    public String getHint() {
        return hint;
    }

    /**
     * @param maxTimeMsec
     * @return this.
     */
    public Query maxTimeMsec(long maxTimeMsec) {

        meta.setMaxTimeMsec(maxTimeMsec);
        return this;
    }

    /**
     * @param timeout
     * @param timeUnit must not be {@literal null}.
     * @return this.
     * @deprecated . Use {@link #maxTime(Duration)} instead.
     */
    @Deprecated
    public Query maxTime(long timeout, TimeUnit timeUnit) {

        meta.setMaxTime(timeout, timeUnit);
        return this;
    }

    /**
     * @param timeout must not be {@literal null}.
     * @return this.
     */
    public Query maxTime(Duration timeout) {

        meta.setMaxTime(timeout);
        return this;
    }

    /**
     * Add a comment to the query that is propagated to the profile log.
     *
     * @param comment must not be {@literal null}.
     * @return this.
     */
    public Query comment(String comment) {

        meta.setComment(comment);
        return this;
    }

    /**
     * Set the number of documents to return in each response batch. <br />
     * Use {@literal 0 (zero)} for no limit. A <strong>negative limit</strong> closes the cursor after returning a single
     * batch indicating to the server that the client will not ask for a subsequent one.
     *
     * @param batchSize The number of documents to return per batch.
     * @return this.
     */
    public Query cursorBatchSize(int batchSize) {

        meta.setCursorBatchSize(batchSize);
        return this;
    }

    /**
     * @return this.
     */
    public Query noCursorTimeout() {

        meta.addFlag(Meta.CursorOption.NO_TIMEOUT);
        return this;
    }

    /**
     * @return this.
     */
    public Query exhaust() {

        meta.addFlag(Meta.CursorOption.EXHAUST);
        return this;
    }


    /**
     * Allows querying of a replica.
     *
     * @return this.
     */
    public Query allowSecondaryReads() {

        meta.addFlag(Meta.CursorOption.SECONDARY_READS);
        return this;
    }

    /**
     * @return this.
     */
    public Query partialResults() {

        meta.addFlag(Meta.CursorOption.PARTIAL);
        return this;
    }

    /**
     * @return never {@literal null}.
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * @param meta must not be {@literal null}.
     */
    public void setMeta(Meta meta) {

        Precondition.notNull(meta, "Query meta might be empty but must not be null.");
        this.meta = meta;
    }


    /**
     * Set the {@link Collation} applying language-specific rules for string comparison.
     *
     * @param collation can be {@literal null}.
     * @return this.
     */
    public Query collation(Collation collation ) {

        this.collation = Optional.ofNullable(collation.toMongoCollation());
        return this;
    }


    /**
     * Set the {@link Collation} applying language-specific rules for string comparison.
     *
     * @param collation can be {@literal null}.
     * @return this.
     */
    public Query collation(com.mongodb.client.model.Collation collation ) {

        this.collation = Optional.ofNullable(collation);
        return this;
    }

    /**
     * Get the {@link Collation} defining language-specific rules for string comparison.
     *
     * @return never {@literal null}.
     */
    public Optional<com.mongodb.client.model.Collation> getCollation() {
        return collation;
    }

    protected List<CriteriaDefinition> getCriteria() {
        return new ArrayList<>(this.criteria.values());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Query: %s, Fields: %s, Sort: %s", getQueryObject() ==null ? "null": getQueryObject().toJson(),
                getFieldsObject()== null ? "null":getFieldsObject().toJson(), getSortObject()==null ? "null":getSortObject().toJson() );
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

        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }

        return querySettingsEquals((Query) obj);
    }

    /**
     * Tests whether the settings of the given {@link Query} are equal to this query.
     *
     * @param that
     * @return
     */
    protected boolean querySettingsEquals(Query that) {

        boolean criteriaEqual = this.criteria.equals(that.criteria);
        boolean fieldsEqual = nullSafeEquals(this.projectionSpec, that.projectionSpec);
        boolean sortEqual = this.sorts.equals(that.sorts);
        boolean hintEqual = nullSafeEquals(this.hint, that.hint);
        boolean skipEqual = this.skip == that.skip;
        boolean limitEqual = this.limit == that.limit;
        boolean metaEqual = nullSafeEquals(this.meta, that.meta);
        boolean collationEqual = nullSafeEquals(this.collation.orElse(null), that.collation.orElse(null));

        return criteriaEqual && fieldsEqual && sortEqual && hintEqual && skipEqual && limitEqual && metaEqual
                && collationEqual;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += 31 * criteria.hashCode();
        result += 31 * nullSafeHashCode(projectionSpec);
        result += 31 * nullSafeHashCode(sorts);
        result += 31 * nullSafeHashCode(hint);
        result += 31 * skip;
        result += 31 * limit;
        result += 31 * nullSafeHashCode(meta);
        result += 31 * nullSafeHashCode(collation.orElse(null));

        return result;
    }
}
