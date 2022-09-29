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
package com.whaleal.mars.core.messaging;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import com.whaleal.icefrog.core.util.ClassUtil;
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.mars.codecs.reader.DocumentReader;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 改变流的事件
 *
 * @author cs
 * @date 2021/04/13
 */
public class ChangeStreamEvent<T> {

    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<ChangeStreamEvent, Object> CONVERTED_UPDATER = AtomicReferenceFieldUpdater
            .newUpdater(ChangeStreamEvent.class, Object.class, "converted");

    // 接收到的 changeStream 文档本身
    private final ChangeStreamDocument<Document> raw;

    //  需要将changeStream 转换到的目标对象类
    private final Class<T> targetType;

    //
    private final CodecRegistry converter ;

    // 被转换的字段 注意 这个是通过 CONVERTED_UPDATER 来实现的
    // accessed through CONVERTED_UPDATER.
    private volatile T converted;


    /**
     * 构造方法
     *
     * @param raw        行内容
     * @param targetType 目标类型
     */
    public ChangeStreamEvent( ChangeStreamDocument<Document> raw, Class<T> targetType,CodecRegistry converter ) {

        this.raw = raw;
        this.targetType = targetType;
        this.converter = converter ;

    }


    /**
     * 得到行
     *
     * @return {@link ChangeStreamDocument<Document>}
     */

    public ChangeStreamDocument<Document> getRaw() {
        return raw;
    }


    /**
     * 获取时间戳
     *
     * @return {@link Instant}
     */

    public Instant getTimestamp() {
        return getBsonTimestamp() != null ? Instant.ofEpochSecond(raw.getClusterTime().getTime(),0)
                : null;
    }


    /**
     * 得到bson类型的时间戳
     *
     * @return {@link BsonTimestamp}
     */

    public BsonTimestamp getBsonTimestamp() {
        return raw != null ? raw.getClusterTime() : null;
    }


    /**
     * 得到令牌
     *
     * @return {@link BsonValue}
     */

    public BsonValue getResumeToken() {
        return raw != null ? raw.getResumeToken() : null;
    }


    /**
     * 获取操作类型
     *
     * @return {@link OperationType}
     */

    public OperationType getOperationType() {
        return raw != null ? raw.getOperationType() : null;
    }


    /**
     * 得到数据库名称
     *
     * @return {@link String}
     */

    public String getDatabaseName() {
        return raw != null ? raw.getNamespace().getDatabaseName() : null;
    }


    /**
     * 获取集合名称
     *
     * @return {@link String}
     */

    public String getCollectionName() {
        return raw != null ? raw.getNamespace().getCollectionName() : null;
    }


    /**
     * 得到raw里面的内容
     *
     * @return {@link T}
     */

    public T getBody() {

        if (raw == null) {
            return null;
        }

        Document fullDocument = raw.getFullDocument();

        if (fullDocument == null) {
            return targetType.cast(fullDocument);
        }

        return getConverted(fullDocument);
    }

    /**
     * 进行转换
     *
     * @param fullDocument 完整的文档
     * @return {@link T}
     */

    private T getConverted(Document fullDocument) {
        return (T) doGetConverted(fullDocument);
    }

    private Object doGetConverted(Document fullDocument) {

        Object result = CONVERTED_UPDATER.get(this);


        if (result != null) {
            return result;
        }


        try {

            if (ClassUtil.isAssignable(Document.class, fullDocument.getClass())) {

                return CONVERTED_UPDATER.compareAndSet(this, null, result) ? result : CONVERTED_UPDATER.get(this);
            }

            result = converter.get(targetType).decode(new DocumentReader(fullDocument), DecoderContext.builder().build());
            return CONVERTED_UPDATER.compareAndSet(this, null, result) ? result : CONVERTED_UPDATER.get(this);

        }catch (Exception e){
            e.getMessage();
        }

        throw new IllegalArgumentException(
                String.format("No converter found capable of converting %s to %s", fullDocument.getClass(), targetType));
    }


    @Override
    public String toString() {
        return "ChangeStreamEvent {" + "raw=" + raw + ", targetType=" + targetType + '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ChangeStreamEvent<?> that = (ChangeStreamEvent<?>) o;

        if (!ObjectUtil.nullSafeEquals(this.raw, that.raw)) {
            return false;
        }
        return ObjectUtil.nullSafeEquals(this.targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = raw != null ? raw.hashCode() : 0;
        result = 31 * result + ObjectUtil.nullSafeHashCode(targetType);
        return result;
    }
}