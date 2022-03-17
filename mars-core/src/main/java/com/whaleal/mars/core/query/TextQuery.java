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


import org.bson.Document;

import java.util.Locale;

/**

 * TextQuery  主要用于文本查找
 * 里面已经封装了 meta  相关的 project 及 sort
 * @author wh
 */
public class TextQuery extends Query {

    private final String DEFAULT_SCORE_FIELD_FIELDNAME = "score";
    private final Document META_TEXT_SCORE = new Document("$meta", "textScore");

    private String scoreFieldName = DEFAULT_SCORE_FIELD_FIELDNAME;
    private boolean includeScore = false;
    private boolean sortByScore = false;

    /**
     * Creates new {@link TextQuery} using the the given {@code wordsAndPhrases} with {@link TextCriteria}
     *
     * @param wordsAndPhrases
     * @see TextCriteria#matching(String)
     */
    public TextQuery(String wordsAndPhrases) {
        super(TextCriteria.forDefaultLanguage().matching(wordsAndPhrases));
    }

    /**
     * Creates new {@link TextQuery} in {@code language}. <br />
     * For a full list of supported languages see the mongdodb reference manual for
     * <a href="https://docs.mongodb.org/manual/reference/text-search-languages/">Text Search Languages</a>.
     *
     * @param wordsAndPhrases
     * @param language
     * @see TextCriteria#forLanguage(String)
     * @see TextCriteria#matching(String)
     */
    public TextQuery( String wordsAndPhrases, String language ) {
        super(TextCriteria.forLanguage(language).matching(wordsAndPhrases));
    }

    /**
     * Creates new {@link TextQuery} using the {@code locale}s language.<br />
     * For a full list of supported languages see the mongdodb reference manual for
     * <a href="https://docs.mongodb.org/manual/reference/text-search-languages/">Text Search Languages</a>.
     *
     * @param wordsAndPhrases must not be {@literal null}.
     * @param locale          can be {@literal null}.
     */
    public TextQuery( String wordsAndPhrases, Locale locale ) {
        this(wordsAndPhrases, locale != null ? locale.getLanguage() : (String) null);
    }

    /**
     * Creates new {@link TextQuery} for given {@link TextCriteria}.
     *
     * @param criteria must not be {@literal null}.
     */
    public TextQuery(TextCriteria criteria) {
        super(criteria);
    }

    /**
     * Creates new {@link TextQuery} searching for given {@link TextCriteria}.
     *
     * @param criteria must not be {@literal null}.
     * @return new instance of {@link TextQuery}.
     */
    public static TextQuery queryText(TextCriteria criteria) {
        return new TextQuery(criteria);
    }

    /**
     * Add sorting by text score. Will also add text score to returned fields.
     *
     * @return this.
     * @see TextQuery#includeScore()
     */
    public TextQuery sortByScore() {

        this.includeScore();
        this.sortByScore = true;
        return this;
    }

    /**
     * Add field {@literal score} holding the documents textScore to the returned fields.
     *
     * @return this.
     */
    public TextQuery includeScore() {

        this.includeScore = true;
        return this;
    }

    /**
     * Include text search document score in returned fields using the given fieldname.
     *
     * @param fieldname must not be {@literal null}.
     * @return this.
     */
    public TextQuery includeScore(String fieldname) {

        setScoreFieldName(fieldname);
        includeScore();
        return this;
    }

    /**
     * Get the fieldname used for scoring
     *
     * @return never {@literal null}.
     */
    public String getScoreFieldName() {
        return scoreFieldName;
    }

    /**
     * Set the fieldname used for scoring.
     *
     * @param fieldName must not be {@literal null}.
     */
    public void setScoreFieldName(String fieldName) {
        this.scoreFieldName = fieldName;
    }

    /*
     * (non-Javadoc)
     * @see Query#getFieldsObject()
     */
    @Override
    public Document getFieldsObject() {

        if (!this.includeScore) {
            return super.getFieldsObject();
        }

        Document fields = super.getFieldsObject();

        fields.put(getScoreFieldName(), META_TEXT_SCORE);
        return fields;
    }

    /*
     * (non-Javadoc)
     * @see Query#getSortObject()
     */
    @Override
    public Document getSortObject() {

        Document sort = new Document();

        if (this.sortByScore) {
            sort.put(getScoreFieldName(), META_TEXT_SCORE);
        }

        sort.putAll(super.getSortObject());

        return sort;
    }

    /*
     * (non-Javadoc)
     * @see Query#isSorted()
     */
    @Override
    public boolean isSorted() {
        return super.isSorted() || sortByScore;
    }
}
