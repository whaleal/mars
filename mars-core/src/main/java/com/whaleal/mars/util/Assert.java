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




import com.mongodb.internal.async.SingleResultCallback;
import com.mongodb.lang.Nullable;
import com.whaleal.mars.core.query.MarsQueryException;

import java.util.Collection;

import static java.lang.String.format;

/**
 * Provides various assertions for Mars during validation
 *
 *
 */
public final class Assert {
    private Assert() {
    }

    /**
     * Throws an QueryException with the given error message.
     *
     * @param error the error message
     */
    public static void raiseError(String error) {
        throw new MarsQueryException(error);
    }

    /**
     * Validates that all the parameters are not null
     *
     * @param names   a comma separated String of parameter names
     * @param objects the proposed parameter values
     */
    public static void parametersNotNull(String names, Object... objects) {
        String msgPrefix = "At least one of the parameters";

        if (objects != null) {
            if (objects.length == 1) {
                msgPrefix = "Parameter";
            }

            for (Object object : objects) {
                if (object == null) {
                    raiseError(String.format("%s '%s' is null.", msgPrefix, names));
                }
            }
        }
    }

    /**
     * Validates that the Iterable is not empty
     *
     * @param name the parameter name
     * @param obj  the proposed parameter value
     */
    public static void parameterNotEmpty(String name, Iterable obj) {
        if (!obj.iterator().hasNext()) {
            raiseError(format("Parameter '%s' from type '%s' is expected to NOT be empty", name, obj.getClass().getName()));
        }
    }

    /**
     * Validates that the value is not empty
     *
     * @param name  the parameter name
     * @param value the proposed parameter value
     */
    public static void parameterNotEmpty(String name, String value) {
        if (value != null && value.isEmpty()) {
            raiseError(format("Parameter '%s' is expected to NOT be empty.", name));
        }
    }


    /**
     * Throw IllegalArgumentException if the value is null.
     *
     * @param name  the parameter name
     * @param value the value that should not be null
     * @param <T>   the value type
     * @return the value
     * @throws java.lang.IllegalArgumentException if value is null
     */
    public static <T> T notNull(final String name, final T value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " can not be null");
        }
        return value;
    }

    /**
     * Throw IllegalArgumentException if the values is null or contains null.
     *
     * <p><b>Note:</b> If performance is a concern, consider deferring the integrity validation
     * to the point of actual data iteration to avoid incurring additional reference chasing for collections of complex objects.
     * However, if performance considerations are low and it is acceptable to iterate over the data twice,
     * this method can still be used for validation purposes.
     *
     * @param name   the parameter name.
     * @param values the values that should not contain null elements.
     * @param <T>    the type of elements in the collection.
     * @return the input collection if it passes the null element validation.
     * @throws java.lang.IllegalArgumentException if the input collection is null or contains null elements.
     */
    public static <T> Iterable<T> notNullElements(final String name, final Iterable<T> values) {
        if (values == null) {
            throw new IllegalArgumentException(name + " can not be null");
        }

        for (T value : values) {
            if (value == null){
                throw new IllegalArgumentException(name + " can not contain null");
            }
        }

        return values;
    }

    /**
     * Throw IllegalArgumentException if the value is null.
     *
     * @param name  the parameter name
     * @param value the value that should not be null
     * @param callback  the callback that also is passed the exception if the value is null
     * @param <T>   the value type
     * @return the value
     * @throws java.lang.IllegalArgumentException if value is null
     */
    public static <T> T notNull(final String name, final T value, final SingleResultCallback<?> callback) {
        if (value == null) {
            IllegalArgumentException exception = new IllegalArgumentException(name + " can not be null");
            callback.onResult(null, exception);
            throw exception;
        }
        return value;
    }

    /**
     * Throw IllegalStateException if the condition if false.
     *
     * @param name      the name of the state that is being checked
     * @param condition the condition about the parameter to check
     * @throws java.lang.IllegalStateException if the condition is false
     */
    public static void isTrue(final String name, final boolean condition) {
        if (!condition) {
            throw new IllegalStateException("state should be: " + name);
        }
    }

    /**
     * Throw IllegalStateException if the condition if false.
     *
     * @param name      the name of the state that is being checked
     * @param condition the condition about the parameter to check
     * @param callback  the callback that also is passed the exception if the condition is not true
     * @throws java.lang.IllegalStateException if the condition is false
     */
    public static void isTrue(final String name, final boolean condition, final SingleResultCallback<?> callback) {
        if (!condition) {
            IllegalStateException exception = new IllegalStateException("state should be: " + name);
            callback.onResult(null, exception);
            throw exception;
        }
    }

    /**
     * Throw IllegalArgumentException if the condition if false.
     *
     * @param name      the name of the state that is being checked
     * @param condition the condition about the parameter to check
     * @throws java.lang.IllegalArgumentException if the condition is false
     */
    public static void isTrueArgument(final String name, final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("state should be: " + name);
        }
    }

    /**
     * Throw IllegalArgumentException if the collection contains a null value.
     *
     * @param name       the name of the collection
     * @param collection the collection
     * @throws java.lang.IllegalArgumentException if the collection contains a null value
     */
    public static void doesNotContainNull(final String name, final Collection<?> collection) {
        // Use a loop instead of the contains method, as some implementations of that method will throw an exception if passed null as a
        // parameter (in particular, lists returned by List.of methods)
        for (Object o : collection) {
            if (o == null) {
                throw new IllegalArgumentException(name + " can not contain a null value");
            }
        }
    }

    /**
     * @param value A value to check.
     * @param <T> The type of {@code value}.
     * @return {@code null}.
     * @throws AssertionError If {@code value} is not {@code null}.
     */
    @Nullable
    public static <T> T assertNull(@Nullable final T value) throws AssertionError {
        if (value != null) {
            throw new AssertionError(value.toString());
        }
        return null;
    }

    /**
     * @param value A value to check.
     * @param <T> The type of {@code value}.
     * @return {@code value}
     * @throws AssertionError If {@code value} is {@code null}.
     */
    public static <T> T assertNotNull(@Nullable final T value) throws AssertionError {
        if (value == null) {
            throw new AssertionError();
        }
        return value;
    }

    /**
     * @param value A value to check.
     * @return {@code true}.
     * @throws AssertionError If {@code value} is {@code false}.
     */
    public static boolean assertTrue(final boolean value) throws AssertionError {
        if (!value) {
            throw new AssertionError();
        }
        return true;
    }

    /**
     * @param value A value to check.
     * @return {@code false}.
     * @throws AssertionError If {@code value} is {@code true}.
     */
    public static boolean assertFalse(final boolean value) throws AssertionError {
        if (value) {
            throw new AssertionError();
        }
        return false;
    }

    /**
     * @throws AssertionError Always
     * @return Never completes normally. The return type is {@link AssertionError} to allow writing {@code throw fail()}.
     * This may be helpful in non-{@code void} methods.
     */
    public static AssertionError fail() throws AssertionError {
        throw new AssertionError();
    }

    /**
     * @param msg The failure message.
     * @throws AssertionError Always
     * @return Never completes normally. The return type is {@link AssertionError} to allow writing {@code throw fail("failure message")}.
     * This may be helpful in non-{@code void} methods.
     */
    public static AssertionError fail(final String msg) throws AssertionError {
        throw new AssertionError(assertNotNull(msg));
    }
}
