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


import org.bson.Document;

import java.util.List;

/**
 * Interface fixing must have operations for {@literal updates} as implemented via {@link Update}.
 */
public interface UpdateDefinition {

    /**
     * If {@literal true} prevents a write operation that affects <strong>multiple</strong> documents from yielding to
     * other reads or writes once the first document is written. <br />
     *
     * @return {@literal true} if update isolated is set.
     */
    Boolean isIsolated();

    /**
     * @return the actual update in its native {@link Document} format. Never {@literal null}.
     */
    Document getUpdateObject();

    /**
     * Check if a given {@literal key} is modified by applying the update.
     *
     * @param key must not be {@literal null}.
     * @return {@literal true} if the actual {@link UpdateDefinition} attempts to modify the given {@literal key}.
     */
    boolean modifies(String key);

    /**
     * 给指定字段属性值加1
     * @param key 不为空
     */
    void inc(String key);

    /**
     *
     * 获取UpdateDefinition中的ArrayFilters
     *
     * @return ArrayFilter类型的List.
     */
    List<ArrayFilter> getArrayFilters();

    /**
     * @return 如果UpdateDefinition包含数组过滤就返回true
     */
    default boolean hasArrayFilters() {
        return !getArrayFilters().isEmpty();
    }





    /**
     * 用于指定修改数组字段中哪些元素的过滤器
     *
     */
    interface ArrayFilter {

        /**
         * Get the {@link Document} representation of the filter to apply. The returned {@link Document} is used directly
         * with the driver without further type or field mapping.
         *
         * @return never {@literal null}.
         */
        Document toData();
    }


}
