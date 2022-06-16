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
package com.whaleal.mars.core.gridfs;


import com.whaleal.icefrog.core.util.URLUtil;


interface ResourceLoader {

    /**
     * Pseudo URL prefix for loading from the class path: "classpath:".
     */
    String CLASSPATH_URL_PREFIX = URLUtil.CLASSPATH_URL_PREFIX;


    /**
     * Return a {@code Resource} handle for the specified resource location.
     * <p>The handle should always be a reusable resource descriptor,
     * <p><ul>
     * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".
     * <li>Must support classpath pseudo-URLs, e.g. "classpath:test.dat".
     * <li>Should support relative file paths, e.g. "WEB-INF/test.dat".
     * (This will be implementation-specific, typically provided by an
     * ApplicationContext implementation.)
     * </ul>
     * <p>Note that a {@code Resource} handle does not imply an existing resource;
     *
     * @param location the resource location
     * @return a corresponding {@code Resource} handle (never {@code null})
     * @see #CLASSPATH_URL_PREFIX
     */
    Resource getResource(String location);

    /**
     * Expose the {@link ClassLoader} used by this {@code ResourceLoader}.
     * <p>Clients which need to access the {@code ClassLoader} directly can do so
     * in a uniform manner with the {@code ResourceLoader}, rather than relying
     * on the thread context {@code ClassLoader}.
     *
     * @return the {@code ClassLoader}
     * (only {@code null} if even the system {@code ClassLoader} isn't accessible)
     */

    ClassLoader getClassLoader();

}