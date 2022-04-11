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


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.icefrog.log.Log;
import com.whaleal.icefrog.log.LogFactory;
import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.internal.InvalidMongoDbApiUsageException;

import com.whaleal.mars.core.internal.ErrorHandler;


import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 消息监听者的容器，里面有好多消息监听
 */

public class DefaultMessageListenerContainer implements MessageListenerContainer {
    private static final Log log = LogFactory.get(DefaultMessageListenerContainer.class);
    private final Executor taskExecutor;
    private final TaskFactory taskFactory;
    private final Optional<ErrorHandler> errorHandler;

    private final Object lifecycleMonitor = new Object();
    private final Map<SubscriptionRequest, Subscription> subscriptions = new LinkedHashMap<>();

    private boolean running = false;


    public DefaultMessageListenerContainer(Mars mars) {
        this(mars, Executors.newSingleThreadExecutor());
    }


    public DefaultMessageListenerContainer(Mars mars, Executor taskExecutor) {
        this(mars, taskExecutor, null);
    }


    public DefaultMessageListenerContainer( Mars mars, Executor taskExecutor,
                                            ErrorHandler errorHandler ) {
        Precondition.notNull(mars, "Mars must not be null!");
        Precondition.notNull(taskExecutor, "TaskExecutor must not be null!");

        this.taskExecutor = taskExecutor;
        this.taskFactory = new TaskFactory(mars);
        this.errorHandler = Optional.ofNullable(errorHandler);
    }


    @Override
    public boolean isAutoStartup() {
        return false;
    }


    @Override
    public void stop(Runnable callback) {

        stop();
        callback.run();
    }


    @Override
    public void start() {

        synchronized (lifecycleMonitor) {

            if (this.running) {
                return;
            }

            subscriptions.values().stream() //
                    .filter(it -> !it.isActive()) //
                    .filter(it -> it instanceof TaskSubscription) //
                    .map(TaskSubscription.class::cast) //
                    .map(TaskSubscription::getTask) //
                    .forEach(taskExecutor::execute);

            running = true;
        }
    }


    @Override
    public void stop() {

        synchronized (lifecycleMonitor) {

            if (this.running) {

                subscriptions.values().forEach(Cancelable::cancel);

                running = false;
            }
        }
    }


    @Override
    public boolean isRunning() {

        synchronized (this.lifecycleMonitor) {
            return running;
        }
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }


    @Override
    public <S, T> Subscription register(SubscriptionRequest<S, ? super T, ? extends SubscriptionRequest.RequestOptions> request,
                                        Class<T> bodyType) {
        return register(request, bodyType, errorHandler.orElseGet(
                () -> new DecoratingLoggingErrorHandler((exception) -> lookup(request).ifPresent(Subscription::cancel))));
    }


    @Override
    public <S, T> Subscription register(SubscriptionRequest<S, ? super T, ? extends SubscriptionRequest.RequestOptions> request,
                                        Class<T> bodyType, ErrorHandler errorHandler) {

        return register(request, taskFactory.forRequest(request, bodyType, errorHandler));
    }


    @Override
    public Optional<Subscription> lookup(SubscriptionRequest<?, ?, ?> request) {

        synchronized (lifecycleMonitor) {
            return Optional.ofNullable(subscriptions.get(request));
        }
    }

    public Subscription register(SubscriptionRequest request, Task task) {
        Subscription subscription = new TaskSubscription(task);

        synchronized (lifecycleMonitor) {

            if (subscriptions.containsKey(request)) {
                return subscriptions.get(request);
            }

            this.subscriptions.put(request, subscription);

            if (this.running) {
                taskExecutor.execute(task);
            }
        }

        return subscription;
    }


    @Override
    public void remove(Subscription subscription) {

        synchronized (lifecycleMonitor) {

            if (subscriptions.containsValue(subscription)) {

                if (subscription.isActive()) {
                    subscription.cancel();
                }

                subscriptions.values().remove(subscription);
            }
        }
    }

    static class TaskSubscription implements Subscription {

        private final Task task;

        TaskSubscription(Task task) {
            this.task = task;
        }

        Task getTask() {
            return task;
        }

        @Override
        public boolean isActive() {
            return task.isActive();
        }

        @Override
        public boolean await(Duration timeout) throws InterruptedException {
            return task.awaitStart(timeout);
        }

        @Override
        public void cancel() throws InvalidMongoDbApiUsageException {
            task.cancel();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TaskSubscription that = (TaskSubscription) o;

            return ObjectUtil.nullSafeEquals(this.task, that.task);
        }

        @Override
        public int hashCode() {
            return ObjectUtil.nullSafeHashCode(task);
        }
    }

    private static class DecoratingLoggingErrorHandler implements ErrorHandler {

        private final ErrorHandler delegate;

        DecoratingLoggingErrorHandler(ErrorHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public void handleError(Throwable t) {

            if (log.isErrorEnabled()) {
                log.error("Unexpected error occurred while listening to MongoDB.", t);
            }

            delegate.handleError(t);
        }
    }
}
