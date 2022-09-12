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


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.mars.core.internal.MongoNamespace;

class SimpleMessage<S, T> implements Message<S, T> {

    private
    final S raw;
    private
    final T body;
    private final MongoNamespace namespace;

    /**
     * @param raw
     * @param body
     * @param namespace must not be {@literal null}.  instead.
     */
    SimpleMessage( S raw,  T body, MongoNamespace namespace) {

        Precondition.notNull(namespace, "Properties must not be null! Use MessageProperties.empty() instead.");

        this.raw = raw;
        this.body = body;
        this.namespace = namespace;
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
    public MongoNamespace getMongoNamespace() {
        return namespace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SimpleMessage<?, ?> that = (SimpleMessage<?, ?>) o;

        if (!ObjectUtil.nullSafeEquals(this.raw, that.raw)) {
            return false;
        }
        if (!ObjectUtil.nullSafeEquals(this.body, that.body)) {
            return false;
        }
        return ObjectUtil.nullSafeEquals(this.namespace, that.namespace);
    }

    @Override
    public int hashCode() {
        int result = ObjectUtil.nullSafeHashCode(raw);
        result = 31 * result + ObjectUtil.nullSafeHashCode(body);
        result = 31 * result + ObjectUtil.nullSafeHashCode(namespace);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleMessage(raw=" + this.getRaw() + ", body=" + this.getBody() + ", properties=" + this.getMongoNamespace()
                + ")";
    }
}
