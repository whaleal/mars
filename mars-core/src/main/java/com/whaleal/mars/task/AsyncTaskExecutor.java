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

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Extended interface for asynchronous {@link TaskExecutor} implementations,
 * offering an overloaded {@link #execute(Runnable, long)} variant with a start
 * timeout parameter as well support for {@link java.util.concurrent.Callable}.
 *
 * <p>Note: The {@link java.util.concurrent.Executors} class includes a set of
 * methods that can convert some other common closure-like objects, for example,
 * {@link java.security.PrivilegedAction} to {@link Callable} before executing them.
 *
 * <p>Implementing this interface also indicates that the {@link #execute(Runnable)}
 * method will not execute its Runnable in the caller's thread but rather
 * asynchronously in some other thread.
 *
 * @see SimpleAsyncTaskExecutor
 * @see java.util.concurrent.Callable
 * @see java.util.concurrent.Executors
 */
public interface AsyncTaskExecutor extends TaskExecutor {

    /**
     * Constant that indicates immediate execution.
     */
    long TIMEOUT_IMMEDIATE = 0;

    /**
     * Constant that indicates no time limit.
     */
    long TIMEOUT_INDEFINITE = Long.MAX_VALUE;


    /**
     * Execute the given {@code task}.
     *
     * @param task         the {@code Runnable} to execute (never {@code null})
     * @param startTimeout the time duration (milliseconds) within which the task is
     *                     supposed to start. This is intended as a hint to the executor, allowing for
     *                     preferred handling of immediate tasks. Typical values are {@link #TIMEOUT_IMMEDIATE}
     *                     or {@link #TIMEOUT_INDEFINITE} (the default as used by {@link #execute(Runnable)}).
     * @throws TaskTimeoutException  in case of the task being rejected because
     *                               of the timeout (i.e. it cannot be started in time)
     * @throws TaskRejectedException if the given task was not accepted
     */
    void execute(Runnable task, long startTimeout);

    /**
     * Submit a Runnable task for execution, receiving a Future representing that task.
     * The Future will return a {@code null} result upon completion.
     *
     * @param task the {@code Runnable} to execute (never {@code null})
     * @return a Future representing pending completion of the task
     * @throws TaskRejectedException if the given task was not accepted
     * @since 3.0
     */
    Future<?> submit(Runnable task);

    /**
     * Submit a Callable task for execution, receiving a Future representing that task.
     * The Future will return the Callable's result upon completion.
     *
     * @param task the {@code Callable} to execute (never {@code null})
     * @return a Future representing pending completion of the task
     * @throws TaskRejectedException if the given task was not accepted
     * @since 3.0
     */
    <T> Future<T> submit(Callable<T> task);

}
