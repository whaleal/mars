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
package com.whaleal.mars.task;

import com.whaleal.mars.task.AsyncTaskExecutor;
import com.whaleal.mars.task.ListenableFuture;

import java.util.concurrent.Callable;


/**
 * Extension of the {@link AsyncTaskExecutor} interface, adding the capability to submit
 * tasks for {@link ListenableFuture ListenableFutures}.
 *
 * @see ListenableFuture
 */
public interface AsyncListenableTaskExecutor extends AsyncTaskExecutor {

    /**
     * Submit a {@code Runnable} task for execution, receiving a {@code ListenableFuture}
     * representing that task. The Future will return a {@code null} result upon completion.
     *
     * @param task the {@code Runnable} to execute (never {@code null})
     * @return a {@code ListenableFuture} representing pending completion of the task
     * @throws TaskRejectedException if the given task was not accepted
     */
    ListenableFuture<?> submitListenable(Runnable task);

    /**
     * Submit a {@code Callable} task for execution, receiving a {@code ListenableFuture}
     * representing that task. The Future will return the Callable's result upon
     * completion.
     *
     * @param task the {@code Callable} to execute (never {@code null})
     * @return a {@code ListenableFuture} representing pending completion of the task
     * @throws TaskRejectedException if the given task was not accepted
     */
    <T> ListenableFuture<T> submitListenable(Callable<T> task);

}
