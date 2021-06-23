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
package com.whaleal.mars.core.messaging;

import com.mongodb.lang.Nullable;
import com.whaleal.mars.util.Assert;
import com.whaleal.mars.util.ObjectUtils;

class SimpleMessage<S, T> implements Message<S, T> {

    private @Nullable
    final S raw;
    private @Nullable
    final T body;
    private final MessageProperties properties;

    /**
     * @param raw
     * @param body
     * @param properties must not be {@literal null}. Use {@link MessageProperties#empty()} instead.
     */
    SimpleMessage(@Nullable S raw, @Nullable T body, MessageProperties properties) {

        Assert.notNull(properties, "Properties must not be null! Use MessageProperties.empty() instead.");

        this.raw = raw;
        this.body = body;
        this.properties = properties;
    }


    @Override
    public S getRaw() {
        return raw;
    }


    @Override
    public T getBody() {
        return body;
    }


    @Override
    public MessageProperties getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SimpleMessage<?, ?> that = (SimpleMessage<?, ?>) o;

        if (!ObjectUtils.nullSafeEquals(this.raw, that.raw)) {
            return false;
        }
        if (!ObjectUtils.nullSafeEquals(this.body, that.body)) {
            return false;
        }
        return ObjectUtils.nullSafeEquals(this.properties, that.properties);
    }

    @Override
    public int hashCode() {
        int result = ObjectUtils.nullSafeHashCode(raw);
        result = 31 * result + ObjectUtils.nullSafeHashCode(body);
        result = 31 * result + ObjectUtils.nullSafeHashCode(properties);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleMessage(raw=" + this.getRaw() + ", body=" + this.getBody() + ", properties=" + this.getProperties()
                + ")";
    }
}
