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
package com.whaleal.mars.core.mapping;


import com.whaleal.icefrog.core.lang.Precondition;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.Optional;

/**
 * Provider interface to obtain {@link CodecRegistry} from the underlying MongoDB Java driver.
 */
@FunctionalInterface
public interface CodecRegistryProvider {

    /**
     * Get the underlying {@link CodecRegistry} used by the MongoDB Java driver.
     *
     * @return never {@literal null}.
     * @throws IllegalStateException if {@link CodecRegistry} cannot be obtained.
     */
    CodecRegistry getCodecRegistry();

    /**
     * Checks if a {@link Codec} is registered for a given type.
     *
     * @param type must not be {@literal null}.
     * @return true if {@link #getCodecRegistry()} holds a {@link Codec} for given type.
     * @throws IllegalStateException if {@link CodecRegistry} cannot be obtained.
     */
    default boolean hasCodecFor(Class<?> type) {
        return getCodecFor(type).isPresent();
    }

    /**
     * Get the {@link Codec} registered for the given {@literal type} or an {@link Optional#empty() empty Optional}
     * instead.
     *
     * @param type must not be {@literal null}.
     * @param <T>
     * @return never {@literal null}.
     * @throws IllegalArgumentException if {@literal type} is {@literal null}.
     */
    default <T> Optional<Codec<T>> getCodecFor(Class<T> type) {

        Precondition.notNull(type, "Type must not be null!");

        try {
            return Optional.of(getCodecRegistry().get(type));
        } catch (CodecConfigurationException e) {
            // ignore
        }
        return Optional.empty();
    }
}
