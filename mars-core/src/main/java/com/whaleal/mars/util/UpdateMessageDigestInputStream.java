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

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Extension of {@link InputStream} that allows for optimized
 * implementations of message digesting.
 */
abstract class UpdateMessageDigestInputStream extends InputStream {

    /**
     * Update the message digest with the rest of the bytes in this stream.
     * <p>Using this method is more optimized since it avoids creating new
     * byte arrays for each call.
     *
     * @param messageDigest the message digest to update
     * @throws IOException when propagated from {@link #read()}
     */
    public void updateMessageDigest(MessageDigest messageDigest) throws IOException {
        int data;
        while ((data = read()) != -1) {
            messageDigest.update((byte) data);
        }
    }

    /**
     * Update the message digest with the next len bytes in this stream.
     * <p>Using this method is more optimized since it avoids creating new
     * byte arrays for each call.
     *
     * @param messageDigest the message digest to update
     * @param len           how many bytes to read from this stream and use to update the message digest
     * @throws IOException when propagated from {@link #read()}
     */
    public void updateMessageDigest(MessageDigest messageDigest, int len) throws IOException {
        int data;
        int bytesRead = 0;
        while (bytesRead < len && (data = read()) != -1) {
            messageDigest.update((byte) data);
            bytesRead++;
        }
    }

}
