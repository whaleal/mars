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
package com.whaleal.mars.core.aggregation.stages;


import com.whaleal.mars.core.aggregation.AggregationException;
import com.whaleal.mars.core.aggregation.expressions.Expressions;
import com.whaleal.mars.core.aggregation.expressions.impls.DocumentExpression;
import com.whaleal.mars.core.aggregation.expressions.impls.Expression;
import com.whaleal.mars.core.aggregation.expressions.impls.Fields;


public class Group extends Stage {
    private final GroupId id;
    private Fields<Group> fields;

    protected Group() {
        super("$group");
        id = null;
    }

    protected Group(GroupId id) {
        super("$group");
        this.id = id;
    }


    public static GroupId id(String name) {
        return new GroupId(Expressions.field(name));
    }


    public static GroupId id(Expression name) {
        return new GroupId(name);
    }


    public static GroupId id() {
        return new GroupId();
    }


    public static Group of(GroupId id) {
        return new Group(id);
    }


    public static Group of() {
        return new Group();
    }


    public Group field(String name) {
        return field(name, Expressions.field(name));
    }


    public Group field(String name, Expression expression) {
        if (fields == null) {
            fields = Fields.on(this);
        }
        fields.add(name, expression);
        return this;
    }


    public Fields<Group> getFields() {
        return fields;
    }


    public GroupId getId() {
        return id;
    }

    /**
     * Defines a group ID
     */
    public static class GroupId {
        private Expression field;
        private DocumentExpression document;

        protected GroupId() {
            document = Expressions.of();
        }

        protected GroupId(Expression value) {
            if (value instanceof DocumentExpression) {
                document = (DocumentExpression) value;
            } else {
                field = value;
            }
        }


        public GroupId field(String name) {
            return field(name, Expressions.field(name));
        }


        public GroupId field(String name, Expression expression) {
            if (field != null) {
                throw new AggregationException("mixedModesNotAllowed(_id)");
            }
            document.field(name, expression);

            return this;
        }


        public DocumentExpression getDocument() {
            return document;
        }


        public Expression getField() {
            return field;
        }
    }
}
