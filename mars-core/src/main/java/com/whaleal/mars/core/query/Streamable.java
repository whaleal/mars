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
package com.whaleal.mars.core.query;

import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.stream.StreamUtil;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple interface to ease streamability of {@link Iterable}s.
 */
@FunctionalInterface
public interface Streamable<T> extends Iterable<T>, Supplier<Stream<T>> {

    /**
     * Returns an empty {@link Streamable}.
     *
     * @return will never be {@literal null}.
     */
    static <T> Streamable<T> empty() {
        return Collections::emptyIterator;
    }

    /**
     * Returns a {@link Streamable} with the given elements.
     *
     * @param t the elements to return.
     * @return
     */
    @SafeVarargs
    static <T> Streamable<T> of(T... t) {
        return () -> Arrays.asList(t).iterator();
    }

    /**
     * Returns a {@link Streamable} for the given {@link Iterable}.
     *
     * @param iterable must not be {@literal null}.
     * @return
     */
    static <T> Streamable<T> of(Iterable<T> iterable) {

        Precondition.notNull(iterable, "Iterable must not be null!");

        return iterable::iterator;
    }

    static <T> Streamable<T> of(Supplier<? extends Stream<T>> supplier) {
        return LazyStreamable.of(supplier);
    }

    /**
     * Creates a non-parallel {@link Stream} of the underlying {@link Iterable}.
     *
     * @return will never be {@literal null}.
     * 本质方法
     */
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a new {@link Streamable} that will apply the given {@link Function} to the current one.
     *
     * @param mapper must not be {@literal null}.
     * @return
     * @see Stream#map(Function)
     */
    default <R> Streamable<R> map(Function<? super T, ? extends R> mapper) {

        Precondition.notNull(mapper, "Mapping function must not be null!");

        return Streamable.of(() -> stream().map(mapper));
    }

    /**
     * Returns a new {@link Streamable} that will apply the given {@link Function} to the current one.
     *
     * @param mapper must not be {@literal null}.
     * @return
     * @see Stream#flatMap(Function)
     */
    default <R> Streamable<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {

        Precondition.notNull(mapper, "Mapping function must not be null!");

        return Streamable.of(() -> stream().flatMap(mapper));
    }

    /**
     * Returns a new {@link Streamable} that will apply the given filter {@link Predicate} to the current one.
     *
     * @param predicate must not be {@literal null}.
     * @return
     * @see Stream#filter(Predicate)
     */
    default Streamable<T> filter(Predicate<? super T> predicate) {

        Precondition.notNull(predicate, "Filter predicate must not be null!");

        return Streamable.of(() -> stream().filter(predicate));
    }

    /**
     * Returns whether the current {@link Streamable} is empty.
     *
     * @return
     */
    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    /**
     * Creates a new {@link Streamable} from the current one and the given {@link Stream} concatenated.
     *
     * @param stream must not be {@literal null}.
     * @return
     * 
     */
    default Streamable<T> and(Supplier<? extends Stream<? extends T>> stream) {

        Precondition.notNull(stream, "Stream must not be null!");

        return Streamable.of(() -> Stream.concat(this.stream(), stream.get()));
    }

    /**
     * Creates a new {@link Streamable} from the current one and the given values concatenated.
     *
     * @param others must not be {@literal null}.
     * @return will never be {@literal null}.
     *  2.2
     */
    @SuppressWarnings("unchecked")
    default Streamable<T> and(T... others) {

        Precondition.notNull(others, "Other values must not be null!");

        return Streamable.of(() -> Stream.concat(this.stream(), Arrays.stream(others)));
    }

    /**
     * Creates a new {@link Streamable} from the current one and the given {@link Iterable} concatenated.
     *
     * @param iterable must not be {@literal null}.
     * @return will never be {@literal null}.
     *  2.2
     */
    default Streamable<T> and(Iterable<? extends T> iterable) {

        Precondition.notNull(iterable, "Iterable must not be null!");

        return Streamable.of(() -> Stream.concat(this.stream(), StreamSupport.stream(iterable.spliterator(), false)));
    }

    /**
     * Convenience method to allow adding a {@link Streamable} directly as otherwise the invocation is ambiguous between
     * {@link #and(Iterable)} and {@link #and(Supplier)}.
     *
     * @param streamable must not be {@literal null}.
     * @return will never be {@literal null}.
     *  2.2
     */
    default Streamable<T> and(Streamable<? extends T> streamable) {
        return and((Supplier<? extends Stream<? extends T>>) streamable);
    }

    /**
     * Creates a new, unmodifiable {@link List}.
     *
     * @return will never be {@literal null}.
     *  2.2
     */
    default List<T> toList() {
        return stream().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Creates a new, unmodifiable {@link Set}.
     *
     * @return will never be {@literal null}.
     *  2.2
     */
    default Set<T> toSet() {
        return stream().collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
    }

    /*
     * (non-Javadoc)
     * @see java.util.function.Supplier#get()
     */
    default Stream<T> get() {
        return stream();
    }

    /**
     * A collector to easily produce a {@link Streamable} from a {@link Stream} using {@link Collectors#toList} as
     * intermediate collector.
     *
     * @return
     * @see #toStreamable(Collector)
     *  2.2
     */
    public static <S> Collector<S, ?, Streamable<S>> toStreamable() {
        return toStreamable(Collectors.toList());
    }

    /**
     * A collector to easily produce a {@link Streamable} from a {@link Stream} and the given intermediate collector.
     *
     * @return
     *  2.2
     */
    @SuppressWarnings("unchecked")
    public static <S, T extends Iterable<S>> Collector<S, ?, Streamable<S>> toStreamable(
            Collector<S, ?, T> intermediate) {

        return Collector.of( //
                (Supplier<T>) intermediate.supplier(), //
                (BiConsumer<T, S>) intermediate.accumulator(), //
                (BinaryOperator<T>) intermediate.combiner(), //
                Streamable::of);
    }
}
