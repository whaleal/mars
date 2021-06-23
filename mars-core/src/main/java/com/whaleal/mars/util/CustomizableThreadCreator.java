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
package com.whaleal.mars.util;

import com.mongodb.lang.Nullable;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple customizable helper class for creating new {@link Thread} instances.
 * Provides various bean properties: thread name prefix, thread priority, etc.
 *
 * <p>Serves as base class for thread factories such as
 * {@link org.springframework.scheduling.concurrent.CustomizableThreadFactory}.
 */

public class CustomizableThreadCreator implements Serializable {

    private final AtomicInteger threadCount = new AtomicInteger();
    private String threadNamePrefix;
    private int threadPriority = Thread.NORM_PRIORITY;
    private boolean daemon = false;
    @Nullable
    private ThreadGroup threadGroup;


    /**
     * Create a new CustomizableThreadCreator with default thread name prefix.
     */
    public CustomizableThreadCreator() {
        this.threadNamePrefix = getDefaultThreadNamePrefix();
    }

    /**
     * Create a new CustomizableThreadCreator with the given thread name prefix.
     *
     * @param threadNamePrefix the prefix to use for the names of newly created threads
     */
    public CustomizableThreadCreator(@Nullable String threadNamePrefix) {
        this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
    }

    /**
     * Return the thread name prefix to use for the names of newly
     * created threads.
     */
    public String getThreadNamePrefix() {
        return this.threadNamePrefix;
    }

    /**
     * Specify the prefix to use for the names of newly created threads.
     * Default is "SimpleAsyncTaskExecutor-".
     */
    public void setThreadNamePrefix(@Nullable String threadNamePrefix) {
        this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
    }

    /**
     * Return the priority of the threads that this factory creates.
     */
    public int getThreadPriority() {
        return this.threadPriority;
    }

    /**
     * Set the priority of the threads that this factory creates.
     * Default is 5.
     *
     * @see Thread#NORM_PRIORITY
     */
    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    /**
     * Return whether this factory should create daemon threads.
     */
    public boolean isDaemon() {
        return this.daemon;
    }

    /**
     * Set whether this factory is supposed to create daemon threads,
     * just executing as long as the application itself is running.
     * <p>Default is "false": Concrete factories usually support explicit cancelling.
     * Hence, if the application shuts down, Runnables will by default finish their
     * execution.
     * <p>Specify "true" for eager shutdown of threads which still actively execute
     * a {@link Runnable} at the time that the application itself shuts down.
     *
     * @see Thread#setDaemon
     */
    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    /**
     * Specify the name of the thread group that threads should be created in.
     *
     * @see #setThreadGroup
     */
    public void setThreadGroupName(String name) {
        this.threadGroup = new ThreadGroup(name);
    }

    /**
     * Return the thread group that threads should be created in
     * (or {@code null} for the default group).
     */
    @Nullable
    public ThreadGroup getThreadGroup() {
        return this.threadGroup;
    }

    /**
     * Specify the thread group that threads should be created in.
     *
     * @see #setThreadGroupName
     */
    public void setThreadGroup(@Nullable ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
    }

    /**
     * Template method for the creation of a new {@link Thread}.
     * <p>The default implementation creates a new Thread for the given
     * {@link Runnable}, applying an appropriate thread name.
     *
     * @param runnable the Runnable to execute
     * @see #nextThreadName()
     */
    public Thread createThread(Runnable runnable) {
        Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
        thread.setPriority(getThreadPriority());
        thread.setDaemon(isDaemon());
        return thread;
    }

    /**
     * Return the thread name to use for a newly created {@link Thread}.
     * <p>The default implementation returns the specified thread name prefix
     * with an increasing thread count appended: e.g. "SimpleAsyncTaskExecutor-0".
     *
     * @see #getThreadNamePrefix()
     */
    protected String nextThreadName() {
        return getThreadNamePrefix() + this.threadCount.incrementAndGet();
    }

    /**
     * Build the default thread name prefix for this factory.
     *
     * @return the default thread name prefix (never {@code null})
     */
    protected String getDefaultThreadNamePrefix() {
        return ClassUtils.getShortName(getClass()) + "-";
    }

}
