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


import com.whaleal.mars.util.Assert;

import com.whaleal.mars.util.ObjectUtil;
import com.whaleal.mars.util.StrUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link CriteriaDefinition} to be used for full text search.
 */
public class TextCriteria implements CriteriaDefinition {

    private final List<Term> terms;
    private final
    String language;
    private
    Boolean caseSensitive;
    private
    Boolean diacriticSensitive;

    /**
     * Creates a new {@link TextCriteria}.
     *
     * @see #forDefaultLanguage()
     * @see #forLanguage(String)
     */
    public TextCriteria() {
        this(null);
    }

    private TextCriteria( String language ) {

        this.language = language;
        this.terms = new ArrayList<>();
    }

    /**
     * Returns a new {@link TextCriteria} for the default language.
     *
     * @return
     */
    public static TextCriteria forDefaultLanguage() {
        return new TextCriteria();
    }

    /**
     * For a full list of supported languages see the mongodb reference manual for
     * <a href="https://docs.mongodb.org/manual/reference/text-search-languages/">Text Search Languages</a>.
     *
     * @param language
     * @return
     */
    public static TextCriteria forLanguage(String language) {

        Assert.hasText(language, "Language must not be null or empty!");
        return new TextCriteria(language);
    }

    /**
     * Configures the {@link TextCriteria} to match any of the given words.
     *
     * @param words the words to match.
     * @return
     */
    public TextCriteria matchingAny(String... words) {

        for (String word : words) {
            matching(word);
        }

        return this;
    }

    /**
     * Adds given {@link Term} to criteria.
     *
     * @param term must not be {@literal null}.
     */
    public TextCriteria matching(Term term) {

        Assert.notNull(term, "Term to add must not be null.");

        this.terms.add(term);
        return this;
    }

    /**
     * @param term
     * @return
     */
    public TextCriteria matching(String term) {

        if (StrUtil.hasText(term)) {
            matching(new Term(term));
        }
        return this;
    }

    /**
     * @param term
     * @return
     */
    public TextCriteria notMatching(String term) {

        if (StrUtil.hasText(term)) {
            matching(new Term(term, Term.Type.WORD).negate());
        }
        return this;
    }

    /**
     * @param words
     * @return
     */
    public TextCriteria notMatchingAny(String... words) {

        for (String word : words) {
            notMatching(word);
        }
        return this;
    }

    /**
     * Given value will treated as a single phrase.
     *
     * @param phrase
     * @return
     */
    public TextCriteria notMatchingPhrase(String phrase) {

        if (StrUtil.hasText(phrase)) {
            matching(new Term(phrase, Term.Type.PHRASE).negate());
        }
        return this;
    }

    /**
     * Given value will treated as a single phrase.
     *
     * @param phrase
     * @return
     */
    public TextCriteria matchingPhrase(String phrase) {

        if (StrUtil.hasText(phrase)) {
            matching(new Term(phrase, Term.Type.PHRASE));
        }
        return this;
    }

    /**
     * Optionally enable or disable case sensitive search.
     *
     * @param caseSensitive boolean flag to enable/disable.
     * @return never {@literal null}.
     */
    public TextCriteria caseSensitive(boolean caseSensitive) {

        this.caseSensitive = caseSensitive;
        return this;
    }

    /**
     * Optionally enable or disable diacritic sensitive search against version 3 text indexes.
     *
     * @param diacriticSensitive boolean flag to enable/disable.
     * @return never {@literal null}.
     */
    public TextCriteria diacriticSensitive(boolean diacriticSensitive) {

        this.diacriticSensitive = diacriticSensitive;
        return this;
    }


    @Override
    public String getKey() {
        return "$text";
    }


    @Override
    public Document getCriteriaObject() {

        Document document = new Document();

        if (StrUtil.hasText(language)) {
            document.put("$language", language);
        }

        if (!terms.isEmpty()) {
            document.put("$search", join(terms));
        }

        if (caseSensitive != null) {
            document.put("$caseSensitive", caseSensitive);
        }

        if (diacriticSensitive != null) {
            document.put("$diacriticSensitive", diacriticSensitive);
        }

        return new Document("$text", document);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof TextCriteria)) {
            return false;
        }

        TextCriteria that = (TextCriteria) o;

        return ObjectUtil.nullSafeEquals(terms, that.terms) && ObjectUtil.nullSafeEquals(language, that.language)
                && ObjectUtil.nullSafeEquals(caseSensitive, that.caseSensitive)
                && ObjectUtil.nullSafeEquals(diacriticSensitive, that.diacriticSensitive);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += ObjectUtil.nullSafeHashCode(terms);
        result += ObjectUtil.nullSafeHashCode(language);
        result += ObjectUtil.nullSafeHashCode(caseSensitive);
        result += ObjectUtil.nullSafeHashCode(diacriticSensitive);

        return result;
    }

    private String join(Iterable<Term> terms) {

        List<String> result = new ArrayList<>();

        for (Term term : terms) {
            if (term != null) {
                result.add(term.getFormatted());
            }
        }

        return StrUtil.collectionToDelimitedString(result, " ");
    }
}
