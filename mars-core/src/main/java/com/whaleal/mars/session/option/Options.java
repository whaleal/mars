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
package com.whaleal.mars.session.option;

import com.mongodb.client.MongoCollection;

/**
 * @param <T> 关于操作的属性的 上层接口
 *            特点通用性
 *            比如 读写偏好
 *            读写关注
 */
@Deprecated
public interface Options<T> {


    /**
     * 用于对 没一个操作 进行 读写 偏好设置的 二次封装
     * 但是 读取 和 写入 操作的侧重点又不相同
     * 具体的在各自的option  实现有些差异。
     * 针对 读取 必须设置 读偏好而不用设置写偏好
     * 针对写入 比如设置 写偏好 而不用设置读偏好
     *
     * @param collection
     * @param <C>
     * @return
     */
    <C> MongoCollection<C> prepare(MongoCollection<C> collection);

    /**
     * 统一提供一个获取options的方法
     *
     * @return
     */
    <T> T getOriginOptions();


}
