/**
 * Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 * <p>
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.whaleal.com/licensing/server-side-public-license>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the Server Side Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.whaleal.mars.codecs.pojo.annotations;

import java.lang.annotation.*;

/**
 * An annotation that configures the discriminator key and value for a class.
 * 只能作用于类上 用于添加该实体类的 类型
 * 添加该标记 时 帮助解析该类的 具体生成对象
 * 主要有两部分
 * key  用于与数据库侧进行交互
 * value 为保存在实体名称
 *
 *
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Discriminator {
    /**
     * 用于标记 是否开启 Discriminator
     * @return 开启标记
     */
    boolean useDiscriminator() default false;

    /**
     * @return the discriminator value to use for this type.
     * 与数据库交互存储的值
     */
    String value() default "";

    /**
     * @return the discriminator key to use for this type.
     * 与数据库交互 存储的字段名称
     */
    String key() default "";
}
