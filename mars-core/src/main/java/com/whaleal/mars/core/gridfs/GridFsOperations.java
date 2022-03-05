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

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.mars.core.query.Criteria;
import com.whaleal.mars.core.query.Query;
import com.whaleal.icefrog.core.util.ReUtil ;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 操作接口
 *
 * @author cs
 * @date 2021/04/07
 */
public interface GridFsOperations extends ResourcePatternResolver {

    default ObjectId storeGridFs( InputStream content, String filename,
                                  String bucketName ) {
        return storeGridFs(content, filename, null, null, bucketName);
    }

    default ObjectId storeGridFs( InputStream content, Object metadata, String bucketName ) {
        return storeGridFs(content, null, metadata, bucketName);
    }

    default ObjectId storeGridFs( InputStream content, Document metadata,
                                  String bucketName ) {
        return storeGridFs(content, null, metadata, bucketName);
    }

    default ObjectId storeGridFs( InputStream content, String filename, String contentType,
                                  String bucketName ) {
        return storeGridFs(content, filename, contentType, null, bucketName);
    }

    default ObjectId storeGridFs( InputStream content, String filename, Object metadata,
                                  String bucketName ) {
        return storeGridFs(content, filename, null, metadata, bucketName);
    }

    ObjectId storeGridFs( InputStream content, String filename, String contentType,
                          Object metadata, String bucketName );

    default ObjectId storeGridFs( InputStream content, String filename, Document metadata,
                                  String bucketName ) {
        return storeGridFs(content, filename, null, metadata, bucketName);
    }

    default ObjectId storeGridFs( InputStream content, String filename, String contentType,
                                  Document metadata, String bucketName ) {

        GridFsUpload.GridFsUploadBuilder<ObjectId> uploadBuilder = GridFsUpload.fromStream(content);
        if (StrUtil.hasText(filename)) {
            uploadBuilder.filename(filename);
        }
        if (!ObjectUtil.isEmpty(metadata)) {
            uploadBuilder.metadata(metadata);
        }
        if (StrUtil.hasText(contentType)) {
            uploadBuilder.contentType(contentType);
        }

        return storeGridFs(uploadBuilder.build(), bucketName);
    }

    default <T> T storeGridFs(GridFsObject<T, InputStream> upload) {
        return storeGridFs(upload, null);
    }

    <T> T storeGridFs(GridFsObject<T, InputStream> upload, String bucketName);

    default GridFSFindIterable findGridFs(Query query) {
        return findGridFs(query, null);
    }

    GridFSFindIterable findGridFs(Query query, String bucketName);


    default GridFSFile findOneGridFs(Query query) {
        return findOneGridFs(query, null);
    }

    GridFSFile findOneGridFs(Query query, String bucketName);

    default void rename(ObjectId id, String newFilename) {
        rename(id, newFilename, null);
    }

    void rename(ObjectId id, String newFilename, String bucketName);

    default void deleteGridFs(Query query) {
        deleteGridFs(query, null);
    }

    void deleteGridFs(Query query, String bucketName);

    default void deleteGridFs(ObjectId id) {
        deleteGridFs(id, null);
    }

    void deleteGridFs(ObjectId id, String bucketName);

    @Override
    default GridFsResource getResource(String location) {
        return Optional.ofNullable(findOneGridFs(Query.query(Criteria.where("filename").is(location)))) //
                .map(this::getResource) //
                .orElseGet(() -> GridFsResource.absent(location));
    }

    default GridFsResource getResource(GridFSFile file) {
        return getResource(file, null);
    }

    GridFsResource getResource(GridFSFile file, String bucketName);

    @Override
    default GridFsResource[] getResources(String filenamePattern) {
        if (!StrUtil.hasText(filenamePattern)) {
            return new GridFsResource[0];
        }

        ReUtil reUtil = new ReUtil();

        if (reUtil.isPattern(filenamePattern)) {

            GridFSFindIterable files = findGridFs(Query.query(Criteria.where("filename").regex(ReUtil.toRegex(filenamePattern))));
            List<GridFsResource> resources = new ArrayList<>();

            for (GridFSFile file : files) {
                resources.add(getResource(file));
            }

            return resources.toArray(new GridFsResource[0]);
        }

        return new GridFsResource[]{getResource(filenamePattern)};
    }

}
