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

import com.mongodb.lang.Nullable;

import java.util.Comparator;
import java.util.Map;

/**
 * {@code RouteMatcher} that delegates to a {@link PathMatcher}.
 *
 * <p><strong>Note:</strong> This implementation is not efficient since
 * {@code PathMatcher} treats paths and patterns as Strings. For more optimized
 * performance use the {@code PathPatternRouteMatcher} from {@code spring-web}
 * which enables use of parsed routes and patterns.
 */
public class SimpleRouteMatcher implements RouteMatcher {

    private final PathMatcher pathMatcher;


    /**
     * Create a new {@code SimpleRouteMatcher} for the given
     * {@link PathMatcher} delegate.
     */
    public SimpleRouteMatcher(PathMatcher pathMatcher) {
        Assert.notNull(pathMatcher, "PathMatcher is required");
        this.pathMatcher = pathMatcher;
    }

    /**
     * Return the underlying {@link PathMatcher} delegate.
     */
    public PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }


    @Override
    public Route parseRoute(String route) {
        return new DefaultRoute(route);
    }

    @Override
    public boolean isPattern(String route) {
        return this.pathMatcher.isPattern(route);
    }

    @Override
    public String combine(String pattern1, String pattern2) {
        return this.pathMatcher.combine(pattern1, pattern2);
    }

    @Override
    public boolean match(String pattern, Route route) {
        return this.pathMatcher.match(pattern, route.value());
    }

    @Override
    @Nullable
    public Map<String, String> matchAndExtract(String pattern, Route route) {
        if (!match(pattern, route)) {
            return null;
        }
        return this.pathMatcher.extractUriTemplateVariables(pattern, route.value());
    }

    @Override
    public Comparator<String> getPatternComparator(Route route) {
        return this.pathMatcher.getPatternComparator(route.value());
    }


    private static class DefaultRoute implements Route {

        private final String path;

        DefaultRoute(String path) {
            this.path = path;
        }

        @Override
        public String value() {
            return this.path;
        }

        @Override
        public String toString() {
            return value();
        }
    }

}
