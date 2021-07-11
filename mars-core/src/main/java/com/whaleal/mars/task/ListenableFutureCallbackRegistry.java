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
import com.whaleal.mars.util.Assert;

import java.util.ArrayDeque;
import java.util.Queue;


/**
 * Helper class for {@link ListenableFuture} implementations that maintains a
 * of success and failure callbacks and helps to notify them.
 *
 * <p>Inspired by {@code com.google.common.util.concurrent.ExecutionList}.
 *
 * @param <T> the callback result type
 */
public class ListenableFutureCallbackRegistry<T> {

    private final Queue<SuccessCallback<? super T>> successCallbacks = new ArrayDeque<>(1);

    private final Queue<FailureCallback> failureCallbacks = new ArrayDeque<>(1);

    private State state = State.NEW;

    @Nullable
    private Object result;

    private final Object mutex = new Object();


    /**
     * Add the given callback to this registry.
     *
     * @param callback the callback to add
     */
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        Assert.notNull(callback, "'callback' must not be null");
        synchronized (this.mutex) {
            switch (this.state) {
                case NEW:
                    this.successCallbacks.add(callback);
                    this.failureCallbacks.add(callback);
                    break;
                case SUCCESS:
                    notifySuccess(callback);
                    break;
                case FAILURE:
                    notifyFailure(callback);
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void notifySuccess(SuccessCallback<? super T> callback) {
        try {
            callback.onSuccess((T) this.result);
        } catch (Throwable ex) {
            // Ignore
        }
    }

    private void notifyFailure(FailureCallback callback) {
        Assert.state(this.result instanceof Throwable, "No Throwable result for failure state");
        try {
            callback.onFailure((Throwable) this.result);
        } catch (Throwable ex) {
            // Ignore
        }
    }

    /**
     * Add the given success callback to this registry.
     *
     * @param callback the success callback to add
     *  4.1
     */
    public void addSuccessCallback(SuccessCallback<? super T> callback) {
        Assert.notNull(callback, "'callback' must not be null");
        synchronized (this.mutex) {
            switch (this.state) {
                case NEW:
                    this.successCallbacks.add(callback);
                    break;
                case SUCCESS:
                    notifySuccess(callback);
                    break;
            }
        }
    }

    /**
     * Add the given failure callback to this registry.
     *
     * @param callback the failure callback to add
     *  4.1
     */
    public void addFailureCallback(FailureCallback callback) {
        Assert.notNull(callback, "'callback' must not be null");
        synchronized (this.mutex) {
            switch (this.state) {
                case NEW:
                    this.failureCallbacks.add(callback);
                    break;
                case FAILURE:
                    notifyFailure(callback);
                    break;
            }
        }
    }


    public void success(@Nullable T result) {
        synchronized (this.mutex) {
            this.state = State.SUCCESS;
            this.result = result;
            SuccessCallback<? super T> callback;
            while ((callback = this.successCallbacks.poll()) != null) {
                notifySuccess(callback);
            }
        }
    }

    public void failure(Throwable ex) {
        synchronized (this.mutex) {
            this.state = State.FAILURE;
            this.result = ex;
            FailureCallback callback;
            while ((callback = this.failureCallbacks.poll()) != null) {
                notifyFailure(callback);
            }
        }
    }


    private enum State {NEW, SUCCESS, FAILURE}

}
