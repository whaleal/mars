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
package com.whaleal.mars.task.support;


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.task.*;

import java.util.concurrent.*;


public class TaskExecutorAdapter implements AsyncListenableTaskExecutor {

    private final Executor concurrentExecutor;


    private TaskDecorator taskDecorator;


    /**
     * Create a new TaskExecutorAdapter,
     * using the given JDK concurrent executor.
     *
     * @param concurrentExecutor the JDK concurrent executor to delegate to
     */
    public TaskExecutorAdapter(Executor concurrentExecutor) {
        Precondition.notNull(concurrentExecutor, "Executor must not be null");
        this.concurrentExecutor = concurrentExecutor;
    }


    public final void setTaskDecorator(TaskDecorator taskDecorator) {
        this.taskDecorator = taskDecorator;
    }


    /**
     * Delegates to the specified JDK concurrent executor.
     *
     * @see java.util.concurrent.Executor#execute(Runnable)
     */
    @Override
    public void execute(Runnable task) {
        try {
            doExecute(this.concurrentExecutor, this.taskDecorator, task);
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        execute(task);
    }

    @Override
    public Future<?> submit(Runnable task) {
        try {
            if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
                return ((ExecutorService) this.concurrentExecutor).submit(task);
            } else {
                FutureTask<Object> future = new FutureTask<>(task, null);
                doExecute(this.concurrentExecutor, this.taskDecorator, future);
                return future;
            }
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        try {
            if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
                return ((ExecutorService) this.concurrentExecutor).submit(task);
            } else {
                FutureTask<T> future = new FutureTask<>(task);
                doExecute(this.concurrentExecutor, this.taskDecorator, future);
                return future;
            }
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        try {
            ListenableFutureTask<Object> future = new ListenableFutureTask<>(task, null);
            doExecute(this.concurrentExecutor, this.taskDecorator, future);
            return future;
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        try {
            ListenableFutureTask<T> future = new ListenableFutureTask<>(task);
            doExecute(this.concurrentExecutor, this.taskDecorator, future);
            return future;
        } catch (RejectedExecutionException ex) {
            throw new TaskRejectedException(
                    "Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
        }
    }


    /**
     * Actually execute the given {@code Runnable} (which may be a user-supplied task
     * or a wrapper around a user-supplied task) with the given executor.
     *
     * @param concurrentExecutor the underlying JDK concurrent executor to delegate to
     * @param taskDecorator      the specified decorator to be applied, if any
     * @param runnable           the runnable to execute
     * @throws RejectedExecutionException if the given runnable cannot be accepted
     *                                    4.3
     */
    protected void doExecute( Executor concurrentExecutor, TaskDecorator taskDecorator, Runnable runnable )
            throws RejectedExecutionException {

        concurrentExecutor.execute(taskDecorator != null ? taskDecorator.decorate(runnable) : runnable);
    }

}
