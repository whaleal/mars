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
package com.whaleal.mars.core.index.annotation;

import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;

/**
 * Defines the collation options for an index
 * @see IndexOptions
 */
public @interface Collation {
    /**
     * Causes secondary differences to be considered in reverse order, as it is done in the French language
     *
     * @return the backwards value
     */
    boolean backwards() default false;

    /**
     * Turns on case sensitivity
     *
     * @return the case level value
     */
    boolean caseLevel() default false;

    /**
     * @return the locale
     */
    String locale();

    /**
     * @return the normalization value.  If true, normalizes text into Unicode NFD.
     */
    boolean normalization() default false;

    /**
     * @return the numeric ordering.  if true will order numbers based on numerical order and not collation order
     */
    boolean numericOrdering() default false;

    /**
     * Controls whether spaces and punctuation are considered base characters
     *
     * @return the alternate
     */
    CollationAlternate alternate() default CollationAlternate.NON_IGNORABLE;

    /**
     * Determines if Uppercase or lowercase values should come first
     *
     * @return the collation case first value
     */
    CollationCaseFirst caseFirst() default CollationCaseFirst.OFF;

    /**
     * @return the maxVariable
     */
    CollationMaxVariable maxVariable() default CollationMaxVariable.PUNCT;

    /**
     * @return the collation strength
     */
    CollationStrength strength() default CollationStrength.TERTIARY;
}
