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
package com.whaleal.mars.bson.codecs;

import com.mongodb.lang.Nullable;
import com.whaleal.mars.internal.NotMappableException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Boolean.FALSE;

/**
 * Defines basic type conversions.  This class is mostly intended for internal use only but its methods are public so that when cases
 * arise where certain conversions are missing, users can add their in between releases.  However, this must be done with the understand
 * that, however unlikely, this API is subject to change and any uses might break at some point.
 * 定义基本类型转换。 此类主要仅供内部使用，但其方法是公共的，因此当情况
 * 在某些转换丢失的情况下出现，用户可以在版本之间添加它们。 但是，这必须在理解的情况下完成
 * 无论如何，此 API 可能会发生变化，并且任何使用都可能会在某些时候中断。
 */
@Slf4j
public final class Conversions {

    private static final Map<Class<?>, Map<Class<?>, Function<?, ?>>> CONVERSIONS = new HashMap<>();

    static {
        registerStringConversions();

        register(Binary.class, byte[].class, Binary::getData);

        register(Date.class, Long.class, Date::getTime);
        register(Date.class, long.class, Date::getTime);

        register(Instant.class, Long.class, Instant::toEpochMilli);
        register(Instant.class, long.class, Instant::toEpochMilli);

        register(Double.class, Long.class, Double::longValue, "Converting a double value to a long.  Possible loss of precision.");
        register(Double.class, Integer.class, Double::intValue, "Converting a double value to an int.  Possible loss of precision.");
        register(Double.class, Float.class, Double::floatValue, "Converting a double value to a float.  Possible loss of precision.");

        register(Integer.class, Byte.class, Integer::byteValue);

        register(Long.class, Double.class, Long::doubleValue);
        register(Long.class, Float.class, Long::floatValue);

        register(Float.class, Long.class, Float::longValue, "Converting a float value to a long.  Possible loss of precision.");
        register(Float.class, Integer.class, Float::intValue, "Converting a float value to an int.  Possible loss of precision.");

        register(URI.class, String.class, u -> {
            try {
                return u.toURL().toExternalForm().replace(".", "%46");
            } catch (MalformedURLException e) {
                throw new NotMappableException("Could not convert URI: " + u);
            }
        });
    }

    private Conversions() {
    }

    private static void registerStringConversions() {
        register(String.class, BigDecimal.class, BigDecimal::new);
        register(String.class, ObjectId.class, ObjectId::new);
        register(String.class, Character.class, s -> {
            if (s.length() == 1) {
                return s.charAt(0);
            } else if (s.isEmpty()) {
                return (char) 0;
            } else {
                throw new NotMappableException("Could not convert String to char: " + s);
            }
        });
        register(String.class, Boolean.class, Boolean::parseBoolean);
        register(String.class, Byte.class, Byte::parseByte);
        register(String.class, Double.class, Double::parseDouble);
        register(String.class, Integer.class, Integer::valueOf);
        register(String.class, Long.class, Long::parseLong);
        register(String.class, Float.class, Float::parseFloat);
        register(String.class, Short.class, Short::parseShort);
        register(String.class, URI.class, str -> URI.create(str.replace("%46", ".")));
    }


    public static <S, T> void register(Class<S> source, Class<T> target, Function<S, T> function) {
        register(source, target, function, null);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Nullable
    public static <T> T convert(@Nullable Object value, Class<T> target) {
        if (value == null) {
            return (T) convertNull(target);
        }

        final Class<?> fromType = value.getClass();
        if (fromType.equals(target)) {
            return (T) value;
        }

        final Function function = CONVERSIONS
                .computeIfAbsent(fromType, (f) -> new HashMap<>())
                .get(target);
        if (function == null) {
            if (target.equals(String.class)) {
                return (T) value.toString();
            }
            if (target.isEnum() && fromType.equals(String.class)) {
                return (T) Enum.valueOf((Class<? extends Enum>) target, (String) value);
            }
            return (T) value;
        }
        return (T) function.apply(value);
    }

    @Nullable
    private static Object convertNull(Class<?> toType) {
        if (isNumber(toType)) {
            return 0;
        } else if (isBoolean(toType)) {
            return FALSE;
        }
        return null;
    }


    public static <S, T> void register(Class<S> source, Class<T> target, Function<S, T> function,
                                       @Nullable String warning) {
        final Function<S, T> conversion = warning == null
                ? function
                : s -> {
            if (log.isWarnEnabled()) {
                log.warn(warning);
            }
            return function.apply(s);
        };
        CONVERSIONS.computeIfAbsent(source, (Class<?> c) -> new HashMap<>())
                .put(target, conversion);
    }

    private static boolean isNumber(Class<?> type) {
        return type.isPrimitive() && !type.equals(boolean.class);
    }

    private static boolean isBoolean(Class<?> type) {
        return type.equals(boolean.class);
    }
}
