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
package com.whaleal.mars.session.executor;


import com.whaleal.mars.session.CrudEnum;

/**
 * @author cx
 * @Date 2020/12/30
 * <p>
 * 创建QueryOperations的静态工厂
 */
public class CrudExecutorFactory {

    public static CrudExecutor create(CrudEnum type) {

        switch (type) {
            case DELETE:

                return new DeleteExecutor();
            case UPDATE:

                return new UpdateExecutor();
            case FIND_ALL:

                return new FindAllExecutor();
            case FIND_ONE:

                return new FindOneExecutor();
            case INSERT_ONE:

                return new InsertOneExecutor();
            case INSERT_MANY:

                return new InsertManyExecutor();
            case INDEX_CREATE_ONE:

                return new CreateIndexExecutor();
            case INDEX_CREATE_MANY:

                return new CreateIndexesExecutor();
            case INDEX_DROP_ONE:

                return new DropIndexExecutor();
            case INDEX_DROP_MANY:

                return new DropIndexesExecutor();
            case INDEX_FIND:

                return new FindIndexesExecutor();
            case UPDATE_DEFINITION:

                return new UpdateDefinitionExecutor();
            case REPLACE:

                return new ReplaceExecutor();

        }

        // 如果type不存在，抛出异常
        throw new IllegalArgumentException("Unknown OperationsEnum: " + type);


    }


}
