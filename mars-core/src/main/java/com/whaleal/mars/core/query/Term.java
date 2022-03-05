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


import com.whaleal.icefrog.core.util.ObjectUtil;

/**
 * A {@link Term} defines one or multiple words {@link Type#WORD} or phrases {@link Type#PHRASE} to be used in the
 * context of full text search.
 */
public class Term {

    private final Type type;
    private final String raw;
    private boolean negated;

    /**
     * Creates a new {@link Term} of {@link Type#WORD}.
     *
     * @param raw
     */
    public Term(String raw) {
        this(raw, Type.WORD);
    }

    /**
     * Creates a new {@link Term} of given {@link Type}.
     *
     * @param raw
     * @param type defaulted to {@link Type#WORD} if {@literal null}.
     */
    public Term( String raw, Type type ) {
        this.raw = raw;
        this.type = type == null ? Type.WORD : type;
    }

    /**
     * Negates the term.
     *
     * @return
     */
    public Term negate() {
        this.negated = true;
        return this;
    }

    /**
     * @return return true if term is negated.
     */
    public boolean isNegated() {
        return negated;
    }

    /**
     * @return type of term. Never {@literal null}.
     */
    public Type getType() {
        return type;
    }

    /**
     * Get formatted representation of term.
     *
     * @return
     */
    public String getFormatted() {

        String formatted = Type.PHRASE.equals(type) ? quotePhrase(raw) : raw;
        return negated ? negateRaw(formatted) : formatted;
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

        if (!(o instanceof Term)) {
            return false;
        }

        Term term = (Term) o;

        return ObjectUtil.nullSafeEquals(negated, term.negated) && ObjectUtil.nullSafeEquals(type, term.type)
                && ObjectUtil.nullSafeEquals(raw, term.raw);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += ObjectUtil.nullSafeHashCode(type);
        result += ObjectUtil.nullSafeHashCode(raw);
        result += ObjectUtil.nullSafeHashCode(negated);

        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getFormatted();
    }

    protected String quotePhrase(String raw) {
        return "\"" + raw + "\"";
    }

    protected String negateRaw(String raw) {
        return "-" + raw;
    }

    public enum Type {
        WORD, PHRASE;
    }
}
