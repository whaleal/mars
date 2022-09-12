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
package com.whaleal.mars.core.messaging;

import com.mongodb.client.MongoCursor;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.internal.InvalidMongoDbApiUsageException;
import com.whaleal.mars.core.internal.ErrorHandler;
import com.whaleal.mars.core.internal.MongoNamespace;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

abstract class CursorReadingTask<T, R> implements Task {

    private final Object lifecycleMonitor = new Object();

    private final Mars mars;
    private final SubscriptionRequest<T, R, SubscriptionRequest.RequestOptions> request;
    private final Class<R> targetType;
    private final ErrorHandler errorHandler;
    private final CountDownLatch awaitStart = new CountDownLatch(1);

    private State state = State.CREATED;

    private MongoCursor<T> cursor;

    /**
     * @param mars       must not be {@literal null}.
     * @param request    must not be {@literal null}.
     * @param targetType must not be {@literal null}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    CursorReadingTask(Mars mars, SubscriptionRequest<?, ? super T, ? extends SubscriptionRequest.RequestOptions> request,
                      Class<R> targetType, ErrorHandler errorHandler) {

        this.mars = mars;
        this.request = (SubscriptionRequest) request;
        this.targetType = targetType;
        this.errorHandler = errorHandler;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable
     */
    @Override
    public void run() {

        try {

            start();

            while (isRunning()) {

                try {

                    T next = execute(this::getNext);

                    if (next != null) {
                        //TODO
                        emitMessage(createMessage(next, targetType, request.getRequestOptions()));
                    } else {
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {

                    synchronized (lifecycleMonitor) {
                        state = State.CANCELLED;
                    }
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (RuntimeException e) {

            synchronized (lifecycleMonitor) {
                state = State.CANCELLED;
            }

            errorHandler.handleError(e);
        }
    }

    /**
     * Initialize the Task by 1st setting the current state to {@link State#STARTING starting} indicating the
     * initialization procedure. <br />
     * Moving on the underlying {@link MongoCursor} gets {@link #, Class) created}
     * and is {@link #isValidCursor(MongoCursor) health checked}. Once a valid {@link MongoCursor} is created the
     * {@link #state} is set to {@link State#RUNNING running}. If the health check is not passed the {@link MongoCursor}
     * is immediately {@link MongoCursor#close() closed} and a new {@link MongoCursor} is requested until a valid one is
     * retrieved or the {@link #state} changes.
     */
    private void start() {

        synchronized (lifecycleMonitor) {
            if (!State.RUNNING.equals(state)) {
                state = State.STARTING;
            }
        }

        do {

            boolean valid = false;

            synchronized (lifecycleMonitor) {

                if (State.STARTING.equals(state)) {

                    MongoCursor<T> cursor = execute(() -> initCursor(mars, request.getRequestOptions(), targetType));

                    valid = isValidCursor(cursor);
                    if (valid) {
                        this.cursor = cursor;
                        state = State.RUNNING;
                    } else if (cursor != null) {
                        cursor.close();
                    }
                }
            }

            if (!valid) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                    synchronized (lifecycleMonitor) {
                        state = State.CANCELLED;
                    }
                    Thread.currentThread().interrupt();
                }
            }
        } while (State.STARTING.equals(getState()));

        if (awaitStart.getCount() == 1) {
            awaitStart.countDown();
        }
    }

    protected abstract MongoCursor<T> initCursor(Mars mars, SubscriptionRequest.RequestOptions options, Class<?> targetType);


    @Override
    public void cancel() throws InvalidMongoDbApiUsageException {

        synchronized (lifecycleMonitor) {

            if (State.RUNNING.equals(state) || State.STARTING.equals(state)) {
                this.state = State.CANCELLED;
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }


    @Override
    public boolean isLongLived() {
        return true;
    }

    @Override
    public State getState() {

        synchronized (lifecycleMonitor) {
            return state;
        }
    }


    private static boolean isValidCursor( MongoCursor<?> cursor ) {

        if (cursor == null) {
            return false;
        }

        if (cursor.getServerCursor() == null || cursor.getServerCursor().getId() == 0) {
            return false;
        }

        return true;
    }

    protected Message<T, R> createMessage(T source, Class<R> targetType, SubscriptionRequest.RequestOptions options) {

        SimpleMessage<T, T> message = new SimpleMessage<>(source, source, new MongoNamespace(options.getDatabaseName(),options.getCollectionName()));

        return new LazyMappingDelegatingMessage<>(message, targetType);
    }

    private boolean isRunning() {
        return State.RUNNING.equals(getState());
    }

    @SuppressWarnings("unchecked")
    private void emitMessage(Message<T, R> message) {
        try {
            request.getMessageListener().onMessage((Message) message);
        } catch (Exception e) {
            errorHandler.handleError(e);
        }
    }


    private T getNext() {

        synchronized (lifecycleMonitor) {
            if (State.RUNNING.equals(state)) {
                return cursor.tryNext();
            }
        }

        throw new IllegalStateException(String.format("Cursor %s is not longer open.", cursor));
    }

    @Override
    public boolean awaitStart(Duration timeout) throws InterruptedException {

        Precondition.notNull(timeout, "Timeout must not be null!");
        Precondition.isTrue(!timeout.isNegative(), "Timeout must not be negative!");

        return awaitStart.await(timeout.toNanos(), TimeUnit.NANOSECONDS);
    }

    /**
     * Execute an operation and take care of translating exceptions using the {@link }
     * {@link } rethrowing the potentially translated
     * exception.
     *
     * @param callback must not be {@literal null}.
     * @param <V>
     * @return can be {@literal null}.
     * @throws RuntimeException The potentially translated exception.
     */

    private <V> V execute(Supplier<V> callback) {

        try {
            return callback.get();
        } catch (RuntimeException e) {
            RuntimeException translated = new RuntimeException();
            throw translated != null ? translated : e;
        }
    }
}