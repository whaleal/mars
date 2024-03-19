package com.whaleal.mars.codecs.pojo.annotations;

/**
 * @author wh
 *
 * 主要用于字段级别的加密
 *
 * 当前使用 AES  加密解密
 *
 *
 *
 * 与 property  注解 冲突
 *
 */
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PropEncrypt  {

    /**
     * 加密秘钥
     * @return
     *
     */
    String  value() default "0123456789abcdef";



    /**
     * 是否启用解密处理
     */
    boolean enableDecrypt() default true;

}

