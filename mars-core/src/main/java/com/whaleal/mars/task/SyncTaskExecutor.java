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

import com.whaleal.mars.util.Assert;

import java.io.Serializable;


/**
 * {@link TaskExecutor} implementation that executes each task <i>synchronously</i>
 * in the calling thread.
 *
 * <p>Mainly intended for testing scenarios.
 *
 * <p>Execution in the calling thread does have the advantage of participating
 * in it's thread context, for example the thread context class loader or the
 * thread's current transaction association. That said, in many cases,
 * asynchronous execution will be preferable: choose an asynchronous
 * {@code TaskExecutor} instead for such scenarios.
 *
 * @see SimpleAsyncTaskExecutor
 */
@SuppressWarnings("serial")
public class SyncTaskExecutor implements TaskExecutor, Serializable {

    /**
     * Executes the given {@code task} synchronously, through direct
     * invocation of it's {@link Runnable#run() run()} method.
     *
     * @throws IllegalArgumentException if the given {@code task} is {@code null}
     */
    @Override
    public void execute(Runnable task) {
        Assert.notNull(task, "Runnable must not be null");
        task.run();
    }

}
