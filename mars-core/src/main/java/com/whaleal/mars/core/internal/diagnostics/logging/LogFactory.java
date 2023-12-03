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

package com.whaleal.mars.core.internal.diagnostics.logging;




import static com.mongodb.assertions.Assertions.notNull;

/**
 * This class is not part of the public API.
 *
 * <p>This class is not part of the public API and may be removed or changed at any time</p>
 */
public final class LogFactory {

    private static final String PREFIX = "com.whaleal.mars" ;

    private static final boolean USE_SLF4J = shouldUseSLF4J();

    /**
     * Gets a logger with the given suffix appended on to {@code PREFIX}, separated by a '.'.
     *
     * @param name the name for the logger
     * @return the logger
     *
     */
    public static Logger getLogger( final String name) {
        notNull("name", name);
        if (name.startsWith(".") || name.endsWith(".")) {
            throw new IllegalArgumentException("The name can not start or end with a '.'");
        }

        if (USE_SLF4J) {
            return new SLF4JLogger(name);
        } else {
            return new NoOpLogger(name);
        }
    }


    /**
     * Gets a logger with the given Class .
     *
     * @param clazz the clazz for the logger
     * @return the logger
     *
     */
    public static Logger getLogger( Class<?> clazz) {
        notNull("clazz", clazz);
       return  getLogger(clazz.getName());
    }


    private LogFactory() {
    }

    private static boolean shouldUseSLF4J() {
        try {
            Class.forName("org.slf4j.Logger");
            return true;
        } catch (ClassNotFoundException e) {
            java.util.logging.Logger.getLogger(PREFIX)
                    .warning(String.format("SLF4J not found on the classpath.  Logging is disabled for the '%s' component", PREFIX));
            return false;
        }
    }
}
