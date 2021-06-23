/*
 * Copyright 2018-2020 the original author or authors.
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

public class FindAndReplaceOptions extends FindAndModifyOptions {

    private static final FindAndReplaceOptions NONE = new FindAndReplaceOptions() {

        private static final String ERROR_MSG = "FindAndReplaceOptions.none() cannot be changed. Please use FindAndReplaceOptions.options() instead.";

        @Override
        public FindAndReplaceOptions returnNew() {
            throw new UnsupportedOperationException(ERROR_MSG);
        }

        @Override
        public FindAndReplaceOptions upsert() {
            throw new UnsupportedOperationException(ERROR_MSG);
        }
    };
    private boolean returnNew;
    private boolean upsert;

    public static FindAndReplaceOptions options() {
        return new FindAndReplaceOptions();
    }

    public static FindAndReplaceOptions none() {
        return NONE;
    }

    public static FindAndReplaceOptions empty() {
        return new FindAndReplaceOptions();
    }

    /**
     * Return the replacement document.
     *
     * @return this.
     */
    public FindAndReplaceOptions returnNew() {

        this.returnNew = true;
        return this;
    }

    /**
     * Insert a new document if not exists.
     *
     * @return this.
     */
    public FindAndReplaceOptions upsert() {

        this.upsert = true;
        return this;
    }

    /**
     * Get the bit indicating to return the replacement document.
     *
     * @return {@literal true} if set.
     */
    public boolean isReturnNew() {
        return returnNew;
    }

    /**
     * Get the bit indicating if to create a new document if not exists.
     *
     * @return {@literal true} if set.
     */
    public boolean isUpsert() {
        return upsert;
    }

}
