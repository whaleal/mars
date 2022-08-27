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


import com.whaleal.mars.core.index.IndexDirection;

import java.lang.annotation.*;

/**
 * Define a field to be used in an index;
 *
 * @see IndexDirection
 * @mongodb.manual  https://www.mongodb.com/docs/v6.0/tutorial/control-results-of-text-search/
 * @code
 * db.blog.createIndex(
 *    {
 *      content: "text",
 *      keywords: "text",
 *      about: "text"
 *    },
 *    {
 *      weights: {
 *        content: 10,
 *        keywords: 5
 *      },
 *      name: "TextIndex"
 *    }
 *  )
 *
 *
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Field {
    /**
     * @return "IndexDirection" of the indexing.  Defaults to {@link IndexDirection#ASC}.
     */
    IndexDirection type() default IndexDirection.ASC;

    /**
     * @return Projection name to index
     */
    String value();

    /**
     * default in mongodb is 1
     * when -1  notwork
     * @return The weight to use when creating a text index.  This value only makes sense when direction is {@link IndexDirection#TEXT}
     */
    int weight() default -1;
}
