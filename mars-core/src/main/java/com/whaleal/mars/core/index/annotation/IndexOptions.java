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


import java.lang.annotation.*;

/**
 * Defines the options to be used when declaring an index.
 * <p>
 *
 * @see com.whaleal.mars.session.option.IndexOptions  两者统一
 * 索引明细  主要分为以下几类
 * 通用类型
 *
 *
 *    background
 *    unique
 *    name
 *    partialFilterExpression
 *    sparse
 *    expireAfterSeconds
 *    hidden
 *
 *
 * Collation
 *    locale: <string>,
 *    caseLevel: <boolean>,
 *    caseFirst: <string>,
 *    strength: <int>,
 *    numericOrdering: <boolean>,
 *    alternate: <string>,
 *    maxVariable: <string>,
 *    backwards: <boolean>
 *    normalization: <boolean>
 *
 * Text
 *  weights
 *  default_language
 *  language_override
 *  textIndexVersion
 * -----以下暂不考虑支持-------
 * 2dsphere
 *  2dsphereIndexVersion
 * 2d
 *  bit
 *  min
 *  max
 * geoHaystack
 *  bucketSize
 * wildcard
 *  wildcardProjection
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface IndexOptions {

    /**
     * @return if true, create the index in the background
     */
    boolean background() default true;

    /**
     * @return The name of the index to create; default is to let the mongodb create a name (in the form of key1_1/-1_key2_1/-1...)
     */
    String name() default "";

    /**
     * @return if true, create the index with the sparse option
     */
    boolean sparse() default false;

    /**
     * @return if true, creates the index as a unique value index; inserting duplicates values in this field will cause errors
     */
    boolean unique() default false;

    /**
     * @return defines the time to live for documents in the collection
     */
    int expireAfterSeconds() default -1;

    /**
     *
     * @return  A flag that determines whether the index is hidden from the query planner. A hidden index is not evaluated as part of the query plan selection.
     */
    boolean hidden() default false ;

    /**
     * @return The default language for the index.
     */
    String language() default "";

    /**
     * @return The field to use to override the default language.
     */
    String languageOverride() default "";

    /**
     * @return the filter to be used for this index
     */
    String partialFilter() default "";

    /**
     * @return the collation to be used for this index
     */
    Collation collation() default @Collation(locale = "");


    /**
     * @return if true, disables validation for the field name
     */
    boolean disableValidation() default false;
}
