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
package com.whaleal.mars.gridfs;


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ObjectUtil;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


public class Lazy<T> implements Supplier<T> {

    private static final Lazy<?> EMPTY = new Lazy<>(() -> null, null, true);

    private final Supplier<? extends T> supplier;

    private T value;
    private volatile boolean resolved;

    /**
     * Creates a new {@link Lazy} instance for the given supplier.
     *
     * @param supplier
     * @deprecated prefer {@link Lazy#of(Supplier)}, to be turned private in 2.5.
     */
    @Deprecated
    public Lazy(Supplier<? extends T> supplier) {
        this(supplier, null, false);
    }

    /**
     * Creates a new {@link Lazy} for the given {@link Supplier}, value and whether it has been resolved or not.
     *
     * @param supplier must not be {@literal null}.
     * @param value    can be {@literal null}.
     * @param resolved whether the value handed into the constructor represents a resolved value.
     */
    private Lazy( Supplier<? extends T> supplier, T value, boolean resolved ) {

        this.supplier = supplier;
        this.value = value;
        this.resolved = resolved;
    }

    /**
     * Creates a new {@link Lazy} to produce an object lazily.
     *
     * @param <T>      the type of which to produce an object of eventually.
     * @param supplier the {@link Supplier} to create the object lazily.
     * @return
     */
    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return new Lazy<>(supplier);
    }

    /**
     * Creates a new {@link Lazy} to return the given value.
     *
     * @param <T>   the type of the value to return eventually.
     * @param value the value to return.
     * @return
     */
    public static <T> Lazy<T> of(T value) {

        Precondition.notNull(value, "Value must not be null!");

        return new Lazy<>(() -> value);
    }

    /**
     * Creates a pre-resolved empty {@link Lazy}.
     *
     * @return
     *
     */
    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> empty() {
        return (Lazy<T>) EMPTY;
    }

    /**
     * Returns the value created by the configured {@link Supplier}. Will return the calculated instance for subsequent
     * lookups.
     *
     * @return
     */
    public T get() {

        T value = getNullable();

        if (value == null) {
            throw new IllegalStateException("Expected lazy evaluation to yield a non-null value but got null!");
        }

        return value;
    }

    /**
     * Returns the {@link Optional} value created by the configured {@link Supplier}, allowing the absence of values in
     * contrast to {@link #get()}. Will return the calculated instance for subsequent lookups.
     *
     * @return
     */
    public Optional<T> getOptional() {
        return Optional.ofNullable(getNullable());
    }

    /**
     * Returns a new Lazy that will consume the given supplier in case the current one does not yield in a result.
     *
     * @param supplier must not be {@literal null}.
     * @return
     */
    public Lazy<T> or(Supplier<? extends T> supplier) {

        Precondition.notNull(supplier, "Supplier must not be null!");

        return Lazy.of(() -> orElseGet(supplier));
    }

    /**
     * Returns a new Lazy that will return the given value in case the current one does not yield in a result.
     *
     * @return
     */
    public Lazy<T> or(T value) {

        Precondition.notNull(value, "Value must not be null!");

        return Lazy.of(() -> orElse(value));
    }

    /**
     * Returns the value of the lazy computation or the given default value in case the computation yields
     * {@literal null}.
     *
     * @param value
     * @return
     */

    public T orElse( T value ) {

        T nullable = getNullable();

        return nullable == null ? value : nullable;
    }

    /**
     * Returns the value of the lazy computation or the value produced by the given {@link Supplier} in case the original
     * value is {@literal null}.
     *
     * @param supplier must not be {@literal null}.
     * @return
     */

    private T orElseGet(Supplier<? extends T> supplier) {

        Precondition.notNull(supplier, "Default value supplier must not be null!");

        T value = getNullable();

        return value == null ? supplier.get() : value;
    }

    /**
     * Creates a new {@link Lazy} with the given {@link Function} lazily applied to the current one.
     *
     * @param function must not be {@literal null}.
     * @return
     */
    public <S> Lazy<S> map(Function<? super T, ? extends S> function) {

        Precondition.notNull(function, "Function must not be null!");

        return Lazy.of(() -> function.apply(get()));
    }

    /**
     * Creates a new {@link Lazy} with the given {@link Function} lazily applied to the current one.
     *
     * @param function must not be {@literal null}.
     * @return
     */
    public <S> Lazy<S> flatMap(Function<? super T, Lazy<? extends S>> function) {

        Precondition.notNull(function, "Function must not be null!");

        return Lazy.of(() -> function.apply(get()).get());
    }

    /**
     * Returns the value of the lazy evaluation.
     *
     * @return
     *
     */

    public T getNullable() {

        if (resolved) {
            return value;
        }

        this.value = supplier.get();
        this.resolved = true;

        return value;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object o ) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Lazy)) {
            return false;
        }

        Lazy<?> lazy = (Lazy<?>) o;

        if (resolved != lazy.resolved) {
            return false;
        }

        if (!ObjectUtil.nullSafeEquals(supplier, lazy.supplier)) {
            return false;
        }

        return ObjectUtil.nullSafeEquals(value, lazy.value);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = ObjectUtil.nullSafeHashCode(supplier);

        result = 31 * result + ObjectUtil.nullSafeHashCode(value);
        result = 31 * result + (resolved ? 1 : 0);

        return result;
    }
}