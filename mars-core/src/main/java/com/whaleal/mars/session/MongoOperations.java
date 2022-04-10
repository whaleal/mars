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
package com.whaleal.mars.session;

import com.mongodb.client.MongoCollection;
import com.whaleal.mars.core.gridfs.GridFsOperations;
import com.whaleal.mars.session.option.CollectionOptions;
import org.bson.Document;

interface MongoOperations extends GridFsOperations, Statistic{

    /**
     * 传递一个类对象，根据对象创建集合
     *
     * @param entityClass 实体类
     * @return {@link MongoCollection<Document>}
     */
    <T> MongoCollection<Document> createCollection(Class<T> entityClass);

    /**
     * 传递对象和集合的选项进行创建集合
     *
     * @param entityClass       实体类
     * @param collectionOptions 收集选项
     * @return {@link MongoCollection<Document>}
     */
    <T> MongoCollection<Document> createCollection( Class<T> entityClass, CollectionOptions collectionOptions );

    /**
     * 根据名字创建集合，使用一些默认的参数
     *
     * @param collectionName 集合名称
     * @return {@link MongoCollection<Document>}
     */
    MongoCollection<Document> createCollection(String collectionName);

    /**
     * 根据名字和选项创建集合
     *
     * @param collectionName    集合名称
     * @param collectionOptions 收集选项
     * @return {@link MongoCollection<Document>}
     */
    MongoCollection<Document> createCollection( String collectionName, CollectionOptions collectionOptions );

    /**
     * 根据类对象删除对应的集合
     *
     * @param entityClass 实体类
     */
    <T> void dropCollection(Class<T> entityClass);

    /**
     * 根据类名称删除对应的集合
     *
     * @param collectionName 集合名称
     */
    void dropCollection(String collectionName);
}
