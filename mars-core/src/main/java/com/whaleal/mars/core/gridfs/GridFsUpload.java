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

import com.whaleal.icefrog.core.lang.Precondition;

import com.whaleal.icefrog.core.lang.loader.Lazy;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * 上传
 *
 * @author cs
 * @date 2021/04/09
 */

public class GridFsUpload<ID> implements GridFsObject<ID, InputStream> {

    private final ID id;
    private final Lazy<InputStream> dataStream;
    private final String filename;
    private final Options options;

    private GridFsUpload( ID id, Lazy<InputStream> dataStream, String filename, Options options ) {

        Precondition.notNull(dataStream, "Data Stream must not be null");
        Precondition.notNull(filename, "Filename must not be null");
        Precondition.notNull(options, "Options must not be null");

        this.id = id;
        this.dataStream = dataStream;
        this.filename = filename;
        this.options = options;
    }

    /**
     * The {@link GridFSFile#getId()} value converted into its simple java type. <br />
     * A {@link org.bson.BsonString} will be converted to plain {@link String}.
     *
     * @return can be {@literal null}.
     */
    @Override

    public ID getFileId() {
        return id;
    }


    /**
     * 获取文件名
     *
     * @return {@link String}
     */
    @Override
    public String getFilename() {
        return filename;
    }


    /**
     * 获取内容
     *
     * @return {@link InputStream}
     */
    @Override
    public InputStream getContent() {
        return dataStream.orElse(new ByteArrayInputStream(new byte[0]));
    }


    /**
     * 得到的选项
     *
     * @return {@link Options}
     */
    @Override
    public Options getOptions() {
        return options;
    }

    /**
     * Create a new instance of {@link GridFsUpload} for the given {@link InputStream}.
     *
     * @param stream must not be {@literal null}.
     * @return new instance of {@link GridFsUpload}.
     */
    public static GridFsUploadBuilder<ObjectId> fromStream(InputStream stream) {
        return new GridFsUploadBuilder<ObjectId>().content(stream);
    }

    /**
     * Builder to create {@link GridFsUpload} in a fluent way.
     *
     * @param <T> the target id type.
     */
    public static class GridFsUploadBuilder<T> {

        private Object id;
        private Lazy<InputStream> dataStream;
        private String filename;
        private Options options = Options.none();

        private GridFsUploadBuilder() {
        }

        /**
         * Define the content of the file to upload.
         *
         * @param stream the upload content.
         * @return this.
         */
        public GridFsUploadBuilder<T> content(InputStream stream) {

            Precondition.notNull(stream, "InputStream must not be null");

            return content(() -> stream);
        }

        /**
         * Define the content of the file to upload.
         *
         * @param stream the upload content.
         * @return this.
         */
        public GridFsUploadBuilder<T> content(Supplier<InputStream> stream) {

            Precondition.notNull(stream, "InputStream Supplier must not be null");

            this.dataStream = Lazy.of(stream);
            return this;
        }

        /**
         * Set the id to use.
         *
         * @param id   the id to save the content to.
         * @param <T1>
         * @return this.
         */
        public <T1> GridFsUploadBuilder<T1> id(T1 id) {

            this.id = id;
            return (GridFsUploadBuilder<T1>) this;
        }

        /**
         * Set the filename.
         *
         * @param filename the filename to use.
         * @return this.
         */
        public GridFsUploadBuilder<T> filename(String filename) {

            this.filename = filename;
            return this;
        }

        /**
         * Set additional file information.
         *
         * @param options must not be {@literal null}.
         * @return this.
         */
        public GridFsUploadBuilder<T> options(Options options) {

            Precondition.notNull(options, "Options must not be null");

            this.options = options;
            return this;
        }

        /**
         * Set the file metadata.
         *
         * @param metadata must not be {@literal null}.
         * @return this.
         */
        public GridFsUploadBuilder<T> metadata(Document metadata) {

            this.options = this.options.metadata(metadata);
            return this;
        }

        /**
         * Set the upload chunk size in bytes.
         *
         * @param chunkSize use negative number for default.
         * @return this.
         */
        public GridFsUploadBuilder<T> chunkSize(int chunkSize) {

            this.options = this.options.chunkSize(chunkSize);
            return this;
        }

        /**
         * Set id, filename, metadata and chunk size from given file.
         *
         * @param gridFSFile must not be {@literal null}.
         * @return this.
         */
        public GridFsUploadBuilder<T> gridFsFile(GridFSFile gridFSFile) {

            Precondition.notNull(gridFSFile, "GridFSFile must not be null");

            this.id = gridFSFile.getId();
            this.filename = gridFSFile.getFilename();
            this.options = this.options.metadata(gridFSFile.getMetadata());
            this.options = this.options.chunkSize(gridFSFile.getChunkSize());

            return this;
        }

        /**
         * Set the content type.
         *
         * @param contentType must not be {@literal null}.
         * @return this.
         */
        public GridFsUploadBuilder<T> contentType(String contentType) {

            this.options = this.options.contentType(contentType);
            return this;
        }

        public GridFsUpload<T> build() {
            return new GridFsUpload(id, dataStream, filename, options);
        }
    }
}
