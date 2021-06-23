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

import java.util.Collection;
import java.util.Collections;

/**
 * A simple instance filter that checks if a given instance match based on
 * a collection of includes and excludes element.
 *
 * <p>Subclasses may want to override {@link #match(Object, Object)} to provide
 * a custom matching algorithm.
 */
public class InstanceFilter<T> {

    private final Collection<? extends T> includes;

    private final Collection<? extends T> excludes;

    private final boolean matchIfEmpty;


    /**
     * Create a new instance based on includes/excludes collections.
     * <p>A particular element will match if it "matches" the one of the element in the
     * includes list and  does not match one of the element in the excludes list.
     * <p>Subclasses may redefine what matching means. By default, an element match with
     * another if it is equals according to {@link Object#equals(Object)}
     * <p>If both collections are empty, {@code matchIfEmpty} defines if
     * an element matches or not.
     *
     * @param includes     the collection of includes
     * @param excludes     the collection of excludes
     * @param matchIfEmpty the matching result if both the includes and the excludes
     *                     collections are empty
     */
    public InstanceFilter(@Nullable Collection<? extends T> includes,
                          @Nullable Collection<? extends T> excludes, boolean matchIfEmpty) {

        this.includes = (includes != null ? includes : Collections.emptyList());
        this.excludes = (excludes != null ? excludes : Collections.emptyList());
        this.matchIfEmpty = matchIfEmpty;
    }


    /**
     * Determine if the specified {code instance} matches this filter.
     */
    public boolean match(T instance) {
        Assert.notNull(instance, "Instance to match must not be null");

        boolean includesSet = !this.includes.isEmpty();
        boolean excludesSet = !this.excludes.isEmpty();
        if (!includesSet && !excludesSet) {
            return this.matchIfEmpty;
        }

        boolean matchIncludes = match(instance, this.includes);
        boolean matchExcludes = match(instance, this.excludes);
        if (!includesSet) {
            return !matchExcludes;
        }
        if (!excludesSet) {
            return matchIncludes;
        }
        return matchIncludes && !matchExcludes;
    }

    /**
     * Determine if the specified {@code instance} is equal to the
     * specified {@code candidate}.
     *
     * @param instance  the instance to handle
     * @param candidate a candidate defined by this filter
     * @return {@code true} if the instance matches the candidate
     */
    protected boolean match(T instance, T candidate) {
        return instance.equals(candidate);
    }

    /**
     * Determine if the specified {@code instance} matches one of the candidates.
     * <p>If the candidates collection is {@code null}, returns {@code false}.
     *
     * @param instance   the instance to check
     * @param candidates a list of candidates
     * @return {@code true} if the instance match or the candidates collection is null
     */
    protected boolean match(T instance, Collection<? extends T> candidates) {
        for (T candidate : candidates) {
            if (match(instance, candidate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append(": includes=").append(this.includes);
        sb.append(", excludes=").append(this.excludes);
        sb.append(", matchIfEmpty=").append(this.matchIfEmpty);
        return sb.toString();
    }

}
