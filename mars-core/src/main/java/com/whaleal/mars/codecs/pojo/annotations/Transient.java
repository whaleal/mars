package com.whaleal.mars.codecs.pojo.annotations;


import java.lang.annotation.*;


/**
 * @author wh
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Transient {

}
