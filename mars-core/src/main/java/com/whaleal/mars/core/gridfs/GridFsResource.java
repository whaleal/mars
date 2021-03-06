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

import com.mongodb.MongoGridFSException;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.log.Log;
import com.whaleal.icefrog.log.LogFactory;
import com.whaleal.mars.core.query.BsonUtil;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * gridfs?????????
 *
 * @author cs
 * @date 2021/04/07
 */

public class GridFsResource extends InputStreamResource implements GridFsObject<Object, InputStream> {

    private static final Log log = LogFactory.get(GridFsResource.class);
    public static final String CONTENT_TYPE_FIELD = "_contentType";
    private static final ByteArrayInputStream EMPTY_INPUT_STREAM = new ByteArrayInputStream(new byte[0]);

    private final
    GridFSFile file;
    private final String filename;


    /**
     * ???????????????????????????????????????
     *
     * @param filename ?????????
     */
    private GridFsResource(String filename) {

        super(EMPTY_INPUT_STREAM, String.format("GridFs resource [%s]", filename));

        this.file = null;
        this.filename = filename;
    }


    /**
     * ????????????????????????????????????
     *
     * @param file ??????
     */
    public GridFsResource(GridFSFile file) {
        this(file, new ByteArrayInputStream(new byte[]{}));
    }


    /**
     * ????????????
     *
     * @param file        ??????
     * @param inputStream ?????????
     */
    public GridFsResource(GridFSFile file, InputStream inputStream) {

        super(inputStream, String.format("GridFs resource [%s]", file.getFilename()));

        this.file = file;
        this.filename = file.getFilename();
    }


    /**
     * ???????????????????????????
     *
     * @param filename ?????????
     * @return {@link GridFsResource}
     */
    public static GridFsResource absent(String filename) {

        Precondition.notNull(filename, "Filename must not be null");

        return new GridFsResource(filename);
    }


    /**
     * ???????????????
     *
     * @return {@link InputStream}
     * @throws IOException           ioexception
     * @throws IllegalStateException ??????????????????
     */
    @Override
    public InputStream getInputStream() throws IOException, IllegalStateException {

        verifyExists();
        return super.getInputStream();
    }


    /**
     * ??????????????????
     *
     * @return long
     * @throws IOException ioexception
     */
    @Override
    public long contentLength() throws IOException {

        verifyExists();
        return getGridFSFile().getLength();
    }


    /**
     * ???????????????
     *
     * @return {@link String}
     * @throws IllegalStateException ??????????????????
     */
    @Override
    public String getFilename() throws IllegalStateException {
        return this.filename;
    }


    /**
     * ????????????????????????
     *
     * @return boolean
     */
    @Override
    public boolean exists() {
        return this.file != null;
    }


    /**
     * ??????????????????????????????
     *
     * @return long
     * @throws IOException ioexception
     */
    @Override
    public long lastModified() throws IOException {

        verifyExists();
        return getGridFSFile().getUploadDate().getTime();
    }


    /**
     * ????????????
     *
     * @return {@link String}
     */
    @Override
    public String getDescription() {
        return String.format("GridFs resource [%s]", this.getFilename());
    }


    /**
     * ??????id
     *
     * @return {@link Object}
     */
    public Object getId() {

        Precondition.state(exists(), () -> String.format("%s does not exist.", getDescription()));

        return getGridFSFile().getId();
    }


    /**
     * ????????????id
     *
     * @return {@link Object}
     */
    @Override
    public Object getFileId() {

        Precondition.state(exists(), () -> String.format("%s does not exist.", getDescription()));
        return BsonUtil.toJavaType(getGridFSFile().getId());
    }


    /**
     * ?????? gridfsfile
     *
     * @return {@link GridFSFile}
     */

    public GridFSFile getGridFSFile() {
        return this.file;
    }


    /**
     * ??????????????????
     *
     * @return {@link String}
     */
    @SuppressWarnings("deprecation")
    public String getContentType() {

        Precondition.state(exists(), () -> String.format("%s does not exist.", getDescription()));

        return Optional.ofNullable(getGridFSFile().getMetadata()).map(it -> it.get(CONTENT_TYPE_FIELD, String.class))
                .orElseThrow(() -> new MongoGridFSException("No contentType data for this GridFS file"));
    }


    /**
     * ????????????
     *
     * @return {@link InputStream}
     */
    @Override
    public InputStream getContent() {

        try {
            return getInputStream();
        } catch (IOException e) {
            log.error("Failed to obtain input stream for " + filename);
            throw new IllegalStateException("Failed to obtain input stream for " + filename, e);
        }
    }


    /**
     * ??????????????????
     *
     * @return {@link Options}
     */
    @Override
    public Options getOptions() {
        return Options.from(getGridFSFile());
    }

    /**
     * ??????????????????
     *
     * @throws FileNotFoundException ?????????????????????
     */
    private void verifyExists() throws FileNotFoundException {

        if (!exists()) {
            log.error(String.format("%s does not exist.", getDescription()));
            throw new FileNotFoundException(String.format("%s does not exist.", getDescription()));
        }
    }
}
