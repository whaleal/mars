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
package com.whaleal.mars.codecs.pojo;


import org.bson.BsonType;

/**
 * 主要为对象字段映射到MongoDB的类型
 * 如果 是默认值 IMPLICIT  就自动转换
 * 如果不是默认值 就用其值类型
 */
public enum StorageType {

    /**
     * Implicit type that is derived from the property value.
     */
    IMPLICIT(-1, null), //
    DOUBLE(1, BsonType.DOUBLE), //
    STRING(2, BsonType.STRING), //
    ARRAY(4, BsonType.ARRAY), //
    BINARY(5, BsonType.BINARY), //
    OBJECT_ID(7, BsonType.OBJECT_ID), //
    BOOLEAN(8, BsonType.BOOLEAN), //
    DATE_TIME(9, BsonType.DATE_TIME), //
    //PATTERN(11, Pattern.class), //
    /**
     * 在MongoDB 4.4  已经删除该 类型
     * Deprecated in MongoDB 4.4
     */
    @Deprecated()
    SCRIPT(13, BsonType.JAVASCRIPT), //
    INT32(15, BsonType.INT32), //
    /**
     * {@link org.bson.BsonTimestamp;}
     * 使用该格式的主要目的 ，1  其增长量也会算入 时间 当中 相关 源码 如下  value = ((long) seconds << 32) | (increment & 0xFFFFFFFFL)
     */
    TIMESTAMP(16, BsonType.TIMESTAMP),
    INT64(17, BsonType.INT64), //
    DECIMAL128(18, BsonType.DECIMAL128);

    //  与 bsontype  对应的 类型 int
    private final int bsonType;
    private final BsonType type;

    StorageType(int bsonType, BsonType type) {

        this.bsonType = bsonType;
        this.type = type;
    }

    public int getBsonType() {
        return bsonType;
    }

    public BsonType getJavaClass() {
        return type;
    }
}
