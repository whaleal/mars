
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

/**
 * {@link MongoClientVersion} holds information about the used mongo-java client and is used to distinguish between
 * different versions.
 */
public class MongoClientVersion {

    private static final boolean SYNC_CLIENT_PRESENT = ClassUtils.isPresent("com.mongodb.MongoClient",
            MongoClientVersion.class.getClassLoader());

    private static final boolean ASYNC_CLIENT_PRESENT = ClassUtils.isPresent("com.mongodb.async.client.MongoClient",
            MongoClientVersion.class.getClassLoader());

    private static final boolean REACTIVE_CLIENT_PRESENT = ClassUtils
            .isPresent("com.mongodb.reactivestreams.client.MongoClient", MongoClientVersion.class.getClassLoader());

    /**
     * @return {@literal true} if MongoDB Java driver version 3.0 or later is on classpath.
     * @deprecated since 2.1, which requires MongoDB Java driver 3.8. Returns {@literal true} by default.
     */
    @Deprecated
    public static boolean isMongo3Driver() {
        return true;
    }

    /**
     * @return {@literal true} if MongoDB Java driver version 3.4 or later is on classpath.
     * @deprecated since 2.1, which requires MongoDB Java driver 3.8. Returns {@literal true} by default.
     */
    @Deprecated
    public static boolean isMongo34Driver() {
        return true;
    }

    /**
     * @return {@literal true} if MongoDB Java driver version 3.8 or later is on classpath.
     */
    public static boolean isMongo38Driver() {
        return true;
    }

    /**
     * @return {@literal true} if the async MongoDB Java driver is on classpath.
     */
    public static boolean isAsyncClient() {
        return ASYNC_CLIENT_PRESENT;
    }

    /**
     * @return {@literal true} if the sync MongoDB Java driver is on classpath.
     */
    public static boolean isSyncClientPresent() {
        return SYNC_CLIENT_PRESENT;
    }

    /**
     * @return {@literal true} if the reactive MongoDB Java driver is on classpath.
     */
    public static boolean isReactiveClientPresent() {
        return REACTIVE_CLIENT_PRESENT;
    }
}
