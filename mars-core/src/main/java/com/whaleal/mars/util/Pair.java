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
package com.whaleal.mars.util;


import com.mongodb.lang.Nullable;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A tuple of things.
 *
 * @param <S> Type of the first thing.
 * @param <T> Type of the second thing.
 */
public final class Pair<S, T> {

    private final S first;
    private final T second;

    private Pair(S first, T second) {

        Assert.notNull(first, "First must not be null!");
        Assert.notNull(second, "Second must not be null!");

        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new {@link Pair} for the given elements.
     *
     * @param first  must not be {@literal null}.
     * @param second must not be {@literal null}.
     * @return
     */
    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair<>(first, second);
    }

    /**
     * Returns the first element of the {@link Pair}.
     *
     * @return
     */
    public S getFirst() {
        return first;
    }

    /**
     * Returns the second element of the {@link Pair}.
     *
     * @return
     */
    public T getSecond() {
        return second;
    }

    /**
     * A collector to create a {@link Map} from a {@link Stream} of {@link Pair}s.
     *
     * @return
     */
    public static <S, T> Collector<Pair<S, T>, ?, Map<S, T>> toMap() {
        return Collectors.toMap(Pair::getFirst, Pair::getSecond);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(@Nullable Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Pair)) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!ObjectUtils.nullSafeEquals(first, pair.first)) {
            return false;
        }

        return ObjectUtils.nullSafeEquals(second, pair.second);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = ObjectUtils.nullSafeHashCode(first);
        result = 31 * result + ObjectUtils.nullSafeHashCode(second);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s->%s", this.first, this.second);
    }
}
