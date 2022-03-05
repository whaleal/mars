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

import com.whaleal.mars.core.Mars;
import com.whaleal.mars.core.internal.ErrorHandler;

import java.util.Optional;

public interface MessageListenerContainer extends SmartLifecycle {

    //TODO 传递的线程需要进行修改
    static MessageListenerContainer create(Mars mars) {
        return new DefaultMessageListenerContainer(mars, null);
    }

    /**
     * Register a new {@link SubscriptionRequest} in the container. If the {@link MessageListenerContainer#isRunning() is
     * already running} the {@link Subscription} will be added and run immediately, otherwise it'll be scheduled and
     * started once the container is actually {@link MessageListenerContainer#start() started}.
     *
     * <pre>
     * <code>
     *     MessageListenerContainer container = ...
     *
     *     MessageListener<ChangeStreamDocument<Entity>, Object> messageListener = (message) -> message....
     *     ChangeStreamRequest<Object> request = new ChangeStreamRequest<>(messageListener, () -> "collection-name");
     *
     *     Subscription subscription = container.register(request);
     * </code>
     * </pre>
     * <p>
     * Errors during {@link Message} retrieval lead to {@link Subscription#cancel() cannelation} of the underlying task.
     *
     * @param request must not be {@literal null}.
     * @return never {@literal null}.
     */
    default <T> Subscription register(SubscriptionRequest<T, Object, ? extends SubscriptionRequest.RequestOptions> request) {
        return register(request, Object.class);
    }

    /**
     * Register a new {@link SubscriptionRequest} in the container. If the {@link MessageListenerContainer#isRunning() is
     * already running} the {@link Subscription} will be added and run immediately, otherwise it'll be scheduled and
     * started once the container is actually {@link MessageListenerContainer#start() started}.
     *
     * <pre>
     * <code>
     *     MessageListenerContainer container = ...
     *
     *     MessageListener<ChangeStreamDocument<Entity>, Entity> messageListener = (message) -> message.getBody().toJson();
     *     ChangeStreamRequest<Entity> request = new ChangeStreamRequest<>(messageListener, () -> "collection-name");
     *
     *     Subscription subscription = container.register(request, Entity.class);
     * </code>
     * </pre>
     * <p>
     * Registering the very same {@link SubscriptionRequest} more than once simply returns the already existing
     * {@link Subscription}.
     * <p/>
     * Unless a {@link Subscription} is {@link #remove(Subscription) removed} form the container, the {@link Subscription}
     * is restarted once the container itself is restarted.
     * <p/>
     * Errors during {@link Message} retrieval lead to {@link Subscription#cancel() cannelation} of the underlying task.
     *
     * @param request  must not be {@literal null}.
     * @param bodyType the exact target or a more concrete type of the {@link Message#getBody()}.
     * @return never {@literal null}.
     */
    <S, T> Subscription register(SubscriptionRequest<S, ? super T, ? extends SubscriptionRequest.RequestOptions> request, Class<T> bodyType);

    /**
     * Register a new {@link SubscriptionRequest} in the container. If the {@link MessageListenerContainer#isRunning() is
     * already running} the {@link Subscription} will be added and run immediately, otherwise it'll be scheduled and
     * started once the container is actually {@link MessageListenerContainer#start() started}.
     *
     * <pre>
     * <code>
     *     MessageListenerContainer container = ...
     *
     *     MessageListener<ChangeStreamDocument<Entity>, Entity> messageListener = (message) -> message.getBody().toJson();
     *     ChangeStreamRequest<Entity> request = new ChangeStreamRequest<>(messageListener, () -> "collection-name");
     *
     *     Subscription subscription = container.register(request, Entity.class);
     * </code>
     * </pre>
     * <p>
     * On {@link MessageListenerContainer#stop()} all {@link Subscription subscriptions} are cancelled prior to shutting
     * down the container itself.
     * <p/>
     * Registering the very same {@link SubscriptionRequest} more than once simply returns the already existing
     * {@link Subscription}.
     * <p/>
     * Unless a {@link Subscription} is {@link #remove(Subscription) removed} form the container, the {@link Subscription}
     * is restarted once the container itself is restarted.
     * <p/>
     * Errors during {@link Message} retrieval are delegated to the given {@link ErrorHandler}.
     *
     * @param request      must not be {@literal null}.
     * @param bodyType     the exact target or a more concrete type of the {@link Message#getBody()}. Must not be {@literal null}.
     * @param errorHandler the callback to invoke when retrieving the {@link Message} from the data source fails for some
     *                     reason.
     * @return never {@literal null}.
     */
    <S, T> Subscription register(SubscriptionRequest<S, ? super T, ? extends SubscriptionRequest.RequestOptions> request, Class<T> bodyType,
                                 ErrorHandler errorHandler);

    /**
     * Unregister a given {@link Subscription} from the container. This prevents the {@link Subscription} to be restarted
     * in a potential {@link SmartLifecycle#stop() stop}/{@link SmartLifecycle#start() start} scenario.<br />
     * An {@link Subscription#isActive() active} {@link Subscription subcription} is {@link Subscription#cancel()
     * cancelled} prior to removal.
     *
     * @param subscription must not be {@literal null}.
     */
    void remove(Subscription subscription);

    /**
     * Lookup the given {@link SubscriptionRequest} within the container and return the associated {@link Subscription} if
     * present.
     *
     * @param request must not be {@literal null}.
     * @return {@link Optional#empty()} if not set.
     */
    Optional<Subscription> lookup(SubscriptionRequest<?, ?, ?> request);
}
