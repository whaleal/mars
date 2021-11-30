/*
 * Copyright 2010-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
package com.whaleal.mars.session.option;


import com.whaleal.mars.core.query.Collation;

import java.util.Optional;

public class FindAndModifyOptions {

    private static final FindAndModifyOptions NONE = new FindAndModifyOptions() {

        private static final String ERROR_MSG = "FindAndModifyOptions.none() cannot be changed. Please use FindAndModifyOptions.options() instead.";

        @Override
        public FindAndModifyOptions returnNew(boolean returnNew) {
            throw new UnsupportedOperationException(ERROR_MSG);
        }

        @Override
        public FindAndModifyOptions upsert(boolean upsert) {
            throw new UnsupportedOperationException(ERROR_MSG);
        }

        @Override
        public FindAndModifyOptions remove(boolean remove) {
            throw new UnsupportedOperationException(ERROR_MSG);
        }

        @Override
        public FindAndModifyOptions collation( Collation collation ) {
            throw new UnsupportedOperationException(ERROR_MSG);
        }
    };
    private boolean returnNew;
    private boolean upsert;
    private boolean remove;
    private
    Collation collation;

    /**
     * Static factory method to create a FindAndModifyOptions instance
     *
     * @return new instance of {@link FindAndModifyOptions}.
     */
    public static FindAndModifyOptions options() {
        return new FindAndModifyOptions();
    }

    /**
     * Static factory method returning an unmodifiable {@link FindAndModifyOptions} instance.
     *
     * @return unmodifiable {@link FindAndModifyOptions} instance.
     */
    public static FindAndModifyOptions none() {
        return NONE;
    }

    /**
     * Create new {@link FindAndModifyOptions} based on option of given {@literal source}.
     *
     * @param source can be {@literal null}.
     * @return new instance of {@link FindAndModifyOptions}.
     */
    public static FindAndModifyOptions of( FindAndModifyOptions source ) {

        FindAndModifyOptions options = new FindAndModifyOptions();
        if (source == null) {
            return options;
        }

        options.returnNew = source.returnNew;
        options.upsert = source.upsert;
        options.remove = source.remove;
        options.collation = source.collation;

        return options;
    }

    public FindAndModifyOptions returnNew(boolean returnNew) {
        this.returnNew = returnNew;
        return this;
    }

    public FindAndModifyOptions upsert(boolean upsert) {
        this.upsert = upsert;
        return this;
    }

    public FindAndModifyOptions remove(boolean remove) {
        this.remove = remove;
        return this;
    }

    /**
     * Define the {@link Collation} specifying language-specific rules for string comparison.
     *
     * @param collation can be {@literal null}.
     * @return this.
     */
    public FindAndModifyOptions collation( Collation collation ) {

        this.collation = collation;
        return this;
    }

    public boolean isReturnNew() {
        return returnNew;
    }

    public boolean isUpsert() {
        return upsert;
    }

    public boolean isRemove() {
        return remove;
    }

    /**
     * Get the {@link Collation} specifying language-specific rules for string comparison.
     *
     * @return never {@literal null}.
     */
    public Optional<Collation> getCollation() {
        return Optional.ofNullable(collation);
    }

}
