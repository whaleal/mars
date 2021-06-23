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

import java.io.*;
import java.util.Properties;

/**
 * Strategy interface for persisting {@code java.util.Properties},
 * allowing for pluggable parsing strategies.
 *
 * <p>The default implementation is DefaultPropertiesPersister,
 * providing the native parsing of {@code java.util.Properties},
 * but allowing for reading from any Reader and writing to any Writer
 * (which allows to specify an encoding for a properties file).
 */
public interface PropertiesPersister {

    /**
     * Load properties from the given InputStream into the given
     * Properties object.
     *
     * @param props the Properties object to load into
     * @param is    the InputStream to load from
     * @throws IOException in case of I/O errors
     * @see Properties#load
     */
    void load(Properties props, InputStream is) throws IOException;

    /**
     * Load properties from the given Reader into the given
     * Properties object.
     *
     * @param props  the Properties object to load into
     * @param reader the Reader to load from
     * @throws IOException in case of I/O errors
     */
    void load(Properties props, Reader reader) throws IOException;

    /**
     * Write the contents of the given Properties object to the
     * given OutputStream.
     *
     * @param props  the Properties object to store
     * @param os     the OutputStream to write to
     * @param header the description of the property list
     * @throws IOException in case of I/O errors
     * @see Properties#store
     */
    void store(Properties props, OutputStream os, String header) throws IOException;

    /**
     * Write the contents of the given Properties object to the
     * given Writer.
     *
     * @param props  the Properties object to store
     * @param writer the Writer to write to
     * @param header the description of the property list
     * @throws IOException in case of I/O errors
     */
    void store(Properties props, Writer writer, String header) throws IOException;

    /**
     * Load properties from the given XML InputStream into the
     * given Properties object.
     *
     * @param props the Properties object to load into
     * @param is    the InputStream to load from
     * @throws IOException in case of I/O errors
     * @see Properties#loadFromXML(InputStream)
     */
    void loadFromXml(Properties props, InputStream is) throws IOException;

    /**
     * Write the contents of the given Properties object to the
     * given XML OutputStream.
     *
     * @param props  the Properties object to store
     * @param os     the OutputStream to write to
     * @param header the description of the property list
     * @throws IOException in case of I/O errors
     * @see Properties#storeToXML(OutputStream, String)
     */
    void storeToXml(Properties props, OutputStream os, String header) throws IOException;

    /**
     * Write the contents of the given Properties object to the
     * given XML OutputStream.
     *
     * @param props    the Properties object to store
     * @param os       the OutputStream to write to
     * @param encoding the encoding to use
     * @param header   the description of the property list
     * @throws IOException in case of I/O errors
     * @see Properties#storeToXML(OutputStream, String, String)
     */
    void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException;

}
