package com.whaleal.mars.codecs.pojo.annotations;


import com.whaleal.icefrog.core.util.StrUtil;

import java.lang.annotation.*;

/**
 * 实体类标记 用于 识别实体
 * 主要用于修改相关表名
 *
 * @author wh
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Entity {

    /**
     * 表名 标记 用于对象和实体之间的差异
     * @return tableName
     */
    String value() default StrUtil.EMPTY;


}
