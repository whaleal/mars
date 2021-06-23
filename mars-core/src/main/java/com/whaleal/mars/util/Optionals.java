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

import com.whaleal.mars.core.query.Streamable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Utility methods to work with {@link Optional}s.
 */
public interface Optionals {

    /**
     * Returns whether any of the given {@link Optional}s is present.
     *
     * @param optionals must not be {@literal null}.
     * @return
     */
    public static boolean isAnyPresent(Optional<?>... optionals) {

        Assert.notNull(optionals, "Optionals must not be null!");

        return Arrays.stream(optionals).anyMatch(Optional::isPresent);
    }

    /**
     * Turns the given {@link Optional} into a one-element {@link Stream} or an empty one if not present.
     *
     * @param optionals must not be {@literal null}.
     * @return
     */
    @SafeVarargs
    public static <T> Stream<T> toStream(Optional<? extends T>... optionals) {

        Assert.notNull(optionals, "Optional must not be null!");

        return Arrays.asList(optionals).stream().flatMap(it -> it.map(Stream::of).orElseGet(Stream::empty));
    }

    /**
     * Applies the given function to the elements of the source and returns the first non-empty result.
     *
     * @param source   must not be {@literal null}.
     * @param function must not be {@literal null}.
     * @return
     */
    public static <S, T> Optional<T> firstNonEmpty(Iterable<S> source, Function<S, Optional<T>> function) {

        Assert.notNull(source, "Source must not be null!");
        Assert.notNull(function, "Function must not be null!");

        return Streamable.of(source).stream()//
                .map(function::apply)//
                .filter(Optional::isPresent)//
                .findFirst().orElseGet(Optional::empty);
    }

    /**
     * Applies the given function to the elements of the source and returns the first non-empty result.
     *
     * @param source   must not be {@literal null}.
     * @param function must not be {@literal null}.
     * @return
     */
    public static <S, T> T firstNonEmpty(Iterable<S> source, Function<S, T> function, T defaultValue) {

        Assert.notNull(source, "Source must not be null!");
        Assert.notNull(function, "Function must not be null!");

        return Streamable.of(source).stream()//
                .map(function::apply)//
                .filter(it -> !it.equals(defaultValue))//
                .findFirst().orElse(defaultValue);
    }

    /**
     * Invokes the given {@link Supplier}s for {@link Optional} results one by one and returns the first non-empty one.
     *
     * @param suppliers must not be {@literal null}.
     * @return
     */
    @SafeVarargs
    public static <T> Optional<T> firstNonEmpty(Supplier<Optional<T>>... suppliers) {

        Assert.notNull(suppliers, "Suppliers must not be null!");

        return firstNonEmpty(Streamable.of(suppliers));
    }

    /**
     * Invokes the given {@link Supplier}s for {@link Optional} results one by one and returns the first non-empty one.
     *
     * @param suppliers must not be {@literal null}.
     * @return
     */
    public static <T> Optional<T> firstNonEmpty(Iterable<Supplier<Optional<T>>> suppliers) {

        Assert.notNull(suppliers, "Suppliers must not be null!");

        return Streamable.of(suppliers).stream()//
                .map(Supplier::get)//
                .filter(Optional::isPresent)//
                .findFirst().orElse(Optional.empty());
    }

    /**
     * Returns the next element of the given {@link Iterator} or {@link Optional#empty()} in case there is no next
     * element.
     *
     * @param iterator must not be {@literal null}.
     * @return
     */
    public static <T> Optional<T> next(Iterator<T> iterator) {

        Assert.notNull(iterator, "Iterator must not be null!");

        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }

    /**
     * Returns a {@link Pair} if both {@link Optional} instances have values or {@link Optional#empty()} if one or both
     * are missing.
     *
     * @param left
     * @param right
     * @return
     */
    public static <T, S> Optional<Pair<T, S>> withBoth(Optional<T> left, Optional<S> right) {
        return left.flatMap(l -> right.map(r -> Pair.of(l, r)));
    }

    /**
     * Invokes the given {@link BiConsumer} if all given {@link Optional} are present.
     *
     * @param left     must not be {@literal null}.
     * @param right    must not be {@literal null}.
     * @param consumer must not be {@literal null}.
     */
    public static <T, S> void ifAllPresent(Optional<T> left, Optional<S> right, BiConsumer<T, S> consumer) {

        Assert.notNull(left, "Optional must not be null!");
        Assert.notNull(right, "Optional must not be null!");
        Assert.notNull(consumer, "Consumer must not be null!");

        mapIfAllPresent(left, right, (l, r) -> {
            consumer.accept(l, r);
            return null;
        });
    }

    /**
     * Maps the values contained in the given {@link Optional} if both of them are present.
     *
     * @param left     must not be {@literal null}.
     * @param right    must not be {@literal null}.
     * @param function must not be {@literal null}.
     * @return
     */
    public static <T, S, R> Optional<R> mapIfAllPresent(Optional<T> left, Optional<S> right,
                                                        BiFunction<T, S, R> function) {

        Assert.notNull(left, "Optional must not be null!");
        Assert.notNull(right, "Optional must not be null!");
        Assert.notNull(function, "BiFunctionmust not be null!");

        return left.flatMap(l -> right.map(r -> function.apply(l, r)));
    }

    /**
     * Invokes the given {@link Consumer} if the {@link Optional} is present or the {@link Runnable} if not.
     *
     * @param optional must not be {@literal null}.
     * @param consumer must not be {@literal null}.
     * @param runnable must not be {@literal null}.
     */
    public static <T> void ifPresentOrElse(Optional<T> optional, Consumer<? super T> consumer, Runnable runnable) {

        Assert.notNull(optional, "Optional must not be null!");
        Assert.notNull(consumer, "Consumer must not be null!");
        Assert.notNull(runnable, "Runnable must not be null!");

        if (optional.isPresent()) {
            optional.ifPresent(consumer);
        } else {
            runnable.run();
        }
    }
}
