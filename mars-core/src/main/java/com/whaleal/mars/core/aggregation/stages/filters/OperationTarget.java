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


import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.PropertyModel;
import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.internal.PathTarget;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;

import java.util.StringJoiner;


public class OperationTarget {
    private final PathTarget target;
    private final Object value;

    /**
     * @param target the target
     * @param value  the value
     */
    public OperationTarget( PathTarget target, Object value ) {
        this.target = target;
        this.value = value;
    }

    /**
     * Encodes this target
     *
     * @param mapper the mapper
     * @return the encoded form
     */
    public Object encode(MongoMappingContext mapper) {
        if (target == null) {
            if (value == null) {
                throw new NullPointerException();
            }
            return value;
        }
        PropertyModel model = this.target.getTarget();
        Object mappedValue = value;


        Codec cachedCodec = model.getCodec();


        DocumentWriter writer = new DocumentWriter();
        Object finalMappedValue = mappedValue;
        ExpressionHelper.document(writer, () -> ExpressionHelper.value(mapper, writer, "mapped", finalMappedValue, EncoderContext.builder().build()));
        mappedValue = writer.getDocument().get("mapped");

        return new Document(target.translatedPath(), mappedValue);
    }

    /**
     * @return the PathTarget for this instance
     */

    public PathTarget getTarget() {
        return target;
    }

    /**
     * @return the value
     */

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OperationTarget.class.getSimpleName() + "[", "]")
                .add("target=" + target)
                .add("value=" + value)
                .toString();
    }
}
