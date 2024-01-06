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
import com.whaleal.mars.util.Assert;

class TaskFactory {

    private final Mars mars;

    /**
     * @param mars must not be {@literal null}.
     */
    TaskFactory(Mars mars) {

        Assert.notNull(mars, "Mars must not be null!");

        this.mars = mars;
    }

    /**
     * Create a {@link Task} for the given {@link SubscriptionRequest}.
     *
     * @param request      must not be {@literal null}.
     * @param targetType   must not be {@literal null}.
     * @param errorHandler must not be {@literal null}.
     * @return must not be {@literal null}. Consider {@code Object.class}.
     * @throws IllegalArgumentException in case the {@link SubscriptionRequest} is unknown.
     */
    <S, T> Task forRequest(SubscriptionRequest<S, ? super T, ? extends SubscriptionRequest.RequestOptions> request, Class<T> targetType,
                           ErrorHandler errorHandler) {

        Assert.notNull(request, "Request must not be null!");
        Assert.notNull(targetType, "TargetType must not be null!");

        if (request instanceof ChangeStreamRequest) {
            return new ChangeStreamTask(mars, (ChangeStreamRequest) request, targetType, errorHandler);
        } else if (request instanceof TailableCursorRequest) {
            return new TailableCursorTask(mars, (TailableCursorRequest) request, targetType, errorHandler);
        }

        throw new IllegalArgumentException(
                "oh wow - seems you're using some fancy new feature we do not support. Please be so kind and leave us a note in the issue tracker so we can get this fixed.\nThank you!");
    }
}