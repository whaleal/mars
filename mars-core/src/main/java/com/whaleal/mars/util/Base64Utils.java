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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * A simple utility class for Base64 encoding and decoding.
 *
 * <p>Adapts to Java 8's {@link Base64} in a convenience fashion.
 */
public abstract class Base64Utils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    /**
     * Base64-encode the given byte array.
     *
     * @param src the original byte array
     * @return the encoded byte array
     */
    public static byte[] encode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getEncoder().encode(src);
    }

    /**
     * Base64-decode the given byte array.
     *
     * @param src the encoded byte array
     * @return the original byte array
     */
    public static byte[] decode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }

    /**
     * Base64-encode the given byte array using the RFC 4648
     * "URL and Filename Safe Alphabet".
     *
     * @param src the original byte array
     * @return the encoded byte array
     */
    public static byte[] encodeUrlSafe(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getUrlEncoder().encode(src);
    }

    /**
     * Base64-decode the given byte array using the RFC 4648
     * "URL and Filename Safe Alphabet".
     *
     * @param src the encoded byte array
     * @return the original byte array
     */
    public static byte[] decodeUrlSafe(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getUrlDecoder().decode(src);
    }

    /**
     * Base64-encode the given byte array to a String.
     *
     * @param src the original byte array
     * @return the encoded byte array as a UTF-8 String
     */
    public static String encodeToString(byte[] src) {
        if (src.length == 0) {
            return "";
        }
        return new String(encode(src), DEFAULT_CHARSET);
    }

    /**
     * Base64-decode the given byte array from an UTF-8 String.
     *
     * @param src the encoded UTF-8 String
     * @return the original byte array
     */
    public static byte[] decodeFromString(String src) {
        if (src.isEmpty()) {
            return new byte[0];
        }
        return decode(src.getBytes(DEFAULT_CHARSET));
    }

    /**
     * Base64-encode the given byte array to a String using the RFC 4648
     * "URL and Filename Safe Alphabet".
     *
     * @param src the original byte array
     * @return the encoded byte array as a UTF-8 String
     */
    public static String encodeToUrlSafeString(byte[] src) {
        return new String(encodeUrlSafe(src), DEFAULT_CHARSET);
    }

    /**
     * Base64-decode the given byte array from an UTF-8 String using the RFC 4648
     * "URL and Filename Safe Alphabet".
     *
     * @param src the encoded UTF-8 String
     * @return the original byte array
     */
    public static byte[] decodeFromUrlSafeString(String src) {
        return decodeUrlSafe(src.getBytes(DEFAULT_CHARSET));
    }

}
