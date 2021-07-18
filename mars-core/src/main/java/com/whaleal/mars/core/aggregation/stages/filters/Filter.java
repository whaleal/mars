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
package com.whaleal.mars.core.aggregation.stages.filters;

import com.mongodb.lang.Nullable;
import com.whaleal.mars.bson.codecs.MongoMappingContext;
import com.whaleal.mars.bson.codecs.pojo.PropertyModel;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.internal.PathTarget;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import static java.lang.String.format;

/**
 * Base class for query filters
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Filter {
    private final String name;
    private String field;
    @Nullable
    private Object value;
    private boolean not;
    private boolean validate;
    private Class<?> entityClass;
    private PathTarget pathTarget;
    private boolean mapped;

    protected Filter(String name) {
        this.name = name;
    }

    protected Filter(String name, @Nullable String field, @Nullable Object value) {
        this.name = name;
        this.field = field != null ? field : "";
        this.value = value;
    }

    /**
     * @return true if this filter has been notted
     * @see #not()
     */
    public boolean isNot() {
        return not;
    }

    /**
     * @param mapper  the mapper
     * @param writer  the writer
     * @param context the context
     */
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        String path = path(mapper);
        if (path != null) {
            ExpressionHelper.document(writer, path, () -> {
                if (not) {
                    ExpressionHelper.document(writer, "$not", () -> {
                        writeNamedValue(name, getValue(mapper), mapper, writer, context);
                    });
                } else {
                    writeNamedValue(name, getValue(mapper), mapper, writer, context);
                }
            });
        }
    }

    /**
     * Sets the query entity type on the filter
     *
     * @param type the type
     * @return this
     */
    public Filter entityType(Class<?> type) {
        this.entityClass = type;
        return this;
    }

    /**
     * @return the filter field
     */
    @Nullable
    public String getField() {
        return field;
    }

    /**
     * Negates this filter by wrapping in "$not: {}"
     *
     * @return this
     * @query.filter $not
     */
    public Filter not() {
        this.not = true;
        return this;
    }

    /**
     * @return the filter name
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * @return the filter value
     */
    @Nullable
    public Object getValue() {
        return value;
    }

    /**
     * Sets whether to validate field names or not
     *
     * @param validate true to validate
     * @return this
     */
    public Filter isValidating(boolean validate) {
        this.validate = validate;
        pathTarget = null;
        mapped = false;
        return this;
    }

    @Nullable
    protected Object getValue(MongoMappingContext mapper) {
        if (!mapped) {
            PathTarget target = pathTarget(mapper);
            OperationTarget operationTarget = new OperationTarget(pathTarget, value);
            this.value = operationTarget.getValue();
            PropertyModel property = target.getTarget();
            if (property != null) {
                this.value = ((Document) operationTarget.encode(mapper)).get(field);
            }
            mapped = true;
        }
        return value;
    }

    @Override
    public String toString() {
        return format("%s %s %s", field, name, value);
    }

    protected String path(MongoMappingContext mapper) {
        return pathTarget(mapper).translatedPath();
    }

    private PathTarget pathTarget(MongoMappingContext mapper) {
        if (pathTarget == null) {
            pathTarget = new PathTarget(mapper, entityClass, field, validate);
        }

        return pathTarget;
    }

    protected void writeNamedValue(@Nullable String name, @Nullable Object named, MongoMappingContext mapper, BsonWriter writer,
                                   EncoderContext encoderContext) {
        writer.writeName(name);
        if (named != null) {
            Codec codec = mapper.getCodecRegistry().get(named.getClass());
            encoderContext.encodeWithChildContext(codec, writer, named);
        } else {
            writer.writeNull();
        }
    }

    protected void writeUnnamedValue(@Nullable Object value, MongoMappingContext mapper, BsonWriter writer, EncoderContext encoderContext) {
        if (value != null) {
            Codec codec = mapper.getCodecRegistry().get(value.getClass());
            encoderContext.encodeWithChildContext(codec, writer, value);
        } else {
            writer.writeNull();
        }
    }
}
