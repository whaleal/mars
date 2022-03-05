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
package com.whaleal.mars.core.gridfs;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;

/**
 * 对象
 *
 * @author cs
 * @date 2021/04/09
 */
public interface GridFsObject<ID, CONTENT> {


    ID getFileId();


    /**
     * 获取文件名
     *
     * @return {@link String}
     */
    String getFilename();


    /**
     * 获取内容
     *
     * @return {@link CONTENT}
     */
    CONTENT getContent();


    /**
     * 获取操作的选项
     *
     * @return {@link Options}
     */
    Options getOptions();


    /**
     * 操作选项
     *
     * @author cs
     * @date 2021/04/07
     */
    class Options {

        private final Document metadata;
        private final int chunkSize;

        private Options(Document metadata, int chunkSize) {

            this.metadata = metadata;
            this.chunkSize = chunkSize;
        }


        /**
         * 什么都没有
         *
         * @return {@link Options}
         */
        public static Options none() {
            return new Options(new Document(), -1);
        }


        /**
         * @param contentType 内容类型
         * @return {@link Options}
         */
        public static Options typed(String contentType) {
            return new Options(new Document("_contentType", contentType), -1);
        }


        /**
         * @param gridFSFile
         * @return {@link Options}
         */
        public static Options from( GridFSFile gridFSFile ) {
            return gridFSFile != null ? new Options(gridFSFile.getMetadata(), gridFSFile.getChunkSize()) : none();
        }


        /**
         * 内容类型
         *
         * @param contentType 内容类型
         * @return {@link Options}
         */
        public Options contentType(String contentType) {

            Options target = new Options(new Document(metadata), chunkSize);
            target.metadata.put("_contentType", contentType);
            return target;
        }


        /**
         * 元数据
         *
         * @param metadata 元数据
         * @return {@link Options}
         */
        public Options metadata(Document metadata) {
            return new Options(metadata, chunkSize);
        }


        /**
         * 块大小
         *
         * @param chunkSize 块大小
         * @return {@link Options}
         */
        public Options chunkSize(int chunkSize) {
            return new Options(metadata, chunkSize);
        }


        /**
         * 获取元数据
         *
         * @return {@link Document}
         */
        public Document getMetadata() {
            return metadata;
        }


        /**
         * 获取块大小
         *
         * @return int
         */
        public int getChunkSize() {
            return chunkSize;
        }


        /**
         * 获取内容类型
         *
         * @return {@link String}
         */

        public String getContentType() {
            return (String) metadata.get("_contentType");
        }
    }
}
