
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

import java.io.ByteArrayOutputStream;

/**
 * An extension of {@link ByteArrayOutputStream} that:
 * <ul>
 * <li>has public  methods
 * to get more control over the size of the internal buffer</li>
 * <li>has a higher initial capacity (256) by default</li>
 * </ul>
 *
 * <p>As of 4.2, this class has been superseded by {@link FastByteArrayOutputStream}
 * for Spring's internal use where no assignability to {@link ByteArrayOutputStream}
 * is needed (since {@link FastByteArrayOutputStream} is more efficient with buffer
 * resize management but doesn't extend the standard {@link ByteArrayOutputStream}).
 */
public class ResizableByteArrayOutputStream extends ByteArrayOutputStream {

    private static final int DEFAULT_INITIAL_CAPACITY = 256;


    /**
     * Create a new <code>ResizableByteArrayOutputStream</code>
     * with the default initial capacity of 256 bytes.
     */
    public ResizableByteArrayOutputStream() {
        super(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Create a new <code>ResizableByteArrayOutputStream</code>
     * with the specified initial capacity.
     *
     * @param initialCapacity the initial buffer size in bytes
     */
    public ResizableByteArrayOutputStream(int initialCapacity) {
        super(initialCapacity);
    }


    /**
     * Resize the internal buffer size to a specified capacity.
     *
     * @param targetCapacity the desired size of the buffer
     * @throws IllegalArgumentException if the given capacity is smaller than
     *                                  the actual size of the content stored in the buffer already
     * @see ResizableByteArrayOutputStream#size()
     */
    public synchronized void resize(int targetCapacity) {
        Assert.isTrue(targetCapacity >= this.count, "New capacity must not be smaller than current size");
        byte[] resizedBuffer = new byte[targetCapacity];
        System.arraycopy(this.buf, 0, resizedBuffer, 0, this.count);
        this.buf = resizedBuffer;
    }

    /**
     * Grow the internal buffer size.
     *
     * @param additionalCapacity the number of bytes to add to the current buffer size
     * @see ResizableByteArrayOutputStream#size()
     */
    public synchronized void grow(int additionalCapacity) {
        Assert.isTrue(additionalCapacity >= 0, "Additional capacity must be 0 or higher");
        if (this.count + additionalCapacity > this.buf.length) {
            int newCapacity = Math.max(this.buf.length * 2, this.count + additionalCapacity);
            resize(newCapacity);
        }
    }

    /**
     * Return the current size of this stream's internal buffer.
     */
    public synchronized int capacity() {
        return this.buf.length;
    }

}
