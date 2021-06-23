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

import com.mongodb.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Extension of {@link FutureTask} that implements {@link ListenableFuture}.
 *
 * @param <T> the result type returned by this Future's {@code get} method
 */
public class ListenableFutureTask<T> extends FutureTask<T> implements ListenableFuture<T> {

    private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry<>();


    /**
     * Create a new {@code ListenableFutureTask} that will, upon running,
     * execute the given {@link Callable}.
     *
     * @param callable the callable task
     */
    public ListenableFutureTask(Callable<T> callable) {
        super(callable);
    }

    /**
     * Create a {@code ListenableFutureTask} that will, upon running,
     * execute the given {@link Runnable}, and arrange that {@link #get()}
     * will return the given result on successful completion.
     *
     * @param runnable the runnable task
     * @param result   the result to return on successful completion
     */
    public ListenableFutureTask(Runnable runnable, @Nullable T result) {
        super(runnable, result);
    }


    @Override
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        this.callbacks.addCallback(callback);
    }

    @Override
    public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback) {
        this.callbacks.addSuccessCallback(successCallback);
        this.callbacks.addFailureCallback(failureCallback);
    }

    @Override
    public CompletableFuture<T> completable() {
        CompletableFuture<T> completable = new DelegatingCompletableFuture<>(this);
        this.callbacks.addSuccessCallback(completable::complete);
        this.callbacks.addFailureCallback(completable::completeExceptionally);
        return completable;
    }


    @Override
    protected void done() {
        Throwable cause;
        try {
            T result = get();
            this.callbacks.success(result);
            return;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        } catch (ExecutionException ex) {
            cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            }
        } catch (Throwable ex) {
            cause = ex;
        }
        this.callbacks.failure(cause);
    }

}
