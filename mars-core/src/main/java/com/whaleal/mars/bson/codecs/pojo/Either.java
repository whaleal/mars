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
package com.whaleal.mars.bson.codecs.pojo;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.whaleal.icefrog.core.lang.Precondition.notNull;

final class Either<L, R> {

    public static <L, R> Either<L, R> left(final L value) {
        return new Either<>(notNull("value", value), null);
    }

    public static <L, R> Either<L, R> right(final R value) {
        return new Either<>(null, notNull("value", value));
    }

    private final L left;
    private final R right;

    private Either(final L l, final R r) {
        left = l;
        right = r;
    }

    public <T> T map(final Function<? super L, ? extends T> lFunc, final Function<? super R, ? extends T> rFunc) {
        return left != null ? lFunc.apply(left) : rFunc.apply(right);
    }

    public void apply(final Consumer<? super L> lFunc, final Consumer<? super R> rFunc) {
        if (left != null) {
            lFunc.accept(left);
        }
        if (right != null) {
            rFunc.accept(right);
        }
    }

    @Override
    public String toString() {
        return "Either{"
                + "left=" + left
                + ", right=" + right
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Either<?, ?> either = (Either<?, ?>) o;
        return Objects.equals(left, either.left) && Objects.equals(right, either.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
