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

package com.whaleal.mars.internal;


import com.whaleal.mars.codecs.MongoMappingContext;
import com.whaleal.mars.codecs.pojo.EntityModel;
import com.whaleal.mars.codecs.pojo.PropertyModel;

import java.util.List;
import java.util.StringJoiner;

import static java.util.Arrays.asList;


/**
 * 通过解析 传入的 表达式
 * 获取类中的 field 的 值 或者 其他值
 * 同时还可以对 中间的值进行 赋值
 * <p>
 * 这个类因为和 框架绑定
 * 主要是通过表达式 返回其中的 propertyModel
 */
public class PathTarget {
    private final List<String> segments;
    private final boolean validateNames;
    private int position;
    private final MongoMappingContext mapper;
    private final EntityModel root;
    private EntityModel context;
    private PropertyModel target;
    private boolean resolved;

    /**
     * Creates a resolution context for the given root and path.
     *
     * @param mapper mapper
     * @param root   root
     * @param path   path
     */
    public PathTarget(MongoMappingContext mapper, EntityModel root, String path) {
        this(mapper, root, path, true);
    }

    /**
     * Creates a resolution context for the given root and path.
     *
     * @param mapper        mapper
     * @param root          root
     * @param path          path
     * @param validateNames true if names should be validated
     */
    public PathTarget( MongoMappingContext mapper, EntityModel root, String path, boolean validateNames ) {
        segments = asList(path.split("\\."));
        this.root = root;
        this.mapper = mapper;
        this.validateNames = validateNames;
        resolved = path.startsWith("$");
    }

    /**
     * Creates a resolution context for the given root and path.
     *
     * @param mapper mapper
     * @param type   the root type
     * @param path   the path
     * @param <T>    the root type
     */
    public <T> PathTarget( MongoMappingContext mapper, Class<T> type, String path ) {
        this(mapper, type != null && mapper.isMappable(type) ? mapper.getEntityModel(type) : null, path, true);
    }

    /**
     * Creates a resolution context for the given root and path.
     *
     * @param mapper        mapper
     * @param type          the root type
     * @param path          the path
     * @param validateNames true if names should be validated
     * @param <T>           the root type
     */
    public <T> PathTarget( MongoMappingContext mapper, Class<T> type, String path, boolean validateNames ) {
        this(mapper, type != null && mapper.isMappable(type) ? mapper.getEntityModel(type) : null, path, validateNames);
    }

    /**
     * Returns the translated path for this context.  If validation is disabled, that path could be the same as the initial value.
     *
     * @return the translated path
     */
    public String translatedPath() {
        if (!resolved) {
            resolve();
        }
        StringJoiner joiner = new StringJoiner(".");
        segments.forEach(joiner::add);
        return joiner.toString();
    }

    /**
     * Returns the PropertyModel found at the end of a path.  May be null if the path is invalid and validation is disabled.
     *
     * @return the field
     */

    public PropertyModel getTarget() {
        if (!resolved) {
            resolve();
        }
        return target;
    }

    @Override
    public String toString() {
        return String.format("PathTarget{root=%s, segments=%s, target=%s}", root.getType().getSimpleName(), segments, target);
    }

    private boolean hasNext() {
        return position < segments.size();
    }

    private void resolve() {
        context = this.root;
        position = 0;
        PropertyModel property = null;
        while (hasNext()) {
            String segment = next();

            // array operator
            if ("$".equals(segment) || (segment.startsWith("$[") && segment.endsWith("]")) || segment.matches("[0-9]+")) {
                if (!hasNext()) {
                    break;
                }
                segment = next();
            }
            property = resolveProperty(segment);

            if (property != null) {

                translate(property.getWriteName());
                if (hasNext()) {
                    next();  // consume the map key segment
                }
            } else {
                if (validateNames) {
                    failValidation();
                }
            }
        }
        target = property;
        resolved = true;
    }

    private void failValidation() {
        resolved = true;
        throw new ValidationException(translatedPath() + root.getType().getName());
    }

    private void translate(String nameToStore) {
        segments.set(position - 1, nameToStore);
    }


    private PropertyModel resolveProperty(String segment) {
        if (context != null) {
            PropertyModel mf = context.getPropertyModel(segment);

            if (mf != null) {
                try {
                    context = mapper.getEntityModel(mf.getNormalizedType());
                } catch (NotMappableException ignored) {
                    context = null;
                }
            }
            return mf;
        } else {
            return null;
        }
    }

    String next() {
        return segments.get(position++);
    }
}
