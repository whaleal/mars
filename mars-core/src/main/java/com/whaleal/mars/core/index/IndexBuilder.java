/**
 * Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 * <p>
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.whaleal.com/licensing/server-side-public-license>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the Server Side Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.whaleal.mars.core.index;

import com.whaleal.mars.core.index.annotation.Field;
import com.whaleal.mars.core.index.annotation.Index;
import com.whaleal.mars.core.index.annotation.IndexOptions;

import java.util.List;

/**
 * Index  注解的子类
 * @see com.whaleal.mars.core.index.annotation.Index
 * @see com.whaleal.mars.core.index.AnnotationBuilder
 * @see FieldBuilder
 *
 *
 * 用于注解的 索引创建
 * @author wh
 */
class IndexBuilder extends AnnotationBuilder< Index > implements Index {
    IndexBuilder() {
    }

    IndexBuilder( Index original ) {
        super(original);
    }

    @Override
    public Class< Index > annotationType() {
        return Index.class;
    }

    @Override
    public Field[] fields() {
        return get("fields");
    }

    @Override
    public IndexOptions options() {
        return get("options");
    }

    IndexBuilder fields( List< Field > fields ) {
        put("fields", fields.toArray(new Field[0]));
        return this;
    }

    IndexBuilder fields( Field... fields ) {
        put("fields", fields);
        return this;
    }

    /**
     * Options to apply to the index.  Use of this field will ignore any of the deprecated options defined on {@link Index} directly.
     */
    IndexBuilder options( IndexOptions options ) {
        put("options", options);
        return this;
    }

    /**
     * Create the index in the background
     */
    IndexBuilder background( boolean background ) {
        put("background", background);
        return this;
    }

    /**
     * The name of the index to create; default is to let the mongodb create a name (in the form of key1_1/-1_key2_1/-1...)
     */
    IndexBuilder name( String name ) {
        put("name", name);
        return this;
    }

    /**
     * Create the index with the sparse option
     */
    IndexBuilder sparse( boolean sparse ) {
        put("sparse", sparse);
        return this;
    }

    /**
     * Creates the index as a unique value index; inserting duplicates values in this field will cause errors
     */
    IndexBuilder unique( boolean unique ) {
        put("unique", unique);
        return this;
    }

    /**
     * List of fields (prepended with "-" for desc; defaults to asc).
     */
    IndexBuilder value( String value ) {
        put("value", value);
        return this;
    }
}
