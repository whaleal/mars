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



import com.whaleal.icefrog.log.Log;
import com.whaleal.icefrog.log.LogFactory;
import com.whaleal.mars.gridfs.GridFsResource;

import java.io.Serializable;

/**
 * Support class for throttling concurrent access to a specific resource.
 *
 * <p>Designed for use as a base class, with the subclass invoking
 * the {@link #beforeAccess()} and {@link #afterAccess()} methods at
 * appropriate points of its workflow. Note that {@code afterAccess}
 * should usually be called in a finally block!
 *
 * <p>The default concurrency limit of this support class is -1
 * ("unbounded concurrency"). Subclasses may override this default;
 * check the javadoc of the concrete class that you're using.
 */
@SuppressWarnings("serial")

public abstract class ConcurrencyThrottleSupport implements Serializable {

    private static final Log log = LogFactory.get(ConcurrencyThrottleSupport.class);

    /**
     * Permit any number of concurrent invocations: that is, don't throttle concurrency.
     */
    public static final int UNBOUNDED_CONCURRENCY = -1;

    /**
     * Switch concurrency 'off': that is, don't allow any concurrent invocations.
     */
    public static final int NO_CONCURRENCY = 0;


    /**
     * Transient to optimize serialization.
     */
    private transient Object monitor = new Object();

    private int concurrencyLimit = UNBOUNDED_CONCURRENCY;

    private int concurrencyCount = 0;

    /**
     * Return the maximum number of concurrent access attempts allowed.
     */
    public int getConcurrencyLimit() {
        return this.concurrencyLimit;
    }

    /**
     * Set the maximum number of concurrent access attempts allowed.
     * -1 indicates unbounded concurrency.
     * <p>In principle, this limit can be changed at runtime,
     * although it is generally designed as a config time setting.
     * <p>NOTE: Do not switch between -1 and any concrete limit at runtime,
     * as this will lead to inconsistent concurrency counts: A limit
     * of -1 effectively turns off concurrency counting completely.
     */
    public void setConcurrencyLimit(int concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }

    /**
     * Return whether this throttle is currently active.
     *
     * @return {@code true} if the concurrency limit for this instance is active
     * @see #getConcurrencyLimit()
     */
    public boolean isThrottleActive() {
        return (this.concurrencyLimit >= 0);
    }


    /**
     * To be invoked before the main execution logic of concrete subclasses.
     * <p>This implementation applies the concurrency throttle.
     *
     * @see #afterAccess()
     */
    protected void beforeAccess() {
        if (this.concurrencyLimit == NO_CONCURRENCY) {
            throw new IllegalStateException(
                    "Currently no invocations allowed - concurrency limit set to NO_CONCURRENCY");
        }
        if (this.concurrencyLimit > 0) {
            boolean debug = log.isDebugEnabled();
            synchronized (this.monitor) {
                boolean interrupted = false;
                while (this.concurrencyCount >= this.concurrencyLimit) {
                    if (interrupted) {
                        throw new IllegalStateException("Thread was interrupted while waiting for invocation access, " +
                                "but concurrency limit still does not allow for entering");
                    }
                    if (debug) {
                        log.debug("Concurrency count " + this.concurrencyCount +
                                " has reached limit " + this.concurrencyLimit + " - blocking");
                    }
                    try {
                        this.monitor.wait();
                    } catch (InterruptedException ex) {
                        // Re-interrupt current thread, to allow other threads to react.
                        Thread.currentThread().interrupt();
                        interrupted = true;
                    }
                }
                if (debug) {
                    log.debug("Entering throttle at concurrency count " + this.concurrencyCount);
                }
                this.concurrencyCount++;
            }
        }
    }

    /**
     * To be invoked after the main execution logic of concrete subclasses.
     *
     * @see #beforeAccess()
     */
    protected void afterAccess() {
        if (this.concurrencyLimit >= 0) {
            synchronized (this.monitor) {
                this.concurrencyCount--;
                if (log.isDebugEnabled()) {
                    log.debug("Returning from throttle at concurrency count " + this.concurrencyCount);
                }
                this.monitor.notify();
            }
        }
    }


}
